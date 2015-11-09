package net.yapbam.gui.util;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

public abstract class LinkEnabler {
	private LinkEnabler() {
		super();
	}
	
	public static void enable(JTable table, int ... columns) {
		URLTableCellRenderer linkCapableCellRenderer = new URLTableCellRenderer();
		URLTableCellEditor linkEnabledCellEditor = new URLTableCellEditor();
		for (int colIndex : columns) {
			if (colIndex>=0) {
				TableColumn column = table.getColumnModel().getColumn(colIndex);
				column.setCellRenderer(linkCapableCellRenderer);
				column.setCellEditor(linkEnabledCellEditor);
			}
		}
	}
}
