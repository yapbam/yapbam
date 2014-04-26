package net.yapbam.gui.dialogs;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.*;

import com.fathzer.soft.ajlib.swing.widget.date.DateWidget;
import com.fathzer.soft.ajlib.utilities.NullUtils;

import net.yapbam.data.*;
import net.yapbam.date.helpers.DateStepper;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.preferences.EditingSettings;
import net.yapbam.gui.widget.CurrencyWidget;

/** This dialog allows to create or edit a transaction */
public class TransactionDialog extends AbstractTransactionDialog<Transaction> {
	private static final int MILLIS_PER_DAY = 60000 * 24;

	private static final long serialVersionUID = 1L;

	private DateWidget date;
	private JTextField transactionNumber;
	private CheckNumberWidget checkNumber;
	private DateWidget defDate;
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
	 * @param withNextButton if this argument is true, a button is displayed, when the user clicks on it, the current transaction is added and the dialog is reset
	 * to edit a new transaction
	 * @return the new transaction or the edited one
	 * @exception IllegalArgumentException if edit and withNextButton are both true
	 */
	public static Transaction open(FilteredData data, Window owner, Transaction transaction, boolean edit, boolean autoAdd, boolean withNextButton) {
		if (edit && withNextButton) {
			throw new IllegalArgumentException();
		}
		GlobalData globalData = data.getGlobalData();
		if (globalData.getAccountsNumber() == 0) {
			// Need to create an account first
			EditAccountDialog.open(globalData, owner, LocalizationData.get("TransactionDialog.needAccount")); //$NON-NLS-1$
			if (globalData.getAccountsNumber() == 0) {
				return null;
			}
		}
		TransactionDialog dialog = new TransactionDialog(owner, data, transaction, edit);
		if (withNextButton) {
			dialog.nextButton.setVisible(true);
			dialog.getRootPane().setDefaultButton(dialog.nextButton);
		}
		dialog.setVisible(true);
		Transaction newTransaction = dialog.getTransaction();
		if ((newTransaction != null) && autoAdd) {
			EditingSettings editingOptions = Preferences.INSTANCE.getEditingOptions();
			if (editingOptions.isAlertOnModifyChecked() && (transaction!=null) && (transaction.getStatement()!=null)) {
				boolean alert = !transaction.getAccount().equals(newTransaction.getAccount());
				alert = alert || (GlobalData.AMOUNT_COMPARATOR.compare(transaction.getAmount(), newTransaction.getAmount())!=0);
				alert = alert || !transaction.getValueDate().equals(newTransaction.getValueDate());
				alert = alert || !NullUtils.areEquals(transaction.getNumber(),newTransaction.getNumber());
				alert = alert || !NullUtils.areEquals(transaction.getStatement(),newTransaction.getStatement());
				if (alert) {
					AlertDialog alertDial = new AlertDialog(owner, LocalizationData.get("ModifyCheckedTransactionAlert.title"), LocalizationData.get("ModifyCheckedTransactionAlert.message")); //$NON-NLS-1$ //$NON-NLS-2$
					alertDial.setVisible(true);
					if (alertDial.getResult()==null) {
						return null;
					}
					if (alertDial.getResult()) {
						editingOptions.setAlertOnModifyChecked(false);
						Preferences.INSTANCE.setEditingOptions(editingOptions);
					}
				}
			}
			globalData.add(newTransaction);
			if (transaction != null) {
				globalData.remove(transaction);
			}
		}
		return newTransaction;
	}
	
