package net.yapbam.gui.transactiontable;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ObjectRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	
	public ObjectRenderer () {
		super();
	}

    @Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
    		boolean hasFocus, int row, int column) {
	    ColoredModel model = (ColoredModel)table.getModel();
	    row = table.convertRowIndexToModel(row);
	    this.setHorizontalAlignment(model.getAlignment(table.convertColumnIndexToModel(column)));
	    model.setRowLook(this, table, row, isSelected);
	    setValue(value);
    	return this;
    }
}
