package net.yapbam.gui.filter;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemListener;
import java.text.MessageFormat;

import javax.swing.JCheckBox;
import javax.swing.border.TitledBorder;

import net.yapbam.gui.LocalizationData;

public class NatureFilterPanel extends ConsistencyCheckedPanel {
	private static final long serialVersionUID = 1L;
	private JCheckBox receipts;
	private JCheckBox expenses;
	
	public NatureFilterPanel() {
		setBorder(new TitledBorder(null, LocalizationData.get("CustomFilterPanel.nature"), //$NON-NLS-1$
				TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null, null));
		GridBagLayout gbl_Receipts_expensesPanel = new GridBagLayout();
		setLayout(gbl_Receipts_expensesPanel);
		GridBagConstraints gbc_receipts = new GridBagConstraints();
		gbc_receipts.anchor = GridBagConstraints.NORTH;
		gbc_receipts.weighty = 1.0;
		gbc_receipts.insets = new Insets(0, 0, 0, 5);
		gbc_receipts.gridx = 0;
		gbc_receipts.gridy = 0;
		add(getReceipts(), gbc_receipts);
		GridBagConstraints gbc_expenses = new GridBagConstraints();
		gbc_expenses.weighty = 1.0;
		gbc_expenses.anchor = GridBagConstraints.NORTHWEST;
		gbc_expenses.weightx = 1.0;
		gbc_expenses.gridx = 1;
		gbc_expenses.gridy = 0;
		add(getExpenses(), gbc_expenses);
	}

	@Override
	protected String computeInconsistencyCause() {
		if (!getExpenses().isSelected() && !getReceipts().isSelected()) {
			return MessageFormat.format(LocalizationData.get("CustomFilterPanel.error.natureStatus"), //$NON-NLS-1$
					LocalizationData.get("MainMenuBar.Expenses"), LocalizationData.get("MainMenuBar.Receipts")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return null;
	}
	
	private ItemListener CONSISTENCY_CHECKER_ITEM_LISTENER = new ItemListener() {
		@Override
		public void itemStateChanged(java.awt.event.ItemEvent e) {
			checkConsistency();  //  @jve:decl-index=0:
		}
	};

	private JCheckBox getReceipts() {
		if (receipts == null) {
			receipts = new JCheckBox(LocalizationData.get("MainMenuBar.Receipts")); //$NON-NLS-1$
			receipts.setToolTipText(LocalizationData.get("CustomFilterPanel.receipts.toolTip")); //$NON-NLS-1$
			receipts.addItemListener(CONSISTENCY_CHECKER_ITEM_LISTENER);
		}
		return receipts;
	}
	private JCheckBox getExpenses() {
		if (expenses == null) {
			expenses = new JCheckBox(LocalizationData.get("MainMenuBar.Expenses")); //$NON-NLS-1$
			expenses.setToolTipText(LocalizationData.get("CustomFilterPanel.expenses.toolTip")); //$NON-NLS-1$
			expenses.addItemListener(CONSISTENCY_CHECKER_ITEM_LISTENER);
		}
		return expenses;
	}

	public boolean isReceiptsSelected() {
		return getReceipts().isSelected();
	}

	public boolean isExpensesSelected() {
		return getExpenses().isSelected();
	}

	public void setSelected(boolean receipts, boolean expenses) {
		getReceipts().setSelected(receipts);
		getExpenses().setSelected(expenses);
	}
}
