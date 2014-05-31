package net.yapbam.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.MainFrame;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.dialogs.preferences.PreferenceDialog;

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
		boolean expertMode = Preferences.INSTANCE.isExpertMode() || ((e.getModifiers() & ActionEvent.SHIFT_MASK) != 0);
		PreferenceDialog dialog = new PreferenceDialog(frame, expertMode);
		dialog.setVisible(true);
		Boolean result = dialog.getChanges();
		if ((result!=null) && result) {
			frame.restart();
		}
	}
}