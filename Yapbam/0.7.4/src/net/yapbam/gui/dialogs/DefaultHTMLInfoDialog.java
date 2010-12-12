package net.yapbam.gui.dialogs;

import java.awt.Window;

import javax.swing.JPanel;

import net.yapbam.gui.LocalizationData;

import java.lang.Object;
import java.lang.String;

@SuppressWarnings("serial")
public class DefaultHTMLInfoDialog extends AbstractDialog {

	public DefaultHTMLInfoDialog(Window owner, String title, String header, String message) {
		super(owner, title, new String[]{header, message});
		this.cancelButton.setVisible(false);
		this.okButton.setText(LocalizationData.get("GenericButton.close")); //$NON-NLS-1$
		this.okButton.setToolTipText(LocalizationData.get("GenericButton.close.ToolTip")); //$NON-NLS-1$
	}

	@Override
	protected Object buildResult() {
		return null;
	}

	@Override
	protected JPanel createCenterPane(Object data) {
		return new DefaultHTMLInfoPanel(((String[])data)[0], ((String[])data)[1]);
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}
}
