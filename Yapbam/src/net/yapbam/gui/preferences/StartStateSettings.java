package net.yapbam.gui.preferences;

public class StartStateSettings {
	private boolean rememberFile;
	private boolean rememberFilter;
	private boolean rememberTabsOrder;
	private boolean rememberColumnsWidth;
	private boolean rememberColumnsOrder;
	private boolean rememberHiddenColumns;
	private boolean rememberRowsSortKeys;
	
	public StartStateSettings(boolean rememberFile, boolean rememberFilter, boolean rememberTabsOrder, boolean rememberColumnsWidth, boolean rememberColumnsOrder, boolean rememberHiddenColumns, boolean rememberRowsSortKeys) {
		this.rememberFile = rememberFile;
		this.rememberFilter = rememberFilter;
		this.rememberTabsOrder = rememberTabsOrder;
		this.rememberColumnsWidth = rememberColumnsWidth;
		this.rememberColumnsOrder = rememberColumnsOrder;
		this.rememberHiddenColumns = rememberHiddenColumns;
		this.rememberRowsSortKeys = rememberRowsSortKeys;
	}

	public boolean isRememberFile() {
		return rememberFile;
	}

	public boolean isRememberFilter() {
		return rememberFilter;
	}

	public boolean isRememberTabsOrder() {
		return rememberTabsOrder;
	}

	public boolean isRememberHiddenColumns() {
		return rememberHiddenColumns;
	}

	public boolean isRememberColumnsOrder() {
		return rememberColumnsOrder;
	}

	public boolean isRememberColumnsWidth() {
		return rememberColumnsWidth;
	}

	public boolean isRememberRowsSortKeys() {
		return rememberRowsSortKeys;
	}
}
