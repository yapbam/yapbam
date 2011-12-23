package net.yapbam.gui.dialogs.preferences;

import java.util.ArrayList;

import net.yapbam.gui.CompoundPreferencePanel;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.MainFrame;
import net.yapbam.gui.PreferencePanel;

public class LookAndFeelPanel extends CompoundPreferencePanel {
	private static final long serialVersionUID = 1L;
	
	/**
	 * This is the default constructor
	 * @param frame Yapbam's Mainframe
	 */
	public LookAndFeelPanel(MainFrame frame) {
		super(getSubPanels(frame));
	}

	private static PreferencePanel[] getSubPanels(MainFrame frame) {
		ArrayList<PreferencePanel> lfPanels = new ArrayList<PreferencePanel>();
		lfPanels.add(new ThemePanel());
		
		for (int i=0 ; i<frame.getPlugInsNumber(); i++) {
			PreferencePanel preferencePanel = frame.getPlugIn(i).getLFPreferencePanel();
			if (preferencePanel!=null) lfPanels.add(preferencePanel) ;
		}
		return lfPanels.toArray(new PreferencePanel[lfPanels.size()]);
	}

	@Override
	public String getTitle() {
		return LocalizationData.get("PreferencesDialog.LookAndFeel.title");
	}

	@Override
	public String getToolTip() {
		return LocalizationData.get("PreferencesDialog.LookAndFeel.toolTip");
	}
}
