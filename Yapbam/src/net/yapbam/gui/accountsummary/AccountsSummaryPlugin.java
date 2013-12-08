package net.yapbam.gui.accountsummary;

import java.awt.print.Printable;

import javax.swing.JPanel;
import javax.swing.JTable.PrintMode;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;

public class AccountsSummaryPlugin extends AbstractPlugIn {
	private static final String STATE_PREFIX = "net.yapbam.accountsummary."; //$NON-NLS-1$
	private AccountsSummaryPanel panel;

	public AccountsSummaryPlugin(FilteredData data, Object state) {
		this.panel = new AccountsSummaryPanel(data.getGlobalData());
		this.setPanelTitle(LocalizationData.get("AccountsSummary.title")); //$NON-NLS-1$
		this.setPanelToolTip(LocalizationData.get("AccountsSummary.tooltip")); //$NON-NLS-1$
		setPrintingSupported(true);
	}

	@Override
	public JPanel getPanel() {
		return this.panel;
	}

	@Override
	public void restoreState() {
		YapbamState.INSTANCE.restoreState(panel.getTable(), STATE_PREFIX);
	}

	@Override
	public void saveState() {
		YapbamState.INSTANCE.saveState(panel.getTable(), STATE_PREFIX);
	}

	@Override
	public boolean allowMenu(int menuId) {
		if (menuId==FILTER_MENU) {
			return false;
		}
		return super.allowMenu(menuId);
	}

	@Override
	protected Printable getPrintable() {
		return this.panel.getTable().getPrintable(PrintMode.FIT_WIDTH, null, null);
	}
}