	/** Creates a new dialog. 
	 * @param owner the dialog's parent frame
	 * @param data the global data
	 * @param transaction the transaction that will initialize the dialog or null ?
	 * @param edit True if we edit an existing transaction, false if we edit a new transaction
	 * @see #open(FilteredData, Window, Transaction, boolean, boolean, boolean)
	 */
	public TransactionDialog(Window owner, final FilteredData data, Transaction transaction, boolean edit) {
		super(owner,
				(edit ? LocalizationData.get("TransactionDialog.title.edit") : LocalizationData.get("TransactionDialog.title.new")), data, transaction); //$NON-NLS-1$ //$NON-NLS-2$
		amount.addPropertyChangeListener(CurrencyWidget.VALUE_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// I've implemented that with Math.signum ... but, I missed that the value may be null
				// In such a case, Math.signum throws a NullPointerException
				Double newValue = (Double) evt.getNewValue();
				Double oldValue = (Double) evt.getOldValue();
				boolean negativeNew = (newValue != null) && (newValue < 0);
				boolean negativeOld = (oldValue != null) && (oldValue < 0);
				if ((negativeNew && !negativeOld) || (negativeOld && !negativeNew)) {
					setTransactionNumberWidget();
				}
			}
		});
		if (useCheckbook() && !edit) {
			// If the transaction is a new one and use a check, change to next check number
			checkNumber.setAccount(data.getGlobalData(), getAccount());
		}
		this.setPredefinedDescriptionComputer(new AbstractPredefinedComputer(data.getGlobalData()) {
			@Override
			protected void process(Transaction transaction) {
				double ranking = getRankingBasedOnDate(now, transaction);
				if (!transaction.getAccount().equals(getAccount())) {
					ranking = ranking / 100;
				}
				super.add(transaction.getDescription(), ranking);
			}
		});
		this.subtransactionsPanel.setPredefinedDescriptionComputer(new AbstractPredefinedComputer(data.getGlobalData()) {
			@Override
			protected void process(Transaction transaction) {
				double ranking = getRankingBasedOnDate(now, transaction);
				if (!transaction.getAccount().equals(getAccount())) {
					ranking = ranking / 100;
				}
				super.add(transaction.getDescription(), ranking/10);
				for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
					super.add(transaction.getSubTransaction(i).getDescription(),ranking);
				}
			}
		});
		this.subtransactionsPanel.setPredefinedDescriptionUpdater(new SubTransactionPanel.PredefinedDescriptionUpdater() {
			private String lastDescription;
			private Category category;
			private double amount;
			
			@Override
			public Category getCategory(String description) {
				update(description);
				return category;
			}
			
			@Override
			public double getAmount(String description, double amount) {
				this.amount = amount;
				update(description);
				return this.amount;
			}

			private void update(String description) {
				if (description==null) {
					this.category = null;
					this.amount = 0.0;
					return;
				}
				if (!NullUtils.areEquals(this.lastDescription, description)) {
					this.lastDescription = description;
					long now = System.currentTimeMillis();
					Map<CategoryAndType, Double> map = new HashMap<CategoryAndType, Double>();
					GlobalData gData = data.getGlobalData();
					for (int i = 0; i < gData.getTransactionsNumber(); i++) {
						Transaction transaction = gData.getTransaction(i);
						// In order to minimize the impact of very old transactions, we will use the date ranking
						double ranking = getRankingBasedOnDate(now, transaction);
						if (!transaction.getAccount().equals(getAccount())) {
							ranking = ranking / 100;
						}
						if (transaction.getDescription().equalsIgnoreCase(description)) {
							add(map, transaction.getCategory(), transaction.getAmount()>0, ranking/10);
						}
						for (int j = 0; j < transaction.getSubTransactionSize(); j++) {
							SubTransaction sub = transaction.getSubTransaction(j);
							if (sub.getDescription().equalsIgnoreCase(description)) {
								add(map, sub.getCategory(), sub.getAmount()>0,ranking);
							}
						}
					}
					CategoryAndType ct = getHeaviest(map);
					this.category = ct.getCategory();
					if (ct.receipt && amount<0) {
						amount = Math.abs(amount);
					} else if (!ct.receipt && amount>0) {
						amount = -amount;
					}
				}
			}

			private void add(Map<CategoryAndType, Double> map, Category category, boolean receipt, double ranking) {
				CategoryAndType ct = new CategoryAndType(receipt, category);
				ranking = map.containsKey(ct)?map.get(ct)+ranking:ranking;
				map.put(ct, ranking);
			}
		});
		if (statement.getText().length()==0) {
			autoFillStatement();
		}
	}
	
	public void setTransactionDate(Date date) {
		this.date.setDate(date);
	}
	
	public void setStatement(String statement) {
		this.statement.setText(statement==null?"":statement); //$NON-NLS-1$
	}

	@Override
	protected void setContent(AbstractTransaction transaction) {
		super.setContent(transaction);
		this.ignoreEvents = true;
		Transaction t = (Transaction) transaction;
		date.setDate(t.getDate());
		transactionNumber.setText(t.getNumber());
		checkNumber.setText(t.getNumber());
		defDate.setDate(t.getValueDate());
		statement.setText(t.getStatement());
		this.ignoreEvents = false;
	}

	@Override
	protected Transaction buildResult() {
		double amount = getAmount();
		String statementId = statement.getText().trim();
		if (statementId.length() == 0) {
			statementId = null;
		}
		String number = transactionNumber.getText().trim();
		if (number.length() == 0) {
			number = null;
		}
		ArrayList<SubTransaction> subTransactions = new ArrayList<SubTransaction>();
		for (int i = 0; i < subtransactionsPanel.getSubtransactionsCount(); i++) {
			subTransactions.add(subtransactionsPanel.getSubtransaction(i));
		}
		return new Transaction(date.getDate(), number, description.getText().trim(), comment.getText().trim(), amount, getAccount(), getCurrentMode(),
				categories.get(), defDate.getDate(), statementId, subTransactions);
	}

	@Override
	protected void buildStatementFields(JPanel centerPane, FocusListener focusListener, GridBagConstraints c) {
		centerPane.add(new JLabel(LocalizationData.get("TransactionDialog.valueDate")), c); //$NON-NLS-1$
		defDate = new DateWidget();
		defDate.setLocale(LocalizationData.getLocale());
		defDate.setToolTipText(LocalizationData.get("TransactionDialog.valueDate.tooltip")); //$NON-NLS-1$
		defDate.getDateField().addFocusListener(focusListener);
		defDate.addPropertyChangeListener(DateWidget.DATE_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!ignoreEvents) {
					autoFillStatement(VALUE_DATE_CHANGED);
					updateOkButtonEnabled();
				}
			}
		});
		c.gridx=1;
		c.weightx=0;
		c.fill = GridBagConstraints.HORIZONTAL;
		centerPane.add(defDate, c);
		c.gridx = 2;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		centerPane.add(new JLabel(LocalizationData.get("TransactionDialog.statement")), c); //$NON-NLS-1$
		statement = new JTextField(15);
		statement.setToolTipText(LocalizationData.get("TransactionDialog.statement.tooltip")); //$NON-NLS-1$
		statement.addFocusListener(focusListener);
		c.gridx = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		centerPane.add(statement, c);
	}

	@Override
	protected void buildNumberField(JPanel centerPane, FocusListener focusListener, GridBagConstraints c) {
		c.fill=GridBagConstraints.NONE;
		c.weightx=0.0;
		centerPane.add(new JLabel(LocalizationData.get("TransactionDialog.number")), c); //$NON-NLS-1$
		JPanel alternateNumberFields = new JPanel(new CardLayout());

		transactionNumber = new JTextField(15);
		transactionNumber.setToolTipText(LocalizationData.get("TransactionDialog.number.tooltip")); //$NON-NLS-1$
		transactionNumber.addFocusListener(focusListener);

		checkNumber = new CheckNumberWidget();
		checkNumber.addPropertyChangeListener(CheckNumberWidget.NUMBER_PROPERTY, new PropertyChangeListener() {
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
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx=1.0;
		centerPane.add(alternateNumberFields, c);
		c.gridx++;
	}

	@Override
	protected void buildDateField(JPanel centerPane, FocusListener focusListener, GridBagConstraints c) {
		JLabel titleDate = new JLabel(LocalizationData.get("TransactionDialog.date")); //$NON-NLS-1$
		centerPane.add(titleDate, c);
		date = new DateWidget();
		date.setLocale(LocalizationData.getLocale());
		date.setToolTipText(LocalizationData.get("TransactionDialog.date.tooltip")); //$NON-NLS-1$
		date.getDateField().addFocusListener(focusListener);
		date.addPropertyChangeListener(DateWidget.DATE_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!ignoreEvents) {
					if (evt.getNewValue() != null) {
						Mode m = getCurrentMode();
						DateStepper vdc = isExpense() ? m.getExpenseVdc() : m.getReceiptVdc();
						// If the date stepper is no more available (if the transaction payment mode is no more usable), use the default value date computer.
						if (vdc==null) {
							vdc = DateStepper.IMMEDIATE;
						}
						defDate.setDate(vdc.getNextStep(date.getDate()));
					}
					autoFillStatement(DATE_CHANGED);
					updateOkButtonEnabled();
				}
			}
		});
		c.gridx++;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		centerPane.add(date, c);
		c.gridx++;
	}

	@Override
	public Transaction getTransaction() {
		return super.getResult();
	}

	@Override
	protected void optionnalUpdatesOnModeChange() {
		setTransactionNumberWidget();
		Mode mode = getCurrentMode();
		DateStepper vdc = isExpense() ? mode.getExpenseVdc() : mode.getReceiptVdc();
		if ((vdc!=null) && (date.getDate()!=null)) {
			defDate.setDate(vdc.getNextStep(date.getDate()));
		}
	}

	@Override
	protected String getOkDisabledCause() {
		String disabledCause = super.getOkDisabledCause();
		if (disabledCause != null) {
			return disabledCause;
		} else if (this.date.getDate() == null) {
			return LocalizationData.get("TransactionDialog.bad.date"); //$NON-NLS-1$
		} else if (this.defDate.getDate() == null) {
			return LocalizationData.get("TransactionDialog.bad.valueDate"); //$NON-NLS-1$
		} else {
			return null;
		}
	}
	
	private static class CategoryAndType extends XAndType<Category>{
		private CategoryAndType(boolean receipt, Category mode) {
			super(receipt, mode);
		}
		public Category getCategory() {
			return x;
		}
	}
	
	private static <T> T getHeaviest(Map<T, Double> map) {
		T ct = null;
		double max = 0.0;
		for (Iterator<Entry<T, Double>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Entry<T, Double> next = iterator.next();
			if (next.getValue() > max) {
				ct = next.getKey();
				max = next.getValue();
			}
		}
		return ct;
	}
	
	private static class ModeAndType extends XAndType<Mode>{
		private ModeAndType(boolean receipt, Mode mode) {
			super(receipt, mode);
		}
		public Mode getMode() {
			return x;
		}
	}
	
	private static class XAndType<T> {
		protected boolean receipt;
		protected T x;
		
		private XAndType(boolean receipt, T x) {
			super();
			this.receipt = receipt;
			this.x = x;
		}

		@Override
		public int hashCode() {
			return x.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof XAndType<?>)) {
				return false;
			}
			if (this.receipt != ((XAndType<?>)obj).receipt) {
				return false;
			}
			return this.x.equals(((XAndType<?>)obj).x);
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

	@Override
	protected void predefinedDescriptionSelected(String description) {
		long now = System.currentTimeMillis();
		Map<ModeAndType, Double> modes = new HashMap<ModeAndType, Double>();
		Map<Category, Double> categories = new HashMap<Category, Double>();
		Map<Double, Double> amount = new HashMap<Double, Double>();
		for (int i = 0; i < data.getGlobalData().getTransactionsNumber(); i++) {
			Transaction transaction = data.getGlobalData().getTransaction(i);
			if (transaction.getDescription().equalsIgnoreCase(description)) {
				Category category = transaction.getCategory();
				// In order to minimize the impact of very old transactions, we will use
				// the date ranking
				double transactionWeight = getRankingBasedOnDate(now, transaction);
				Double weight = categories.get(category);
				categories.put(category, transactionWeight + (weight == null ? 0 : weight));
				if (transaction.getAccount() == getAccount()) {
					// As mode are attached to accounts, it would be unsafe to try to
					// deduce modes on accounts different from the current one.
					ModeAndType mt = new ModeAndType(transaction.getAmount()>0, transaction.getMode());
					weight = modes.get(mt);
					modes.put(mt, transactionWeight + (weight == null ? 0 : weight));
				}
			}
		}
		// Search for the mode and category with the highest weight.
		this.categories.set(getHeaviest(categories));
		ModeAndType modeAndType = getHeaviest(modes);
		if (modeAndType != null) {
			this.receipt.setSelected(modeAndType.receipt);
			this.setMode(modeAndType.getMode());
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
			if (checkNumberRequired) {
				layout.first(parent);
			} else {
				layout.last(parent);
			}
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

	/** Constant that indicates the date has changed.
	 * @see #autoFillStatement(int)
	 */
	private final static int DATE_CHANGED = 1;
	/** Constant that indicates the date has changed.
	 * @see #autoFillStatement(int)
	 */
	private final static int VALUE_DATE_CHANGED = 2;
	/** Updates, if needed, the statement id field.
	 * @param changed an integer that identifies what field has been changed.
	 * This integer can be the sum of the DATE_CHANGED and VALUE_DATE_CHANGED constants
	 * @see #DATE_CHANGED
	 * @see #VALUE_DATE_CHANGED
	 */
	private void autoFillStatement(int changed) {
		EditingSettings editOptions = Preferences.INSTANCE.getEditingOptions();
		if (editOptions.isAutoFillStatement()) {
			Date aDate = null;
			if (((changed&DATE_CHANGED)!=0) && !editOptions.isDateBasedAutoStatement()) {
				aDate = date.getDate();
			} else if (((changed&VALUE_DATE_CHANGED)!=0) && editOptions.isDateBasedAutoStatement()) {
				aDate = defDate.getDate();
			}
			if (aDate!=null) {
				statement.setText(editOptions.getStatementId(aDate));
			}
		}
	}
	/** Updates the statement id field.
	 */
	public void autoFillStatement() {
		autoFillStatement(DATE_CHANGED+VALUE_DATE_CHANGED);
	}

	private JButton nextButton;
	@Override
	protected JPanel createButtonsPane() {
		super.createButtonsPane(); // This line initialize the ok and cancel buttons
		JPanel result = new JPanel();
		super.createButtonsPane(); // This line initialize the ok and cancel buttons
		result.add(getOkButton());
		nextButton = new JButton(LocalizationData.get("TransactionDialog.next")); //$NON-NLS-1$
		nextButton.setToolTipText(LocalizationData.get("TransactionDialog.next.tooltip")); //$NON-NLS-1$
		nextButton.setVisible(false);
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				data.getGlobalData().add(buildResult());
				description.requestFocus();
				Date today = date.getDate();
				setContent(new Transaction(today, null, "", null, 0.0, getAccount(), Mode.UNDEFINED, Category.UNDEFINED, today, null, new ArrayList<SubTransaction>())); //$NON-NLS-1$
				autoFillStatement();
			}
		});
		result.add(nextButton);
		result.add(getCancelButton());
		return result;
	}

	@Override
	public void updateOkButtonEnabled() {
		super.updateOkButtonEnabled();
		nextButton.setEnabled(getOkButton().isEnabled());
	}
}
