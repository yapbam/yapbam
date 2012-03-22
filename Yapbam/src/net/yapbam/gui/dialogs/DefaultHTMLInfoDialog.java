package net.yapbam.gui.dialogs;

import java.awt.Window;

import javax.swing.JPanel;

import net.astesana.ajlib.swing.dialog.AbstractDialog;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public class DefaultHTMLInfoDialog extends AbstractDialog<String[], Void> {

	public DefaultHTMLInfoDialog(Window owner, String title, String header, String message) {
		super(owner, title, new String[]{header, message});
		this.cancelButton.setVisible(false);
		this.okButton.setText(LocalizationData.get("GenericButton.close")); //$NON-NLS-1$
		this.okButton.setToolTipText(LocalizationData.get("GenericButton.close.ToolTip")); //$NON-NLS-1$
	}

	@Override
	protected Void buildResult() {
		return null;
	}

	@Override
	protected JPanel createCenterPane() {
		return new DefaultHTMLInfoPanel(data[0], data[1]);
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}
}
