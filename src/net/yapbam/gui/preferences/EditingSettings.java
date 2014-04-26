package net.yapbam.gui.preferences;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditingSettings {
	private boolean alertOnDelete;
	private boolean alertOnModifyChecked;
	private boolean autoFillStatement;
	private boolean dateBasedAutoStatement;
	private boolean duplicateTransactionDateToCurrent;
	private SimpleDateFormat dateFormat;
	private EditionWizardSettings editionWizardSettings;
	
	public EditingSettings(boolean alertOnDelete, boolean alertOnModifyChecked, boolean duplicateTransactionDateToCurrent,
			boolean autoFillStatement, boolean dateBasedAutoStatement, SimpleDateFormat dateFormat,
			EditionWizardSettings edwSettings) {
		super();
		this.alertOnDelete = alertOnDelete;
		this.alertOnModifyChecked = alertOnModifyChecked;
		this.duplicateTransactionDateToCurrent = duplicateTransactionDateToCurrent;
		this.autoFillStatement = autoFillStatement;
		this.dateBasedAutoStatement = dateBasedAutoStatement;
		this.dateFormat = dateFormat;
		this.editionWizardSettings = edwSettings;
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

	public EditionWizardSettings getEditionWizardSettings() {
		return editionWizardSettings;
	}

	public void setEditionWizardSettings(EditionWizardSettings editionWizardSettings) {
		this.editionWizardSettings = editionWizardSettings;
	}

	public void setStatementDateFormat(SimpleDateFormat format) {
		this.dateFormat = format;
	}
}
