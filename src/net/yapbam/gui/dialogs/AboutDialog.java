package net.yapbam.gui.dialogs;

import java.awt.Window;

import javax.swing.JPanel;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.MainFrame;

@SuppressWarnings("serial")
public class AboutDialog extends AbstractDialog<Void, Void> {

	public AboutDialog(Window owner) {
		super(owner, MainFrame.APPLICATION_NAME, null);
		getCancelButton().setVisible(false);
		getOkButton().setText(LocalizationData.get("GenericButton.close")); //$NON-NLS-1$
		getOkButton().setToolTipText(LocalizationData.get("GenericButton.close.ToolTip")); //$NON-NLS-1$
		this.setResizable(true);
		this.pack();
		this.setMinimumSize(getSize());
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
