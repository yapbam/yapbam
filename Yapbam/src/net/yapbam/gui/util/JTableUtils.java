package net.yapbam.gui.util;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.fathzer.soft.ajlib.swing.Utils;

/** Useful methods on JTables.
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public abstract class JTableUtils {
	private JTableUtils() {
		// Prevents instantiation
	}
	
	/**
	 * This method picks column sizes in order all headers and content are
	 * visible. Be aware that JTable.setAutoResizemode may have an impact on this
	 * method.
	 * @param table
	 *          The table to resize
	 * @param maxColumnSize
	 *          the maximum size for a column (use Integer.MAX_VALUE to have this
	 *          argument ignored).
	 */
	public static void initColumnSizes(JTable table, int maxColumnSize) {
		TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
		int intercellspacing = table.getIntercellSpacing().width;
		for (int i = 0; i < table.getColumnCount(); i++) {
			TableColumn column = table.getColumnModel().getColumn(i);

			Component comp = headerRenderer.getTableCellRendererComponent(null, column.getHeaderValue(), false, false, 0, 0);
			int columnWidth = Math.min(comp.getPreferredSize().width, maxColumnSize);

			for (int j = 0; j < table.getRowCount(); j++) {
				comp = table.getDefaultRenderer(table.getModel().getColumnClass(i)).getTableCellRendererComponent(table,
						table.getModel().getValueAt(j, i), false, false, 0, i);
				columnWidth = Math.max(Math.min(comp.getPreferredSize().width + intercellspacing, maxColumnSize), columnWidth);
			}
			column.setPreferredWidth(columnWidth);
		}
	}
	
	public static void fixColumnSize(JTable table, int modelColumnIndex, int margin) {
		int width = Utils.packColumn(table, modelColumnIndex, margin);
		if (width>0) {
			// If column is visible
			TableColumn firstColumn = table.getColumnModel().getColumn(table.convertColumnIndexToView(modelColumnIndex));
			firstColumn.setMinWidth(width);
			firstColumn.setMaxWidth(width);
			firstColumn.setResizable(false);
	//		if (columIndex!=0) { // If the open/close subtransactions column is not the first one
	//			transactionTable.moveColumn(columIndex, 0);
	//			//TODO Prevent the column from being moved (unfortunatly, this seems not easy at all, see http://stackoverflow.com/questions/1155137/how-to-keep-a-single-column-from-being-reordered-in-a-jtable/ Kleopatra's answer).
	//		}
		}
	}
}
