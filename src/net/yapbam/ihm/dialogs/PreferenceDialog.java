package net.yapbam.ihm.dialogs;

import java.awt.BorderLayout;
import java.awt.Window;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.Preferences;

import java.lang.Object;
import java.lang.String;

@SuppressWarnings("serial")
public class PreferenceDialog extends AbstractDialog {
	public static final long LOCALIZATION_CHANGED = 1;
	public static final long LOOK_AND_FEEL_CHANGED = 2;

	private LocalizationPanel localizationPanel;
	private LookAndFeelPanel lookAndFeelPanel;

	public PreferenceDialog(Window owner) {
		super(owner, LocalizationData.get("PreferencesDialog.title"), null);
	}

	@Override
	protected Object buildResult() {
		long result = 0;
		if (localizationPanel.isChanged()) {
			boolean needIHMRefresh = !localizationPanel.getBuiltLocale().equals(Preferences.INSTANCE.getLocale());
			Preferences.INSTANCE.setLocale(localizationPanel.getBuiltLocale(), localizationPanel.isDefaultCountry(), localizationPanel.isDefaultLanguage());
			if (needIHMRefresh) {
				result = result + LOCALIZATION_CHANGED;
				LocalizationData.reset();
			}
		}
		if (lookAndFeelPanel.isCustomLookAndFeel()==Preferences.INSTANCE.isJavaLookAndFeel()) {
			result = result + LOOK_AND_FEEL_CHANGED;
			Preferences.INSTANCE.setJavaLookAndFeel(!Preferences.INSTANCE.isJavaLookAndFeel());
		}
		return result;
	}

	@Override
	protected JPanel createCenterPane(Object data) {
		JPanel panel = new JPanel(new BorderLayout());
		JTabbedPane tabbedPane = new JTabbedPane();
		localizationPanel = new LocalizationPanel();
		tabbedPane.add(LocalizationData.get("PreferencesDialog.Localization.title"), localizationPanel); //$NON-NLS-1$
		lookAndFeelPanel = new LookAndFeelPanel();
		tabbedPane.add(LocalizationData.get("PreferencesDialog.LookAndFeel.title"), lookAndFeelPanel); //$NON-NLS-1$
		panel.add(tabbedPane, BorderLayout.CENTER);
		return panel;
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}
	
	public Long getChanges() {
		return (Long) getResult();
	}
}
