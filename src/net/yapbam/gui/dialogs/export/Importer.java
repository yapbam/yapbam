package net.yapbam.gui.dialogs.export;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.yapbam.data.Account;
import net.yapbam.data.Category;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Mode;
import net.yapbam.data.SubTransaction;
import net.yapbam.data.Transaction;
import net.yapbam.date.helpers.DateStepper;
import net.yapbam.gui.LocalizationData;

public class Importer {
	private File file;
	private String separator;
	private boolean ignoreFirstLine;
	private int[] importedFilecolumns;
	private Account defaultAccount;
	
	private CurrentTransaction current;
	private DateFormat dateFormatter;
	
	public Importer(File file, char separator, boolean ignoreFirstLine,
			int[] importedFilecolumns, GlobalData data, Account defaultAccount) {
		super();
		this.file = file;
		this.separator = new String(new char[]{separator});
		this.ignoreFirstLine = ignoreFirstLine;
		this.importedFilecolumns = importedFilecolumns;
		this.defaultAccount = defaultAccount;
		dateFormatter = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, LocalizationData.getLocale());
	}

	public int[] getImportedColumns() {
		return importedFilecolumns;
	}
	public ImportError[] importFile(GlobalData data) throws IOException {
		if (data!=null) data.setEventsEnabled(false);
		this.current = null;
		boolean accountPart = true;
		ArrayList<ImportError> errors = new ArrayList<ImportError>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			try {
				int lineNumber = 0;
				if (ignoreFirstLine) {
					reader.readLine();
					lineNumber++;
				}
				for (String line = reader.readLine(); line != null; line = reader.readLine()) {
					lineNumber++;
					String[] fields = line.split(separator);
					try {
						accountPart = !importLine(data, lineNumber, fields, accountPart) && accountPart;
					} catch (ImportException e) {
						errors.add(e.getError());
					}
				}
			} finally {
				try {
					if (data!=null) recordCurrentTransaction(data);
					reader.close();
				} catch (IOException e) {}
			}
		} finally {
			if (data!=null) data.setEventsEnabled(true);
		}
		return errors.toArray(new ImportError[errors.size()]);
	}

	private void recordCurrentTransaction(GlobalData data) {
		if (current!=null) {
			data.add(current.toTransaction());
			current = null;
		}
	}

	/** Import a line.
	 * @param data The data where line will be imported
	 * @param lineNumber The line number
	 * @param fields The line, converted to fields 
	 * @param accountPart The file part; true if the line is located before the first transaction
	 * @return true if the line is a transaction line (it contains a date)
	 * @throws ParseException
	 */
	private boolean importLine(GlobalData data, int lineNumber, String[] fields, boolean accountPart) throws ImportException {
		// The challenge here is to detect subtransactions and account initial balance.
		// RULES : Subtransactions and initial balance have no transaction date
		//         Every initial balances are located before the first transaction
		
		boolean[] invalidFields = new boolean[ExportTableModel.columns.length];
		boolean hasError = false;
		
		// Decoding amount
		int index = importedFilecolumns[ExportTableModel.AMOUNT_INDEX];
		double amount = 0.0;
		try {
			amount = parseAmount(getField(fields, index, "")); //$NON-NLS-1$
		} catch (ParseException e) {
			invalidFields[index] = true;
			hasError = true;
		}
		
		// Decoding date
		index = importedFilecolumns[ExportTableModel.DATE_INDEX];
		Date date = null;
		try {
			date = parseDate(getField(fields, index, "")); //$NON-NLS-1$
		} catch (ParseException e) {
			invalidFields[index] = true;			
			hasError = true;
		}
		
		boolean isTransaction = date != null;
		
		// Decoding account
		Account account = null;
		if ((isTransaction || accountPart) && !hasError) {
			index = importedFilecolumns[ExportTableModel.ACCOUNT_INDEX];
			String accountStr = getField(fields, index, ""); //$NON-NLS-1$
			if (accountStr.length()==0) { // No account specified
				accountStr = defaultAccount==null?LocalizationData.get("ImportDialog.defaultAccount"):defaultAccount.getName(); //$NON-NLS-1$
			}
			if (data!=null) {
				account =  data.getAccount(accountStr);
				if (account==null) { // New account
					account = new Account(accountStr, 0);
					data.add(account);
				}
			}
		}
		
		if (accountPart && !isTransaction) {
			if ((data!=null) && !hasError) {
				// Initial balance line => Add the amount to the account initial balance
				double initialBalance = account.getInitialBalance() + amount;
				data.setInitialBalance(account, initialBalance);
			}
		} else {
			// Description
			index = importedFilecolumns[ExportTableModel.DESCRIPTION_INDEX];
			String description = getField(fields, index, ""); //$NON-NLS-1$
					
			// Value date
			index = importedFilecolumns[ExportTableModel.VALUE_DATE_INDEX];
			Date valueDate = null;
			try {
				valueDate = parseDate(getField(fields, index, "")); //$NON-NLS-1$
				if (valueDate==null) valueDate = date;
			} catch (ParseException e) {
				invalidFields[index] = true;
				hasError = true;
			}

			if (hasError) {
				throw new ImportException(new ImportError(lineNumber, fields, invalidFields));
			}
			// Category
			index = importedFilecolumns[ExportTableModel.CATEGORY_INDEX];
			String categoryName = getField(fields, index, ""); //$NON-NLS-1$
			Category category = categoryName.length()==0?Category.UNDEFINED:data.getCategory(categoryName);
			if (category==null) {
				category = new Category(categoryName);
				data.add(category);
			}
			
			if (isTransaction) {
				// Number
				index = importedFilecolumns[ExportTableModel.NUMBER_INDEX];
				String number = getField(fields, index, null);
		
				// Statement
				index = importedFilecolumns[ExportTableModel.STATEMENT_INDEX];
				String statement = getField(fields, index, ""); //$NON-NLS-1$
				if (statement.length()==0) statement = null;
					
				// Mode
				index = importedFilecolumns[ExportTableModel.MODE_INDEX];
				String modeName = getField(fields, index, ""); //$NON-NLS-1$
				Mode mode = modeName.length()==0?Mode.UNDEFINED:account.getMode(modeName);
				if ((data!=null) && (mode==null)) {
					mode = new Mode(modeName, DateStepper.IMMEDIATE, DateStepper.IMMEDIATE, false);
					data.add(account, mode);
				}
				if (data!=null) recordCurrentTransaction(data);
				current = new CurrentTransaction(account, description, date, amount, category, mode, number, valueDate, statement);
			} else {
				current.subtransactions.add(new SubTransaction(amount, description, category));
			}
		}
		return isTransaction;
	}

	private String getField(String[] fields, int index, String defaultValue) {
		if ((index==-1) || (index>=fields.length)) return defaultValue;
		return fields[index].trim();
	}
	
	private ParsePosition ppos = new ParsePosition(0);
	private double parseAmount(String text) throws ParseException {
		NumberFormat format = NumberFormat.getCurrencyInstance(LocalizationData.getLocale());
		try {
			return format.parse(text).doubleValue();
		} catch (ParseException e) {
			format = NumberFormat.getInstance(LocalizationData.getLocale());
			ppos.setIndex(0);
			double value = format.parse(text, ppos).doubleValue();
			if (ppos.getIndex()<text.length()) throw new ParseException(text, ppos.getIndex());
			return value;
		}
	}
	
	private Date parseDate(String text) throws ParseException {
		if (text.length()==0) return null;
		return dateFormatter.parse(text);
	}
	
	static class CurrentTransaction {
		Account account;
		String description;
		Date date;
		double amount;
		Category category;
		Mode mode;
		String number;
		Date valueDate;
		String statement;
		ArrayList<SubTransaction> subtransactions;
		
		public CurrentTransaction(Account account, String description,
				Date date, double amount, Category category, Mode mode,
				String number, Date valueDate, String statement) {
			super();
			this.account = account;
			this.description = description;
			this.date = date;
			this.amount = amount;
			this.category = category;
			this.mode = mode;
			this.number = number;
			this.valueDate = valueDate;
			this.statement = statement;
			this.subtransactions = new ArrayList<SubTransaction>();
		}

		public Transaction toTransaction() {
			return new Transaction(date, number, description, amount, account, mode, category, valueDate, statement, subtransactions);
		}
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public boolean isIgnoreFirstLine() {
		return ignoreFirstLine;
	}

	public void setIgnoreFirstLine(boolean ignoreFirstLine) {
		this.ignoreFirstLine = ignoreFirstLine;
	}
	
	@SuppressWarnings("serial")
	static private class ImportException extends Exception {
		private ImportError error;

		public ImportException(ImportError error) {
			super();
			this.error = error;
		}

		public ImportError getError() {
			return error;
		}
	}

	public File getFile() {
		return file;
	}
}
