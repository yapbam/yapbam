package net.yapbam.gui.dialogs;

import javax.swing.table.AbstractTableModel;

import net.yapbam.data.ChequeBook;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public abstract class AbstractChequeBookListModel extends AbstractTableModel {
	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex==0) return LocalizationData.get("ChequeBookDialog.prefix.short"); //$NON-NLS-1$
		if (columnIndex==1) return LocalizationData.get("ChequeBookDialog.number.short"); //$NON-NLS-1$
		if (columnIndex==2) return LocalizationData.get("ChequeBookDialog.next.short"); //$NON-NLS-1$
		return "?"; //$NON-NLS-1$
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public abstract int getRowCount();
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ChequeBook book = getChequeBook(rowIndex);
		if (columnIndex==0) return book.getPrefix();
		if (columnIndex==1) return book.getChequesNumber();
		if (columnIndex==2) return book.getNextChequeNumber();
		return "?"; //$NON-NLS-1$
	}

	protected abstract ChequeBook getChequeBook(int rowIndex);

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if ((columnIndex==1)) return Integer.class;
		return String.class;
	}
}
