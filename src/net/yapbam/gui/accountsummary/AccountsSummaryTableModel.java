package net.yapbam.gui.accountsummary;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import net.yapbam.data.GlobalData;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.gui.LocalizationData;

class AccountsSummaryTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private GlobalData data;
	
	AccountsSummaryTableModel(JTable table, GlobalData data) {
		super();
		this.data = data;
		this.data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				fireTableDataChanged(); //TODO
			}
		});
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return Object.class;
	}

	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int columnIndex) {
		//TODO
		if (columnIndex==0) return LocalizationData.get("Transaction.account"); //$NON-NLS-1$
		if (columnIndex==1) return "checked"; //LOCAL
		if (columnIndex==2) return "current";
		if (columnIndex==3) return "final";
		return "?"; //$NON-NLS-1$
	}

	public int getRowCount() {
		return data==null?0:data.getAccountsNumber();
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex==0) return data.getAccount(rowIndex).getName();
		if (columnIndex==1) return data.getAccount(rowIndex).getBalanceData().getCheckedBalance();
		if (columnIndex==2) return data.getAccount(rowIndex).getBalanceData().getCurrentBalance();
		if (columnIndex==3) return data.getAccount(rowIndex).getBalanceData().getFinalBalance();
		return null;
	}
}
