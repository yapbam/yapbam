package net.yapbam.gui;

public class EditingOptions {
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
}
