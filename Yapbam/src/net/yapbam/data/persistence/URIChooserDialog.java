package net.yapbam.data.persistence;

import java.awt.BorderLayout;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import net.astesana.ajlib.swing.dialog.AbstractDialog;
import net.astesana.dropbox.DropboxFileChooser;
import net.astesana.dropbox.FileId;

@SuppressWarnings("serial")
class URIChooserDialog extends AbstractDialog<URIChooser, FileId> {

	public URIChooserDialog(Window owner, String title, URIChooser panel) {
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
		JPanel result = new JPanel(new BorderLayout());
		result.add(this.data, BorderLayout.CENTER);
		return result;
	}

	@Override
	protected FileId buildResult() {
		return null; //TODO this.data.getSelectedFile();
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
//TODO		return this.data.getSelectedFile()==null?"Please select a file":null;
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
