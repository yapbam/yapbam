package net.yapbam.gui.dialogs.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import net.yapbam.data.Account;
import net.yapbam.data.FilteredData;
import net.yapbam.data.SubTransaction;
import net.yapbam.data.Transaction;
import net.yapbam.gui.LocalizationData;

public class Exporter {
	private int[] fields;
	private boolean insertHeader;
	private char separator;
	private boolean exportInitialBalance;
	private boolean exportFilteredData;
	
	private DateFormat dateFormatter;
	private NumberFormat amountFormatter;
	
	public Exporter(int[] fields, boolean insertHeader, char separator,
			boolean exportInitialBalance, boolean exportFilteredData) {
		super();
		this.fields = fields;
		this.insertHeader = insertHeader;
		this.separator = separator;
		this.exportInitialBalance = exportInitialBalance;
		this.exportFilteredData = exportFilteredData;
		dateFormatter = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, LocalizationData.getLocale());
		amountFormatter = NumberFormat.getNumberInstance(LocalizationData.getLocale());
		amountFormatter.setMaximumFractionDigits(LocalizationData.getCurrencyInstance().getMaximumFractionDigits());
		amountFormatter.setMinimumFractionDigits(LocalizationData.getCurrencyInstance().getMinimumFractionDigits());
	}
	
	public void exportFile(File file, FilteredData data) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		try {
			if (insertHeader) {
				// insert the header line
				for (int i = 0; i < fields.length; i++) {
					if (i>0) writer.write(separator);
					writer.write(ExportTableModel.columns[fields[i]]);
				}
				writer.newLine();
			}
			if (exportInitialBalance) {
				//Export accounts initial balance
				for (int i = 0; i < data.getGlobalData().getAccountsNumber(); i++) {
					Account account = data.getGlobalData().getAccount(i);
					if (data.isOk(account) || !exportFilteredData) {
						for (int j = 0; j < fields.length; j++) {
							if (j>0) writer.write(separator);
							writer.write(getField(account, fields[j]));
						}
						writer.newLine();
					}
				}
			}
			Iterator<Transaction> transactions = exportFilteredData?new FilteredTransactions(data):new GlobalTransactions(data);
			while (transactions.hasNext()) {
				Transaction transaction = transactions.next();
				for (int i = 0; i < fields.length; i++) {
					if (i>0) writer.write(separator);
					writer.write(getField(transaction, fields[i]));
				}
				writer.newLine();
				for (int j=0;j<transaction.getSubTransactionSize();j++) {
					SubTransaction sub = transaction.getSubTransaction(j);
					for (int i = 0; i < fields.length; i++) {
						if (i>0) writer.write(separator);
						writer.write(getField(sub, fields[i]));
					}
					writer.newLine();
				}
			}
		} finally {
			writer.close();
		}
	}
	
	private String getField(Account account, int field) {
		String result = null;
		if ((field==ExportTableModel.DATE_INDEX) || (field==ExportTableModel.CATEGORY_INDEX) ||
				(field==ExportTableModel.MODE_INDEX) || (field==ExportTableModel.NUMBER_INDEX) ||
				(field==ExportTableModel.STATEMENT_INDEX) || (field==ExportTableModel.VALUE_DATE_INDEX) ||
				(field==ExportTableModel.DESCRIPTION_INDEX)) {
			result = null;
		} else if (field==ExportTableModel.ACCOUNT_INDEX) {
			result = account.getName();
		} else if (field==ExportTableModel.AMOUNT_INDEX) {
			double amount = account.getInitialBalance();
			result = format(amount);
		} else {
			throw new IllegalArgumentException();
		}
		return result==null?"":result;
	}

	private String format(double amount) {
		return amountFormatter.format(amount);
	}
	
	private String getField(SubTransaction transaction, int field) {
		String result = null;
		if ((field==ExportTableModel.ACCOUNT_INDEX) || (field==ExportTableModel.DATE_INDEX) ||
				(field==ExportTableModel.MODE_INDEX) || (field==ExportTableModel.NUMBER_INDEX) ||
				(field==ExportTableModel.STATEMENT_INDEX) || (field==ExportTableModel.VALUE_DATE_INDEX)) {
			result = null;
		} else if (field==ExportTableModel.AMOUNT_INDEX) {
			result = format(transaction.getAmount());
		} else if (field==ExportTableModel.CATEGORY_INDEX) {
			result = transaction.getCategory().getName();
		} else if (field==ExportTableModel.DESCRIPTION_INDEX) {
			result = transaction.getDescription();
		} else {
			throw new IllegalArgumentException();
		}
		return result==null?"":result;
	}
	
	private String getField(Transaction transaction, int field) {
		String result = null;
		if (field==ExportTableModel.ACCOUNT_INDEX) {
			result = transaction.getAccount().getName();
		} else if (field==ExportTableModel.AMOUNT_INDEX) {
			result = format(transaction.getAmount());
		} else if (field==ExportTableModel.CATEGORY_INDEX) {
			result = transaction.getCategory().getName();
		} else if (field==ExportTableModel.DATE_INDEX) {
			result = dateFormatter.format(transaction.getDate());
		} else if (field==ExportTableModel.DESCRIPTION_INDEX) {
			result = transaction.getDescription();
		} else if (field==ExportTableModel.MODE_INDEX) {
			result = transaction.getMode().getName();
		} else if (field==ExportTableModel.NUMBER_INDEX) {
			result = transaction.getNumber();
		} else if (field==ExportTableModel.STATEMENT_INDEX) {
			result = transaction.getStatement();
		} else if (field==ExportTableModel.VALUE_DATE_INDEX) {
			result = dateFormatter.format(transaction.getValueDate());
		} else {
			throw new IllegalArgumentException();
		}
		return result==null?"":result;
	}
	
	private static class GlobalTransactions implements Iterator<Transaction> {
		protected int index = 0;
		protected FilteredData data;
		
		private GlobalTransactions(FilteredData data) {
			this.index = 0;
			this.data = data;
		} 
		
		@Override
		public boolean hasNext() {
			return index<data.getGlobalData().getTransactionsNumber();
		}

		@Override
		public Transaction next() {
			Transaction transaction = data.getGlobalData().getTransaction(index);
			index++;
			return transaction;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	private static class FilteredTransactions extends GlobalTransactions {
		private FilteredTransactions(FilteredData data) {
			super(data);
		}

		@Override
		public boolean hasNext() {
			return index<data.getTransactionsNumber();
		}

		@Override
		public Transaction next() {
			Transaction transaction = data.getTransaction(index);
			index++;
			return transaction;
		}
		
	}
}
