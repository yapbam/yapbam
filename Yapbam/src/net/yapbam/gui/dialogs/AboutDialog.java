package net.yapbam.gui.dialogs;

import java.awt.Window;

import javax.swing.JPanel;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.AbstractDialog;

@SuppressWarnings("serial")
public class AboutDialog extends AbstractDialog<Void, Void> {

	public AboutDialog(Window owner) {
		super(owner, LocalizationData.get("ApplicationName"), null);
		this.cancelButton.setVisible(false);
		this.okButton.setText(LocalizationData.get("GenericButton.close")); //$NON-NLS-1$
		this.okButton.setToolTipText(LocalizationData.get("GenericButton.close.ToolTip")); //$NON-NLS-1$
		this.setMinimumSize(getSize());
		this.setResizable(true);
	}

	@Override
	protected Void buildResult() {
		return null;
	}

	@Override
	protected JPanel createCenterPane() {
		return new AboutPanel();
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}
}
