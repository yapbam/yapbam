package net.yapbam.gui.dialogs;

import java.awt.Color;
import java.awt.Component;

import javax.swing.SwingConstants;

import com.fathzer.soft.ajlib.swing.table.JTable;

import net.yapbam.gui.transactiontable.TransactionsPreferencePanel;
import net.yapbam.gui.util.PaintedTable;
import net.yapbam.gui.util.TablePainter;

public class SubTransactionsTable extends JTable implements PaintedTable {
	private static final long serialVersionUID = 1L;
	
	private final TablePainter painter;

	public SubTransactionsTable(SubTransactionsTableModel tableModel) {
		super(tableModel);
		painter = new TablePainter() {
			private static final long serialVersionUID = 1L;

			private Color[] backColors = TransactionsPreferencePanel.getBackgroundColors(); 
			@Override
			public void setRowLook(Component renderer, javax.swing.JTable table, int row, boolean isSelected) {
				if (isSelected) {
					renderer.setBackground(table.getSelectionBackground());
					renderer.setForeground(table.getSelectionForeground());
				} else {
					boolean expense = ((SubTransactionsTableModel)table.getModel()).get(row).getAmount() < 0;
					renderer.setForeground(table.getForeground());
					renderer.setBackground(expense ? backColors[0] : backColors[1]);
				}
			}

			@Override
			public int getAlignment(int column) {
				return SwingConstants.LEFT;
			}
		};
	}

	@Override
	public TablePainter getPainter() {
		return painter;
	}
}
