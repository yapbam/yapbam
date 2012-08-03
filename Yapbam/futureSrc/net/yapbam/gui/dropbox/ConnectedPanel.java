package net.yapbam.gui.dropbox;

import javax.swing.JPanel;
import javax.swing.JLabel;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

@SuppressWarnings("serial")
public class ConnectedPanel extends JPanel {
	private JLabel lblDropboxAccount;
	private JLabel accountLabel;

	/**
	 * Create the panel.
	 */
	public ConnectedPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbc_lblDropboxAccount = new GridBagConstraints();
		gbc_lblDropboxAccount.insets = new Insets(0, 0, 0, 5);
		gbc_lblDropboxAccount.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblDropboxAccount.gridx = 0;
		gbc_lblDropboxAccount.gridy = 0;
		add(getLblDropboxAccount(), gbc_lblDropboxAccount);
		GridBagConstraints gbc_accountLabel = new GridBagConstraints();
		gbc_accountLabel.weightx = 1.0;
		gbc_accountLabel.anchor = GridBagConstraints.NORTHWEST;
		gbc_accountLabel.gridx = 1;
		gbc_accountLabel.gridy = 0;
		add(getAccountLabel(), gbc_accountLabel);

	}

	private JLabel getLblDropboxAccount() {
		if (lblDropboxAccount == null) {
			lblDropboxAccount = new JLabel("Dropbox account:");
		}
		return lblDropboxAccount;
	}
	private JLabel getAccountLabel() {
		if (accountLabel == null) {
			accountLabel = new JLabel(" ");
		}
		return accountLabel;
	}

	public void setConnection(DropboxAPI<YapbamDropboxSession> dropbox) {
		try {
			getAccountLabel().setText(dropbox.accountInfo().displayName);
		} catch (DropboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
