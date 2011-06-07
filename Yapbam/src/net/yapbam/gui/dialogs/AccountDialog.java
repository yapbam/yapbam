package net.yapbam.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.KeyListener;

import javax.swing.*;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.AbstractDialog;
import net.yapbam.gui.widget.AmountWidget;
import net.yapbam.gui.widget.AutoSelectFocusListener;

public class AccountDialog extends AbstractDialog<String, Account> {
	private static final long serialVersionUID = 1L;
	
	private JTextField bankAccountField;
	private AmountWidget balanceField;
	private GlobalData globalData;
	private ModeListPanel modesPanel;
	private String initialName;

	public AccountDialog(Window owner, String message, GlobalData data) {
		super(owner, LocalizationData.get("AccountDialog.title.new"), message); //$NON-NLS-1$
		this.globalData = data;
		this.initialName = null;
	}
	
	@Override
	protected JPanel createCenterPane() {
		// Create the content pane.
		JPanel northPanel = new JPanel(new GridBagLayout());
		KeyListener listener = new AutoUpdateOkButtonKeyListener(this);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.WEST;
		if (data != null) {
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = GridBagConstraints.REMAINDER;
			northPanel.add(new JLabel(data), c);
			c.fill = GridBagConstraints.NONE;
			c.gridwidth = 1;
			c.gridy++;
		}

		JLabel titleCompte = new JLabel(LocalizationData.get("AccountDialog.account")); //$NON-NLS-1$
		northPanel.add(titleCompte, c);
		bankAccountField = new JTextField(20);
		bankAccountField.addFocusListener(AutoSelectFocusListener.INSTANCE);
		bankAccountField.addKeyListener(listener);
		bankAccountField.setToolTipText(LocalizationData.get("AccountDialog.account.tooltip")); //$NON-NLS-1$
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		northPanel.add(bankAccountField, c);

		JLabel titleBalance = new JLabel(LocalizationData.get("AccountDialog.balance")); //$NON-NLS-1$
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		c.gridy++;
		c.gridx = 0;
		northPanel.add(titleBalance, c);
		balanceField = new AmountWidget(LocalizationData.getLocale());
		balanceField.addFocusListener(AutoSelectFocusListener.INSTANCE);
		balanceField.setValue(new Double(0));
		balanceField.addKeyListener(listener);
		balanceField.setToolTipText(LocalizationData.get("AccountDialog.balance.tooltip")); //$NON-NLS-1$
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		northPanel.add(balanceField, c);

		JPanel centerPane = new JPanel(new BorderLayout());

		modesPanel = new ModeListPanel();
		modesPanel.setBorder(BorderFactory.createTitledBorder(LocalizationData.get("AccountDialog.modes.border.title"))); //$NON-NLS-1$
		centerPane.add(northPanel, BorderLayout.NORTH);
		centerPane.add(modesPanel, BorderLayout.CENTER);
		modesPanel.setVisible(false); // TODO If we decide to keep it invisible, we could remove all stuff related to this panel

		return centerPane;
	}
	
	public void setContent(Account account) {
		this.setTitle(LocalizationData.get("AccountDialog.title.edit")); //$NON-NLS-1$
		this.initialName = account.getName();
		this.bankAccountField.setText(this.initialName);
		this.balanceField.setValue(account.getInitialBalance());
		this.modesPanel.setContent(account);
		this.updateOkButtonEnabled();
	}

	@Override
	protected Account buildResult() {
		Number value = (Number) this.balanceField.getValue();
		return new Account(this.bankAccountField.getText(), value.doubleValue(), modesPanel.getModes());
	}

	/** Opens the dialog, and add the newly created account to the data
	 * @param data The global data where to append the new account
	 * @param owner The frame upon which the dialog will be displayed
	 * @param message A optional message (for instance to explain that before creating a transaction, you have to
	 * 	create an account. Null if no message is required
	 * @return The newly created account or null if the operation was canceled
	 */
	public static Account open(GlobalData data, Window owner, String message) {
		AccountDialog dialog = new AccountDialog(owner, message, data);
		dialog.setVisible(true);
		Account newAccount = dialog.buildResult();
		if (newAccount!=null) {
			data.add(newAccount);
		}
		return newAccount;
	}
	
	@Override
	protected String getOkDisabledCause() {
		String name = this.bankAccountField.getText().trim();
		if (name.length()==0) {
			return LocalizationData.get("AccountDialog.err1"); //$NON-NLS-1$
		} else if ((this.globalData.getAccount(name)!=null) && !name.equalsIgnoreCase(this.initialName)) {
			return LocalizationData.get("AccountDialog.err2"); //$NON-NLS-1$
		} else if (this.balanceField.getValue()==null) {
			return LocalizationData.get("AccountDialog.err3"); //$NON-NLS-1$
		}
		return null;
	}
}
