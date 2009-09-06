package net.yapbam.ihm.dialogs;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import net.yapbam.ihm.MainFrame;
import net.yapbam.ihm.Preferences;

import java.lang.Object;
import java.lang.String;

@SuppressWarnings("serial")
public class PreferenceDialog extends AbstractDialog {

	private LocalizationPanel localizationPanel;

	public PreferenceDialog(MainFrame frame) {
		super(frame, "Préférences", null); //LOCAL
	}

	@Override
	protected Object buildResult() {
		boolean restart = localizationPanel.isChanged();
		Preferences.INSTANCE.setLocale(localizationPanel.getBuiltLocale(), localizationPanel.isDefaultCountry(), localizationPanel.isDefaultLanguage());
		//TODO Other panels
		if (restart) ((MainFrame)this.getOwner()).restart();
		return null;
	}

	@Override
	protected JPanel createCenterPane(Object data) {
		JPanel panel = new JPanel(new BorderLayout());
		JTabbedPane tabbedPane = new JTabbedPane();
		localizationPanel = new LocalizationPanel();
		tabbedPane.add("Localisation", localizationPanel);
		tabbedPane.add("Présentation", new JPanel()); //TODO
		panel.add(tabbedPane, BorderLayout.CENTER);
		return panel;
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}
}
