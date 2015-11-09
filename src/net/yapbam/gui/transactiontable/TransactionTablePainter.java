package net.yapbam.gui.transactiontable;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;

public abstract class TransactionTablePainter implements TablePainter {
	public static Color[] BACK_COLORS;
	protected TransactionTablePainter() {
		BACK_COLORS = TransactionsPreferencePanel.getBackgroundColors();
	}
	@Override
	public void setRowLook(Component renderer, JTable table, int row, boolean isSelected) {
		if (isSelected) {
			renderer.setBackground(table.getSelectionBackground());
			renderer.setForeground(table.getSelectionForeground());
		} else {
			renderer.setForeground(table.getForeground());
			if ((BACK_COLORS[0]!=null) && (BACK_COLORS[1]!=null)) {
				boolean expense = ((GenericTransactionTableModel)table.getModel()).getTransaction(row).getAmount() < 0;
				renderer.setBackground(expense ? BACK_COLORS[0] : BACK_COLORS[1]);
			} else {
				renderer.setBackground(table.getBackground());
			}
		}
	}
}
