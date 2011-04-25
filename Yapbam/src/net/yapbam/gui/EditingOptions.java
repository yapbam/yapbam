package net.yapbam.gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditingOptions {
	public static DateFormat SHORT_FORMAT = new SimpleDateFormat("yyyyMM");  //$NON-NLS-1$
	public static DateFormat LONG_FORMAT = new SimpleDateFormat("yyyy MMMM"); //$NON-NLS-1$

	private boolean alertOnDelete;
	private boolean alertOnModifyChecked;
	private boolean autoFillStatement;
	private boolean dateBasedAutoStatement;
	private boolean longFormatAutoStatement;
	
	public EditingOptions(boolean alertOnDelete, boolean alertOnModifyChecked, boolean autoFillStatement,
			boolean dateBasedAutoStatement, boolean longFormatAutoStatement) {
		super();
		this.alertOnDelete = alertOnDelete;
		this.alertOnModifyChecked = alertOnModifyChecked;
		this.autoFillStatement = autoFillStatement;
		this.dateBasedAutoStatement = dateBasedAutoStatement;
		this.longFormatAutoStatement = longFormatAutoStatement;
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

	public boolean isLongFormatStatement() {
		return longFormatAutoStatement;
	}

	public String getStatementId(Date date) {
		return (this.longFormatAutoStatement?LONG_FORMAT:SHORT_FORMAT).format(date);
	}
}
