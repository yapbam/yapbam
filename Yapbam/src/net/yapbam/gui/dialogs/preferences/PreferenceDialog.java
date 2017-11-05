package net.yapbam.gui.dialogs.preferences;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.MainFrame;
import net.yapbam.gui.util.AutoUpdateOkButtonPropertyListener;
import net.yapbam.util.Portable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public class PreferenceDialog extends AbstractDialog<Boolean, Boolean> {
	public static final long LOCALIZATION_CHANGED = 1;
	public static final long LOOK_AND_FEEL_CHANGED = 2;

	private List<PreferencePanel> panels;
	private JTabbedPane tabbedPane;

	public PreferenceDialog(MainFrame owner, boolean expertMode) {
		super(owner, LocalizationData.get("PreferencesDialog.title"), expertMode); //$NON-NLS-1$
		this.setLocationRelativeTo(owner.getJFrame());
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
		tabbedPane = new JTabbedPane();
		this.panels = new ArrayList<PreferencePanel>();
		MainFrame frame = (MainFrame) getOwner();
		this.panels.addAll(Arrays.asList(new PreferencePanel[]{new LocalizationPanel(), new LookAndFeelPanel(frame),
				new TransactionEditingPanel(), new ProxyPanel(), new ReportErrorPanel(), new RestoreStatePanel()}));
		if (!Portable.isWebStarted()) {
			this.panels.add(new AutoUpdatePanel());
		}
		for (int i=0 ; i<frame.getPlugInsNumber(); i++) {
			PreferencePanel preferencePanel = frame.getPlugIn(i).getPreferencePanel();
			if (preferencePanel!=null) {
				this.panels.add(preferencePanel) ;
			}
		}
		tabbedPane.addChangeListener(new ChangeListener() {
			private int lastSelected = -1;

			@Override
			public void stateChanged(ChangeEvent e) {
				if (lastSelected>=0) {
					panels.get(lastSelected).setDisplayed(false);
				}
				lastSelected = tabbedPane.getSelectedIndex();
				panels.get(lastSelected).setDisplayed(true);
			}
		});
		for (PreferencePanel panel : panels) {
			panel.setExpertMode(data);
			tabbedPane.addTab(panel.getTitle(), null, panel, panel.getToolTip());
			panel.addPropertyChangeListener(PreferencePanel.OK_DISABLED_CAUSE_PROPERTY, new AutoUpdateOkButtonPropertyListener(this));
		}
		JPanel panel = new JPanel(new BorderLayout());
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
				if (result!=null) {
					break;
				}
			}
		}
		return result;
	}
	
	public Boolean getChanges() {
		return (Boolean) getResult();
	}
}
