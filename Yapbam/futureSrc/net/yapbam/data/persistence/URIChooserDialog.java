package net.yapbam.data.persistence;

import java.awt.BorderLayout;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;

import javax.swing.JPanel;

import net.astesana.ajlib.swing.dialog.AbstractDialog;

@SuppressWarnings("serial")
class URIChooserDialog extends AbstractDialog<URIChooser, URI> {

	public URIChooserDialog(Window owner, String title, URIChooser panel) {
		super(owner, title, panel);
		panel.setDialog(this);
	}

	@Override
	protected JPanel createCenterPane() {
		this.data.addPropertyChangeListener(AbstractURIChooserPanel.SELECTED_URI_PROPERTY, new PropertyChangeListener() {
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
	protected URI buildResult() {
		return this.data.getSelectedURI();
	}

	@Override
	protected String getOkDisabledCause() {
		if (this.data.getSelectedURI()==null) return "This button is disabled because no file is selected";
		return null;
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
