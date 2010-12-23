package net.yapbam.gui.dialogs;

import java.awt.Window;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.YapbamState;
import net.yapbam.update.UpdateInformation;
import net.yapbam.update.VersionManager;

import java.io.IOException;
import java.lang.Object;
import java.lang.String;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("serial")
/** This dialog connects to the Yapbam update site, allow to cancel the search for updates and
 * displays the result.
 * It has some specific features :
 * <UL><LI>It shows with a delay, if the search completes during this time, the dialog is not displayed, and the results is.</LI>
 * <LI>Once it is displayed, it remains visible for a minimum time (to prevent a flash effect if the search completes just after the pop up delay.</LI></UL>
 */
public class CheckUpdateDialog extends LongTaskDialog {
	private boolean auto;

	public CheckUpdateDialog(Window owner, boolean auto) {
		super(owner, LocalizationData.get("MainMenu.CheckUpdate.Dialog.title"), null); //$NON-NLS-1$
		this.auto = auto;
		this.cancelButton.setToolTipText(LocalizationData.get("MainMenu.CheckUpdate.Dialog.cancel.tooltip")); //$NON-NLS-1$
	}

	@Override
	protected Object buildResult() {
		return null;
	}

	@Override
	protected JPanel createCenterPane() {
		return new CheckUpdatePanel();
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}
	
	protected SwingWorker<?, ?> getWorker() {
		return new UpdateSwingWorker(getOwner());
	}
	
	// A SwingWorker that performs the update availability check
	class UpdateSwingWorker extends SwingWorker<UpdateInformation, Void> {
		private static final String LAST_UPDATE_CHECK_KEY = "net.yapbam.lastUpdateCheck"; //$NON-NLS-1$
		private Window owner;

		UpdateSwingWorker(Window owner) {
			this.owner = owner;
		}
		
		@Override
		protected UpdateInformation doInBackground() throws Exception {
			Thread.sleep(1000); //TODO
			return VersionManager.getUpdateInformation();
		}
		public void done() {
			boolean silentFail = (auto && Preferences.INSTANCE.getAutoUpdateSilentFail());
			try {
				if (!isCancelled()) {
					UpdateInformation update = get();
					CheckUpdateDialog.this.setVisible(false);
					if (update.getHttpErrorCode()!=HttpURLConnection.HTTP_OK) { // Connection error
						if (!silentFail) {
							String pattern = LocalizationData.get("MainMenu.CheckUpdate.HttpError"); //$NON-NLS-1$
							String message = MessageFormat.format(pattern, update.getHttpErrorCode(), VersionManager.YABAM_HOME_URL);
							JOptionPane.showMessageDialog(owner, message, LocalizationData.get("MainMenu.CheckUpdate.Error.title"), JOptionPane.ERROR_MESSAGE);	//$NON-NLS-1$
						}
					} else {
						YapbamState.put(LAST_UPDATE_CHECK_KEY, new Date());
						if (update.getLastestRelease().compareTo(VersionManager.getVersion())>0) { // There's an update
							String pattern = LocalizationData.get("MainMenu.CheckUpdate.Success.Detail"); //$NON-NLS-1$
							String message = MessageFormat.format(pattern, VersionManager.getVersion(),update.getLastestRelease(),update.getUpdateURL());
							new DefaultHTMLInfoDialog(owner, LocalizationData.get("MainMenu.CheckUpdate.Success.title"), LocalizationData.get("MainMenu.CheckUpdate.Success.Header"), //$NON-NLS-1$ //$NON-NLS-2$
									message).setVisible(true);
						} else { // Version is up to date
							if (!auto) JOptionPane.showMessageDialog(owner, LocalizationData.get("MainMenu.CheckUpdate.NoUpdate"), LocalizationData.get("MainMenu.CheckUpdate.NoUpdate.title"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
						}
					}
				}
			} catch (InterruptedException e) {
			} catch (ExecutionException e) {
				if (!(isCancelled() || silentFail)) {
					CheckUpdateDialog.this.setVisible(false);
					Throwable cause = e.getCause();
					if ((cause instanceof IOException) || (cause instanceof UnknownHostException)) {
						String pattern = LocalizationData.get("MainMenu.CheckUpdate.IOError"); //$NON-NLS-1$
						String message = MessageFormat.format(pattern, cause, VersionManager.YABAM_HOME_URL);
						JOptionPane.showMessageDialog(owner, message, LocalizationData.get("MainMenu.CheckUpdate.Error.title"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
					} else {
						ErrorManager.INSTANCE.log(cause);
					}
				}
			}
		}
	}
}
