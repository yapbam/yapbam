package net.yapbam.gui.dropbox;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import java.awt.GridBagLayout;
import java.awt.Window;

import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.net.URI;
import java.text.MessageFormat;
import javax.swing.JButton;

import net.astesana.ajlib.swing.Utils;
import net.yapbam.gui.Browser;

import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.WebAuthSession;
import com.dropbox.client2.session.WebAuthSession.WebAuthInfo;

import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.text.JTextComponent;

@SuppressWarnings("serial")
public class ConnectionPanel extends JPanel {
	public enum State {
		PENDING, GRANTED, REJECTED, FAILED
	}
	
	public static final String STATE_PROPERTY = "State";
	
	private JTextComponent lblNewLabel;
	private JButton connectButton;
	private State state;
	private String userId;
	private AccessTokenPair token;
	private JLabel lblNewLabel_1;
	private JTextComponent textArea;
	
	/**
	 * Create the panel.
	 */
	public ConnectionPanel() {
		this.state = State.PENDING;
		this.userId = null;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 0;
		add(getLblNewLabel_1(), gbc_lblNewLabel_1);
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.fill = GridBagConstraints.HORIZONTAL;
		gbc_textArea.weightx = 1.0;
		gbc_textArea.insets = new Insets(0, 0, 5, 0);
		gbc_textArea.gridx = 1;
		gbc_textArea.gridy = 0;
		add(getTextArea(), gbc_textArea);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 0;
		gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel.weightx = 1.0;
		gbc_lblNewLabel.insets = new Insets(5, 5, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		add(getLblNewLabel(), gbc_lblNewLabel);
		GridBagConstraints gbc_connectButton = new GridBagConstraints();
		gbc_connectButton.gridwidth = 0;
		gbc_connectButton.weighty = 1.0;
		gbc_connectButton.anchor = GridBagConstraints.NORTH;
		gbc_connectButton.insets = new Insets(0, 0, 0, 5);
		gbc_connectButton.gridx = 0;
		gbc_connectButton.gridy = 2;
		add(getConnectButton(), gbc_connectButton);
	}

	private JLabel getLblNewLabel_1() {
		if (lblNewLabel_1 == null) {
			lblNewLabel_1 = new JLabel(UIManager.getIcon("OptionPane.informationIcon"));
		}
		return lblNewLabel_1;
	}
	
	private JTextComponent getLblNewLabel() {
		if (lblNewLabel == null) {
			String message = "<html>Yapbam will only have access to a specific folder (/Applications/Yapbam), not your whole account.<br>" +
					"<br>Click the \"<b>{0}</b>\" button when you are ready to link Yapbam to your account (requires an Internet connection)<br>" +
					"Then, you will be redirected to a browser window where Dropbox will ask you to grant access to Yapbam.<br></html>";
			message = MessageFormat.format(message, getConnectButtonName());
			lblNewLabel = buildTextComponent(message);
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
					WebAuthSession was = new YapbamDropboxSession();
					try {
						Window window = Utils.getOwnerWindow(connectButton);
						final WebAuthInfo info = was.getAuthInfo();
						Browser.show(new URI(info.url), window, "Unable to launch browser");
						JOptionPane.showMessageDialog(window, "<html>Please close this message box <b>after<b> granted access to your Dropbox account to Yapbam", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
						userId = was.retrieveWebAccessToken(info.requestTokenPair);
						token = was.getAccessTokenPair();
						setState(State.GRANTED);
					} catch (DropboxUnlinkedException e) {
						setState(State.REJECTED);
					} catch (Throwable e) {
						setState(State.FAILED);
					}
				}
			});
		}
		return connectButton;
	}
	
	private void setState(State state) {
		if (!state.equals(this.state)) {
			State old = this.state;
			this.state = state;
			firePropertyChange(STATE_PROPERTY, old, this.state);
		}
	}
	
	public State getState() {
		return this.state;
	}

	public String getUserId() {
		return userId;
	}

	public AccessTokenPair getAccessTokenPair() {
		return token;
	}
	private JTextComponent getTextArea() {
		if (textArea == null) {
			textArea = buildTextComponent("<html>Storing data to <b>Dropbox</b> requires that you authorize Yapbam to connect to your Yapbam account.</html>");
		}
		return textArea;
	}
	
	private JTextComponent buildTextComponent(String text) {
		JTextArea result = new JTextArea();
//		result.setContentType("text/html");
		result.setLineWrap(true);
		result.setText(text);
		result.setEditable(false);
		result.setOpaque(false);
		return result;
	}
}
