package net.yapbam.gui.preferences;

public class StartStateOptions {
	private boolean rememberFile;
	private boolean rememberFilter;
	private boolean rememberTabsOrder;
	private boolean rememberHiddenColumns;
	
	public StartStateOptions(boolean rememberFile, boolean rememberFilter, boolean rememberTabsOrder, boolean rememberHiddenColumns) {
		this.rememberFile = rememberFile;
		this.rememberFilter = rememberFilter;
		this.rememberTabsOrder = rememberTabsOrder;
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
}
