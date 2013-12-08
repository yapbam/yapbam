package net.yapbam.gui.dialogs;

import java.awt.Window;

import javax.swing.JPanel;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;

public class ErrorDialog extends AbstractDialog<Throwable, Boolean> {
	private static final long serialVersionUID = 1L;
	
	ErrorPanel panel;
	private boolean prefcanBeSaved;
	
	public ErrorDialog(Window parent, Throwable throwable) {
		super(parent, LocalizationData.get("ErrorManager.title"), throwable); //$NON-NLS-1$
		getCancelButton().setText(LocalizationData.get("GenericButton.no")); //$NON-NLS-1$
		getCancelButton().setToolTipText(LocalizationData.get("ErrorManager.report.dontSend.tootip")); //$NON-NLS-1$
		getOkButton().setText(LocalizationData.get("GenericButton.yes")); //$NON-NLS-1$
		getOkButton().setToolTipText(LocalizationData.get("ErrorManager.report.send.tootip")); //$NON-NLS-1$
		this.prefcanBeSaved = Preferences.canSave();
		panel.setDontAskMeVisible(this.prefcanBeSaved);
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
		super.cancel();
		setPreferences(-1);
	}
	
	private void setPreferences(int action) {
		// Be aware that Preferences.INSTANCE could be null (if we are reporting an error during its instantiation)
		// Be also aware that in such a case, you absolutely may not access Preferences.INSTANCE, it would cause a deadlock !
		if (this.prefcanBeSaved) {
			Preferences.INSTANCE.setCrashReportAction(panel.isDontAskMeSelected()?action:0);
		}
	}
}
