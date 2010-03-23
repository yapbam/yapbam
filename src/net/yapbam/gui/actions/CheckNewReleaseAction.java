package net.yapbam.gui.actions;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.net.HttpURLConnection;
import java.text.MessageFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.dialogs.DefaultHTMLInfoDialog;
import net.yapbam.update.UpdateInformation;
import net.yapbam.update.VersionManager;
import net.yapbam.util.DateUtils;

/** This class is in charge of checking for Yapbam updates over the Internet */
@SuppressWarnings("serial")
public class CheckNewReleaseAction extends AbstractAction {
	private static final String LAST_UPDATE_CHECK_KEY = "net.yapbam.lastUpdateCheck"; //$NON-NLS-1$
	private Window owner;

	/** Constructor
	 * @param owner The parent window of all dialogs that may be opened by this action
	 */
	public CheckNewReleaseAction(Window owner) {
		super(LocalizationData.get("MainMenu.CheckUpdate")); //$NON-NLS-1$
        putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.MainMenu.CheckUpdate.ToolTip")); //$NON-NLS-1$
        this.owner = owner;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		check(owner, false);
	}
	
	/** Checks for updates over the Internet.
	 * @param owner The parent window of all dialogs that may be opened by this method
	 * @param auto true if the check is an automatic one (it changes the behavior in case of fail or if no update is available)
	 */
	private static void check(Window owner, boolean auto) {
		//TODO It could be cool to display an information window (maybe the check is very, very, long and the user is waiting)
		// The best could be to open the window only if we wait more than xxx seconds. But it's not so obvious to implement
		// When we will have time, and when Sun will add a indetermined state to its ProgressMonitor class, we could implement it
		try {
			UpdateInformation update = VersionManager.getUpdateInformation();
			if (update.getHttpErrorCode()!=HttpURLConnection.HTTP_OK) {
				String pattern = LocalizationData.get("MainMenu.CheckUpdate.HttpError"); //$NON-NLS-1$
				String message = MessageFormat.format(pattern, update.getHttpErrorCode(), VersionManager.YABAM_HOME_URL);
				JOptionPane.showMessageDialog(owner, message, LocalizationData.get("MainMenu.CheckUpdate.Error.title"), JOptionPane.ERROR_MESSAGE);	//$NON-NLS-1$
			} else {
				YapbamState.put(LAST_UPDATE_CHECK_KEY, new Date());
				if (update.getLastestRelease().compareTo(VersionManager.getVersion())>0) {
					String pattern = LocalizationData.get("MainMenu.CheckUpdate.Success.Detail"); //$NON-NLS-1$
					String message = MessageFormat.format(pattern, VersionManager.getVersion(),update.getLastestRelease(),update.getUpdateURL());
					new DefaultHTMLInfoDialog(owner, LocalizationData.get("MainMenu.CheckUpdate.Success.title"), LocalizationData.get("MainMenu.CheckUpdate.Success.Header"), //$NON-NLS-1$ //$NON-NLS-2$
							message).setVisible(true);
				} else {
					if (!auto) JOptionPane.showMessageDialog(owner, LocalizationData.get("MainMenu.CheckUpdate.NoUpdate"), LocalizationData.get("MainMenu.CheckUpdate.NoUpdate.title"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		} catch (Exception e1) {
			String pattern = LocalizationData.get("MainMenu.CheckUpdate.IOError"); //$NON-NLS-1$
			String message = MessageFormat.format(pattern, e1.toString(), VersionManager.YABAM_HOME_URL);
			JOptionPane.showMessageDialog(owner, message, LocalizationData.get("MainMenu.CheckUpdate.Error.title"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
		}
	}
	
	public static void doAutoCheck(final Window owner) {
		// This could be a useful reading to understand this method code http://java.sun.com/docs/books/tutorial/uiswing/concurrency/index.html
		if (Preferences.INSTANCE.isFirstRun()) {
			// Ask the user to grant us the right to connect to Internet to check for updates
			int option = JOptionPane.showOptionDialog(owner, LocalizationData.get("MainMenu.CheckUpdate.FirstRun.message"), //$NON-NLS-1$
					LocalizationData.get("MainMenu.CheckUpdate.FirstRun.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, //$NON-NLS-1$
					new String[]{LocalizationData.get("GenericButton.yes"), LocalizationData.get("GenericButton.no")}, LocalizationData.get("GenericButton.yes")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			Preferences.INSTANCE.setAutoUpdate(-option, false);
		}
    	int days = Preferences.INSTANCE.getAutoUpdatePeriod();
		if (days>=0) { // If autocheck is on
			Date last = YapbamState.getDate(LAST_UPDATE_CHECK_KEY);
			if (DateUtils.dateToInteger(new Date())-DateUtils.dateToInteger(last)>=days) {
				// Post a SwingWorker to perform the check
				SwingWorker<UpdateInformation, Void> worker = new SwingWorker<UpdateInformation, Void>() {
					@Override
					protected UpdateInformation doInBackground() throws Exception {
						return VersionManager.getUpdateInformation();
					}
					public void done() {
						try {
							UpdateInformation update = get();
							if (update.getHttpErrorCode()!=HttpURLConnection.HTTP_OK) {
								if (!Preferences.INSTANCE.getAutoUpdateSilentFail()) {
									String pattern = LocalizationData.get("MainMenu.CheckUpdate.HttpError"); //$NON-NLS-1$
									String message = MessageFormat.format(pattern, update.getHttpErrorCode(), VersionManager.YABAM_HOME_URL);
									JOptionPane.showMessageDialog(owner, message, LocalizationData.get("MainMenu.CheckUpdate.Error.title"), JOptionPane.ERROR_MESSAGE);	//$NON-NLS-1$
								}
							} else {
								YapbamState.put(LAST_UPDATE_CHECK_KEY, new Date());
								if (update.getLastestRelease().compareTo(VersionManager.getVersion())>0) {
									String pattern = LocalizationData.get("MainMenu.CheckUpdate.Success.Detail"); //$NON-NLS-1$
									String message = MessageFormat.format(pattern, VersionManager.getVersion(),update.getLastestRelease(),update.getUpdateURL());
									new DefaultHTMLInfoDialog(owner, LocalizationData.get("MainMenu.CheckUpdate.Success.title"), LocalizationData.get("MainMenu.CheckUpdate.Success.Header"), //$NON-NLS-1$ //$NON-NLS-2$
											message).setVisible(true);
								}
							}
						} catch (InterruptedException e) {
						} catch (ExecutionException e) {
							String pattern = LocalizationData.get("MainMenu.CheckUpdate.IOError"); //$NON-NLS-1$
							String message = MessageFormat.format(pattern, e.toString(), VersionManager.YABAM_HOME_URL);
							JOptionPane.showMessageDialog(owner, message, LocalizationData.get("MainMenu.CheckUpdate.Error.title"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
						}
					}
				};
				worker.execute();
			}
		}
	}
}