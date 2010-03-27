package net.yapbam.gui.dialogs;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.YapbamState;
import net.yapbam.update.UpdateInformation;
import net.yapbam.update.VersionManager;

import java.lang.Object;
import java.lang.String;
import java.net.HttpURLConnection;
import java.text.MessageFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("serial")
/** This dialog connects to the Yapbam update site, allow to cancel the search for updates and
 * displays the result.
 * It has some particularities :
 * <UL><LI>It shows with a delay, if the search completes during this time, the dialog is not displayed, and the results is.</LI>
 * <LI>If it is displayed, it remains visible for a minimum time (to prevent a flash effect if the search completes just after the pop up delay.</LI></UL>
 */
public class CheckUpdateDialog extends AbstractDialog {
	private static final int DELAY = 500;
	private static final int MINIMUM_TIME_VISIBLE = 1000;
	
	private Window owner;
	private boolean auto;
	private long setVisibleTime;
	private UpdateSwingWorker updateSwingWorker;

	public CheckUpdateDialog(Window owner, boolean auto) {
		super(owner, LocalizationData.get("MainMenu.CheckUpdate.Dialog.title"), null); //$NON-NLS-1$
		this.owner = owner;
		this.auto = auto;
		this.okButton.setVisible(false);
		this.cancelButton.setText(LocalizationData.get("GenericButton.cancel")); //$NON-NLS-1$
		this.cancelButton.setToolTipText(LocalizationData.get("MainMenu.CheckUpdate.Dialog.cancel.tooltip")); //$NON-NLS-1$
		this.cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateSwingWorker.cancel(false);
			}
		});
	}

	@Override
	protected Object buildResult() {
		return null;
	}

	@Override
	protected JPanel createCenterPane(Object data) {
		return new CheckUpdatePanel();
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}
	
	@Override
	public void setVisible(boolean visible) {
		if (visible) { // If the dialog is opened
			// Start the "check for update" thread.
			this.updateSwingWorker = new UpdateSwingWorker(this.owner);
			updateSwingWorker.execute();
			// Start a thread that will delay the dialog display 
			new SwingWorker<Object, Void>() {
				@Override
				protected Object doInBackground() throws Exception {
					Thread.sleep(DELAY);
					return null;
				}

				@Override
				protected void done() {
					super.done();
					if (!updateSwingWorker.isDone()) {
						doShow();
					}
				}
			}.execute();
		} else { // If the dialog is closed
			long delay = MINIMUM_TIME_VISIBLE-(System.currentTimeMillis()-this.setVisibleTime);
			try {
				if (delay>0) { // If the dialog is display for less than 500 ms, wait for the user to see what happens ;-)
					Thread.sleep(delay);
				}
			} catch (InterruptedException e) {
			}
			super.setVisible(visible);
		}
	}
	
	private void doShow() {
		CheckUpdateDialog.this.setVisibleTime = System.currentTimeMillis(); // Remember when the dialog was displayed
		super.setVisible(true);
	}
	
	// Post a SwingWorker to perform the check
	class UpdateSwingWorker extends SwingWorker<UpdateInformation, Void> {
		private static final String LAST_UPDATE_CHECK_KEY = "net.yapbam.lastUpdateCheck"; //$NON-NLS-1$
		private Window owner;

		UpdateSwingWorker(Window owner) {
			this.owner = owner;
		}
		
		@Override
		protected UpdateInformation doInBackground() throws Exception {
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
					String pattern = LocalizationData.get("MainMenu.CheckUpdate.IOError"); //$NON-NLS-1$
					String message = MessageFormat.format(pattern, e.toString(), VersionManager.YABAM_HOME_URL);
					JOptionPane.showMessageDialog(owner, message, LocalizationData.get("MainMenu.CheckUpdate.Error.title"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
				}
			}
		}
	}
}
