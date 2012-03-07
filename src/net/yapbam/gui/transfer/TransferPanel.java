package net.yapbam.gui.transfer;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.DateWidget;
import net.yapbam.gui.widget.AmountWidget;
import java.awt.GridLayout;

public class TransferPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JPanel upperPane;
	private JPanel fromPane;
	private JPanel toPane;
	private JLabel dateLabel;
	private DateWidget dateField;
	private JLabel amountLabel;
	private AmountWidget amountField;
	private JLabel fromAccountLabel;
	private JPanel panel;
	private JLabel toAccountLabel;

	/**
	 * Create the panel.
	 */
	public TransferPanel() {

		initialize();
	}
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbc_upperPane = new GridBagConstraints();
		gbc_upperPane.insets = new Insets(0, 0, 5, 0);
		gbc_upperPane.weightx = 1.0;
		gbc_upperPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_upperPane.anchor = GridBagConstraints.NORTHWEST;
		gbc_upperPane.gridx = 0;
		gbc_upperPane.gridy = 0;
		add(getUpperPane(), gbc_upperPane);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 0;
		gbc_panel.weighty = 1.0;
		gbc_panel.weightx = 1.0;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		add(getPanel(), gbc_panel);
	}

	private JPanel getUpperPane() {
		if (upperPane == null) {
			upperPane = new JPanel();
			GridBagLayout gbl_upperPane = new GridBagLayout();
			upperPane.setLayout(gbl_upperPane);
			GridBagConstraints gbc_dateLabel = new GridBagConstraints();
			gbc_dateLabel.insets = new Insets(0, 0, 0, 5);
			gbc_dateLabel.anchor = GridBagConstraints.WEST;
			gbc_dateLabel.gridx = 0;
			gbc_dateLabel.gridy = 0;
			upperPane.add(getDateLabel(), gbc_dateLabel);
			GridBagConstraints gbc_dateField = new GridBagConstraints();
			gbc_dateField.insets = new Insets(0, 0, 0, 5);
			gbc_dateField.gridx = 1;
			gbc_dateField.gridy = 0;
			upperPane.add(getDateField(), gbc_dateField);
			GridBagConstraints gbc_amountLabel = new GridBagConstraints();
			gbc_amountLabel.insets = new Insets(0, 0, 0, 5);
			gbc_amountLabel.anchor = GridBagConstraints.EAST;
			gbc_amountLabel.gridx = 2;
			gbc_amountLabel.gridy = 0;
			upperPane.add(getAmountLabel(), gbc_amountLabel);
			GridBagConstraints gbc_amountField = new GridBagConstraints();
			gbc_amountField.gridx = 3;
			gbc_amountField.gridy = 0;
			upperPane.add(getAmountField(), gbc_amountField);
		}
		return upperPane;
	}
	private JPanel getFromPane() {
		if (fromPane == null) {
			fromPane = new JPanel();
			fromPane.setBorder(new TitledBorder(null, "From", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagLayout gbl_fromPane = new GridBagLayout();
			fromPane.setLayout(gbl_fromPane);
			GridBagConstraints gbc_fromAccountLabel = new GridBagConstraints();
			gbc_fromAccountLabel.anchor = GridBagConstraints.WEST;
			gbc_fromAccountLabel.gridx = 0;
			gbc_fromAccountLabel.gridy = 0;
			fromPane.add(getFromAccountLabel(), gbc_fromAccountLabel);
		}
		return fromPane;
	}
	private JPanel getToPane() {
		if (toPane == null) {
			toPane = new JPanel();
			toPane.setBorder(new TitledBorder(null, "to", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagLayout gbl_toPane = new GridBagLayout();
			toPane.setLayout(gbl_toPane);
			GridBagConstraints gbc_toAccountLabel = new GridBagConstraints();
			gbc_toAccountLabel.fill = GridBagConstraints.HORIZONTAL;
			gbc_toAccountLabel.anchor = GridBagConstraints.WEST;
			gbc_toAccountLabel.gridx = 1;
			gbc_toAccountLabel.gridy = 0;
			toPane.add(getToAccountLabel(), gbc_toAccountLabel);
		}
		return toPane;
	}
	private JLabel getDateLabel() {
		if (dateLabel == null) {
			dateLabel = new JLabel(LocalizationData.get("TransactionDialog.date")); //$NON-NLS-1$
		}
		return dateLabel;
	}
	private DateWidget getDateField() {
		if (dateField == null) {
			dateField = new DateWidget();
			dateField.setToolTipText(LocalizationData.get("TransactionDialog.date.tooltip")); //$NON-NLS-1$
			dateField.setColumns(10);
		}
		return dateField;
	}
	private JLabel getAmountLabel() {
		if (amountLabel == null) {
			amountLabel = new JLabel(LocalizationData.get("TransactionDialog.amount")); //$NON-NLS-1$
		}
		return amountLabel;
	}
	private AmountWidget getAmountField() {
		if (amountField == null) {
			amountField = new AmountWidget();
			amountField.setColumns(10);
		}
		return amountField;
	}
	private JLabel getFromAccountLabel() {
		if (fromAccountLabel == null) {
			fromAccountLabel = new JLabel(LocalizationData.get("AccountDialog.account")); //$NON-NLS-1$
		}
		return fromAccountLabel;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new GridLayout(0, 2, 0, 0));
			panel.add(getFromPane());
			panel.add(getToPane());
		}
		return panel;
	}
	private JLabel getToAccountLabel() {
		if (toAccountLabel == null) {
			toAccountLabel = new JLabel(LocalizationData.get("AccountDialog.account")); //$NON-NLS-1$
		}
		return toAccountLabel;
	}
}
