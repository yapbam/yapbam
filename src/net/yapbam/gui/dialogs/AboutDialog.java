package net.yapbam.gui.dialogs;

import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.MainFrame;

@SuppressWarnings("serial")
public class AboutDialog extends AbstractDialog<Boolean, Void> {
	public AboutDialog(Window owner, boolean showJavaVersion) {
		super(owner, MainFrame.APPLICATION_NAME, showJavaVersion);
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

	@Override
	protected JComponent createExtraComponent() {
		return data.booleanValue() ? new JLabel(System.getProperty("java.version")) : null;
	}
}
