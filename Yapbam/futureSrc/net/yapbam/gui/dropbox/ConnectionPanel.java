package net.yapbam.gui.dropbox;

import javax.swing.JPanel;

import java.awt.Dialog.ModalityType;
import java.awt.GridBagLayout;
import java.awt.Window;

import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import javax.swing.JButton;

import net.astesana.ajlib.swing.Utils;
import net.astesana.ajlib.swing.worker.WorkInProgressFrame;
import net.astesana.ajlib.swing.worker.Worker;
import net.yapbam.gui.Browser;

import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.WebAuthSession;
import com.dropbox.client2.session.WebAuthSession.WebAuthInfo;

import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ConnectionPanel extends JPanel {
	private JLabel lblNewLabel;
	private JButton connectButton;

	/**
	 * Create the panel.
	 */
	public ConnectionPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel.weightx = 1.0;
		gbc_lblNewLabel.insets = new Insets(5, 5, 0, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(getLblNewLabel(), gbc_lblNewLabel);
		GridBagConstraints gbc_connectButton = new GridBagConstraints();
		gbc_connectButton.insets = new Insets(0, 0, 0, 5);
		gbc_connectButton.gridx = 1;
		gbc_connectButton.gridy = 0;
		add(getConnectButton(), gbc_connectButton);
	}

	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			String message = "<html>Storing data to Dropbox requires that you authorize Yapbam to connect to your Yapbam account.<br>" +
					"<br>Yapbam will only have access to a specific folder (/Applications/Yapbam), not your whole account.<br>" +
					"<br>Click the \"<b>{0}</b>\" button when you are ready to link Yapbam to your account (requires an Internet connection)<br>" +
					"Then, you will be redirected to a browser window where Dropbox will ask you to grant access to Yapbam.<br></html>";
			message = MessageFormat.format(message, getConnectButtonName());
			lblNewLabel = new JLabel(message);
		}
		return lblNewLabel;
	}

	private String getConnectButtonName() {
		return "Connect";
	}
	private JButton getConnectButton() {
		if (connectButton == null) {
			connectButton = new JButton(getConnectButtonName());
			connectButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					final WebAuthSession was = new YapbamDropboxSession();
					try {
						Window window = Utils.getOwnerWindow(connectButton);
						final WebAuthInfo info = was.getAuthInfo();
						Browser.show(new URI(info.url), window, "Unable to launch browser");
						boolean ok = false;
						while (!ok) {
							try {
								System.out.println (was.retrieveWebAccessToken(info.requestTokenPair));
								System.out.println (was.getAccessTokenPair());
								ok = true;
							} catch (DropboxUnlinkedException e) {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e1) {
								}
							}
						}
//
//						WorkInProgressFrame dialog = new WorkInProgressFrame(window, "Waiting ...", ModalityType.APPLICATION_MODAL, new Worker<AccessTokenPair, Void>() {
//							@Override
//							protected AccessTokenPair doInBackground() throws Exception {
//								System.out.println ("Let's go !!!");
//								while (true) {
//									System.out.println (was.retrieveWebAccessToken(info.requestTokenPair));
//									System.out.println (was.getAccessTokenPair());
//									break;
//								}
//								System.out.println ("We are leaving");
//								return was.getAccessTokenPair();
//							}
//						});
//						dialog.setVisible(true);
					} catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (DropboxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		}
		return connectButton;
	}
}
