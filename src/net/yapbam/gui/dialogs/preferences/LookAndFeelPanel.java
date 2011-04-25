package net.yapbam.gui.dialogs.preferences;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.PreferencePanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;

public class LookAndFeelPanel extends PreferencePanel {
	private static final long serialVersionUID = 1L;
	
	private PreferencePanel[] panels;

	/**
	 * This is the default constructor
	 */
	public LookAndFeelPanel(PreferencePanel[] extraPanels) {
		super();
		initialize(extraPanels);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize(PreferencePanel[] extraPanels) {
		this.panels = new PreferencePanel[extraPanels.length+1];
		setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane);
		
		this.panels[0] = new ThemePanel();
		for (int i = 0; i < extraPanels.length; i++) {
			this.panels[i+1] = extraPanels[i];
		}
		
		for (PreferencePanel panel:this.panels) {
			tabbedPane.addTab(panel.getTitle(), null, panel, panel.getToolTip());
		}
	}

	@Override
	public String getTitle() {
		return LocalizationData.get("PreferencesDialog.LookAndFeel.title");
	}

	@Override
	public String getToolTip() {
		return LocalizationData.get("PreferencesDialog.LookAndFeel.toolTip");
	}

	@Override
	public boolean updatePreferences() {
		boolean result = false;
		for (PreferencePanel panel:this.panels) {
			result = result || panel.updatePreferences();
		}
		return result;
	}
}
