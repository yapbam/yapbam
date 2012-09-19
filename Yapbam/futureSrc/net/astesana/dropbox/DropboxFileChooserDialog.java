package net.astesana.dropbox;

import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import net.astesana.ajlib.swing.dialog.AbstractDialog;

@SuppressWarnings("serial")
public class DropboxFileChooserDialog extends AbstractDialog<DropboxFileChooser, FileId> {

	public DropboxFileChooserDialog(Window owner, String title, DropboxFileChooser panel) {
		super(owner, title, panel);
	}

	@Override
	protected JPanel createCenterPane() {
		this.data.addPropertyChangeListener(DropboxFileChooser.SELECTED_FILEID_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateOkButtonEnabled();
			}
		});
		return this.data;
	}

	@Override
	protected FileId buildResult() {
		return this.data.getSelectedFile();
	}

	@Override
	protected String getOkDisabledCause() {
		return this.data.getSelectedFile()==null?"Please select a file":null;
	}

	/* (non-Javadoc)
	 * @see net.astesana.ajlib.swing.dialog.AbstractDialog#confirm()
	 */
	@Override
	protected void confirm() {
		super.confirm();
	}

	/* (non-Javadoc)
	 * @see net.astesana.ajlib.swing.dialog.AbstractDialog#cancel()
	 */
	@Override
	protected void cancel() {
		super.cancel();
	}
}
