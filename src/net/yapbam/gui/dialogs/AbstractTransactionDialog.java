package net.yapbam.gui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.prefs.Preferences;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;
import com.fathzer.soft.ajlib.swing.widget.ComboBox;
import com.fathzer.soft.ajlib.swing.widget.TextWidget;
import com.fathzer.soft.ajlib.utilities.NullUtils;




import net.yapbam.data.*;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.AutoUpdateOkButtonPropertyListener;
import net.yapbam.gui.widget.AutoSelectFocusListener;
import net.yapbam.gui.widget.CurrencyWidget;

/** This dialog allows to create or edit a transaction */
public abstract class AbstractTransactionDialog<V> extends AbstractDialog<GlobalData, V> {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTransactionDialog.class);

	private AccountWidget accounts;
	protected TextWidget description;
	protected CommentWidget comment;
	protected CurrencyWidget amount;
	protected JCheckBox receipt;
	protected ModeWidget modes;
	protected CategoryWidget categories;
	protected SubtransactionListPanel subtransactionsPanel;
	private Mode originalMode;
	private boolean originalIsExpense;
	private PredefinedDescriptionComputer pdc;
	protected boolean ignoreEvents;
	
	protected AbstractTransactionDialog(Window owner, String title, GlobalData data, AbstractTransaction transaction) {
		super(owner, title, data);
		pdc = null;
		this.ignoreEvents = false;
		if (transaction!=null) {
			setContent(transaction);
		}
	}
	
	protected void setPredefinedDescriptionComputer(PredefinedDescriptionComputer pdc) {
		this.pdc = pdc;
		if (pdc!=null) {
			description.setPredefined(pdc.getPredefined(), pdc.getUnsortedSize());
		}
	}

	protected void setContent(AbstractTransaction transaction) {
		this.ignoreEvents = true;
		accounts.set(transaction.getAccount());
		description.setText(transaction.getDescription());
		comment.setText(transaction.getComment());
		amount.setValue(Math.abs(transaction.getAmount()));
		originalIsExpense = transaction.getAmount()<=0;
		receipt.setSelected(!originalIsExpense);
		// Be aware, as its listener changes the selectedMode, receipt must always be set before mode.
		originalMode = transaction.getMode();
		// Update mode list
		buildModes(originalIsExpense);
		modes.set(transaction.getMode());
		categories.set(transaction.getCategory());
		subtransactionsPanel.fill(transaction);
		this.ignoreEvents = false;
	}

	protected void setMode(Mode mode) {
		Account account = getAccount();
		int index = account.indexOf(mode);
		if (index>=0) {
			// If the mode isn't available for this account, do nothing.
			modes.set(mode);
		}
	}
	
	/** Gets the currently selected account.
	 * @return an account.
	 */
	protected Account getAccount() {
		return accounts.get();
	}

	/** Gets the currently selected amount.
	 * @return a double, positive if the transaction is a receipt, negative if not.
	 */
	protected double getAmount() {
		double amount = this.amount.getValue()!=null?Math.abs(this.amount.getValue()):0.0;
		// Beware of zero value, a zero expense should be considered as a receipt,
		// because expenses and receipts have not the same modes available.
		// We will transform zero value into very, very, very small non zero values.
		if (amount==0) {
			amount=Double.MIN_VALUE;
		}
		if (!this.receipt.isSelected()) {
			amount = -amount;
		}
		return amount;
	}
	
	/** Returns whether the currently edited transaction is an expense. 
	 * @return true for an expense, false for a receipt
	 */
	protected boolean isExpense() {
		return getAmount()<0;
	}

	@Override
	protected JPanel createCenterPane() {
		// Create the content pane.
		JPanel centerPane = new JPanel(new GridBagLayout());

		// Account
		Insets insets = new Insets(5,5,5,5);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = insets;
		c.gridx=0;
		c.gridy=0;
		c.anchor=GridBagConstraints.WEST;
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx=1.0;
		accounts = new AccountWidget(data);
		AccountsListener accountListener = new AccountsListener();
		accounts.addPropertyChangeListener(AccountWidget.ACCOUNT_PROPERTY, accountListener);
		accounts.setToolTipText(LocalizationData.get("TransactionDialog.account.tooltip")); //$NON-NLS-1$
		centerPane.add(accounts, c);

		// Description
		JPanel panel = new JPanel(new GridBagLayout());
		c.gridy=1;
		c.insets=new Insets(0, 0, 0, 0);
		centerPane.add(panel, c);
		JLabel titleLibelle = new JLabel(LocalizationData.get("TransactionDialog.description")); //$NON-NLS-1$
		c = new GridBagConstraints();
		c.insets = insets;
		c.gridx=0;
		c.gridy=0;
		c.anchor = GridBagConstraints.WEST;
		c.weightx=0.0;
		panel.add(titleLibelle, c);
		description = new TextWidget();
		description.setToolTipText(LocalizationData.get("TransactionDialog.description.tooltip")); //$NON-NLS-1$
		description.addPropertyChangeListener(TextWidget.PREDEFINED_VALUE, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getNewValue()!=null) {
					predefinedDescriptionSelected((String) evt.getNewValue());
				}
			}
		});
		description.addFocusListener(AutoSelectFocusListener.INSTANCE);
		c.gridx=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.weightx=1.0;
		panel.add(description, c);
		
		// Comment
		comment = new CommentWidget();
		c.gridx=2;
		panel.add(comment, c);

		// Next line
		c = new GridBagConstraints();
		c.insets = insets;
		c.gridx=0;
		c.gridy=2;
		c.anchor = GridBagConstraints.WEST;
		buildDateField(centerPane, AutoSelectFocusListener.INSTANCE, c); // Subclasses may insert a date field here

		c.fill=GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 0;
		centerPane.add(new JLabel(LocalizationData.get("TransactionDialog.amount")), c); //$NON-NLS-1$
		amount = new CurrencyWidget(LocalizationData.getLocale());
		amount.setColumns(10);
		amount.addFocusListener(AutoSelectFocusListener.INSTANCE);
		amount.addPropertyChangeListener(CurrencyWidget.VALUE_PROPERTY, new AutoUpdateOkButtonPropertyListener(this));
		amount.setToolTipText(LocalizationData.get("TransactionDialog.amount.tooltip")); //$NON-NLS-1$
		c.gridx++;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		centerPane.add(amount, c);
		
		receipt = new JCheckBox(LocalizationData.get("TransactionDialog.receipt")); //$NON-NLS-1$
		receipt.setToolTipText(LocalizationData.get("TransactionDialog.receipt.tooltip")); //$NON-NLS-1$
		receipt.addItemListener(new ReceiptListener());
		c.gridx++;
		c.weightx=1.0;
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = GridBagConstraints.REMAINDER;
		centerPane.add(receipt, c);

		// Next line
		c = new GridBagConstraints();
		c.insets = insets;
		c.gridx = 0;
		c.gridy = 3;
		c.anchor = GridBagConstraints.WEST;
		centerPane.add(new JLabel(LocalizationData.get("TransactionDialog.mode")), c); //$NON-NLS-1$
		modes = new ModeWidget(new ModeWidgetParams(data, getAccount(), true));
		modes.getJLabel().setVisible(false);
		buildModes(!receipt.isSelected());
		ModesListener modeListener = new ModesListener();
		modes.addPropertyChangeListener(ModeWidget.MODE_PROPERTY, modeListener);
		modes.setToolTipText(LocalizationData.get("TransactionDialog.mode.tooltip")); //$NON-NLS-1$
		c.gridx = 1;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		centerPane.add(modes, c);
		
		c.gridx = 2;
		buildNumberField(centerPane, AutoSelectFocusListener.INSTANCE, c); // Subclasses may insert a number field here

		categories = new CategoryWidget(this.data);
		c.gridx++;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		centerPane.add(categories, c);

		// Next line
		c = new GridBagConstraints();
		c.insets=insets;
		c.gridx=0;
		c.gridy=5;
		c.anchor = GridBagConstraints.WEST;
		buildStatementFields(centerPane, AutoSelectFocusListener.INSTANCE, c);

		// Next Line
		c.gridx=0;
		c.gridy++;
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		centerPane.add(new JSeparator(JSeparator.HORIZONTAL), c);

		c.insets = insets;
		c.gridx=0;
		c.gridy++;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		subtransactionsPanel = new SubtransactionListPanel(this.data);
		subtransactionsPanel.addPropertyChangeListener(SubtransactionListPanel.SUM_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!ignoreEvents && (amount.getValue() != null) && subtransactionsPanel.isAddToTransactionSelected()) {
					double diff = (Double) evt.getNewValue() - (Double) evt.getOldValue();
					if (isExpense()) {
						diff = -diff;
					}
					double newValue = amount.getValue() + diff;
					if (newValue < 0) {
						newValue = -newValue;
						receipt.setSelected(!receipt.isSelected());
					}
					amount.setValue(newValue);
				}
			}
		});
		centerPane.add(subtransactionsPanel, c);

		amount.setValue(0.0);
		return centerPane;
	}

	protected void predefinedDescriptionSelected(String description) {
	}

	protected abstract void buildStatementFields(JPanel centerPane, FocusListener focusListener, GridBagConstraints c);

	protected abstract void buildNumberField(JPanel centerPane, FocusListener focusListener, GridBagConstraints c);

	protected abstract void buildDateField(JPanel centerPane, FocusListener focusListener, GridBagConstraints c);

	/**
	 * Refreshes the mode list according to the current account.
	 * <BR>If a mode with the same name that the currently
	 * selected mode is available, it will be selected. Else, the first mode of
	 * the list will be selected.
	 */
	private void buildModes(boolean expense) {
		Mode current = modes.get();
		modes.setParameters(new ModeWidgetParams(data, getAccount(), expense));
		ComboBox combo = modes.getCombo();
		combo.setActionEnabled(false);
		if ((originalMode!=null) && (originalIsExpense==expense) && !combo.contains(originalMode) && (getAccount().indexOf(originalMode)>=0)) {
			// It is possible that the original mode of the transaction is no more available for this kind of transaction.
			// For instance, if this account had a payment mode that was usable for expenses, but that is, now, no more usable.
			// In order to allow the user to leave unchanged the original payment mode, will we add it to the available
			// payment modes.
			combo.addItem(originalMode);
		}
		// Clears the selection in order future selection to fire a selection change
		// event ... even if the same value is selected (as selectedMode and value date may be
		// changed)
		modes.set(null);
		combo.setActionEnabled(true);
		// Restore the previously selected mode, if it is still available
		if ((current != null) && (combo.contains(current))) {
			modes.set(current);
		} else {
			combo.setSelectedIndex(0);
		}
	}

	public AbstractTransaction getTransaction() {
		return (Transaction) super.getResult();
	}

	protected Mode getCurrentMode() {
		return modes.get();
	}

	class AccountsListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (!ignoreEvents) {
				LOGGER.trace("Account {} is selected", getAccount()); //$NON-NLS-1$
				buildModes(isExpense());
				if (pdc!=null) {
					description.setPredefined(pdc.getPredefined(), pdc.getUnsortedSize());
				}
			}
		}
	}

	class ReceiptListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (!ignoreEvents) {
				buildModes(e.getStateChange() == ItemEvent.DESELECTED);
			}
		}
	}

	class ModesListener implements PropertyChangeListener {
		private Object lastSelected = null;
		private boolean lastWasExpense = true;

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			Mode selected = modes.get();
			if (!(ignoreEvents || (NullUtils.areEquals(lastSelected, selected) && (isExpense()==lastWasExpense)))) {
				LOGGER.trace("Mode {} is selected", selected); //$NON-NLS-1$
				optionnalUpdatesOnModeChange();
			}
			lastSelected = selected;
			lastWasExpense = isExpense();
		}
	}

	protected void optionnalUpdatesOnModeChange() {
	}

	@Override
	protected String getOkDisabledCause() {
		if (this.amount.getValue() == null){
			return LocalizationData.get("TransactionDialog.bad.amount"); //$NON-NLS-1$
		}
		return null;
	}
	
	@Override
	public void setVisible(boolean visible) {
		String prefix = this.getClass().getCanonicalName();
		int max = Preferences.MAX_KEY_LENGTH-15;
		if (prefix.length()>max) {
			prefix = prefix.substring(prefix.length()-max);
		}
		if (visible) {
			subtransactionsPanel.restoreState(prefix);
		} else {
			subtransactionsPanel.saveState(prefix);
		}
		super.setVisible(visible);
	}
}
