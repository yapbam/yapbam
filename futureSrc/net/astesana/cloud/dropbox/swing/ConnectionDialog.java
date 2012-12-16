package net.astesana.cloud.dropbox.swing;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.text.MessageFormat;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Account;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.WebAuthSession;
import com.dropbox.client2.session.WebAuthSession.WebAuthInfo;

import net.astesana.ajlib.swing.Utils;
import net.astesana.ajlib.swing.dialog.AbstractDialog;
import net.yapbam.gui.Browser;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public class ConnectionDialog extends AbstractDialog<DropboxAPI<? extends WebAuthSession>, AccessTokenPair> {
	private boolean connectionHasStarted;
	private WebAuthInfo info;
	private AccessTokenPair pair;
	private JButton connectButton;
	private Account accountInfo;

	public ConnectionDialog(Window owner, DropboxAPI<? extends WebAuthSession> dropboxAPI) {
		super(owner, LocalizationData.get("dropbox.Chooser.ConnectionDialog.title"), dropboxAPI); //$NON-NLS-1$
		this.connectionHasStarted = false;
	}

	@Override
	protected JPanel createCenterPane() {
		return new ConnectionPanel(getOkButton().getText());
	}

	@Override
	protected AccessTokenPair buildResult() {
		return pair;
	}
	
	/* (non-Javadoc)
	 * @see net.astesana.ajlib.swing.dialog.AbstractDialog#confirm()
	 */
	@Override
	protected void confirm() {
		try {
			data.getSession().retrieveWebAccessToken(info.requestTokenPair);
			pair = data.getSession().getAccessTokenPair();
			accountInfo = data.accountInfo();
		} catch (DropboxUnlinkedException e) {
			// The user didn't grant the access to Dropbox
			JOptionPane.showMessageDialog(this, LocalizationData.get("dropbox.Chooser.ConnectionDialog.accessNotGranted"), LocalizationData.get("ErrorManager.title"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			connectionHasStarted = false;
			getConnectButton().setEnabled(true);
			updateOkButtonEnabled();
			return;
		} catch (DropboxException e) {
			JOptionPane.showMessageDialog(this, LocalizationData.get("dropbox.Chooser.ConnectionDialog.unexpectedError"), LocalizationData.get("ErrorManager.title"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
		}
		super.confirm();
	}

	@Override
	protected String getOkDisabledCause() {
		String btnName = LocalizationData.get("dropbox.Chooser.ConnectionDialog.startButton"); //$NON-NLS-1$
		if (!this.connectionHasStarted) return MessageFormat.format(LocalizationData.get("dropbox.Chooser.ConnectionDialog.error.processNotStarted"),btnName); //$NON-NLS-1$
		return null;
	}

	/* (non-Javadoc)
	 * @see net.astesana.ajlib.swing.dialog.AbstractDialog#createButtonsPane()
	 */
	@Override
	protected JPanel createButtonsPane() {
		JPanel panel = new JPanel();
		panel.add(getConnectButton());
		panel.add(getOkButton());
		panel.add(getCancelButton());
		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Window window = Utils.getOwnerWindow(connectButton);
				try {
					info = data.getSession().getAuthInfo();
					Browser.show(new URI(info.url), window, LocalizationData.get("dropbox.Chooser.ConnectionDialog.error.unableToLaunchBrowser.title")); //$NON-NLS-1$
					connectionHasStarted = true;
				} catch (Throwable e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(window, LocalizationData.get("dropbox.Chooser.ConnectionDialog.error.unableToLaunchBrowser.message"), LocalizationData.get("ErrorManager.title"), JOptionPane.ERROR_MESSAGE);  //$NON-NLS-1$//$NON-NLS-2$
				}
				connectButton.setEnabled(false);
				updateOkButtonEnabled();
			}
		});
		return panel;
	}
	
	private JButton getConnectButton() {
		if (connectButton==null) {
			connectButton = new JButton(LocalizationData.get("dropbox.Chooser.ConnectionDialog.startButton"));
			connectButton.setToolTipText(LocalizationData.get("dropbox.Chooser.ConnectionDialog.startButton.tooltip")); //$NON-NLS-1$
		}
		return connectButton;
	}
	
	public Account getAccountInfo() {
		return this.accountInfo;
	}
}
