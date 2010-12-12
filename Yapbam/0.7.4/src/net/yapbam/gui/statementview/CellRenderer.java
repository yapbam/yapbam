package net.yapbam.gui.statementview;

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
		int alignment = SwingConstants.LEFT;
		if (value instanceof Date) {
			alignment = SwingConstants.CENTER;
			value=SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, LocalizationData.getLocale()).format(value);
		} else if (value instanceof Double) {
			alignment = SwingConstants.RIGHT;
			value = LocalizationData.getCurrencyInstance().format(value);
		}
		this.setHorizontalAlignment(alignment);
		if (isSelected) {
      this.setBackground(table.getSelectionBackground());
      this.setForeground(table.getSelectionForeground());
    } else {
      this.setBackground(table.getBackground());
      this.setForeground(table.getForeground());
		}
		setValue(value);
		return this;
	}

}
