package net.yapbam.gui.dialogs;

import java.awt.Window;

import javax.swing.JPanel;

import net.yapbam.gui.LocalizationData;

public class ErrorDialog extends AbstractDialog<Throwable> {
	private static final long serialVersionUID = 1L;
	
	ErrorPanel panel;
	
	public ErrorDialog(Window owner, Throwable throwable) {
		super(owner, LocalizationData.get("ErrorManager.title"), throwable); //$NON-NLS-1$
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
	protected Object buildResult() {
		return Boolean.TRUE;
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}
}
