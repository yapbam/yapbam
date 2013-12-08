package net.yapbam.gui.dialogs;

import javax.swing.table.AbstractTableModel;

import net.yapbam.data.Mode;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public abstract class AbstractModeListModel extends AbstractTableModel {
	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex==0) {
			return LocalizationData.get("ModeDialog.name.short"); //$NON-NLS-1$
		} else if (columnIndex==1) {
			return LocalizationData.get("ModeDialog.forDebts.short"); //$NON-NLS-1$
		} else if (columnIndex==2) {
			return LocalizationData.get("ModeDialog.forReceipts.short"); //$NON-NLS-1$
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
		Mode mode = getMode(rowIndex);
		if (columnIndex==0) {
			return mode.getName();
		} else if (columnIndex==1) {
			return mode.getExpenseVdc()!=null;
		} else if (columnIndex==2) {
			return mode.getReceiptVdc()!=null;
		} else {
			return "?"; //$NON-NLS-1$
		}
	}

	protected abstract Mode getMode(int rowIndex);

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if ((columnIndex==1)||(columnIndex==2)) {
			return Boolean.class;
		}
		return String.class;
	}
}
