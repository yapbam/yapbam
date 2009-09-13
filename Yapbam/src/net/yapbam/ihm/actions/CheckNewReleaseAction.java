package net.yapbam.ihm.actions;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.net.HttpURLConnection;
import java.text.MessageFormat;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import net.yapbam.ihm.LocalizationData;
import net.yapbam.update.UpdateInformation;
import net.yapbam.update.VersionManager;

@SuppressWarnings("serial")
public class CheckNewReleaseAction extends AbstractAction {
	private Window owner;

	public CheckNewReleaseAction(Window owner) {
		super(LocalizationData.get("MainMenu.CheckUpdate")); //$NON-NLS-1$
        putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.MainMenu.CheckUpdate.ToolTip")); //$NON-NLS-1$
        this.owner = owner;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			UpdateInformation update = VersionManager.getUpdateInformation();
			if (update.getHttpErrorCode()!=HttpURLConnection.HTTP_OK) {
				String pattern = LocalizationData.get("MainMenu.CheckUpdate.HttpError"); //$NON-NLS-1$
				String message = MessageFormat.format(pattern, update.getHttpErrorCode(), VersionManager.YABAM_HOME_URL);
				JOptionPane.showMessageDialog(owner, message, LocalizationData.get("MainMenu.CheckUpdate.Error.title"), JOptionPane.ERROR_MESSAGE);				 //$NON-NLS-1$
			} else {
				if (update.getLastestRelease().compareTo(VersionManager.getVersion())>0) {
					String pattern = LocalizationData.get("MainMenu.CheckUpdate.Success"); //$NON-NLS-1$
					String message = MessageFormat.format(pattern, VersionManager.getVersion(),update.getLastestRelease(),update.getUpdateURL());
					JOptionPane.showMessageDialog(owner, message, LocalizationData.get("MainMenu.CheckUpdate.Success.title"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
				}
			}
		} catch (Exception e1) {
			String pattern = LocalizationData.get("MainMenu.CheckUpdate.IOError"); //$NON-NLS-1$
			String message = MessageFormat.format(pattern, e1.toString(), VersionManager.YABAM_HOME_URL);
			JOptionPane.showMessageDialog(owner, message, LocalizationData.get("MainMenu.CheckUpdate.Error.title"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
		}
	}
}