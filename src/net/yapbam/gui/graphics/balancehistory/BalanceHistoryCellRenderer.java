package net.yapbam.gui.graphics.balancehistory;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.SwingConstants;

import net.yapbam.gui.util.CellRenderer;
import net.yapbam.gui.util.PaintedTable;

@SuppressWarnings("serial")
final class BalanceHistoryCellRenderer extends CellRenderer {
	private Font defaultFont;
	private Font boldFont;
	
	BalanceHistoryCellRenderer() {
	}
	
	@Override
	protected Object getValue(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowModel, int columnModel) {
		BalanceHistoryModel model = (BalanceHistoryModel) table.getModel();
		return (columnModel==model.getSettings().getRemainingColumn() && model.getHideIntermediateBalances() && !model.isDayBalance(rowModel)) ?
				"" : super.getValue(table, value, isSelected, hasFocus, rowModel, columnModel); //$NON-NLS-1$
	}

	@Override
	protected int getAlignment(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowModel, int columnModel) {
		TableSettings settings = ((BalanceHistoryModel) table.getModel()).getSettings();
		if ((columnModel == settings.getAmountColumn()) || (columnModel == settings.getReceiptColumn())
				|| (columnModel == settings.getExpenseColumn()) || (columnModel == settings.getRemainingColumn())) {
			return SwingConstants.RIGHT;
		} else if ((columnModel == settings.getAccountColumn()) || (columnModel == settings.getDescriptionColumn())) {
			return SwingConstants.LEFT;
		} else {
			return SwingConstants.CENTER;
		}
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component result = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		row = table.convertRowIndexToModel(row);
		column = table.convertColumnIndexToModel(column);
		((PaintedTable)table).getPainter().setRowLook(result, table, row, isSelected);
		BalanceHistoryModel model = (BalanceHistoryModel) table.getModel();
		TableSettings settings = model.getSettings();
		Font font = (!model.getHideIntermediateBalances() && column==settings.getRemainingColumn() && model.isDayBalance(row)) ? getBoldFont(result) : getStdFont(result);
		result.setFont(font);
		return result;
	}

	private Font getBoldFont(Component component) {
		initFonts(component);
		return boldFont;
	}

	private Font getStdFont(Component component) {
		initFonts(component);
		return defaultFont;
	}

	private void initFonts(Component component) {
		if (boldFont==null) {
			defaultFont = component.getFont();
			boldFont = defaultFont.deriveFont(Font.BOLD);
		}
	}
}
