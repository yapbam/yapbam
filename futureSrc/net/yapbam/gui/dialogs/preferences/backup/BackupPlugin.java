package net.yapbam.gui.dialogs.preferences.backup;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.PreferencePanel;

public class BackupPlugin extends AbstractPlugIn {
	public BackupPlugin(FilteredData data, Object state) {
	}
	
	@Override
	public PreferencePanel getPreferencePanel() {
		return new BackupPanel();
	}
}
