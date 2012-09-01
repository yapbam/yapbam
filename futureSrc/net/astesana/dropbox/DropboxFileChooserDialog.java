package net.astesana.dropbox;

import java.awt.Window;

import javax.swing.JPanel;

import net.astesana.ajlib.swing.dialog.AbstractDialog;

@SuppressWarnings("serial")
public class DropboxFileChooserDialog extends AbstractDialog<DropboxFileChooser, String> {

	public DropboxFileChooserDialog(Window owner, String title, DropboxFileChooser panel) {
		super(owner, title, panel);
	}

	@Override
	protected JPanel createCenterPane() {
		return this.data;
	}

	@Override
	protected String buildResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getOkDisabledCause() {
		// TODO Auto-generated method stub
		return null;
	}

}
