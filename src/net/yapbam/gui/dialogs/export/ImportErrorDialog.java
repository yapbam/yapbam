package net.yapbam.gui.dialogs.export;

import java.awt.Window;

import javax.swing.JPanel;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;

import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public class ImportErrorDialog extends AbstractDialog<Object[], Boolean> {

	public ImportErrorDialog(Window owner, int[] importedFields, ImportError[] errors) {
		super(owner, LocalizationData.get("ImportDialog.errorMessage.title"), new Object[]{importedFields, errors}); //$NON-NLS-1$
		getCancelButton().setToolTipText(LocalizationData.get("ImportDialog.errorMessage.cancel.tooltip")); //$NON-NLS-1$
		getOkButton().setText(LocalizationData.get("GenericButton.continue")); //$NON-NLS-1$
		getOkButton().setToolTipText(LocalizationData.get("ImportDialog.errorMessage.continue.tooltip")); //$NON-NLS-1$
	}

	@Override
	protected Boolean buildResult() {
		return Boolean.TRUE;
	}

	@Override
	protected JPanel createCenterPane() {
		int[] importedFields = (int[])data[0];
		ImportError[] errors = (ImportError[])data[1];
		return new ImportErrorPanel(importedFields, errors);
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}

	@Override
	public void setVisible(boolean visible) {
		this.pack();
		super.setVisible(visible);
	}
}
