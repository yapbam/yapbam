package net.yapbam.gui.dialogs.preferences;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.MainFrame;
import net.yapbam.gui.PreferencePanel;
import net.yapbam.gui.dialogs.AbstractDialog;

import java.lang.Object;
import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public class PreferenceDialog extends AbstractDialog {
	public static final long LOCALIZATION_CHANGED = 1;
	public static final long LOOK_AND_FEEL_CHANGED = 2;

	private List<PreferencePanel> panels;

	public PreferenceDialog(MainFrame owner) {
		super(owner, LocalizationData.get("PreferencesDialog.title"), owner);
	}

	@Override
	protected Object buildResult() {
		boolean result = false;
		for (int i = 0; i < panels.size(); i++) {
			result = panels.get(i).updatePreferences() || result;
		}
		return result;
	}

	@Override
	protected JPanel createCenterPane(Object data) {
		JPanel panel = new JPanel(new BorderLayout());
		final JTabbedPane tabbedPane = new JTabbedPane();
		this.panels = new ArrayList<PreferencePanel>();
		this.panels.addAll(Arrays.asList(new PreferencePanel[]{new LocalizationPanel(), new LookAndFeelPanel(), new NetworkPanel(), new AutoUpdatePanel()}));
		for (int i=0 ; i<((MainFrame)data).getPlugInsNumber(); i++) {
			PreferencePanel preferencePanel = ((MainFrame)data).getPlugIn(i).getPreferencePanel();
			if (preferencePanel!=null) this.panels.add(preferencePanel) ;
		}
		tabbedPane.addChangeListener(new ChangeListener() {
			private int lastSelected = -1;

			@Override
			public void stateChanged(ChangeEvent e) {
				if (lastSelected>=0) panels.get(lastSelected).setDisplayed(false);
				lastSelected = tabbedPane.getSelectedIndex();
				panels.get(lastSelected).setDisplayed(true);
			}
		});
		for (int i = 0; i < panels.size(); i++) {
			tabbedPane.addTab(panels.get(i).getTitle(), null, panels.get(i), panels.get(i).getToolTip());
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
