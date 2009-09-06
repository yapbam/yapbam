package net.yapbam.ihm.dialogs;

import java.awt.BorderLayout;
import java.awt.Window;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import java.lang.Object;
import java.lang.String;

@SuppressWarnings("serial")
public class PreferenceDialog extends AbstractDialog {

	public PreferenceDialog(Window owner) {
		super(owner, "Préférences", null); //LOCAL
		this.cancelButton.setVisible(false);
	}

	@Override
	protected Object buildResult() {
		//TODO
		return null;
	}

	@Override
	protected JPanel createCenterPane(Object data) {
		JPanel panel = new JPanel(new BorderLayout());
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.add("Localisation", new LocalizationPanel());
		tabbedPane.add("Présentation", new JPanel()); //TODO
		panel.add(tabbedPane, BorderLayout.CENTER);
		return panel;
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}
}
