package net.astesana.cloud.swing;

import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;

import javax.swing.JPanel;

import net.astesana.ajlib.swing.dialog.AbstractDialog;

@SuppressWarnings("serial")
public class FileChooserDialog extends AbstractDialog<URIChooser, URI> {

	public FileChooserDialog(Window owner, String title, URIChooser panel) {
		super(owner, title, panel);
	}

	@Override
	protected JPanel createCenterPane() {
		this.data.addPropertyChangeListener(URIChooser.SELECTED_URI_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateOkButtonEnabled();
			}
		});
		return this.data;
	}

	@Override
	protected URI buildResult() {
		return this.data.getSelectedURI();
	}

	@Override
	protected String getOkDisabledCause() {
		return this.data.getSelectedURI()==null?"Please select a file":null; //LOCAL
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
