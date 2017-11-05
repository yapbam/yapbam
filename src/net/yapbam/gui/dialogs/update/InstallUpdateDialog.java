package net.yapbam.gui.dialogs.update;

import java.awt.Window;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.utilities.FileUtils;
import com.fathzer.soft.ajlib.utilities.NullUtils;

import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.MainFrame;
import net.yapbam.gui.Preferences;
import net.yapbam.update.UpdateInformation;
import net.yapbam.util.Portable;
import net.yapbam.util.SecureDownloader;
import net.yapbam.util.SecureDownloader.DownloadInfo;

public class InstallUpdateDialog extends LongTaskDialog<UpdateInformation, Void> {
	private static final long serialVersionUID = 1L;

	private WaitPanel waitPanel;
	private boolean auto;

	public InstallUpdateDialog(Window owner, boolean auto, UpdateInformation data) {
		super(owner, LocalizationData.get("Update.Downloading.title"), data); //$NON-NLS-1$
		this.auto = auto;
		if (auto) {
			setDelay(Long.MAX_VALUE);
		}
	}

	@Override
	protected SwingWorker<?, ?> getWorker() {
		return new DownloadSwingWorker(this.getOwner());
	}

	@Override
	protected JPanel createCenterPane() {
		waitPanel = new WaitPanel();
		waitPanel.setMessage(Formatter.format(LocalizationData.get("Update.Downloading.message"),data.getLastestRelease().toString())); //$NON-NLS-1$
		waitPanel.setIndeterminate(false);
		waitPanel.setMaximum(100);
		return waitPanel;
	}

	@Override
	protected Void buildResult() {
		return null;
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}

	// A SwingWorker that performs the update availability check
	class DownloadSwingWorker extends SwingWorker<Boolean, Integer> {
		private Window owner;
		private int lastProgress;

		DownloadSwingWorker(Window owner) {
			this.owner = owner;
			this.lastProgress = 0;
		}
		
		@Override
		protected Boolean doInBackground() throws Exception {
			while (true) {
				try {
					File destinationFolder = Portable.getUpdateFileDirectory();
					// delete the destination directory
					FileUtils.deleteDirectory(destinationFolder);
					// create it again
					destinationFolder.mkdirs();
					
					// Download the files
					boolean ok = false;
					String errorMessage = null;
					SecureDownloader sd = new MyDownloader(Preferences.INSTANCE.getHttpProxy());
					DownloadInfo info = sd.download(data.getAutoUpdateURL(), new File(destinationFolder,"update.zip")); //$NON-NLS-1$
					String zipChck = info==null?null:info.getCheckSum();
					if (NullUtils.areEquals(zipChck, data.getAutoUpdateCheckSum())) {
						DownloadInfo jarInfo = sd.download(data.getAutoUpdaterURL(), new File(destinationFolder,"updater.jar")); //$NON-NLS-1$
						String updaterChck = jarInfo==null?null:jarInfo.getCheckSum();
						ok = NullUtils.areEquals(updaterChck, data.getAutoUpdaterCheckSum());
						if (!ok) {
							errorMessage = "Checksum of "+data.getAutoUpdaterURL()+Messages.getString("InstallUpdateDialog.5")+zipChck+Messages.getString("InstallUpdateDialog.6")+data.getAutoUpdaterCheckSum()+Messages.getString("InstallUpdateDialog.7"); //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						}
					} else {
						errorMessage = Messages.getString("InstallUpdateDialog.8")+data.getAutoUpdateURL()+Messages.getString("InstallUpdateDialog.9")+zipChck+Messages.getString("InstallUpdateDialog.10")+data.getAutoUpdateCheckSum()+Messages.getString("InstallUpdateDialog.11"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					}

					if (this.isCancelled() || !ok) {
						FileUtils.deleteDirectory(destinationFolder);
					}
					if (this.isCancelled()) {
						return null;
					} else if (!ok) {
						throw new IOException(errorMessage);
					} else {
						return Boolean.TRUE;
					}
				} catch (Exception e) {
					if (!auto) {
						DoShowDialog doShowDialog = new DoShowDialog();
						SwingUtilities.invokeAndWait(doShowDialog);
						if (!doShowDialog.proceedConfirmed) {
							return null;
						}
					}
				}
			}
		}
				
		private class MyDownloader extends SecureDownloader {
			MyDownloader(Proxy proxy) {
				super(proxy);
			}

			@Override
			protected void progress(long downloadedSize) {
				if (DownloadSwingWorker.this.isCancelled()) {
					this.cancel();
				} else {
					int percent = (int) (downloadedSize*100/(data.getAutoUpdateSize()+data.getAutoUpdaterSize()));
					if (percent!=lastProgress) {
						DownloadSwingWorker.this.publish(percent);
						lastProgress = percent;
					}
				}
			}
		}
		
		@Override
		public void done() {
			InstallUpdateDialog.this.setVisible(false);
			try {
				if (this.get()!=null) { // If download wasn't canceled
					if (this.get()) { // download is ok
						// I've thought about adding here a shutdown hook to uncompress the downloaded zip file,
						// but it seems it's not really safe, as it would occur in a very critical time
						// for the JVM (see Shutdown hook documentation)
						// I preferred to use the "standard" MainFrame close job.
						MainFrame.updater = getUpdaterFile();
						// Display message to inform the user that the download is completed
						String message = Formatter.format(LocalizationData.get(Messages.getString("InstallUpdateDialog.12")),data.getLastestRelease().toString()); //$NON-NLS-1$
						Object[] options = {LocalizationData.get("GenericButton.close"), LocalizationData.get("Update.Downloaded.quitNow")}; //$NON-NLS-1$ //$NON-NLS-2$
						int choice = JOptionPane.showOptionDialog(owner, message, LocalizationData.get("Update.Downloaded.title"), JOptionPane.OK_OPTION, //$NON-NLS-1$
								JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
						if (choice==1) {
							// There could be some dialogs showing at this time (for instance, at launch time, the welcome screen could be displayed)
							// We should close them or the application will never quit
							for (Window window : owner.getOwnedWindows()) {
								window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
							}
							owner.dispatchEvent(new WindowEvent(owner, WindowEvent.WINDOW_CLOSING));
						}
					}
				}
			} catch (CancellationException e) {
				// The task has been canceled, no problem here, we just have to do ... nothing
			} catch (InterruptedException e) {
			} catch (ExecutionException e) {
				ErrorManager.INSTANCE.log(owner,e);
			}
		}

		private File getUpdaterFile() {
			return new File(Portable.getUpdateFileDirectory(),"updater.jar"); //$NON-NLS-1$
		}

		class DoShowDialog implements Runnable {
			boolean proceedConfirmed;

			@Override
			public void run() {
				Object[] options = { LocalizationData.get("GenericButton.cancel"), LocalizationData.get("Update.DownloadFailed.retry") }; //$NON-NLS-1$ //$NON-NLS-2$
				int n = JOptionPane.showOptionDialog(owner, LocalizationData.get("Update.DownloadFailed.message"), //$NON-NLS-1$
						LocalizationData.get("Update.DownloadFailed.title"), JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, //$NON-NLS-1$
						null, options, options[1]);
				proceedConfirmed = (n == 1);
			}
		}

		@Override
		protected void process(List<Integer> chunks) {
			Integer value = chunks.get(chunks.size()-1);
			waitPanel.setValue(value);
		}
	}
}
