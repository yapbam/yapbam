package net.astesana.comptes.ihm.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;

import javax.swing.*;

import net.astesana.comptes.data.Account;
import net.astesana.comptes.data.GlobalData;
import net.astesana.comptes.ihm.LocalizationData;
import net.astesana.comptes.ihm.widget.AmountWidget;
import net.astesana.comptes.ihm.widget.AutoSelectFocusListener;

public class NewBankAccountDialog extends AbstractDialog {
	private static final long serialVersionUID = 1L;
	
	private JTextField bankAccountField;
	private AmountWidget balanceField;
	private GlobalData data;

	private NewBankAccountDialog(Window owner, String message, GlobalData data) {
		super(owner, LocalizationData.get("NewAccountDialog.title"), message); //$NON-NLS-1$
		this.data = data;
	}
	
	protected JPanel createCenterPane(Object message) {
        //Create the content pane.
        JPanel centerPane = new JPanel(new GridBagLayout());
        KeyListener listener = new AutoUpdateOkButtonKeyListener(this);
        FocusListener focusListener = new AutoSelectFocusListener();
        GridBagConstraints c = new GridBagConstraints(); c.gridx = 0; c.gridy = 0; c.insets=new Insets(5, 5, 5, 5); c.anchor=GridBagConstraints.WEST;
        if (message!=null) {
            c.fill=GridBagConstraints.HORIZONTAL; c.gridwidth=GridBagConstraints.REMAINDER;
        	centerPane.add(new JLabel((String) message), c);
            c.fill=GridBagConstraints.NONE; c.gridwidth=1;
            c.gridy++;
        }
        
        JLabel titleCompte = new JLabel(LocalizationData.get("NewBankAccountDialog.account")); //$NON-NLS-1$
        centerPane.add(titleCompte, c);
        bankAccountField = new JTextField(20);
        bankAccountField.addFocusListener(focusListener);
        bankAccountField.addKeyListener(listener);
        bankAccountField.setToolTipText(LocalizationData.get("NewBankAccountDialog.account.tooltip")); //$NON-NLS-1$
        c.weightx=1; c.fill=GridBagConstraints.HORIZONTAL; c.gridx=1;
        centerPane.add(bankAccountField,c);
        
        JLabel titleBalance = new JLabel(LocalizationData.get("NewBankAccountDialog.balance")); //$NON-NLS-1$
        c.weightx=0; c.fill=GridBagConstraints.NONE; c.gridy++; c.gridx=0;
        centerPane.add(titleBalance,c);
        balanceField = new AmountWidget();
        balanceField.addFocusListener(focusListener);
        balanceField.setValue(new Double(0));
        balanceField.addKeyListener(listener);
        balanceField.setToolTipText(LocalizationData.get("NewBankAccountDialog.balance.tooltip")); //$NON-NLS-1$
        c.weightx=1; c.fill=GridBagConstraints.HORIZONTAL; c.gridx=1;
        centerPane.add(balanceField,c);
        
        return centerPane;
    }
	
	public Account getAccount() {
		return (Account) getResult();
	}

	@Override
	protected Object buildResult() {
		Number value = (Number) this.balanceField.getValue();
		return new Account(this.bankAccountField.getText(), value.doubleValue());
	}

	/** Opens the dialog, and add the newly created account to the data
	 * @param data The global data where to append the new account
	 * @param owner The frame upon which the dialog will be displayed
	 * @param message A optional message (for instance to explain that before creating a transaction, you have to
	 * 	create an account. Null if no message is required
	 * @return The newly created account or null if the operation was canceled
	 */
	public static Account open(GlobalData data, Window owner, String message) {
		NewBankAccountDialog dialog = new NewBankAccountDialog(owner, message, data);
		dialog.setVisible(true);
		Account newAccount = dialog.getAccount();
		if (newAccount!=null) {
			data.add(newAccount);
		}
		return newAccount;
	}
	
	@Override
	protected String getOkDisabledCause() {
		String name = this.bankAccountField.getText().trim();
		if (name.length()==0) {
			return LocalizationData.get("NewBankAccountDialog.err1"); //$NON-NLS-1$
		} else if (this.data.getAccount(name)!=null) {
			return LocalizationData.get("NewBankAccountDialog.err2"); //$NON-NLS-1$
		} else if (this.balanceField.getValue()==null) {
			return LocalizationData.get("NewBankAccountDialog.err3"); //$NON-NLS-1$
		}
		return null;
	}
}
