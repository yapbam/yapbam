package net.yapbam.gui.archive;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.BorderLayout;

import javax.swing.JButton;

import net.yapbam.data.GlobalData;

@SuppressWarnings("serial")
public class FilterPanel extends JPanel {
	private JLabel lblWhatAccountsDo;
	private JTable table;
	private JScrollPane scrollPane;
	private JPanel accountPanel;
	private JPanel panel;
	private JButton allButton;
	private JButton noneButton;
	
	private GlobalData data;

	/**
	 * Create the panel.
	 */
	public FilterPanel() {
		this(null);
	}

	public FilterPanel(GlobalData data) {
		this.data = data;
		initialize();
	}

	private void initialize() {
		add(getAccountPanel());
	}

	private JLabel getLblWhatAccountsDo() {
		if (lblWhatAccountsDo == null) {
			lblWhatAccountsDo = new JLabel("What accounts do you want to migrate ?");
		}
		return lblWhatAccountsDo;
	}
	private JTable getTable() {
		if (table == null) {
			table = new JTable();
			table.setModel(new AccountTableModel(data));
		}
		return table;
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTable());
		}
		return scrollPane;
	}
	private JPanel getAccountPanel() {
		if (accountPanel == null) {
			accountPanel = new JPanel();
			accountPanel.setLayout(new BorderLayout(0, 0));
			accountPanel.add(getLblWhatAccountsDo(), BorderLayout.NORTH);
			accountPanel.add(getScrollPane());
			accountPanel.add(getPanel(), BorderLayout.SOUTH);
		}
		return accountPanel;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.add(getAllButton());
			panel.add(getNoneButton());
		}
		return panel;
	}
	private JButton getAllButton() {
		if (allButton == null) {
			allButton = new JButton("Select all");
		}
		return allButton;
	}
	private JButton getNoneButton() {
		if (noneButton == null) {
			noneButton = new JButton("Deselect all");
		}
		return noneButton;
	}
}
