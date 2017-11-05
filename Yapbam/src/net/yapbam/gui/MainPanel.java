package net.yapbam.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.yapbam.gui.widget.TabbedPane;

/** Yapbam's main panel.
 */
@SuppressWarnings("serial")
public class MainPanel extends TabbedPane {
	public static final String SELECTED_PLUGIN_PROPERTY = "selectedPlugin"; //$NON-NLS-1$
	private List<AbstractPlugIn> paneledPlugins;
	private AbstractPlugIn selectedPlugin;

	public MainPanel(AbstractPlugIn[] plugins) {
		super();
		this.paneledPlugins = new ArrayList<AbstractPlugIn>();
		for (int i = 0; i < plugins.length; i++) {
			if (plugins[i] != null) {
				JPanel pane = plugins[i].getPanel();
				if (pane != null) {
					paneledPlugins.add(plugins[i]);
					addTab(plugins[i].getPanelTitle(), null, pane, plugins[i].getPanelToolTip());
					if (plugins[i].getPanelIcon() != null) {
						setIconAt(getTabCount() - 1, plugins[i].getPanelIcon());
					}
				}
				// Listening for panel title, tooltip and icon changes
				plugins[i].getPropertyChangeSupport().addPropertyChangeListener(new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						int tabIndex = paneledPlugins.indexOf(evt.getSource());
						if (tabIndex >= 0) {
							tabIndex = getIndexOf(tabIndex);
							if (evt.getPropertyName().equals(AbstractPlugIn.PANEL_ICON_PROPERTY_NAME)) {
								setIconAt(tabIndex, (Icon) evt.getNewValue());
							} else if (evt.getPropertyName().equals(AbstractPlugIn.PANEL_TITLE_PROPERTY_NAME)) {
								setTitleAt(tabIndex, (String) evt.getNewValue());
							} else if (evt.getPropertyName().equals(AbstractPlugIn.PANEL_TOOLTIP_PROPERTY_NAME)) {
								setToolTipTextAt(tabIndex, (String) evt.getNewValue());
							}
						}
					}
				});
			}
		}
		addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateSelectedPlugin();
			}
		});
		updateSelectedPlugin();
	}
	
	private void updateSelectedPlugin() {
		int index = getSelectedIndex();
		AbstractPlugIn old = selectedPlugin;
		if (old!=null) {
			old.setDisplayed(false);
		}
		selectedPlugin = index<0?null:this.paneledPlugins.get(getId(index));
		if (selectedPlugin!=null) {
			selectedPlugin.setDisplayed(true);
		}
		firePropertyChange(SELECTED_PLUGIN_PROPERTY, old, selectedPlugin);
	}
	
	/** Gets the plugin currently displayed in the tabbed pane.
	 * @return the currently displayed plugin
	 */
	public AbstractPlugIn getSelectedPlugIn() {
		return selectedPlugin;
	}
}
