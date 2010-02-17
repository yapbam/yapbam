package net.yapbam.gui.dialogs.export;

import java.awt.Window;

import javax.swing.JPanel;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.AbstractDialog;

@SuppressWarnings("serial")
public class ImportDialog extends AbstractDialog {

	public ImportDialog(Window owner) {
		super(owner, LocalizationData.get("ImportDialog.title"), null); //$NON-NLS-1$
	}

	@Override
	protected Object buildResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JPanel createCenterPane(Object data) {
		ImportPanel importPanel = new ImportPanel();
		return importPanel;
	}

	@Override
	protected String getOkDisabledCause() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setVisible(boolean visible) {
		this.pack(); //TODO Sure it's useful ?
		super.setVisible(visible);
	}

}
