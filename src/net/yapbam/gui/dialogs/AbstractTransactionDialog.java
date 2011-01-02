package net.yapbam.gui.dialogs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

import net.yapbam.data.*;
import net.yapbam.date.helpers.DateStepper;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.AmountWidget;
import net.yapbam.gui.widget.AutoSelectFocusListener;
import net.yapbam.gui.widget.CoolJComboBox;
import net.yapbam.gui.widget.PopupTextFieldList;

/** This dialog allows to create or edit a transaction */
public abstract class AbstractTransactionDialog extends AbstractDialog<GlobalData> {
	private static final long serialVersionUID = 1L;
	private static final boolean DEBUG = false;
	
	protected int selectedAccount;
	private JComboBox accounts;
	protected PopupTextFieldList description;
	protected AmountWidget amount;
	private JCheckBox receipt;
	private int selectedMode;
	private CoolJComboBox modes;
	protected CategoryPanel categories;
	protected SubtransactionListPanel subtransactionsPanel;

	protected AbstractTransactionDialog(Window owner, String title, GlobalData data, AbstractTransaction transaction) {
		super(owner, title, data); //$NON-NLS-1$
		if (transaction!=null) setContent(transaction);
		//TODO remove
		this.pack();
		setResizable(true);
	}

	protected void setContent(AbstractTransaction transaction) {
		Account account = transaction.getAccount();
		accounts.setSelectedIndex(data.indexOf(account));
		description.setText(transaction.getDescription());
		subtransactionsPanel.fill(transaction);
		// Danger, subtransaction.fill throws Property Change events that may alter the amount field content.
		// So, always set up the amountField after the subtransactions list.
		amount.setValue(Math.abs(transaction.getAmount()));
		receipt.setSelected(transaction.getAmount()>0);
		// Be aware, as its listener change the selectedMode, receipt must always be set before mode.
		modes.setSelectedIndex(account.findMode(transaction.getMode(), transaction.getAmount()<=0));
		categories.setCategory(transaction.getCategory());
	}
	
	protected void setMode(Mode mode) {
		Account account = data.getAccount(selectedAccount);
		int index = account.findMode(mode, getAmount()<=0);
		if (index>=0) {
			modes.setSelectedIndex(index); // If the mode isn't available for this kind of transaction, do nothing.
		}
	}
	
	/** Gets the currently selected account.
	 * @return an account.
	 */
	protected Account getAccount() {
		return this.data.getAccount(selectedAccount);
	}

	/** Gets the currently selected amount.
	 * @return a double, positive if the transaction is a receipt, negative if not.
	 */
	protected double getAmount() {
		double amount = Math.abs(this.amount.getValue());
		if (isExpense()) amount = -amount;
		return amount;
	}
	
	/** Returns whether the currently edited transaction is an expense. 
	 * @return true for an expense, false for a receipt
	 */
	protected boolean isExpense() {
		return !this.receipt.isSelected();
	}

/**/	private JPanel combine (JComboBox box, JButton button) {
        JPanel pane = new JPanel(new GridBagLayout());
        Dimension dimension = box.getPreferredSize();
        button.setPreferredSize(new Dimension(dimension.height, dimension.height));
        GridBagConstraints c = new GridBagConstraints(); c.gridx = 1;
        pane.add(button, c);
        c.gridx = 0; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        pane.add(box, c);
        return pane;
	}/**/ //TODO remove
	
