package net.yapbam.gui.dialogs.export;

import java.awt.Window;

import javax.swing.JPanel;

import net.yapbam.gui.ImportErrorPanel;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.AbstractDialog;

@SuppressWarnings("serial")
public class ImportErrorDialog extends AbstractDialog {

	public ImportErrorDialog(Window owner, ImportError[] errors) {
		super(owner, LocalizationData.get("ImportDialog.errorMessage.title"), errors); //$NON-NLS-1$
		super.cancelButton.setVisible(false);
	}

	@Override
	protected Object buildResult() {
		return null;
	}

	@Override
	protected JPanel createCenterPane(Object data) {
		return new ImportErrorPanel((ImportError[]) data);
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}

}
