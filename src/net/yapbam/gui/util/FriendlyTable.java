package net.yapbam.gui.util;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;

import com.fathzer.soft.ajlib.swing.table.JTable;

import net.yapbam.gui.util.FriendlyTable.ExportFormat;
import net.yapbam.gui.widget.JLabelMenu;

/**
 * A JTable that allows columns to be hidden.
 * 
 * @author Jean-Marc Astesana <BR>
 *         License : GPL v3
 */
public class FriendlyTable extends JTable {
	private static final long serialVersionUID = 1L;

	/** Constructor. */
	public FriendlyTable() {
		super();
		this.setColumnModel(new XTableColumnModel());
	}

	/**
	 * Tests whether a table column is visible or not.
	 * 
	 * @param index the view index of the column. This index takes into account the
	 *              invisible columns.
	 * @return true if the column is visible.
	 */
	public boolean isColumnVisible(int index) {
		XTableColumnModel model = (XTableColumnModel) getColumnModel();
		return model.isColumnVisible(model.getColumn(index, false));
	}

	/**
	 * Sets the visibility of a column.
	 * 
	 * @param index   the view index of the column. This index takes into account
	 *                the invisible columns.
	 * @param visible true to make the column visible, false to hide it
	 */
	public void setColumnVisible(int index, boolean visible) {
		XTableColumnModel model = (XTableColumnModel) getColumnModel();
		model.setColumnVisible(model.getColumn(index, false), visible);
	}

	/**
	 * Gets the number of column in this table.
	 * 
	 * @param onlyVisible true if we want to ignore the hidden columns.
	 * @return an int
	 */
	public int getColumnCount(boolean onlyVisible) {
		XTableColumnModel model = (XTableColumnModel) getColumnModel();
		return model.getColumnCount(onlyVisible);
	}

	/**
	 * Gets the name of a column of this table.
	 * 
	 * @param index       The index of the column
	 * @param onlyVisible true if we want to ignore hidden columns.
	 * @return a String
	 */
	public String getColumnName(int index, boolean onlyVisible) {
		XTableColumnModel model = (XTableColumnModel) getColumnModel();
		if (onlyVisible) {
			return getColumnName(index);
		} else {
			return this.getModel().getColumnName(model.getColumn(index, onlyVisible).getModelIndex());
		}
	}

	private static final class ShowHideColumnAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private int index;
		private FriendlyTable table;

		public ShowHideColumnAction(FriendlyTable table, int i) {
			super(table.getColumnName(i, false));
			this.index = i;
			this.table = table;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean visible = !table.isColumnVisible(index);
			table.setColumnVisible(index, visible);
		}
	}

	/**
	 * A menu that interact with a friendly table to show/hide its columns.
	 * 
	 * @see JLabelMenu
	 */
	public static class ShowHideColumsMenu extends JLabelMenu {
		private static final long serialVersionUID = 1L;
		private FriendlyTable table;

		/**
		 * Constructor.
		 * 
		 * @param table The table to interact with
		 * @param title The menu title.
		 */
		public ShowHideColumsMenu(FriendlyTable table, String title) {
			super(title);
			this.table = table;
		}

		@Override
		protected void fillPopUp(JPopupMenu popup) {
			for (int i = 0; i < table.getColumnCount(false); i++) {
				JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(new ShowHideColumnAction(table, i));
				menuItem.setSelected(table.isColumnVisible(i));
				popup.add(menuItem);
			}
		}
	}

	/**
	 * The format of an export.
	 * 
	 * @see FriendlyTable#export(File, ExportFormat)
	 */
	public interface ExportFormat {
		/**
		 * Gets the header attribute of this format.
		 * 
		 * @return true if we want an header line to be output
		 */
		public boolean hasHeader();

		/**
		 * Gets the columns separator.
		 * 
		 * @return a char
		 */
		public char getSeparator();

		/**
		 * Gets the formatted view of a cell content.
		 * 
		 * @param obj The cell content.
		 * @return a String that will be output to the file.
		 */
		public String formatValue(Object obj);

		/**
		 * Exports the visible content of the table on a file.
		 * 
		 * @param table    Table to be export
		 * @param onFile   The file where to export the table
		 * @param inFormat The export format
		 * @throws IOException
		 * @see ExportFormat
		 */
		public void export(FriendlyTable table, File onFile) throws IOException;
	}
}