package net.yapbam.gui.dropbox;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class DropboxFileChooser extends JPanel {
	private JPanel northPanel;
	private JPanel panel_1;
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	private JPanel panel_2;
	private JButton btnNewButton_2;
	private JList list;
	private JPanel panel_3;
	private JLabel lblNewLabel;
	private JTextField fileNameField;

	/**
	 * Create the panel.
	 */
	public DropboxFileChooser() {
		setLayout(new BorderLayout(0, 0));
		add(getNorthPanel(), BorderLayout.NORTH);
		add(getPanel_1(), BorderLayout.SOUTH);
		add(getPanel_2(), BorderLayout.CENTER);

	}

	private JPanel getNorthPanel() {
		if (northPanel == null) {
			northPanel = new ConnectionPanel();
		}
		return northPanel;
	}
	private JPanel getPanel_1() {
		if (panel_1 == null) {
			panel_1 = new JPanel();
			GridBagLayout gbl_panel_1 = new GridBagLayout();
			panel_1.setLayout(gbl_panel_1);
			GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
			gbc_btnNewButton.anchor = GridBagConstraints.EAST;
			gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
			gbc_btnNewButton.weightx = 1.0;
			gbc_btnNewButton.gridx = 0;
			gbc_btnNewButton.gridy = 0;
			panel_1.add(getBtnNewButton(), gbc_btnNewButton);
			GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
			gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
			gbc_btnNewButton_1.gridx = 1;
			gbc_btnNewButton_1.gridy = 0;
			panel_1.add(getBtnNewButton_1(), gbc_btnNewButton_1);
		}
		return panel_1;
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
	private JPanel getPanel_2() {
		if (panel_2 == null) {
			panel_2 = new JPanel();
			GridBagLayout gbl_panel_2 = new GridBagLayout();
			panel_2.setLayout(gbl_panel_2);
			
			JLabel lblHereIsThe = new JLabel("Last synchronization on ...");
			GridBagConstraints gbc_lblHereIsThe = new GridBagConstraints();
			gbc_lblHereIsThe.insets = new Insets(0, 5, 5, 5);
			gbc_lblHereIsThe.anchor = GridBagConstraints.WEST;
			gbc_lblHereIsThe.gridx = 0;
			gbc_lblHereIsThe.gridy = 0;
			panel_2.add(lblHereIsThe, gbc_lblHereIsThe);
			GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
			gbc_btnNewButton_2.anchor = GridBagConstraints.WEST;
			gbc_btnNewButton_2.insets = new Insets(0, 10, 5, 0);
			gbc_btnNewButton_2.gridx = 1;
			gbc_btnNewButton_2.gridy = 0;
			panel_2.add(getBtnNewButton_2(), gbc_btnNewButton_2);
			GridBagConstraints gbc_list = new GridBagConstraints();
			gbc_list.weightx = 1.0;
			gbc_list.fill = GridBagConstraints.BOTH;
			gbc_list.gridwidth = 0;
			gbc_list.weighty = 1.0;
			gbc_list.insets = new Insets(0, 0, 5, 0);
			gbc_list.gridx = 0;
			gbc_list.gridy = 1;
			panel_2.add(getList(), gbc_list);
			GridBagConstraints gbc_panel_3 = new GridBagConstraints();
			gbc_panel_3.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel_3.gridwidth = 0;
			gbc_panel_3.insets = new Insets(0, 5, 5, 5);
			gbc_panel_3.gridx = 0;
			gbc_panel_3.gridy = 2;
			panel_2.add(getPanel_3(), gbc_panel_3);
		}
		return panel_2;
	}
	private JButton getBtnNewButton_2() {
		if (btnNewButton_2 == null) {
			btnNewButton_2 = new JButton("Synchonize", new ImageIcon(getClass().getResource("Synchronize32.png")));
		}
		return btnNewButton_2;
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
