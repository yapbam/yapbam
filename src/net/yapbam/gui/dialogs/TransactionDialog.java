package net.yapbam.gui.dialogs;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Window;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.*;

import net.yapbam.data.*;
import net.yapbam.date.helpers.DateStepper;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.AmountWidget;
import net.yapbam.gui.widget.DateWidgetPanel;
import net.yapbam.util.NullUtils;

/** This dialog allows to create or edit a transaction */
public class TransactionDialog extends AbstractTransactionDialog {
	private static final int MILLIS_PER_DAY = 60000 * 24;

	private static final long serialVersionUID = 1L;

	private DateWidgetPanel date;
	private JTextField transactionNumber;
	private CheckNumberPanel checkNumber;
	private DateWidgetPanel defDate;
	private JTextField statement;
	private boolean checkNumberIsVisible;

	/** Display the creation dialog, if the creation is confirmed, add the transaction to the global data 
	 * @param data the global data
	 * @param owner the dialog's parent frame
	 * @param transaction the transaction we want to edit, or null if we want to create a new transaction
	 * @param edit True if we edit an existing transaction, false if we edit a new transaction. Note : Maybe you're thinking
	 * that this argument is redundant with transaction!=null, but it is not. You have to think of transaction argument as a model
	 * to create a new transaction.
	 * @param autoAdd if true, the created or edited transaction is automatically added to the global data instance.
	 * @return the new transaction or the edited one
	 */
	public static Transaction open(FilteredData data, Window owner, Transaction transaction, boolean edit, boolean autoAdd) {
		GlobalData globalData = data.getGlobalData();
		if (globalData.getAccountsNumber() == 0) {
			// Need to create an account first
			AccountDialog.open(globalData, owner, LocalizationData.get("TransactionDialog.needAccount")); //$NON-NLS-1$
			if (globalData.getAccountsNumber() == 0) return null;
		}
		TransactionDialog dialog = new TransactionDialog(owner, data, transaction, edit);
		dialog.setVisible(true);
		Transaction newTransaction = dialog.getTransaction();
		if ((newTransaction != null) && autoAdd) {
			globalData.add(newTransaction);
			if (transaction != null) globalData.remove(transaction);
		}
		return newTransaction;
	}
	
