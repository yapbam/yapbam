package net.yapbam.gui.util;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public abstract class TablePainter extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

	/** Apply the style of a row to a renderer
	 * @param renderer The renderer to set up in order to display the row
	 * @param table the Table we are displaying
	 * @param row The row index in the model
	 * @param isSelected true if the row is currently selected
	 */
	public void setRowLook (Component renderer, JTable table, int row, boolean isSelected) {
		if (isSelected) {
			renderer.setForeground(table.getSelectionForeground());
			renderer.setBackground(table.getSelectionBackground());
		} else {
			// Call super in order to have background color initialized
			super.getTableCellRendererComponent(table, null, isSelected, false, table.convertRowIndexToView(row), 0);
			Color bg = getBackground();
			renderer.setForeground(getForeground());
			// We have to create a new color object because Nimbus returns
			// a color of type DerivedColor, which behaves strange, not sure why.
			renderer.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
		}
	}
	
	/** Gets the alignment of a column.
	 * @param column the column model index
	 * @return a SwingConstant
	 * @see SwingConstants#LEFT
	 * @see SwingConstants#CENTER
	 * @see SwingConstants#RIGHT
	 */
	public int getAlignment(int column) {
		return SwingConstants.LEFT;
	}
}

