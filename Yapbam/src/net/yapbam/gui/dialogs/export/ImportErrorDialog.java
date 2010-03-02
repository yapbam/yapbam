package net.yapbam.gui.dialogs.export;

import java.awt.Window;

import javax.swing.JPanel;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.AbstractDialog;

@SuppressWarnings("serial")
public class ImportErrorDialog extends AbstractDialog {

	public ImportErrorDialog(Window owner, int[] importedFields, ImportError[] errors) {
		super(owner, LocalizationData.get("ImportDialog.errorMessage.title"), new Object[]{importedFields, errors}); //$NON-NLS-1$
		super.cancelButton.setVisible(false);
		this.okButton.setText(LocalizationData.get("GenericButton.close")); //$NON-NLS-1$
		this.okButton.setToolTipText(LocalizationData.get("GenericButton.close.ToolTip")); //$NON-NLS-1$
	}

	@Override
	protected Object buildResult() {
		return null;
	}

	@Override
	protected JPanel createCenterPane(Object data) {
		int[] importedFields = (int[]) ((Object[])data)[0];
		ImportError[] errors = (ImportError[]) ((Object[])data)[1];
		return new ImportErrorPanel(importedFields, errors);
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}

	public void setVisible(boolean visible) {
		this.pack();
		super.setVisible(visible);
	}
}
