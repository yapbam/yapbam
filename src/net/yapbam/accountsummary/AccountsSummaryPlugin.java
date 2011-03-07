package net.yapbam.accountsummary;

import javax.swing.JPanel;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;

public class AccountsSummaryPlugin extends AbstractPlugIn {
	private static final String STATE_PREFIX = "net.yapbam.accountsummary."; //$NON-NLS-1$
	private AccountsSummaryPanel panel;

	public AccountsSummaryPlugin(FilteredData data, Object state) {
		this.panel = new AccountsSummaryPanel(data.getGlobalData());
		this.setPanelTitle("Accounts summary"); //LOCAL
		this.setPanelToolTip("This tab presents a summary of your accounts"); //LOCAL
	}

	@Override
	public JPanel getPanel() {
		return this.panel;
	}

	@Override
	public void restoreState() {
		YapbamState.restoreState(panel.getTransactionsTable(), STATE_PREFIX);
	}

	@Override
	public void saveState() {
		YapbamState.saveState(panel.getTransactionsTable(), STATE_PREFIX);
	}

	@Override
	public boolean allowMenu(int menuId) {
		if (menuId==FILTER_MENU) return false;
		return super.allowMenu(menuId);
	}
}
