package net.yapbam.gui.dialogs;

import java.awt.Component;

import javax.swing.SwingConstants;
import javax.swing.table.TableModel;

import com.fathzer.soft.ajlib.swing.table.JTable;

import net.yapbam.gui.transactiontable.TransactionTablePainter;
import net.yapbam.gui.util.PaintedTable;
import net.yapbam.gui.util.TablePainter;

public class SubTransactionsTable extends JTable implements PaintedTable {
	private static final long serialVersionUID = 1L;
	
	private final TablePainter painter;

	public SubTransactionsTable(TableModel tableModel) {
		super(tableModel);
		painter = new TablePainter() {
			@Override
			public void setRowLook(Component renderer, javax.swing.JTable table, int row, boolean isSelected) {
				if (isSelected) {
					renderer.setBackground(table.getSelectionBackground());
					renderer.setForeground(table.getSelectionForeground());
				} else {
					boolean expense = ((SubTransactionsTableModel)table.getModel()).get(row).getAmount() < 0;
					renderer.setForeground(table.getForeground());
					renderer.setBackground(expense ? TransactionTablePainter.BACK_COLORS[0] : TransactionTablePainter.BACK_COLORS[1]);
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
