package net.yapbam.gui.transactiontable;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;

import net.yapbam.gui.util.TablePainter;

public abstract class TransactionTablePainter extends TablePainter {
	private static final long serialVersionUID = 1L;
	private Color[] backColors;
	
	protected TransactionTablePainter() {
		backColors = TransactionsPreferencePanel.getBackgroundColors();
	}
	
	@Override
	public void setRowLook(Component renderer, JTable table, int row, boolean isSelected) {
		if (isSelected || (backColors[0]==null) && (backColors[1]==null)) {
			super.setRowLook(renderer, table, row, isSelected);
		} else {
			renderer.setForeground(table.getForeground());
			boolean expense = ((TransactionsModel)table.getModel()).getTransaction(row).getAmount() < 0;
			renderer.setBackground(expense ? backColors[0] : backColors[1]);
		}
	}
}
