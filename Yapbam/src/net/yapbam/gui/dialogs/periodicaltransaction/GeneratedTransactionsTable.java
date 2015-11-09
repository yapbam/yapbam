package net.yapbam.gui.dialogs.periodicaltransaction;

import java.awt.Dimension;

import javax.swing.SwingConstants;

import com.fathzer.soft.ajlib.swing.table.JTable;

import net.yapbam.gui.transactiontable.PaintedTable;
import net.yapbam.gui.transactiontable.TablePainter;
import net.yapbam.gui.transactiontable.TransactionTablePainter;

public class GeneratedTransactionsTable extends JTable implements PaintedTable {
	private static final long serialVersionUID = 1L;
	private TablePainter painter;

	public GeneratedTransactionsTable(final GenerateTableModel model) {
		super(model);
		painter = new TransactionTablePainter() {
			@Override
			public int getAlignment(int column) {
				if (column==GenerateTableModel.AMOUNT_INDEX) {
					return SwingConstants.RIGHT;
				} else if ((column==GenerateTableModel.ACCOUNT_INDEX) || (column==GenerateTableModel.DESCRIPTION_INDEX)) {
					return SwingConstants.LEFT;
				} else {
					return SwingConstants.CENTER;
				}
			}
		};
	}

	@Override
	public TablePainter getPainter() {
		return painter;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#getPreferredScrollableViewportSize()
	 */
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		Dimension size = super.getPreferredScrollableViewportSize();
		size.width = getPreferredSize().width;
		return size;
	}
}
