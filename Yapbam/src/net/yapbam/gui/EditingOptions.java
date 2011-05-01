package net.yapbam.gui;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditingOptions {
	private boolean alertOnDelete;
	private boolean alertOnModifyChecked;
	private boolean autoFillStatement;
	private boolean dateBasedAutoStatement;
	private SimpleDateFormat dateFormat;
	
	public EditingOptions(boolean alertOnDelete, boolean alertOnModifyChecked, boolean autoFillStatement,
			boolean dateBasedAutoStatement, SimpleDateFormat dateFormat/*boolean longFormatAutoStatement*/) {
		super();
		this.alertOnDelete = alertOnDelete;
		this.alertOnModifyChecked = alertOnModifyChecked;
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
}