	/** Creates a new dialog. 
	 * @param owner the dialog's parent frame
	 * @param data the global data
	 * @param transaction the transaction that will initialize the dialog or null ?
	 * @param edit True if we edit an existing transaction, false if we edit a new transaction
	 * @see #open(GlobalData, Window, Transaction, boolean, boolean)
	 */
	public TransactionDialog(Window owner, FilteredData data, Transaction transaction, boolean edit) {
		super(owner,
				(edit ? LocalizationData.get("TransactionDialog.title.edit") : LocalizationData.get("TransactionDialog.title.new")), data, transaction); //$NON-NLS-1$ //$NON-NLS-2$
		amount.addPropertyChangeListener(AmountWidget.VALUE_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// I've implemented that with Math.signum ... but, I missed that the value may be null
				// In such a case, Math.signum throws a NullPointerException
				Double newValue = (Double) evt.getNewValue();
				Double oldValue = (Double) evt.getOldValue();
				boolean negativeNew = (newValue != null) && (newValue < 0);
				boolean negativeOld = (oldValue != null) && (oldValue < 0);
				if ((negativeNew && !negativeOld) || (negativeOld && !negativeNew)) setTransactionNumberWidget();
			}
		});
		if (useCheckbook() && !edit) { // If the transaction is a new one and use a check, change to next check number
			checkNumber.setAccount(data.getGlobalData(), getAccount());
		}
	}

	public void setTransactionDate(Date date) {
		this.date.setDate(date);
	}

	protected void setContent(AbstractTransaction transaction) {
		super.setContent(transaction);
		Transaction t = (Transaction) transaction;
		date.setDate(t.getDate());
		checkNumber.setText(t.getNumber());
		defDate.setDate(t.getValueDate());
		statement.setText(t.getStatement());
	}

	@Override
	protected Object buildResult() {
		double amount = getAmount();
		String statementId = statement.getText().trim();
		if (statementId.length() == 0) statementId = null;
		String number = transactionNumber.getText().trim();
		if (number.length() == 0) number = null;
		ArrayList<SubTransaction> subTransactions = new ArrayList<SubTransaction>();
		for (int i = 0; i < subtransactionsPanel.getSubtransactionsCount(); i++) {
			subTransactions.add(subtransactionsPanel.getSubtransaction(i));
		}
		return new Transaction(date.getDate(), number, description.getText().trim(), amount, getAccount(), getCurrentMode(),
				categories.getCategory(), defDate.getDate(), statementId, subTransactions);
	}

	protected void buildStatementFields(JPanel centerPane, FocusListener focusListener, GridBagConstraints c) {
		centerPane.add(new JLabel(LocalizationData.get("TransactionDialog.valueDate")), c); //$NON-NLS-1$
		defDate = new DateWidgetPanel();
		defDate.setLocale(LocalizationData.getLocale());
		defDate.setToolTipText(LocalizationData.get("TransactionDialog.valueDate.tooltip")); //$NON-NLS-1$
		defDate.getDateWidget().addFocusListener(focusListener);
		defDate.addPropertyChangeListener(DateWidgetPanel.DATE_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateOkButtonEnabled();
			}
		});
		c.gridx=1; c.weightx=0; c.fill = GridBagConstraints.HORIZONTAL;
		centerPane.add(defDate, c);
		c.gridx = 2;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		centerPane.add(new JLabel(LocalizationData.get("TransactionDialog.statement")), c); //$NON-NLS-1$
		statement = new JTextField(15);
		statement.setToolTipText(LocalizationData.get("TransactionDialog.statement.tooltip")); //$NON-NLS-1$
		statement.addFocusListener(focusListener);
		c.gridx = 3; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1.0;
		centerPane.add(statement, c);
	}

	protected void buildNumberField(JPanel centerPane, FocusListener focusListener, GridBagConstraints c) {
		c.fill=GridBagConstraints.NONE; c.weightx=0.0;
		centerPane.add(new JLabel(LocalizationData.get("TransactionDialog.number")), c); //$NON-NLS-1$
		JPanel alternateNumberFields = new JPanel(new CardLayout());

		transactionNumber = new JTextField(15);
		transactionNumber.setToolTipText(LocalizationData.get("TransactionDialog.number.tooltip")); //$NON-NLS-1$
		transactionNumber.addFocusListener(focusListener);

		checkNumber = new CheckNumberPanel();
		checkNumber.addPropertyChangeListener(CheckNumberPanel.NUMBER_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				transactionNumber.setText(checkNumber.getNumber());
			}
		});
		
		alternateNumberFields.add(checkNumber, checkNumber.getClass().getName());
		alternateNumberFields.add(transactionNumber, transactionNumber.getClass().getName());
		CardLayout layout = (CardLayout) alternateNumberFields.getLayout();
		layout.last(alternateNumberFields);

		c.gridx++;
		c.fill = GridBagConstraints.HORIZONTAL; c.weightx=1.0;
		centerPane.add(alternateNumberFields, c);
		c.gridx++;
	}

	protected void buildDateField(JPanel centerPane, FocusListener focusListener, GridBagConstraints c) {
		JLabel titleDate = new JLabel(LocalizationData.get("TransactionDialog.date")); //$NON-NLS-1$
		centerPane.add(titleDate, c);
		date = new DateWidgetPanel();
		date.setLocale(LocalizationData.getLocale());
		date.setToolTipText(LocalizationData.get("TransactionDialog.date.tooltip")); //$NON-NLS-1$
		date.getDateWidget().addFocusListener(focusListener);
		date.addPropertyChangeListener(DateWidgetPanel.DATE_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getNewValue() != null) {
					Mode m = getCurrentMode();
					DateStepper vdc = isExpense() ? m.getExpenseVdc() : m.getReceiptVdc();
					// If the date stepper is no more available (if the transaction payment mode is no more usable), use the default value date computer.
					if (vdc==null) vdc = DateStepper.IMMEDIATE;
					defDate.setDate(vdc.getNextStep(date.getDate()));
				}
				updateOkButtonEnabled();
			}
		});
		c.gridx++;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		centerPane.add(date, c);
		c.gridx++;
	}

	public Transaction getTransaction() {
		return (Transaction) super.getResult();
	}

	protected void optionnalUpdatesOnModeChange() {
		setTransactionNumberWidget();
		Mode mode = getCurrentMode();
		DateStepper vdc = isExpense() ? mode.getExpenseVdc() : mode.getReceiptVdc();
		if (vdc!=null) defDate.setDate(vdc.getNextStep(date.getDate()));
	}

	@Override
	protected String getOkDisabledCause() {
		String disabledCause = super.getOkDisabledCause();
		if (disabledCause != null) return disabledCause;
		if (this.date.getDate() == null) return LocalizationData.get("TransactionDialog.bad.date"); //$NON-NLS-1$
		if (this.defDate.getDate() == null) return LocalizationData.get("TransactionDialog.bad.valueDate"); //$NON-NLS-1$
		return null;
	}
	
	@SuppressWarnings({ "unchecked" })
	protected void setPredefinedDescriptions() {
		HashMap<String, Double> map = new HashMap<String, Double>();
		long now = System.currentTimeMillis();
		for (int i = 0; i < this.data.getTransactionsNumber(); i++) {
			Transaction transaction = this.data.getTransaction(i);
			String desc = transaction.getDescription();
			double ranking = getRankingBasedOnDate(now, transaction);
			if (!transaction.getAccount().equals(data.getGlobalData().getAccount(selectedAccount))) ranking = ranking / 100;
			Double current = map.get(desc);
			if (current==null) {
				map.put(desc, ranking);
			} else {
				map.put(desc, (ranking + current));
			}
		}
		// Sort the map by ranking
		LinkedList<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(map.entrySet());
		Collections.sort(list, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				return -((Comparable<Double>) ((Map.Entry<String, Double>) (o1)).getValue()).compareTo(((Map.Entry<String, Double>) (o2)).getValue());
			}
		});
		String[] array = new String[list.size()];
		Iterator<Map.Entry<String, Double>> iterator = list.iterator();
		for (int i = 0; i < array.length; i++) {
			array[i] = iterator.next().getKey();
		}
		description.setPredefined(array);
	}
	
	private static class ModeAndType {
		private boolean receipt;
		private Mode mode;
		
		private ModeAndType(boolean receipt, Mode mode) {
			super();
			this.receipt = receipt;
			this.mode = mode;
		}

		@Override
		public int hashCode() {
			return mode.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (this.receipt != ((ModeAndType)obj).receipt) return false;
			return this.mode.equals(((ModeAndType)obj).mode);
		}
	}
	
	/** Gets the ranking of a transaction, based on its date.
	 * This method is used by the wizard to determine which descriptions are the most probable.
	 * @param transaction The transaction
	 * @return a double
	 */
	private double getRankingBasedOnDate(long now, Transaction transaction) {
		// we will use a function between 0 (for very, very old ones) and 1 for
		// recent one.
		// Probably this function could be improved ...
		long time = Math.abs(transaction.getDate().getTime() - now) / MILLIS_PER_DAY;
		return 2 / Math.sqrt(time + 4);
	}

	protected void predefinedDescriptionSelected(String description) {
		long now = System.currentTimeMillis();
		HashMap<ModeAndType, Double> modes = new HashMap<ModeAndType, Double>();
		HashMap<Category, Double> categories = new HashMap<Category, Double>();
		for (int i = 0; i < data.getTransactionsNumber(); i++) {
			Transaction transaction = data.getTransaction(i);
			if (transaction.getDescription().equalsIgnoreCase(description)) {
				Category category = transaction.getCategory();
				// In order to minimize the impact of very old transactions, we will use
				// the date ranking
				double transactionWeight = getRankingBasedOnDate(now, transaction);
				Double weight = categories.get(category);
				categories.put(category, transactionWeight + (weight == null ? 0 : weight));
				if (transaction.getAccount() == data.getGlobalData().getAccount(selectedAccount)) {
					// As mode are attached to accounts, it would be unsafe to try to
					// deduce modes on accounts different from the current one.
					ModeAndType mt = new ModeAndType(transaction.getAmount()>0, transaction.getMode());
					weight = modes.get(mt);
					modes.put(mt, transactionWeight + (weight == null ? 0 : weight));
				}
			}
		}
		// Search for the mode and category with the highest weight.
		Category category = null;
		double max = 0;
		for (Iterator<Category> iterator = categories.keySet().iterator(); iterator.hasNext();) {
			Category next = iterator.next();
			if (categories.get(next) > max) {
				category = next;
				max = categories.get(next);
			}
		}
		this.categories.setCategory(category);
		ModeAndType modeAndType = null;
		max = 0;
		for (Iterator<ModeAndType> iterator = modes.keySet().iterator(); iterator.hasNext();) {
			ModeAndType next = iterator.next();
			if (modes.get(next) > max) {
				modeAndType = next;
				max = modes.get(next);
			}
		}
		if (modeAndType != null) {
			this.receipt.setSelected(modeAndType.receipt);
			this.setMode(modeAndType.mode);
		}
	}

	private void setTransactionNumberWidget() {
		boolean checkNumberRequired = useCheckbook();
		if (checkNumberRequired != checkNumberIsVisible) {
			if (!checkNumberRequired) {
				transactionNumber.setText(""); //$NON-NLS-1$
			} else {
				checkNumber.setAccount(data.getGlobalData(), getAccount());
			}
			// If we need to switch from text field to check numbers popup
			Container parent = checkNumber.getParent();
			CardLayout layout = (CardLayout) parent.getLayout();
			if (checkNumberRequired) layout.first(parent); else layout.last(parent);
			checkNumberIsVisible = !checkNumberIsVisible;
		} else if (checkNumberRequired && !NullUtils.areEquals(checkNumber.getAccount(), getAccount())) {
			checkNumber.setAccount(data.getGlobalData(), getAccount());
		}
	}

	/**
	 * Tests whether the currently documented transaction use a checkbook or not.
	 * 
	 * @return a boolean - true if a checkbook is used.
	 */
	private boolean useCheckbook() {
		return (isExpense()) && (getCurrentMode().isUseCheckBook());
	}

}
