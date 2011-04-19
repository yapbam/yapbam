package net.yapbam.gui.dialogs;

import java.awt.Window;

import javax.swing.JPanel;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.util.AbstractDialog;

public class ErrorDialog extends AbstractDialog<Throwable, Boolean> {
	private static final long serialVersionUID = 1L;
	
	ErrorPanel panel;
	
	public ErrorDialog(Window parent, Throwable throwable) {
		super(parent, LocalizationData.get("ErrorManager.title"), throwable); //$NON-NLS-1$
		this.cancelButton.setText(LocalizationData.get("GenericButton.no")); //$NON-NLS-1$
		this.cancelButton.setToolTipText(LocalizationData.get("ErrorManager.report.dontSend.tootip")); //$NON-NLS-1$
		this.okButton.setText(LocalizationData.get("GenericButton.yes")); //$NON-NLS-1$
		this.okButton.setToolTipText(LocalizationData.get("ErrorManager.report.send.tootip")); //$NON-NLS-1$
		this.pack();
	}

	@Override
	protected JPanel createCenterPane() {
		panel = new ErrorPanel();
		return panel;
	}

	@Override
	protected Boolean buildResult() {
		setPreferences(1);
		return Boolean.TRUE;
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}

	@Override
	protected void cancel() {
		setPreferences(-1);
	}
	
	private void setPreferences(int action) {
		Preferences.INSTANCE.setCrashReportAction(panel.isDontAskMeSelected()?action:0);
	}
}
