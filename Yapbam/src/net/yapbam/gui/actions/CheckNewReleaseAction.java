package net.yapbam.gui.actions;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.net.HttpURLConnection;
import java.text.MessageFormat;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

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
	//FIXME Need a rewrite because this action may take a long time and is invoked by the swing dispatch event thread.
	// For instance, trying to display a dialog to tell that the operation takes too much time will fail
	// because the repaint swiing events will not be handled properly.
	//See http://java.sun.com/docs/books/tutorial/uiswing/concurrency/index.html
	
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
	
//	static class Toto implements Runnable {
//		private boolean disabled;
//		private JDialog dialog;
//
//		public Toto(Window owner) {
//			JOptionPane jOptionPane = new JOptionPane("Checking for updates, please wait", JOptionPane.INFORMATION_MESSAGE);
//			dialog = jOptionPane.createDialog(owner, "Please wait");
//			dialog.setModal(true);
//		}
//		
//		@Override
//		public void run() {
//			try {
//				Thread.sleep(500);
//				synchronized (this) {
//					if (!disabled) {
//						dialog.setVisible(true);
//					}					
//				}
//			} catch (InterruptedException e) {
//				// Ok, not a problem, the thread was interrupted, so, let's do nothing
//			}
//		}
//		
//		public synchronized void stop() {
//			this.disabled = true;
//			dialog.setVisible(false);
//		}
//	}

	/** Checks for updates over the Internet.
	 * @param owner The parent window of all dialogs that may be opened by this method
	 * @param auto true if the check is an automatic one (it changes the behavior in case of fail or if no update is available)
	 */
	private static void check(Window owner, boolean auto) {
		//TODO It could be cool to display an information window (maybe the check is very, very, long and the user is waiting)
		//See the FIXME comment at the beginning of this file
		boolean silentFail = (auto && Preferences.INSTANCE.getAutoUpdateSilentFail());
		try {
			UpdateInformation update = VersionManager.getUpdateInformation();
			if (update.getHttpErrorCode()!=HttpURLConnection.HTTP_OK) {
				if (!silentFail) {
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
				} else {
					if (!auto) JOptionPane.showMessageDialog(owner, LocalizationData.get("MainMenu.CheckUpdate.NoUpdate"), LocalizationData.get("MainMenu.CheckUpdate.NoUpdate.title"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		} catch (Exception e1) {
			if (!silentFail) {
				String pattern = LocalizationData.get("MainMenu.CheckUpdate.IOError"); //$NON-NLS-1$
				String message = MessageFormat.format(pattern, e1.toString(), VersionManager.YABAM_HOME_URL);
				JOptionPane.showMessageDialog(owner, message, LocalizationData.get("MainMenu.CheckUpdate.Error.title"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			}
		}
	}
	
	public static void doAutoCheck() {
		if (Preferences.INSTANCE.isFirstRun()) {
			int option = JOptionPane.showOptionDialog(null, LocalizationData.get("MainMenu.CheckUpdate.FirstRun.message"), //$NON-NLS-1$
					LocalizationData.get("MainMenu.CheckUpdate.FirstRun.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, //$NON-NLS-1$
					new String[]{LocalizationData.get("GenericButton.yes"), LocalizationData.get("GenericButton.no")}, LocalizationData.get("GenericButton.yes")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			Preferences.INSTANCE.setAutoUpdate(-option, false);
		}
    	int days = Preferences.INSTANCE.getAutoUpdatePeriod();
		if (days>=0) { // If autocheck is on
			Date last = YapbamState.getDate(LAST_UPDATE_CHECK_KEY);
			if (DateUtils.dateToInteger(new Date())-DateUtils.dateToInteger(last)>=days) {
				check (null, true);
			}
		}
	}
}