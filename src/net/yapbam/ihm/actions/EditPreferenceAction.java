package net.yapbam.ihm.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.MainFrame;
import net.yapbam.ihm.dialogs.PreferenceDialog;

@SuppressWarnings("serial")
public class EditPreferenceAction extends AbstractAction {
	private MainFrame frame;
	
	public EditPreferenceAction(MainFrame frame) {
		super(LocalizationData.get("MainMenu.Preferences")); //$NON-NLS-1$
        putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Preferences.ToolTip")); //$NON-NLS-1$
        putValue(Action.MNEMONIC_KEY, (int)LocalizationData.getChar("MainMenu.Preferences.Mnemonic")); //$NON-NLS-1$
        this.frame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new PreferenceDialog(frame).setVisible(true);
	}
}