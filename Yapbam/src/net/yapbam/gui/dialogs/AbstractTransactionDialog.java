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

/** This dialog allows to create or edit a transaction */
public abstract class AbstractTransactionDialog extends AbstractDialog {
	private static final long serialVersionUID = 1L;
	private static final boolean DEBUG = false;
	
	protected int selectedAccount;
	private JComboBox accounts;
	protected JTextField description;
	protected AmountWidget amount;
	protected JCheckBox receipt;
	private int selectedMode;
	private CoolJComboBox modes;
	protected CategoryPanel categories;
	protected SubtransactionListPanel subtransactionsPanel;
	
	protected GlobalData data;

	protected AbstractTransactionDialog(Window owner, String title, GlobalData data, AbstractTransaction transaction) {
		super(owner, title, data); //$NON-NLS-1$
		if (transaction!=null) setContent(transaction);
		this.data = data;
	}

	protected void setContent(AbstractTransaction transaction) {
		Account account = transaction.getAccount();
		selectedAccount = data.indexOf(account);
		accounts.setSelectedIndex(selectedAccount);
		description.setText(transaction.getDescription());
		subtransactionsPanel.fill(transaction);
		// Danger, subtransaction.fill throws Property Change events that may alter the amount field content.
		// So, set up the amountField after the subtransactions list.
		amount.setValue(Math.abs(transaction.getAmount()));
		receipt.setSelected(transaction.getAmount()>0);
		// Be aware, as its listener change the selectedMode, receipt must always be set before mode.
		selectedMode = account.findMode(transaction.getMode(), transaction.getAmount()<=0);
		modes.setSelectedIndex(selectedMode);
		categories.setCategory(transaction.getCategory());
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
	protected JPanel createCenterPane(Object data) {
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
        description = new JTextField(40);
        description.addFocusListener(focusListener);
        c.gridx=1; c.gridwidth=5; c.fill = GridBagConstraints.HORIZONTAL;
    	centerPane.add(description,c);
       
        c = new GridBagConstraints();

        c.insets = insets; c.gridx=0; c.gridy=2; c.anchor = GridBagConstraints.WEST;
    	buildDateField(centerPane, focusListener, listener, c);
        
        c.fill=GridBagConstraints.NONE; c.anchor = GridBagConstraints.WEST; c.weightx = 0;
        centerPane.add(new JLabel(LocalizationData.get("TransactionDialog.amount")),c); //$NON-NLS-1$
        amount = new AmountWidget();
        amount.addFocusListener(focusListener);
        amount.addKeyListener(listener);
        amount.setValue(new Double(0));
        amount.setToolTipText(LocalizationData.get("TransactionDialog.amount.tooltip")); //$NON-NLS-1$
        c.gridx++; c.weightx=0; c.fill = GridBagConstraints.HORIZONTAL;
        centerPane.add(amount,c);
        receipt = new JCheckBox(LocalizationData.get("TransactionDialog.receipt")); //$NON-NLS-1$
        receipt.addItemListener(new ReceiptListener());
        c.gridx++; c.weightx=0; c.anchor = GridBagConstraints.WEST;
        centerPane.add(receipt, c);
        
        c = new GridBagConstraints();
        c.insets = insets; c.gridx=0; c.gridy=3; c.anchor = GridBagConstraints.WEST;
		centerPane.add(new JLabel(LocalizationData.get("TransactionDialog.mode")), c); //$NON-NLS-1$
        modes = new CoolJComboBox();
        buildModes();
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
		buildStatementFields(centerPane, focusListener, listener, c);

		c.gridx=0; c.gridy++; c.gridwidth = 6; c.fill=GridBagConstraints.HORIZONTAL;
		centerPane.add(new JSeparator(JSeparator.HORIZONTAL),c);

		c.insets = insets; c.gridx=0; c.gridy++; c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 3; c.fill=GridBagConstraints.HORIZONTAL;
		subtransactionsPanel = new SubtransactionListPanel(this.data);
		subtransactionsPanel.addPropertyChangeListener(SubtransactionListPanel.SUM_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ((amount.getValue()!=null) && subtransactionsPanel.isAddToTransactionSelected()) {
					double diff = (Double)evt.getNewValue()-(Double)evt.getOldValue();
					if (!receipt.isSelected()) diff = -diff;
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

	protected abstract void buildStatementFields(JPanel centerPane, FocusListener focusListener, KeyListener listener, GridBagConstraints c);

	protected abstract void buildNumberField(JPanel centerPane, FocusListener focusListener, GridBagConstraints c);

	protected abstract void buildDateField(JPanel centerPane, FocusListener focusListener, KeyListener listener, GridBagConstraints c);

	private void buildModes() {
		modes.setActionEnabled(false);
		modes.removeAllItems();
		Account currentAccount = data.getAccount(selectedAccount);
		boolean expense = !receipt.isSelected();
		int nb = currentAccount.getModesNumber(expense);
		for (int i = 0; i < nb; i++) {
			modes.addItem(currentAccount.getMode(i, expense).getName());
		}
		modes.setActionEnabled(true);
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
		boolean expense = !receipt.isSelected();
		DateStepper vdc = expense ? mode.getExpenseVdc() : mode.getReceiptVdc();
		return (vdc != null)? mode : null;
	}
	
	protected Mode getCurrentMode() {
		Account account = AbstractTransactionDialog.this.data.getAccount(selectedAccount);
		return account.getMode(selectedMode, !receipt.isSelected());
	}
	
	class AccountsListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == accounts) {
				int index = accounts.getSelectedIndex();
				if (index!=selectedAccount) {
					selectedAccount = index;
					if (DEBUG) System.out.println ("Account "+selectedAccount+" is selected"); //$NON-NLS-1$ //$NON-NLS-2$
					buildModes();
					modes.setSelectedItem(0);
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
			boolean receipt = (e.getStateChange()==ItemEvent.SELECTED);
			// We invert the receipt/expense value in order to have the mode currently selected and not the same mode
			// index with another expense/receipt value ... ro an exception
			Mode current = data.getAccount(selectedAccount).getMode(selectedMode, receipt);
			// needClearMode = the currentMode does'nt support the new expense/receipt state
			boolean needClearMode = (current!=null);
			DateStepper vdc = null;
			if (needClearMode) {
				vdc = receipt?current.getReceiptVdc():current.getExpenseVdc();
				needClearMode = (vdc==null);
			}
			buildModes();
			int modeIndex = 0;
			if (!needClearMode) {
				modeIndex = data.getAccount(selectedAccount).findMode(current, !receipt);
			}
			modes.setSelectedIndex(modeIndex);
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
