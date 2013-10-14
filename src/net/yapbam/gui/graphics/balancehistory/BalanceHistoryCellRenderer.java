package net.yapbam.gui.graphics.balancehistory;

import java.awt.Color;

import javax.swing.JTable;

import net.yapbam.gui.statementview.CellRenderer;

@SuppressWarnings("serial")
public class BalanceHistoryCellRenderer extends CellRenderer {

	@Override
	protected Color getBackground(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowModel, int columnModel) {
		BalanceHistoryModel model = (BalanceHistoryModel) table.getModel();
		if (!isSelected && (((Double)model.getValueAt(rowModel, 9))<0.0)) {
			return Color.RED; //TODO
		} else {
			return super.getBackground(table, value, isSelected, hasFocus, rowModel, columnModel);
		}
	}
}
