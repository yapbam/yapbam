package net.astesana.dropbox;

import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import net.astesana.ajlib.swing.dialog.AbstractDialog;

@SuppressWarnings("serial")
public class DropboxFileChooserDialog extends AbstractDialog<DropboxFileChooser, String> {

	public DropboxFileChooserDialog(Window owner, String title, DropboxFileChooser panel) {
		super(owner, title, panel);
	}

	@Override
	protected JPanel createCenterPane() {
		this.data.addPropertyChangeListener(DropboxFileChooser.SELECTED_FILE_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateOkButtonEnabled();
			}
		});
		return this.data;
	}

	@Override
	protected String buildResult() {
		return this.data.getSelectedFile();
	}

	@Override
	protected String getOkDisabledCause() {
		return this.data.getSelectedFile()==null?"Please select a file":null;
	}

	public void setVisible(boolean visible) {
		if (!isVisible() && visible) {
			this.data.refresh();
		}
		super.setVisible(visible);
	}
}
