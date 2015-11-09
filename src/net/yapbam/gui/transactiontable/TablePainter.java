package net.yapbam.gui.transactiontable;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;

public interface TablePainter {
	/** Apply the style of a row to a renderer
	 * @param renderer The renderer to set up in order to display the row
	 * @param table the Table we are displaying
	 * @param row The row index in the model
	 * @param isSelected true if the row is currently selected
	 */
	public abstract void setRowLook (Component renderer, JTable table, int row, boolean isSelected);
	
	/** Gets the alignment of a column.
	 * @param column the column model index
	 * @return a SwingConstant
	 * @see SwingConstants#LEFT
	 * @see SwingConstants#CENTER
	 * @see SwingConstants#RIGHT
	 */
	public abstract int getAlignment(int column);
}

