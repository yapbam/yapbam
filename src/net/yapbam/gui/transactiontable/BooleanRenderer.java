package net.yapbam.gui.transactiontable;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class BooleanRenderer extends JCheckBox implements TableCellRenderer {
	private static final long serialVersionUID = 1L;
	
	public BooleanRenderer () {
		super();
	}

    @Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
    		boolean hasFocus, int row, int column) {
	    ColoredModel model = (ColoredModel)table.getModel();
	    this.setHorizontalAlignment(model.getAlignment(table.convertColumnIndexToModel(column)));
		model.setRowLook(this, table, row, isSelected, hasFocus);
	    setSelected((Boolean)value);
    	return this;
    }
}
