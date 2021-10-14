package net.yapbam.gui.dialogs.export;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import net.yapbam.data.Account;
import net.yapbam.data.FilteredData;
import net.yapbam.data.SubTransaction;
import net.yapbam.data.Transaction;
import net.yapbam.export.Exporter;
import net.yapbam.export.ExportWriter;

public class DataExporter extends Exporter<DataExporterParameters, FilteredData> {
	public DataExporter(DataExporterParameters parameters) {
		super(parameters);
	}

	@Override
	public void export(FilteredData data, ExportWriter format) throws IOException {
		int[] fields = getParameters().getExportedIndexes();
		Iterator<Transaction> transactions = getParameters().isExportFilteredData() ? new FilteredTransactions(data)
				: new GlobalTransactions(data);

		format.addHeader();
		if (getParameters().isInsertHeader()) {
			// insert the header line
			format.addLineStart();
			for (int i = 0; i < fields.length; i++) {
				format.addValue(ExportTableModel.COLUMNS[fields[i]]);
			}
			format.addLineEnd();
		}
		if (getParameters().isExportInitialBalance()) {
			// Export accounts initial balance
			for (int i = 0; i < data.getGlobalData().getAccountsNumber(); i++) {
				format.addLineStart();
				Account account = data.getGlobalData().getAccount(i);
				if (data.getFilter().isOk(account) || !getParameters().isExportFilteredData()) {
					for (int j = 0; j < fields.length; j++) {
						format.addValue(format(getField(account, fields[j])));
					}
				}
				format.addLineEnd();
			}
		}
		while (transactions.hasNext()) {
			Transaction transaction = transactions.next();
			format.addLineStart();
			for (int i = 0; i < fields.length; i++) {
				format.addValue(format(getField(transaction, fields[i])));
			}
			format.addLineEnd();
			for (int j = 0; j < transaction.getSubTransactionSize(); j++) {
				SubTransaction sub = transaction.getSubTransaction(j);
				format.addLineStart();
				for (int i = 0; i < fields.length; i++) {
					format.addValue(format(getField(sub, fields[i])));
				}
				format.addLineEnd();
			}
		}
		format.addFooter();
	}
	
	private Object getField(Account account, int field) {
		if ((field==ExportTableModel.DATE_INDEX) || (field==ExportTableModel.CATEGORY_INDEX) ||
				(field==ExportTableModel.MODE_INDEX) || (field==ExportTableModel.NUMBER_INDEX) ||
				(field==ExportTableModel.STATEMENT_INDEX) || (field==ExportTableModel.VALUE_DATE_INDEX) ||
				(field==ExportTableModel.DESCRIPTION_INDEX) || (field==ExportTableModel.COMMENT_INDEX)) {
			return null;
		} else if (field==ExportTableModel.ACCOUNT_INDEX) {
			return account.getName();
		} else if (field==ExportTableModel.AMOUNT_INDEX) {
			return account.getInitialBalance();
		} else {
			throw new IllegalArgumentException();
		}
	}

	private Object getField(SubTransaction transaction, int field) {
		if ((field==ExportTableModel.ACCOUNT_INDEX) || (field==ExportTableModel.DATE_INDEX) ||
				(field==ExportTableModel.MODE_INDEX) || (field==ExportTableModel.NUMBER_INDEX) ||
				(field==ExportTableModel.STATEMENT_INDEX) || (field==ExportTableModel.VALUE_DATE_INDEX)
				|| (field==ExportTableModel.COMMENT_INDEX)) {
			return null;
		} else if (field==ExportTableModel.AMOUNT_INDEX) {
			return transaction.getAmount();
		} else if (field==ExportTableModel.CATEGORY_INDEX) {
			return transaction.getCategory().getName();
		} else if (field==ExportTableModel.DESCRIPTION_INDEX) {
			return transaction.getDescription();
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	private Object getField(Transaction transaction, int field) {
		if (field==ExportTableModel.ACCOUNT_INDEX) {
			return transaction.getAccount().getName();
		} else if (field==ExportTableModel.AMOUNT_INDEX) {
			return transaction.getAmount();
		} else if (field==ExportTableModel.CATEGORY_INDEX) {
			return transaction.getCategory().getName();
		} else if (field==ExportTableModel.DATE_INDEX) {
			return transaction.getDate();
		} else if (field==ExportTableModel.DESCRIPTION_INDEX) {
			return transaction.getDescription();
		} else if (field==ExportTableModel.COMMENT_INDEX) {
			return transaction.getComment();
		} else if (field==ExportTableModel.MODE_INDEX) {
			return transaction.getMode().getName();
		} else if (field==ExportTableModel.NUMBER_INDEX) {
			return transaction.getNumber();
		} else if (field==ExportTableModel.STATEMENT_INDEX) {
			return transaction.getStatement();
		} else if (field==ExportTableModel.VALUE_DATE_INDEX) {
			return transaction.getValueDate();
		} else {
			throw new IllegalArgumentException();
		}
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
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
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
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			Transaction transaction = data.getTransaction(index);
			index++;
			return transaction;
		}
	}
}
