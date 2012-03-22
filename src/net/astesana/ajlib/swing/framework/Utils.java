package net.astesana.ajlib.swing.framework;

import java.awt.Component;
import java.awt.Window;

import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class Utils {
	/** Gets the window which contains a component.
	 * @param component the component
	 * @return The window containing the component or null if no window contains the component.
	 */
	public static Window getOwnerWindow(Component component) {
		while ((component!=null) && !(component instanceof Window)) {
			if (component instanceof JPopupMenu) {
				component = ((JPopupMenu)component).getInvoker();
			} else {
				component = component.getParent();
			}
		}
		return (Window) component;
	}
	
	
	public static void centerWindow(Window window, Window reference) {
		window.setLocation(reference.getLocation().x+(reference.getWidth()-window.getWidth())/2, reference.getLocation().y+(reference.getHeight()-window.getHeight())/2);
	}
	
	public static void packColumns(JTable table, int margin) {
		for (int i = 0; i < table.getColumnCount(); i++) {
			packColumn(table, i, 2);
		}
	}
	
	/** Sets the preferred width of the visible column specified by vColIndex.
	 * <br>The column will be just wide enough to show the column head and the widest cell in the column.
	 * margin pixels are added to the left and right (resulting in an additional width of 2*margin pixels).
	 * @param table a table
	 * @param vColIndex the column index (model based not view based)
	 * @param margin The cells margin
	 * @return The width of the column. 
	 */
	public static int packColumn(JTable table, int vColIndex, int margin) {
		TableColumnModel colModel = table.getColumnModel();
		TableColumn col = colModel.getColumn(vColIndex);
		int width = 0; // Will get width of column
		//Get width of column header
		TableCellRenderer renderer = col.getHeaderRenderer();
		if (renderer == null) {
			renderer = table.getTableHeader().getDefaultRenderer();
		}
		Component comp = renderer.getTableCellRendererComponent( table, col.getHeaderValue(), false, false, 0, 0);
		width = comp.getPreferredSize().width;

		//Get maximum width of column data
		for (int r=0; r<table.getRowCount(); r++) {
			renderer = table.getCellRenderer(r, vColIndex);
			comp = renderer.getTableCellRendererComponent( table, table.getValueAt(r, vColIndex), false, false, r, vColIndex);
			width = Math.max(width, comp.getPreferredSize().width); 
		}
		
		// Add margin
		width += 2*margin;
		
		// Set the width
		col.setPreferredWidth(width);
		return width;
	}
}
