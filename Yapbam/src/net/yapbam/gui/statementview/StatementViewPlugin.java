package net.yapbam.gui.statementview;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.print.Printable;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import net.yapbam.data.Account;
import net.yapbam.data.FilteredData;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.TransactionSelector;
import net.yapbam.gui.YapbamState;

public class StatementViewPlugin extends AbstractPlugIn {
	private static final String STATE_PREFIX = "net.yapbam.statementView."; //$NON-NLS-1$
	private static final String UNCHECKED_PREFIX = "net.yapbam.statementView.unchecked."; //$NON-NLS-1$
	private StatementViewPanel panel;

	public StatementViewPlugin(FilteredData data, Object state) {
		this.panel = new StatementViewPanel(data);
		this.setPanelTitle(LocalizationData.get("StatementView.title")); //$NON-NLS-1$
		this.setPanelToolTip(LocalizationData.get("StatementView.tooltip")); //$NON-NLS-1$
		this.setPrintingSupported(true);
	}

	@Override
	public JPanel getPanel() {
		return this.panel;
	}

	@Override
	public void restoreState() {
		YapbamState.INSTANCE.restoreState(panel.getTransactionsTable(), STATE_PREFIX);
		YapbamState.INSTANCE.restoreState(panel.getUncheckedTransactionsTable(), UNCHECKED_PREFIX);
	}

	@Override
	public void saveState() {
		YapbamState.INSTANCE.saveState(panel.getTransactionsTable(), STATE_PREFIX);
		YapbamState.INSTANCE.saveState(panel.getUncheckedTransactionsTable(), UNCHECKED_PREFIX);
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
		return panel.getPrintable();
	}

	@Override
	public TransactionSelector getTransactionSelector() {
		return new DualTransactionSelector(panel);
	}

	@Override
	public JMenuItem[] getMenuItem(int part) {
		if (part==TRANSACTIONS_PART) {
			JMenuItem item = new JMenuItem(panel.checkAction);
			item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			return new JMenuItem[]{null, item};
		} else {
			return super.getMenuItem(part);
		}
	}

	@Override
	public void setDisplayed(boolean displayed) {
		super.setDisplayed(displayed);
		if (displayed) {
			panel.checkAction.updateEnabled();
		} else {
			panel.checkAction.setEnabled(false);
		}
	}

	@Override
	public Account getSelectedAccount() {
		return this.panel.getSelectedAccount();
	}
}
