package net.yapbam.gui.filter;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.border.TitledBorder;

import com.fathzer.jlocal.Formatter;

import net.yapbam.gui.LocalizationData;

public class NatureFilterPanel extends ConsistencyCheckedPanel {
	private static final long serialVersionUID = 1L;
	private JCheckBox receipts;
	private JCheckBox expenses;
	
	public NatureFilterPanel() {
		setBorder(new TitledBorder(null, LocalizationData.get("CustomFilterPanel.nature"), //$NON-NLS-1$
				TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null, null));
		GridBagLayout gblReceiptsExpensesPanel = new GridBagLayout();
		setLayout(gblReceiptsExpensesPanel);
		GridBagConstraints gbcReceipts = new GridBagConstraints();
		gbcReceipts.anchor = GridBagConstraints.NORTH;
		gbcReceipts.weighty = 1.0;
		gbcReceipts.insets = new Insets(0, 0, 0, 5);
		gbcReceipts.gridx = 0;
		gbcReceipts.gridy = 0;
		add(getReceipts(), gbcReceipts);
		GridBagConstraints gbcExpenses = new GridBagConstraints();
		gbcExpenses.weighty = 1.0;
		gbcExpenses.anchor = GridBagConstraints.NORTHWEST;
		gbcExpenses.weightx = 1.0;
		gbcExpenses.gridx = 1;
		gbcExpenses.gridy = 0;
		add(getExpenses(), gbcExpenses);
	}

	@Override
	protected String computeInconsistencyCause() {
		if (!getExpenses().isSelected() && !getReceipts().isSelected()) {
			return Formatter.format(LocalizationData.get("CustomFilterPanel.error.natureStatus"), //$NON-NLS-1$
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
	
	public void clear() {
		this.setSelected(true, true);
	}
}
