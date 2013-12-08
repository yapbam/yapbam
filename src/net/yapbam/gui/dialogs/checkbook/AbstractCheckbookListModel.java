package net.yapbam.gui.dialogs.checkbook;

import javax.swing.table.AbstractTableModel;

import net.yapbam.data.Checkbook;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public abstract class AbstractCheckbookListModel extends AbstractTableModel {
	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex==0) {
			return LocalizationData.get("checkbookDialog.book.short"); //$NON-NLS-1$
		} else if (columnIndex==1) {
			return LocalizationData.get("checkbookDialog.next.short"); //$NON-NLS-1$
		} else if (columnIndex==2) {
			return LocalizationData.get("checkbookDialog.remaining.short"); //$NON-NLS-1$
		} else {
			return "?"; //$NON-NLS-1$
		}
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public abstract int getRowCount();
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Checkbook book = getCheckBook(rowIndex);
		if (columnIndex==0) {
			return book.getFullNumber(book.getFirst())+"->"+book.getFullNumber(book.getLast()); //$NON-NLS-1$
		} else if (columnIndex==1) {
			return book.getFullNumber(book.getNext());
		} else if (columnIndex==2) {
			return book.getRemaining();
		} else {
			return "?"; //$NON-NLS-1$
		}
	}

	protected abstract Checkbook getCheckBook(int rowIndex);

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if ((columnIndex==2)) {
			return Integer.class;
		}
		return String.class;
	}
}
