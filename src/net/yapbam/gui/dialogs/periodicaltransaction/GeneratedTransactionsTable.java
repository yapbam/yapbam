package net.yapbam.gui.dialogs.periodicaltransaction;

import java.awt.Dimension;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.table.JTable;

import net.yapbam.gui.transactiontable.TransactionTablePainter;
import net.yapbam.gui.util.LinkEnabler;
import net.yapbam.gui.util.PaintedTable;
import net.yapbam.gui.util.TablePainter;

public class GeneratedTransactionsTable extends JTable implements PaintedTable {
	private static final long serialVersionUID = 1L;
	private TablePainter painter;

	public GeneratedTransactionsTable(final GenerateTableModel model) {
		super(model);
		painter = new TransactionTablePainter() {
			private static final long serialVersionUID = 1L;

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
		LinkEnabler.enable(this, GenerateTableModel.DESCRIPTION_INDEX);
		this.setAutoResizeMode(AUTO_RESIZE_OFF);
		model.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				// We use a invokeLater because table internal (typically its row sorter) is refreshed after this method is called
				// Calling directly packColums resulted in exceptions when rows were removed 
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						Utils.packColumns(GeneratedTransactionsTable.this, 2);
					}
				});
			}
		});
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
