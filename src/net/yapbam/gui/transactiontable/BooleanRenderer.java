package net.yapbam.gui.transactiontable;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import net.yapbam.gui.util.PaintedTable;
import net.yapbam.gui.util.TablePainter;

public class BooleanRenderer extends JCheckBox implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	public BooleanRenderer() {
		super();
		// There's a bug in Nimbus L&F, background of non opaque CellRenderers is broken
		// see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6723524
		// Setting the component opaque is a workaround
		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		TablePainter painter = ((PaintedTable) table).getPainter();
		this.setHorizontalAlignment(painter.getAlignment(table.convertColumnIndexToModel(column)));
		row = table.convertRowIndexToModel(row);
		painter.setRowLook(this, table, row, isSelected);
		if (value!=null) {
			setSelected((Boolean) value);
		}
		return this;
	}
}
