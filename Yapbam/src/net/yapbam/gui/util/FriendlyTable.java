package net.yapbam.gui.util;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;

import com.fathzer.soft.ajlib.swing.table.JTable;
import com.fathzer.soft.ajlib.utilities.CSVWriter;
import com.fathzer.soft.ajlib.utilities.FileUtils;

import net.yapbam.gui.widget.JLabelMenu;

/** A JTable that allows columns to be hidden.
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
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
	 * @param index the view index of the column. This index takes into account the invisible columns.
	 * @return true if the column is visible.
	 */
	public boolean isColumnVisible(int index) {
		XTableColumnModel model = (XTableColumnModel)getColumnModel();
		return model.isColumnVisible(model.getColumn(index, false));
	}
	
	/** Sets the visibility of a column.
	 * @param index the view index of the column. This index takes into account the invisible columns.
	 * @param visible true to make the column visible, false to hide it
	 */
	public void setColumnVisible(int index, boolean visible) {
		XTableColumnModel model = (XTableColumnModel)getColumnModel();
		model.setColumnVisible(model.getColumn(index, false), visible);		
	}

	/** Gets the number of column in this table.
	 * @param onlyVisible true if we want to ignore the hidden columns.
	 * @return an int
	 */
	public int getColumnCount(boolean onlyVisible) {
		XTableColumnModel model = (XTableColumnModel)getColumnModel();
		return model.getColumnCount(onlyVisible);
	}
	
	/** Gets the name of a column of this table.
	 * @param index The index of the column
	 * @param onlyVisible true if we want to ignore hidden columns.
	 * @return a String
	 */
	public String getColumnName (int index, boolean onlyVisible) {
		XTableColumnModel model = (XTableColumnModel)getColumnModel();
		if (onlyVisible) {
			return getColumnName(index);
		} else {
			return this.getModel().getColumnName(model.getColumn(index, onlyVisible).getModelIndex());
		}
	}
	
	private final class ShowHideColumnAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private int index;
		
		public ShowHideColumnAction(int i) {
			super (getColumnName(i, false));
			this.index = i;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean visible = !isColumnVisible(index);
			setColumnVisible(index, visible);
		}
	}

	/** Gets a menu that interact with this table to show/hide its columns.
	 * @param title The menu title.
	 * @return a JLabelMenu
	 * @see JLabelMenu
	 */
	public JLabelMenu getShowHideColumnsMenu(String title) {
		return new JLabelMenu(title) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void fillPopUp(JPopupMenu popup) {
				for (int i = 0; i < getColumnCount(false); i++) {
					JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(new ShowHideColumnAction(i));
					menuItem.setSelected(isColumnVisible(i));
					popup.add(menuItem);
				}
			}
		};
	}
	
	/** Exports the visible content of this table to an excel like formatted file.
	 * @param file The file where to export the table
	 * @param format The export format
	 * @throws IOException
	 * @see ExportFormat
	 */
	public void export(File file, ExportFormat format) throws IOException {
		Writer fileWriter = new FileWriter(FileUtils.getCanonical(file));
		try {
			CSVWriter out = new CSVWriter(fileWriter);
			out.setSeparator(format.getSeparator());
			int[] modelIndexes = new int[getColumnCount(false)];
			for (int colIndex=0; colIndex < getColumnCount(false); colIndex++) {
				if (isColumnVisible(colIndex)) {
					modelIndexes[colIndex] = ((XTableColumnModel)getColumnModel()).getColumn(colIndex, false).getModelIndex();
					if (format.hasHeader()) {
						out.writeCell(getModel().getColumnName(modelIndexes[colIndex]));
					}
				}
			}
			out.newLine();
			for (int rowIndex = 0; rowIndex < getRowCount(); rowIndex++) {
				int modelRowIndex = convertRowIndexToModel(rowIndex);
				for (int colIndex=0; colIndex < getColumnCount(false); colIndex++) {
					if (isColumnVisible(colIndex)) {
						Object obj = getModel().getValueAt(modelRowIndex, modelIndexes[colIndex]);
						out.writeCell(format.format(obj));
					}
				}
				out.newLine();
			}
			out.flush();
		} finally {
			fileWriter.close();
		}
	}
	
	/** The format of an export.
	 * @see FriendlyTable#export(File, ExportFormat)
	 */
	public interface ExportFormat {
		/** Gets the header attribute of this format.
		 * @return true if we want an header line to be output
		 */
		public boolean hasHeader();
		
		/** Gets the columns separator.
		 * @return a char
		 */
		public char getSeparator();
		
		/** Gets the formatted view of a cell content.
		 * @param obj The cell content.
		 * @return a String that will be output to the file.
		 */
		public String format(Object obj);
	}
}