package net.yapbam.gui.dialogs.update;

import java.awt.Window;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.utilities.FileUtils;

import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.HelpManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.actions.CheckNewReleaseAction;
import net.yapbam.gui.util.MessageWithLink;
import net.yapbam.update.UpdateInformation;
import net.yapbam.update.VersionManager;
import net.yapbam.util.ApplicationContext;
import net.yapbam.util.Portable;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("serial")
/** This dialog connects to the Yapbam update site, allow to cancel the search for updates and
 * displays the result.
 * It has some specific features :
 * <UL><LI>It shows with a delay, if the search completes during this time, the dialog is not displayed, and the results is.</LI>
 * <LI>Once it is displayed, it remains visible for a minimum time (to prevent a flash effect if the search completes just after the pop up delay.</LI></UL>
 * This url could be a useful reading to understand this class : http://java.sun.com/docs/books/tutorial/uiswing/concurrency/index.html
 */
public class CheckUpdateDialog extends LongTaskDialog<Void, Void> {
	private static final boolean SLOW_UPDATE_CHECKING = Boolean.getBoolean("SlowUpdateChecking"); //$NON-NLS-1$
	private static final String YABAM_HOME_URL = "https://www.yapbam.net"; //$NON-NLS-1$

	private boolean auto;

	/** Constructor.
	 * @param owner The owner window of the this dialog. 
	 * @param auto true if the dialog is created by an automated task, false, if it is the result of a user action.
	 * @param forced true if the dialog is created because the release is very old, despite of user's preferences
	 */
	CheckUpdateDialog(Window owner, boolean auto, boolean forced) {
		super(owner, LocalizationData.get("MainMenu.CheckUpdate.Dialog.title"), null); //$NON-NLS-1$
		this.auto = auto;
		if (auto) {
			setDelay(Long.MAX_VALUE);
		}
		getCancelButton().setToolTipText(LocalizationData.get("MainMenu.CheckUpdate.Dialog.cancel.tooltip")); //$NON-NLS-1$
	}
	
	@Override
	protected Void buildResult() {
		return null;
	}

	@Override
	protected JPanel createCenterPane() {
		return new WaitPanel();
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}
	
	@Override
	protected SwingWorker<?, ?> getWorker() {
		return new UpdateSwingWorker(getOwner());
	}
	
	public static void check(Window owner, boolean auto, boolean forced) {
		CheckUpdateDialog dialog = new CheckUpdateDialog(owner, auto, forced);
		dialog.setVisible(true);
		dialog.dispose(); //TODO This should be useless ... but it seems there's a bug in LongTaskDialog that don't release the dialog once the task has ended
	}

	// A SwingWorker that performs the update availability check
	class UpdateSwingWorker extends SwingWorker<UpdateInformation, Void> {
		private Window owner;

		UpdateSwingWorker(Window owner) {
			this.owner = owner;
		}
		
		@Override
		protected UpdateInformation doInBackground() throws Exception {
			if (SLOW_UPDATE_CHECKING) {
				Thread.sleep(2000);
			}
			return VersionManager.getUpdateInformation();
		}
		
