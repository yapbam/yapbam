package net.yapbam.gui.transfer;

import javax.swing.JMenuItem;

import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.AbstractPlugIn;

public class TransferPlugin extends AbstractPlugIn {
	private GlobalData data;
	
	public TransferPlugin(FilteredData data, Object state) {
		this.data = data.getGlobalData();
	}
	
	@Override
	public JMenuItem[] getMenuItem(int part) {
		if (part==ACCOUNTS_PART) {
			return new JMenuItem[] {null, new JMenuItem(new NewTransferAction(this.data, this))};
		} else {
			return null;
		}
	}
}
