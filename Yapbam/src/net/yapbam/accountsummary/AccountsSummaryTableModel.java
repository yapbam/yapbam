package net.yapbam.accountsummary;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import net.yapbam.data.GlobalData;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.gui.LocalizationData;

class AccountsSummaryTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private transient DateFormat dateFormater;
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
		return 1;
	}

	@Override
	public String getColumnName(int columnIndex) {
		//TODO
		if (columnIndex==0) return LocalizationData.get("Transaction.account"); //$NON-NLS-1$
		return "?"; //$NON-NLS-1$
	}

	public int getRowCount() {
		return data==null?0:data.getAccountsNumber();
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (dateFormater==null) {
			dateFormater = SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, LocalizationData.getLocale());
		}
		if (columnIndex==0) return data.getAccount(rowIndex).getName();
		return null;
	}
/*
	public void setTransactions(Transaction[] transactions) {
		this.transactions = transactions;
		this.fireTableDataChanged();
	}

	public Transaction[] getTransactions() {
		return this.transactions;
	}*/
}
