package net.yapbam.gui.transactiontable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ObjectRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	static Color CASHIN = new Color(240,255,240);
	static Color CASHOUT = new Color(255,240,240);
	
	public ObjectRenderer () {
		super();
	}

    @Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
    		boolean hasFocus, int row, int column) {
	    ColoredModel model = (ColoredModel)table.getModel();
	    row = table.convertRowIndexToModel(row);
	    this.setHorizontalAlignment(model.getAlignment(table.convertColumnIndexToModel(column)));
		if (isSelected) {
	        setBackground(table.getSelectionBackground());
	        setForeground(table.getSelectionForeground());
	    } else {
	        boolean expense = model.isExpense(row);  	
	        setForeground(table.getForeground());
	        setBackground(expense?CASHOUT:CASHIN);
	    }
	    boolean isChecked = model.isChecked(row);
    	Font font = this.getFont().deriveFont(isChecked ? Font.ITALIC : Font.PLAIN + Font.BOLD);
    	this.setFont(font);
	    setValue(value);
    	return this;
    }
}
