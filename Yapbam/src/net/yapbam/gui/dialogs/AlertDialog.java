package net.yapbam.gui.dialogs;

import java.awt.Window;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;

import net.yapbam.gui.LocalizationData;

public class AlertDialog extends AbstractDialog<String, Boolean> {
	private static final long serialVersionUID = 1L;
	
	ErrorPanel panel;
	
	public AlertDialog(Window parent, String title, String message) {
		super(parent, title, message);
		getOkButton().setText(LocalizationData.get("GenericButton.continue")); //$NON-NLS-1$
		this.pack();
	}

	@SuppressWarnings("serial")
	@Override
	protected JPanel createCenterPane() {
		panel = new ErrorPanel(){
			@Override
			protected String getDontAskMeText() {
				return LocalizationData.get("GenericButton.dontAskMeAnyMore"); //$NON-NLS-1$
			}
			@Override
			protected String getDontAskMeTooltip() {
				return ""; //$NON-NLS-1$
			}
			@Override
			protected Icon getIcon() {
				return UIManager.getIcon("OptionPane.warningIcon"); //$NON-NLS-1$
			}
			@Override
			protected String getMessage() {
				return data;
			}	
		};
		return panel;
	}

	@Override
	protected Boolean buildResult() {
		return panel.isDontAskMeSelected();
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}
}
