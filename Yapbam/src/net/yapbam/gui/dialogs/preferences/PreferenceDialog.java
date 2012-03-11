package net.yapbam.gui.dialogs.preferences;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.MainFrame;
import net.yapbam.gui.PreferencePanel;
import net.yapbam.gui.dialogs.preferences.backup.BackupPanel;
import net.yapbam.gui.util.AbstractDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public class PreferenceDialog extends AbstractDialog<MainFrame, Boolean> {
	public static final long LOCALIZATION_CHANGED = 1;
	public static final long LOOK_AND_FEEL_CHANGED = 2;

	private List<PreferencePanel> panels;
	private JTabbedPane tabbedPane;

	public PreferenceDialog(MainFrame owner) {
		super(owner, LocalizationData.get("PreferencesDialog.title"), owner);
		this.setLocationRelativeTo(owner);
	}

	@Override
	protected Boolean buildResult() {
		boolean result = false;
		for (int i = 0; i < panels.size(); i++) {
			result = panels.get(i).updatePreferences() || result;
		}
		return result;
	}

	@Override
	protected JPanel createCenterPane() {
		JPanel panel = new JPanel(new BorderLayout());
		tabbedPane = new JTabbedPane();
		this.panels = new ArrayList<PreferencePanel>();
		this.panels.addAll(Arrays.asList(new PreferencePanel[]{new LocalizationPanel(), new LookAndFeelPanel(data),
				new TransactionEditingPanel(), new ProxyPanel(), new AutoUpdatePanel(), new ReportErrorPanel(), new RestoreStatePanel()/**/, new BackupPanel()/**/})); //0.10.0
		for (int i=0 ; i<data.getPlugInsNumber(); i++) {
			PreferencePanel preferencePanel = data.getPlugIn(i).getPreferencePanel();
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
		PropertyChangeListener listener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateOkButtonEnabled();
			}
		};
		for (int i = 0; i < panels.size(); i++) {
			tabbedPane.addTab(panels.get(i).getTitle(), null, panels.get(i), panels.get(i).getToolTip());
			panels.get(i).addPropertyChangeListener(PreferencePanel.OK_DISABLED_CAUSE_PROPERTY, listener);
		}
		panel.add(tabbedPane, BorderLayout.CENTER);
		return panel;
	}

	@Override
	protected String getOkDisabledCause() {
		int selected = tabbedPane.getSelectedIndex();
		String result = this.panels.get(selected).getOkDisabledCause();
		if (result!=null) {
			for (PreferencePanel panel:this.panels) {
				result = panel.getOkDisabledCause();
				if (result!=null) break;
			}
		}
		return result;
	}
	
	public Boolean getChanges() {
		return (Boolean) getResult();
	}
}
