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
		if (columnIndex!=0) return Double.class;
		return Object.class;
	}

	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex==0) return LocalizationData.get("Transaction.account"); //$NON-NLS-1$
		if (columnIndex==3) return LocalizationData.get("MainFrame.CheckedBalance"); //$NON-NLS-1$
		if (columnIndex==1) return LocalizationData.get("MainFrame.CurrentBalance"); //$NON-NLS-1$
		if (columnIndex==2) return LocalizationData.get("MainFrame.FinalBalance"); //$NON-NLS-1$
		return "?"; //$NON-NLS-1$
	}

	public int getRowCount() {
		if (data==null) return 0;
		int nb = data.getAccountsNumber();
		if (nb>1) nb = nb + 2;
		return nb;
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex==data.getAccountsNumber()) {
			return null;
		}	else if (rowIndex>data.getAccountsNumber()) {
			if (columnIndex==0) return LocalizationData.get("BudgetPanel.sum"); //$NON-NLS-1$
			double result = 0.0;
			for (int i = 0; i < data.getAccountsNumber(); i++) {
				if (columnIndex==3) result += data.getAccount(i).getBalanceData().getCheckedBalance();
				else if (columnIndex==1) result += data.getAccount(i).getBalanceData().getCurrentBalance();
				else if (columnIndex==2) result += data.getAccount(i).getBalanceData().getFinalBalance();
			}
			return result;
		} else {
			if (columnIndex==0) return data.getAccount(rowIndex).getName();
			if (columnIndex==3) return data.getAccount(rowIndex).getBalanceData().getCheckedBalance();
			if (columnIndex==1) return data.getAccount(rowIndex).getBalanceData().getCurrentBalance();
			if (columnIndex==2) return data.getAccount(rowIndex).getBalanceData().getFinalBalance();
		}
		return null;
	}
}
