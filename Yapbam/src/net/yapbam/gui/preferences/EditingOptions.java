package net.yapbam.gui.preferences;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditingOptions {
	private boolean alertOnDelete;
	private boolean alertOnModifyChecked;
	private boolean autoFillStatement;
	private boolean dateBasedAutoStatement;
	private boolean duplicateTransactionDateToCurrent;
	private SimpleDateFormat dateFormat;
	
	public EditingOptions(boolean alertOnDelete, boolean alertOnModifyChecked, boolean duplicateTransactionDateToCurrent,
			boolean autoFillStatement, boolean dateBasedAutoStatement, SimpleDateFormat dateFormat) {
		super();
		this.alertOnDelete = alertOnDelete;
		this.alertOnModifyChecked = alertOnModifyChecked;
		this.duplicateTransactionDateToCurrent = duplicateTransactionDateToCurrent;
		this.autoFillStatement = autoFillStatement;
		this.dateBasedAutoStatement = dateBasedAutoStatement;
		this.dateFormat = dateFormat;
	}

	public boolean isAlertOnDelete() {
		return alertOnDelete;
	}

	public boolean isAlertOnModifyChecked() {
		return alertOnModifyChecked;
	}

	public boolean isAutoFillStatement() {
		return autoFillStatement;
	}

	public boolean isDateBasedAutoStatement() {
		return dateBasedAutoStatement;
	}
	
	public SimpleDateFormat getStatementDateFormat() {
		return this.dateFormat;
	}
 
	public String getStatementId(Date date) {
		String result = this.dateFormat.format(date);
		// Uppercase the first character
		result = result.substring(0,1).toUpperCase() + result.substring(1);
		return result;
	}
	
	public void setAlertOnDelete(boolean alertOnDelete) {
		this.alertOnDelete = alertOnDelete;
	}

	public void setAlertOnModifyChecked(boolean alertOnModifyChecked) {
		this.alertOnModifyChecked = alertOnModifyChecked;
	}

	public boolean isDuplicateTransactionDateToCurrent() {
		return duplicateTransactionDateToCurrent;
	}
}
