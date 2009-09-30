package net.yapbam.gui.dialogs.preferences;

import java.awt.BorderLayout;
import java.awt.Window;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.AbstractDialog;

import java.lang.Object;
import java.lang.String;

@SuppressWarnings("serial")
public class PreferenceDialog extends AbstractDialog {
	public static final long LOCALIZATION_CHANGED = 1;
	public static final long LOOK_AND_FEEL_CHANGED = 2;

	private PreferencePanel[] panels;

	public PreferenceDialog(Window owner) {
		super(owner, LocalizationData.get("PreferencesDialog.title"), null);
	}

	@Override
	protected Object buildResult() {
		boolean result = false;
		for (int i = 0; i < panels.length; i++) {
			result = panels[i].updatePreferences() || result;
		}
		return result;
	}

	@Override
	protected JPanel createCenterPane(Object data) {
		JPanel panel = new JPanel(new BorderLayout());
		JTabbedPane tabbedPane = new JTabbedPane();
		this.panels = new PreferencePanel[]{new LocalizationPanel(), new LookAndFeelPanel(), new NetworkPanel(), new AutoUpdatePanel()};
		for (int i = 0; i < panels.length; i++) {
			tabbedPane.addTab(panels[i].getTitle(), null, panels[i], panels[i].getToolTip());
		}
		panel.add(tabbedPane, BorderLayout.CENTER);
		return panel;
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}
	
	public Boolean getChanges() {
		return (Boolean) getResult();
	}
}
