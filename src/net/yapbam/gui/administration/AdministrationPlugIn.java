package net.yapbam.gui.administration;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.util.HtmlUtils;

/** This plugin implements the Administration panel.
 * @author Jean-Marc Astesana
 */
public class AdministrationPlugIn extends AbstractPlugIn {
	private AdministrationPanel panel;
	private boolean supportFilter;

	public AdministrationPlugIn(FilteredData filteredData, Object restartData) {
		final String toolTip = LocalizationData.get("AdministrationPlugIn.toolTip"); //$NON-NLS-1$
		this.panel = new AdministrationPanel(filteredData);
		this.panel.addPropertyChangeListener(new PropertyChangeListenerProxy(AdministrationPanel.ALERT_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				final String alert = (String) evt.getNewValue();
				AdministrationPlugIn.this.setPanelIcon(alert==null ? null : IconManager.get(Name.ALERT));
				AdministrationPlugIn.this.setPanelToolTip(alert==null ? toolTip : HtmlUtils.linesToHtml(true, toolTip, alert));
			}
		}));
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
		this.setPanelToolTip(toolTip);
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
