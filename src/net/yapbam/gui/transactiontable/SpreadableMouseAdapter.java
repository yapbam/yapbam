package net.yapbam.gui.transactiontable;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

public class SpreadableMouseAdapter extends MouseAdapter {
	// This listener waits for clicks on the "spread sub-transaction" zone
	@Override
	public void mousePressed(MouseEvent e) {
		JTable table = (JTable) e.getSource(); 
		Point p = e.getPoint();
		int viewColumn = table.columnAtPoint(p);
		int viewRow = table.rowAtPoint(p);
		// For a unknown reason, the point is sometime out of the table
		// We will trap this case in order to prevent having an exception when converting the view indexes to model ones.
		if (viewColumn<0 || viewRow<0) {
			return;
		}
		int column = table.convertColumnIndexToModel(viewColumn);
		int row = table.convertRowIndexToModel(viewRow);
		SpreadableTableModel model = (SpreadableTableModel) table.getModel();
		if ((column == model.getSpreadColumnNumber()) && (row >= 0) && model.isSpreadable(row)) {
			boolean spread = model.isSpread(row);
			model.setSpread(row, !spread);
			if (spread) {
				table.setRowHeight(viewRow, table.getRowHeight());
			} else {
				int numberOfLines = model.getSpreadLines(row);
				table.setRowHeight(viewRow, table.getRowHeight() * numberOfLines);
			}
		}
	}
}