	@Override
	protected JPanel createCenterPane() {
		this.data = (GlobalData) data;
		
        //Create the content pane.
        JPanel centerPane = new JPanel(new GridBagLayout());
        FocusListener focusListener = new AutoSelectFocusListener();
        KeyListener listener = new AutoUpdateOkButtonKeyListener(this);

        Insets insets = new Insets(5,5,5,5);
        JLabel titleCompte = new JLabel(LocalizationData.get("AccountDialog.account")); //$NON-NLS-1$
        GridBagConstraints c = new GridBagConstraints();
		c.insets = insets; c.gridx=0; c.gridy=0; c.anchor=GridBagConstraints.WEST;
        centerPane.add(titleCompte,c);
        accounts = new JComboBox(getAccounts());
        selectedAccount = 0; //TODO let select the last selected account
        accounts.setSelectedIndex(selectedAccount);
        AccountsListener accountListener = new AccountsListener();
		accounts.addActionListener(accountListener);
        accounts.setToolTipText(LocalizationData.get("TransactionDialog.account.tooltip")); //$NON-NLS-1$
        JButton newAccount = new JButton(IconManager.NEW_ACCOUNT);
        newAccount.setFocusable(false);
        newAccount.addActionListener(accountListener);
        newAccount.setToolTipText(LocalizationData.get("TransactionDialog.account.new.tooltip")); //$NON-NLS-1$
        c.gridx=1; c.gridwidth =5; c.fill = GridBagConstraints.HORIZONTAL; c.weightx=1;
        centerPane.add(combine(accounts, newAccount), c);
        
     	JLabel titleLibelle = new JLabel(LocalizationData.get("TransactionDialog.description")); //$NON-NLS-1$
        c = new GridBagConstraints();
        c.insets = insets; c.gridx=0; c.gridy=1; c.anchor = GridBagConstraints.WEST;
		centerPane.add(titleLibelle, c);
        description = new PopupTextFieldList();
        description.setToolTipText(LocalizationData.get("TransactionDialog.description.tooltip")); //$NON-NLS-1$
		setPredefinedDescriptions();
		description.addPropertyChangeListener(PopupTextFieldList.PREDEFINED_VALUE, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getNewValue()!=null) predefinedDescriptionSelected((String) evt.getNewValue());
			}
		});
        description.addFocusListener(focusListener);
        c.gridx=1; c.gridwidth=5; c.fill = GridBagConstraints.HORIZONTAL;
    	centerPane.add(description,c);
       
        c = new GridBagConstraints();

        c.insets = insets; c.gridx=0; c.gridy=2; c.anchor = GridBagConstraints.WEST;
    	buildDateField(centerPane, focusListener, c);
        
        c.fill=GridBagConstraints.NONE; c.anchor = GridBagConstraints.WEST; c.weightx = 0;
        centerPane.add(new JLabel(LocalizationData.get("TransactionDialog.amount")),c); //$NON-NLS-1$
        amount = new AmountWidget(LocalizationData.getLocale());
        amount.addFocusListener(focusListener);
        amount.addKeyListener(listener);
        amount.setValue(new Double(0));
        amount.setToolTipText(LocalizationData.get("TransactionDialog.amount.tooltip")); //$NON-NLS-1$
        c.gridx++; c.weightx=1.0; c.fill = GridBagConstraints.HORIZONTAL;
        centerPane.add(amount,c);
        receipt = new JCheckBox(LocalizationData.get("TransactionDialog.receipt")); //$NON-NLS-1$
        receipt.setToolTipText(LocalizationData.get("TransactionDialog.receipt.tooltip")); //$NON-NLS-1$
        receipt.addItemListener(new ReceiptListener());
        c.gridx++; c.anchor = GridBagConstraints.WEST;
        centerPane.add(receipt, c);
        
        c = new GridBagConstraints();
        c.insets = insets; c.gridx=0; c.gridy=3; c.anchor = GridBagConstraints.WEST;
		centerPane.add(new JLabel(LocalizationData.get("TransactionDialog.mode")), c); //$NON-NLS-1$
        modes = new CoolJComboBox();
        buildModes(!receipt.isSelected());
        selectedMode = 0;
        ModesListener modeListener = new ModesListener();
		modes.addActionListener(modeListener);
        modes.setToolTipText(LocalizationData.get("TransactionDialog.mode.tooltip")); //$NON-NLS-1$
        c.gridx=1; c.weightx=0;
        JButton newMode = new JButton(IconManager.NEW_MODE);
        newMode.setFocusable(false);
        newMode.addActionListener(modeListener);
        newMode.setToolTipText(LocalizationData.get("TransactionDialog.mode.new.tooltip")); //$NON-NLS-1$
        centerPane.add(combine(modes, newMode), c);
        c.gridx=2;
        buildNumberField(centerPane, focusListener, c);
        centerPane.add(new JLabel(LocalizationData.get("TransactionDialog.category")),c); //$NON-NLS-1$
        categories = new CategoryPanel(this.data);
        c.gridx++; c.weightx=1; c.fill=GridBagConstraints.HORIZONTAL;
        centerPane.add(categories, c);
        
        c = new GridBagConstraints();
		c.insets = insets; c.gridx=0; c.gridy=5; c.anchor = GridBagConstraints.WEST;
		buildStatementFields(centerPane, focusListener, c);

		c.gridx=0; c.gridy++; c.gridwidth = 6; c.fill=GridBagConstraints.HORIZONTAL;
		centerPane.add(new JSeparator(JSeparator.HORIZONTAL),c);

		c.insets = insets; c.gridx=0; c.gridy++; c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1; c.fill=GridBagConstraints.BOTH; c.weightx = 1.0; c.weighty = 1.0;
		subtransactionsPanel = new SubtransactionListPanel(this.data);
		subtransactionsPanel.addPropertyChangeListener(SubtransactionListPanel.SUM_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ((amount.getValue()!=null) && subtransactionsPanel.isAddToTransactionSelected()) {
					double diff = (Double)evt.getNewValue()-(Double)evt.getOldValue();
					if (isExpense()) diff = -diff;
					double newValue = amount.getValue()+diff;
					if (newValue<0) {
						newValue = -newValue;
						receipt.setSelected(!receipt.isSelected());
					}
					amount.setValue(newValue);
				}
			}
		});
		centerPane.add(subtransactionsPanel,c);

		return centerPane;
	}
	
	protected void setPredefinedDescriptions() {}
	
	protected void predefinedDescriptionSelected(String description) {}

	protected abstract void buildStatementFields(JPanel centerPane, FocusListener focusListener, GridBagConstraints c);

	protected abstract void buildNumberField(JPanel centerPane, FocusListener focusListener, GridBagConstraints c);

	protected abstract void buildDateField(JPanel centerPane, FocusListener focusListener, GridBagConstraints c);

	/** Refresh the mode list according to the current account and receipt/expense kind of transaction.
	 * If a mode with the same name that the currently selected mode is available, it will be selected.
	 * else, the first mode of the list will be selected.
	*/
	private void buildModes(boolean expense) {
		// Prevents selection events to be sent
		modes.setActionEnabled(false);
		String current = (String) modes.getSelectedItem();
		modes.removeAllItems();
		Account currentAccount = data.getAccount(selectedAccount);
		int nb = currentAccount.getModesNumber(expense);
		for (int i = 0; i < nb; i++) {
			modes.addItem(currentAccount.getMode(i, expense).getName());
		}
		// Clears the selection in order future selection to fire a selection change event ... even
		// if the same value is selected (as selectedMode and value date may be changed)
		modes.setSelectedIndex(-1);
		selectedMode = -1;
		modes.setActionEnabled(true);
		// Restore the previously selected mode, if it is available
		int index = 0;
		if (current!=null) {
			Mode mode = currentAccount.getMode(current);
			if (mode!=null) { // If the last selected mode exists in the account for this transaction kind
				index = currentAccount.findMode(mode, expense);
			}
		}
		modes.setSelectedIndex(index>=0?index:0);
	}

	private String[] getAccounts() {
		String[] result = new String[data.getAccountsNumber()];
		for (int i = 0; i < data.getAccountsNumber(); i++) {
			result[i] = data.getAccount(i).getName();
		}
		return result;
	}

	public AbstractTransaction getTransaction() {
		return (Transaction) super.getResult();
	}

	private Mode displayNewModeDialog() {
		Account ac = data.getAccount(selectedAccount);
		Mode mode = ModeDialog.open(data, ac, this);
		if (mode==null) return null;
		DateStepper vdc = isExpense() ? mode.getExpenseVdc() : mode.getReceiptVdc();
		return (vdc != null)? mode : null;
	}
	
	protected Mode getCurrentMode() {
		Account account = AbstractTransactionDialog.this.data.getAccount(selectedAccount);
		return account.getMode(selectedMode, isExpense());
	}
	
	class AccountsListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == accounts) {
				int index = accounts.getSelectedIndex();
				if (index!=selectedAccount) {
					selectedAccount = index;
					if (DEBUG) System.out.println ("Account "+selectedAccount+" is selected"); //$NON-NLS-1$ //$NON-NLS-2$
					buildModes(isExpense());
				}
			} else {
				Account ac = AccountDialog.open(data, AbstractTransactionDialog.this, null);
				if (ac!=null) {
					accounts.addItem(ac.getName());
					accounts.setSelectedIndex(accounts.getItemCount()-1);
					pack();
				}
			}
		}
	}

	class ReceiptListener implements ItemListener{
		public void itemStateChanged(ItemEvent e) {
			buildModes(e.getStateChange()==ItemEvent.DESELECTED);
		}
	}

	class ModesListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource()==modes) {
				int index = modes.getSelectedIndex();
				if (index!=selectedMode) {
					selectedMode = index;
					if (DEBUG) System.out.println("Mode "+selectedMode+" is selected"); //$NON-NLS-1$ //$NON-NLS-2$
					optionnalUpdatesOnModeChange();
				}
			} else {
				// New mode required
				Mode m = displayNewModeDialog();
				if (m!=null) {
					modes.addItem(m.getName());
					modes.setSelectedIndex(modes.getItemCount()-1);
					pack();
				}
			}
		}
    }
	
	protected void optionnalUpdatesOnModeChange() {}

	@Override
	protected String getOkDisabledCause() {
		if (this.amount.getValue()==null) return LocalizationData.get("TransactionDialog.bad.amount"); //$NON-NLS-1$
		return null;
	}
}
