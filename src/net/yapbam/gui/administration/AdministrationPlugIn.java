package net.yapbam.gui.administration;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.LocalizationData;

/** This plugin implements the Administration panel.
 * @author Jean-Marc Astesana
 */
public class AdministrationPlugIn extends AbstractPlugIn {
	private AdministrationPanel panel;
	private boolean supportFilter;

	public AdministrationPlugIn(FilteredData filteredData, Object restartData) {
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

		this.setPanelTitle(LocalizationData.get("AdministrationPlugIn.title")); //$NON-NLS-1$
		this.setPanelToolTip(LocalizationData.get("AdministrationPlugIn.toolTip")); //$NON-NLS-1$
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
}
