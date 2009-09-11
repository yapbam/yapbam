package net.yapbam.ihm.transactiontable;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

public class SpreadableMouseAdapter extends MouseAdapter {
	// This listener waits for clicks on the "spread sub-transaction" zone
	public void mousePressed(MouseEvent e) {
		JTable table = (JTable) e.getSource(); 
		Point p = e.getPoint();
		int column = table.convertColumnIndexToModel(table.columnAtPoint(p));
		int row = table.rowAtPoint(p);
		SpreadableTableModel model = (SpreadableTableModel) table.getModel();
		if ((column == model.getSpreadColumnNumber()) && (row >= 0) && model.isSpreadable(row)) {
			boolean spread = table.getRowHeight()!=table.getRowHeight(row);
			if (spread) {
				table.setRowHeight(row, table.getRowHeight());
			} else {
				int numberOfLines = model.getSpreadLines(row);
				table.setRowHeight(row, table.getRowHeight() * numberOfLines);
			}
		}
	}

}