		@Override
		public void done() {
			try {
				if (!isCancelled()) {
					UpdateInformation update = get();
					CheckUpdateDialog.this.setVisible(false);
					int code = update.getHttpErrorCode();
					if (code!=HttpURLConnection.HTTP_OK) {
						// If an error occurred while getting the update information
						if (!auto) {
							String message;
							if (code==HttpURLConnection.HTTP_PROXY_AUTH) {
								message = LocalizationData.get("MainMenu.CheckUpdate.ProxyAuthError"); //$NON-NLS-1$
							} else {
								String pattern = LocalizationData.get("MainMenu.CheckUpdate.HttpError"); //$NON-NLS-1$
								message = Formatter.format(pattern, code, YABAM_HOME_URL);
							}
							JOptionPane.showMessageDialog(owner, message, LocalizationData.get("MainMenu.CheckUpdate.Error.title"), JOptionPane.ERROR_MESSAGE);	//$NON-NLS-1$
						}
					} else {
						// If we've got the update information
						YapbamState.INSTANCE.put(CheckNewReleaseAction.LAST_UPDATE_CHECK_KEY, new Date());
						// Using a tray icon was an idea I had to alert the user in case of update availability
						// Unfortunately, as far as I understood the java tray icon implementation, user can disable 
						// java tray icons in a windows configuration panel. So it's safer to use a dialog.
						if (update.getLastestRelease().compareTo(ApplicationContext.getVersion())>0) {
							// If there's an update
							if (isUpdateInstallable(update)) {
								CheckUpdateDialog.this.setVisible(false);
								InstallUpdateDialog dialog = new InstallUpdateDialog(owner, auto && Preferences.INSTANCE.getAutoUpdateInstall(), update);
								dialog.setVisible(true);
							}
//TODO Remove the localized wordings (verify they are no more used)						new DefaultHTMLInfoDialog(owner, LocalizationData.get("MainMenu.CheckUpdate.Success.title"), LocalizationData.get("MainMenu.CheckUpdate.Success.Header"), //$NON-NLS-1$ //$NON-NLS-2$
//									message).setVisible(true);
						} else {
							// Version is up to date
							if (!auto) {
								JOptionPane.showMessageDialog(owner, LocalizationData.get("MainMenu.CheckUpdate.NoUpdate"), LocalizationData.get("MainMenu.CheckUpdate.NoUpdate.title"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
							}
						}
					}
				}
			} catch (InterruptedException e) {
			} catch (ExecutionException e) {
				if (!(isCancelled() || auto)) {
					CheckUpdateDialog.this.setVisible(false);
					Throwable cause = e.getCause();
					if ((cause instanceof IOException) || (cause instanceof UnknownHostException)) {
						String pattern = LocalizationData.get("MainMenu.CheckUpdate.IOError"); //$NON-NLS-1$
						String message = Formatter.format(pattern, cause, YABAM_HOME_URL);
						JOptionPane.showMessageDialog(owner, message, LocalizationData.get("MainMenu.CheckUpdate.Error.title"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
					} else {
						ErrorManager.INSTANCE.log(owner,cause);
					}
				}
			}
		}

		/** Gets the action to do when an update is available.
		 * @param update The update information
		 * @return true if the update should be installed
		 */
		private boolean isUpdateInstallable(UpdateInformation update) {
			//TODO Use a HTMLPane in order to be able to display the relnotes
			File launchDirectory = Portable.getApplicationDirectory();
			boolean canWrite = FileUtils.isWritable(launchDirectory);
			if (auto && Preferences.INSTANCE.getAutoUpdateInstall() && canWrite) {
				return true;
			}

			String title = LocalizationData.get("MainMenu.CheckUpdate.Success.title"); //$NON-NLS-1$
			if (!canWrite) {
				// The application hasn't the write permission to the installation directory
				String open = LocalizationData.get("MainMenu.CheckUpdate.Success.openSite"); //$NON-NLS-1$
				String cancel = LocalizationData.get("GenericButton.cancel"); //$NON-NLS-1$
				String message;
				if (launchDirectory.canWrite()) {
					// If the launch directory is writable but this application has not the right (example, "program files" protected by Windows UAC).
					message = LocalizationData.get("MainMenu.CheckUpdate.Success.systemProtected"); //$NON-NLS-1$
				} else {
					// If the launch directory is not writable.
					message = LocalizationData.get("MainMenu.CheckUpdate.Success.writeProtected"); //$NON-NLS-1$
				}
				message = Formatter.format(message, ApplicationContext.getVersion(), update.getLastestRelease());
				int choice = JOptionPane.showOptionDialog(owner, new MessageWithLink(message), title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{cancel, open}, open);
				if (choice==1) {
					try {
						HelpManager.show(owner, new URI(YABAM_HOME_URL));
					} catch (Exception e) {
						ErrorManager.INSTANCE.log(owner, e);
					}
				}
				return false;
			} else {
				// The application can write to the the installation directory
				String pattern = LocalizationData.get("MainMenu.CheckUpdate.Success.Detail"); //$NON-NLS-1$
				String download = LocalizationData.get("MainMenu.CheckUpdate.installNow"); //$NON-NLS-1$
				int choice = JOptionPane.showOptionDialog(owner, Formatter.format(pattern, ApplicationContext.getVersion(),update.getLastestRelease()), title, //$NON-NLS-1$
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
					new String[]{LocalizationData.get("MainMenu.CheckUpdate.cancel"), download}, download); //$NON-NLS-1$
				return choice == 1;
			}
		}
	}
}
