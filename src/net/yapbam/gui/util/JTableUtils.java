package net.yapbam.gui.util;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public abstract class JTableUtils {
	
    /**
     * This method picks column sizes in order all headers and content are visible.
     * Be aware that JTable.setAutoResizemode may have an impact on this method.
     * @param table The table to resize
     * @param maxColumnSize the maximum size for a column (use Integer.MAX_VALUE to have this argument ignored).
     */
    public static void initColumnSizes(JTable table, int maxColumnSize) {
        TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
        int intercellspacing = table.getIntercellSpacing().width;
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);

            Component comp = headerRenderer.getTableCellRendererComponent(
                                 null, column.getHeaderValue(),
                                 false, false, 0, 0);
            int columnWidth = Math.min(comp.getPreferredSize().width, maxColumnSize);

            for (int j = 0; j < table.getRowCount(); j++) {
	            comp = table.getDefaultRenderer(table.getModel().getColumnClass(i)).
	                             getTableCellRendererComponent(
	                                 table, table.getModel().getValueAt(j, i),
	                                 false, false, 0, i);
	            columnWidth = Math.max(Math.min(comp.getPreferredSize().width + intercellspacing, maxColumnSize), columnWidth);
			}
            column.setPreferredWidth(columnWidth);
        }
    }
}
