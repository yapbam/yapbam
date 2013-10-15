package net.yapbam.gui.transactiontable;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;

/**
 * A model that is able to tell what style to apply to its rows.
 * @see ObjectRenderer
 */
public interface ColoredModel { //TODO Not a very good idea. TableModel is to represent data, not GUI interface style
	/** Apply the style of a row to a renderer
	 * @param renderer The renderer to set up in order to display the row
	 * @param table the Table we are displaying
	 * @param row The row index in the model
	 * @param isSelected true if the row is currently selected
	 */
	public abstract void setRowLook (Component renderer, JTable table, int row, boolean isSelected);
	
	/** Gets the alignment of a column.
	 * @param column the column index
	 * @return a SwingConstant
	 * @see SwingConstants#LEFT
	 * @see SwingConstants#CENTER
	 * @see SwingConstants#RIGHT
	 */
	public abstract int getAlignment(int column);
}
