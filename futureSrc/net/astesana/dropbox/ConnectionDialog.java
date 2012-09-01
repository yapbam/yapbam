package net.astesana.dropbox;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.text.MessageFormat;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.WebAuthSession;
import com.dropbox.client2.session.WebAuthSession.WebAuthInfo;

import net.astesana.ajlib.swing.Utils;
import net.astesana.ajlib.swing.dialog.AbstractDialog;
import net.yapbam.gui.Browser;

@SuppressWarnings("serial")
public class ConnectionDialog extends AbstractDialog<WebAuthSession, AccessTokenPair> {
	private boolean connectionHasStarted;
	private WebAuthInfo info;

	public ConnectionDialog(Window owner, String title, WebAuthSession session) {
		super(owner, title, session);
		this.connectionHasStarted = false;
	}

	@Override
	protected JPanel createCenterPane() {
		return new ConnectionPanel(getOkButton().getText());
	}

	@Override
	protected AccessTokenPair buildResult() {
		try {
			data.retrieveWebAccessToken(info.requestTokenPair);
			return data.getAccessTokenPair();
		} catch (DropboxUnlinkedException e) {
			// The user didn't grant the access to Dropbox
			e.printStackTrace();
			//TODO
//			setState(State.REJECTED);
		} catch (Throwable e) {
			JOptionPane.showMessageDialog(this, "There was something wrong", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	@Override
	protected String getOkDisabledCause() {
		String btnName = "Start Connection";
		if (!this.connectionHasStarted) return MessageFormat.format("<html>You may click the <b>{0}</b> button first</html>",btnName);
		return null;
	}

	/* (non-Javadoc)
	 * @see net.astesana.ajlib.swing.dialog.AbstractDialog#createButtonsPane()
	 */
	@Override
	protected JPanel createButtonsPane() {
		JPanel panel = new JPanel();
		final JButton connectButton = new JButton("Start Connection");
		panel.add(connectButton);
		connectButton.setToolTipText("Bla bla"); //TODO
		panel.add(getOkButton());
		panel.add(getCancelButton());
		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Window window = Utils.getOwnerWindow(connectButton);
				try {
					info = data.getAuthInfo();
					Browser.show(new URI(info.url), window, "Unable to launch browser");
					connectionHasStarted = true;
				} catch (Throwable e) {
					JOptionPane.showMessageDialog(window, "<html>Sorry, an unexpected error occurred", "Error", JOptionPane.ERROR_MESSAGE);
				}
				connectButton.setEnabled(false);
				updateOkButtonEnabled();
			}
		});
		return panel;
	}
	
	
}
