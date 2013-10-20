package net.yapbam.gui.archive;

import javax.swing.JMenuItem;

import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.AbstractPlugIn;

/** Plugin that adds the ability to archive part of the data to another file. */
public class ArchivePlugin extends AbstractPlugIn {
	private GlobalData data;
	
	public ArchivePlugin(FilteredData data, Object state) {
		this.data = data.getGlobalData();
	}

	@Override
	public JMenuItem[] getMenuItem(int part) {
		if (part==AbstractPlugIn.IMPORT_EXPORT_PART) {
			return new JMenuItem[]{null, new JMenuItem(new ArchiveAction(data))};
		} else {
			return super.getMenuItem(part);
		}
	}
}
