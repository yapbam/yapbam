package net.yapbam.gui.dropbox;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JList;
import javax.swing.JTextField;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.session.AccessTokenPair;

import net.yapbam.gui.Preferences;
import net.yapbam.gui.dropbox.ConnectionPanel.State;

@SuppressWarnings("serial")
public class DropboxFileChooser extends JPanel {
	private JPanel northPanel;
	private JPanel contentPanel;
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	private JPanel bottomPanel;
	private JList list;
	private JPanel panel_3;
	private JLabel lblNewLabel;
	private JTextField fileNameField;
	
	private DropboxAPI<YapbamDropboxSession> dropboxAPI;

	/**
	 * Create the panel.
	 */
	public DropboxFileChooser() {
		setLayout(new BorderLayout(0, 0));
		add(getNorthPanel(), BorderLayout.NORTH);
		add(getContentPanel(), BorderLayout.SOUTH);
		add(getBottomPanel(), BorderLayout.CENTER);
	}

	private JPanel getNorthPanel() {
		if (northPanel == null) {
			String accessKey = Preferences.INSTANCE.getProperty("Dropbox.access.key");
			String accessSecret = Preferences.INSTANCE.getProperty("Dropbox.access.secret");
			if (accessKey==null || accessSecret==null) {
				final ConnectionPanel connectionPanel = new ConnectionPanel();
				connectionPanel.addPropertyChangeListener(ConnectionPanel.STATE_PROPERTY, new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						ConnectionPanel.State state = (State) evt.getNewValue();
						if (state.equals(ConnectionPanel.State.FAILED)) {
							JOptionPane.showMessageDialog(DropboxFileChooser.this, "There was something wrong", "Error", JOptionPane.ERROR_MESSAGE);
						} else if (state.equals(ConnectionPanel.State.GRANTED)) {
							AccessTokenPair pair = connectionPanel.getAccessTokenPair();
							Preferences.INSTANCE.setProperty("Dropbox.access.key", pair.key);
							Preferences.INSTANCE.setProperty("Dropbox.access.secret", pair.secret);
							remove(connectionPanel);
							buildStandardPanel(pair);
							getContentPanel().setVisible(true);
							getBottomPanel().setVisible(true);
						}
					}
				});
				northPanel = connectionPanel;
				getContentPanel().setVisible(false);
				getBottomPanel().setVisible(false);
			} else {
				buildStandardPanel(new AccessTokenPair(accessKey, accessSecret));
			}
		}
		return northPanel;
	}

	private void buildStandardPanel(AccessTokenPair pair) {
		System.out.println(System.currentTimeMillis());
		YapbamDropboxSession session = new YapbamDropboxSession();
		System.out.println("After new session : "+System.currentTimeMillis());
		session.setAccessTokenPair(pair);
		System.out.println("After setAccessToken : "+System.currentTimeMillis());
		dropboxAPI = new DropboxAPI<YapbamDropboxSession>(session);
		System.out.println("After new DropbaxAPI : "+System.currentTimeMillis());
		northPanel = new ConnectedPanel();
		((ConnectedPanel)northPanel).setConnection(dropboxAPI);
		System.out.println("After panel.setConnection() : "+System.currentTimeMillis());
		add(northPanel, BorderLayout.NORTH);
	}
	
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			contentPanel = new JPanel();
			GridBagLayout gbl_contentPanel = new GridBagLayout();
			contentPanel.setLayout(gbl_contentPanel);
			GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
			gbc_btnNewButton.anchor = GridBagConstraints.EAST;
			gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
			gbc_btnNewButton.weightx = 1.0;
			gbc_btnNewButton.gridx = 0;
			gbc_btnNewButton.gridy = 0;
			contentPanel.add(getBtnNewButton(), gbc_btnNewButton);
			GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
			gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
			gbc_btnNewButton_1.gridx = 1;
			gbc_btnNewButton_1.gridy = 0;
			contentPanel.add(getBtnNewButton_1(), gbc_btnNewButton_1);
		}
		return contentPanel;
	}
	private JButton getBtnNewButton() {
		if (btnNewButton == null) {
			btnNewButton = new JButton("New button");
		}
		return btnNewButton;
	}
	private JButton getBtnNewButton_1() {
		if (btnNewButton_1 == null) {
			btnNewButton_1 = new JButton("New button");
		}
		return btnNewButton_1;
	}
	private JPanel getBottomPanel() {
		if (bottomPanel == null) {
			bottomPanel = new JPanel();
			GridBagLayout gbl_bottomPanel = new GridBagLayout();
			bottomPanel.setLayout(gbl_bottomPanel);
			GridBagConstraints gbc_list = new GridBagConstraints();
			gbc_list.weightx = 1.0;
			gbc_list.fill = GridBagConstraints.BOTH;
			gbc_list.gridwidth = 0;
			gbc_list.weighty = 1.0;
			gbc_list.insets = new Insets(0, 0, 5, 0);
			gbc_list.gridx = 0;
			gbc_list.gridy = 1;
			bottomPanel.add(getList(), gbc_list);
			GridBagConstraints gbc_panel_3 = new GridBagConstraints();
			gbc_panel_3.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel_3.gridwidth = 0;
			gbc_panel_3.insets = new Insets(0, 5, 5, 5);
			gbc_panel_3.gridx = 0;
			gbc_panel_3.gridy = 2;
			bottomPanel.add(getPanel_3(), gbc_panel_3);
		}
		return bottomPanel;
	}
	private JList getList() {
		if (list == null) {
			list = new JList();
		}
		return list;
	}
	private JPanel getPanel_3() {
		if (panel_3 == null) {
			panel_3 = new JPanel();
			panel_3.setLayout(new BorderLayout(0, 0));
			panel_3.add(getLblNewLabel(), BorderLayout.WEST);
			panel_3.add(getFileNameField(), BorderLayout.CENTER);
		}
		return panel_3;
	}
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("File: ");
		}
		return lblNewLabel;
	}
	private JTextField getFileNameField() {
		if (fileNameField == null) {
			fileNameField = new JTextField();
			fileNameField.setColumns(10);
		}
		return fileNameField;
	}
}
