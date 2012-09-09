package net.yapbam.gui.transactiontable;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

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
		ColoredModel model = (ColoredModel) table.getModel();
		this.setHorizontalAlignment(model.getAlignment(table.convertColumnIndexToModel(column)));
		row = table.convertRowIndexToModel(row);
		model.setRowLook(this, table, row, isSelected);
		setSelected((Boolean) value);
		return this;
	}
}
