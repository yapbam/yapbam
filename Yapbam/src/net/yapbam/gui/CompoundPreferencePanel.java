package net.yapbam.gui;

import javax.swing.JTabbedPane;



import net.yapbam.gui.dialogs.preferences.PreferencePanel;

import java.awt.BorderLayout;

/** A preferences panel that groups several preference panels in a JTabbedPane.
 */
public abstract class CompoundPreferencePanel extends PreferencePanel {
	private static final long serialVersionUID = 1L;
	
	private PreferencePanel[] panels;

	/**
	 * Default constructor.
	 * @param panels The sub panels grouped in this compound panel
	 */
	public CompoundPreferencePanel(PreferencePanel[] panels) {
		super();
		this.panels = panels;
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		setLayout(new BorderLayout());
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane);
		
		for (PreferencePanel panel:this.panels) {
			tabbedPane.addTab(panel.getTitle(), null, panel, panel.getToolTip());
		}
	}

	@Override
	public boolean updatePreferences() {
		boolean result = false;
		for (PreferencePanel panel:this.panels) {
			result = panel.updatePreferences() || result;
		}
		return result;
	}
}
