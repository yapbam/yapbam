package net.yapbam.gui.statementview;

import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import net.yapbam.gui.LocalizationData;

public class CellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		row = table.convertRowIndexToModel(row);
		column = table.convertColumnIndexToModel(column);
		this.setHorizontalAlignment(getAlignment(table, value, isSelected, hasFocus, row, column));
    this.setBackground(getBackground(table, value, isSelected, hasFocus, row, column));
    this.setForeground(getForeground(table, value, isSelected, hasFocus, row, column));
		setValue(getValue(table, value, isSelected, hasFocus, row, column));
		return this;
	}
	
	protected int getAlignment(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowModel, int columnModel) {
		int alignment = SwingConstants.LEFT;
		if (value instanceof Date) {
			alignment = SwingConstants.CENTER;
		} else if (value instanceof Double) {
			alignment = SwingConstants.RIGHT;
		}
		return alignment;
	}

	protected Object getValue(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowModel, int columnModel) {
		if (value instanceof Date) {
			value=SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, LocalizationData.getLocale()).format(value);
		} else if (value instanceof Double) {
			value = LocalizationData.getCurrencyInstance().format(value);
		}
		return value;
	}
	
	protected Color getBackground(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowModel, int columnModel) {
		return isSelected ? table.getSelectionBackground() : table.getBackground();
	}

	protected Color getForeground(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowModel, int columnModel) {
		return isSelected ? table.getSelectionForeground() : table.getForeground();
	}
}
