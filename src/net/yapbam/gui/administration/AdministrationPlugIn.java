package net.yapbam.gui.administration;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fathzer.jlocal.Formatter;

import net.yapbam.data.Account;
import net.yapbam.data.FilteredData;
import net.yapbam.data.event.AccountAddedEvent;
import net.yapbam.data.event.AccountPropertyChangedEvent;
import net.yapbam.data.event.AccountRemovedEvent;
import net.yapbam.data.event.CheckbookAddedEvent;
import net.yapbam.data.event.CheckbookPropertyChangedEvent;
import net.yapbam.data.event.CheckbookRemovedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.TransactionsAddedEvent;
import net.yapbam.data.event.TransactionsRemovedEvent;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;

/** This plugin implements the Administration panel.
 * @author Jean-Marc Astesana
 */
public class AdministrationPlugIn extends AbstractPlugIn implements AbstractAlertPanel {
	
	private FilteredData data;
	private AdministrationPanel panel;
	private boolean supportFilter;

	public AdministrationPlugIn(FilteredData filteredData, Object restartData) {
		this.data = filteredData;
		this.panel = new AdministrationPanel(filteredData);
		this.supportFilter = isPeriodicalTransactionDisplayed();
		this.panel.getTabbedPane().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				boolean newSupportFilter = isPeriodicalTransactionDisplayed();
				if (newSupportFilter!=supportFilter) {
					supportFilter = newSupportFilter;
					getPropertyChangeSupport().firePropertyChange(FILTER_SUPPORTED_PROPERTY_NAME, !supportFilter, supportFilter);
				}
			}
		});
		this.data.getGlobalData().addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if ((event instanceof EverythingChangedEvent) || (event instanceof AccountAddedEvent)
						|| (event instanceof AccountRemovedEvent) || (event instanceof AccountPropertyChangedEvent)
						|| (event instanceof TransactionsAddedEvent) || (event instanceof TransactionsRemovedEvent)
						|| (event instanceof CheckbookAddedEvent) || (event instanceof CheckbookRemovedEvent)
						|| (event instanceof CheckbookPropertyChangedEvent)) {
					hasAlert();
				}
			}
		});
		this.setPanelTitle(LocalizationData.get("AdministrationPlugIn.title")); //$NON-NLS-1$
		this.setPanelToolTip(LocalizationData.get("AdministrationPlugIn.toolTip")); //$NON-NLS-1$
		hasAlert();
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}

	@Override
	public void restoreState() {
		panel.restoreState();
	}

	@Override
	public void saveState() {
		panel.saveState();
	}

	@Override
	public boolean allowMenu(int menuId) {
		if (menuId==FILTER_MENU) {
			return isPeriodicalTransactionDisplayed();
		}
		return super.allowMenu(menuId);
	}

	private boolean isPeriodicalTransactionDisplayed() {
		return panel.getTabbedPane().getSelectedComponent() instanceof PeriodicalTransactionListPanel;
	}

	@Override
	public boolean hasAlert() {
		boolean result = false;
		if (this.data != null) {
			for (int i = 0; i < this.data.getGlobalData().getAccountsNumber() && !result; i++) {
				Account account = this.data.getGlobalData().getAccount(i);
				if (account != null) {
					for (int c = 0; c < account.getCheckbooksNumber() && !result; c++) {
						if (account.getCheckbook(c).getRemaining() > 0 && //
								account.getCheckbook(c).getRemaining() <= account.getCheckNumberAlertThreshold()) {
							result = true;
						}
					}
				}
			}
		}
		setPanelIcon(result ? IconManager.get(Name.ALERT) : null);
		setPanelToolTip(result ? //
				Formatter.format(LocalizationData.get("AdministrationPlugIn.toolTip.checkbookAlert"), LocalizationData.get("AdministrationPlugIn.toolTip")) : //
				LocalizationData.get("AdministrationPlugIn.toolTip") //
		);
		return result;
	}
	
}
