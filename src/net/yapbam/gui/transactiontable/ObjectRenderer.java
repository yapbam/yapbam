package net.yapbam.gui.transactiontable;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import net.yapbam.gui.util.PaintedTable;
import net.yapbam.gui.util.TablePainter;

public class ObjectRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	
	public ObjectRenderer () {
		super();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		TablePainter painter = ((PaintedTable) table).getPainter();
		row = table.convertRowIndexToModel(row);
		this.setHorizontalAlignment(painter.getAlignment(table.convertColumnIndexToModel(column)));
		painter.setRowLook(this, table, row, isSelected);
		setValue(value);
		return this;
	}
}
