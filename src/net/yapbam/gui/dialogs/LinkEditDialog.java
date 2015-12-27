package net.yapbam.gui.dialogs;

import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;
import com.fathzer.soft.ajlib.swing.widget.TextWidget;

import net.yapbam.gui.LocalizationData;
import net.yapbam.util.HtmlUtils;

public class LinkEditDialog extends AbstractDialog<String[], String> {
	private static final long serialVersionUID = 1L;
	private LinkEditPanel linkEditPanel;
	
	public LinkEditDialog(Window owner, String[] data) {
		super(owner, LocalizationData.get("LinkEditor.dialog.title"), decode(data)); //$NON-NLS-1$
	}

	private static String[] decode(String[] data) {
		return new String[] { data[1] == null ? data[0] : HtmlUtils.decodeLinkName(data[0]), data[1] };
	}

	@Override
	protected JPanel createCenterPane() {
		linkEditPanel = new LinkEditPanel(super.data);
		PropertyChangeListener listener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				LinkEditDialog.super.updateOkButtonEnabled();
			}
		};
		linkEditPanel.getTextField().addPropertyChangeListener(TextWidget.TEXT_PROPERTY, listener);
		linkEditPanel.getUrlField().addPropertyChangeListener(TextWidget.TEXT_PROPERTY, listener);
		return linkEditPanel;
	}

	@Override
	protected String buildResult() {
		if (linkEditPanel.getUrlField().getText().isEmpty()) {
			return linkEditPanel.getTextField().getText();
		} else {
			return HtmlUtils.toEncoded(linkEditPanel.getTextField().getText(), linkEditPanel.getUrlField().getText());
		}
	}

	@Override
	protected String getOkDisabledCause() {
		if (linkEditPanel.getTextField().getText().isEmpty()) {
			return LocalizationData.get("LinkEditor.textIsEmpty"); //$NON-NLS-1$
		} else if (!linkEditPanel.isURLOk()) {
			return LocalizationData.get("LinkEditor.incorrectURL"); //$NON-NLS-1$
		}
		return null;
	}
}
