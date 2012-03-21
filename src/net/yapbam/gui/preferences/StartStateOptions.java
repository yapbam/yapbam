package net.yapbam.gui.preferences;

public class StartStateOptions {
	private boolean rememberFile;
	private boolean rememberFilter;
	private boolean rememberTabsOrder;
	private boolean rememberColumnsWidth;
	private boolean rememberColumnsOrder;
	private boolean rememberHiddenColumns;
	
	public StartStateOptions(boolean rememberFile, boolean rememberFilter, boolean rememberTabsOrder, boolean rememberColumnsWidth, boolean rememberColumnsOrder, boolean rememberHiddenColumns) {
		this.rememberFile = rememberFile;
		this.rememberFilter = rememberFilter;
		this.rememberTabsOrder = rememberTabsOrder;
		this.rememberColumnsWidth = rememberColumnsWidth;
		this.rememberColumnsOrder = rememberColumnsOrder;
		this.rememberHiddenColumns = rememberHiddenColumns;
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
}