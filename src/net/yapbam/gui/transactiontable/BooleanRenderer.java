package net.yapbam.gui.transactiontable;

import java.awt.Component;
import java.awt.Font;

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
		if (isSelected) {
	        setBackground(table.getSelectionBackground());
	        setForeground(table.getSelectionForeground());
	    } else {
	        boolean expense = model.isExpense(row);  	
	        setForeground(table.getForeground());
	        setBackground(expense?ObjectRenderer.CASHOUT:ObjectRenderer.CASHIN);
	    }
	    boolean isChecked = model.isChecked(row);
    	Font font = this.getFont().deriveFont(isChecked ? Font.ITALIC : Font.PLAIN + Font.BOLD);
    	this.setFont(font);
	    setSelected((Boolean)value);
    	return this;
    }
}
