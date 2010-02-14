package net.yapbam.gui.dialogs.export;

import java.awt.Window;

import javax.swing.JPanel;

import net.yapbam.gui.dialogs.AbstractDialog;

@SuppressWarnings("serial")
public class ExportDialog extends AbstractDialog {

	public ExportDialog(Window owner, String title, Object data) {
		super(owner, title, data);
	}

	@Override
	protected Object buildResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JPanel createCenterPane(Object data) {
		return new ExportPanel();
	}

	@Override
	protected String getOkDisabledCause() {
		// TODO Auto-generated method stub
		return null;
	}

}
