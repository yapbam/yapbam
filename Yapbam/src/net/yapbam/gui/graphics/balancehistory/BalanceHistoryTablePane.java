package net.yapbam.gui.graphics.balancehistory;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import java.awt.GridBagLayout;
import javax.swing.JLabel;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.JLabelMenu;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;

public class BalanceHistoryTablePane extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public BalanceHistoryTablePane() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		add(scrollPane, gbc_scrollPane);
		
		JLabel lblSortBy = new JLabelMenu("Sort by:") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void fillPopUp(JPopupMenu popup) {
				boolean valueDateSelected = true; //TODO
				JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem("Value date", valueDateSelected);
				popup.add(menuItem);
				menuItem = new JRadioButtonMenuItem("Transaction's date", !valueDateSelected);
				popup.add(menuItem);
			}
		};
		GridBagConstraints gbc_lblSortBy = new GridBagConstraints();
		gbc_lblSortBy.weightx = 1.0;
		gbc_lblSortBy.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblSortBy.insets = new Insets(0, 0, 0, 5);
		gbc_lblSortBy.gridx = 0;
		gbc_lblSortBy.gridy = 1;
		add(lblSortBy, gbc_lblSortBy);
		
		JLabel label = new JLabelMenu(LocalizationData.get("MainFrame.showColumns")) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void fillPopUp(JPopupMenu popup) {/*
				for (int i = 0; i < transactionTable.getColumnCount(false); i++) {
					JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(new ShowHideColumnAction(i));
					menuItem.setSelected(transactionTable.isColumnVisible(i));
					popup.add(menuItem);
				}*/
			}
		};
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.weightx = 1.0;
		gbc_label.gridx = 1;
		gbc_label.gridy = 1;
		add(label, gbc_label);

	}

}
