package net.yapbam.gui.dialogs;

import java.awt.Window;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.MainFrame;
import net.yapbam.gui.Preferences;
import net.yapbam.update.UpdateInformation;
import net.yapbam.util.CheckSum;
import net.yapbam.util.FileUtils;
import net.yapbam.util.NullUtils;
import net.yapbam.util.Portable;

public class InstallUpdateDialog extends LongTaskDialog<UpdateInformation> {
	private static final long serialVersionUID = 1L;

	private WaitPanel waitPanel;
	private boolean auto;

	public InstallUpdateDialog(Window owner, boolean auto, UpdateInformation data) {
		super(owner, LocalizationData.get("Update.Downloading.title"), data);
		this.auto = auto;
		if (auto) setDelay(Long.MAX_VALUE);
	}

	@Override
	protected SwingWorker<?, ?> getWorker() {
		return new DownloadSwingWorker(this.getOwner());
	}

	@Override
	protected JPanel createCenterPane() {
		waitPanel = new WaitPanel();
		waitPanel.setMessage(MessageFormat.format(LocalizationData.get("Update.Downloading.message"),data.getLastestRelease().toString()));
		waitPanel.setIndeterminate(false);
		waitPanel.setMaximum(100);
		return waitPanel;
	}

	@Override
	protected Object buildResult() {
		return null;
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}

	// A SwingWorker that performs the update availability check
	class DownloadSwingWorker extends SwingWorker<Boolean, Integer> {
		private Window owner;
		private long downloaded;
		private int lastProgress;

		DownloadSwingWorker(Window owner) {
			this.owner = owner;
			this.lastProgress = 0;
		}
		
		@Override
		protected Boolean doInBackground() throws Exception {
			while (true) {
				try {
					this.downloaded = 0;
					File destinationFolder = Portable.getUpdateFileDirectory();
					// delete the destination directory
					FileUtils.deleteDirectory(destinationFolder);
					// create it again
					destinationFolder.mkdirs();
					// Store the checksum in a file in order to be able to verify the file checksum, when the application
					// will restart ... not useful
//					BufferedWriter out = new BufferedWriter(new FileWriter(new File(destinationFolder, "checksum.txt")));
//					out.write(data.getAutoUpdateCheckSum());
//					out.flush();
//					out.close();
					
					// Download the files
					boolean ok = false;
					String zipChck = download(data.getAutoUpdateURL(), new File(destinationFolder,"update.zip"));
					if (NullUtils.areEquals(zipChck, data.getAutoUpdateCheckSum())) {
						String updaterChck = download(data.getAutoUpdaterURL(), new File(destinationFolder,"updater.jar"));
						ok = NullUtils.areEquals(updaterChck, data.getAutoUpdaterCheckSum());
						if (!ok) System.err.println ("ALERT checksum of "+data.getAutoUpdaterURL()+" is "+zipChck+" ("+data.getAutoUpdaterCheckSum()+" was expected)");
					} else {
						System.err.println ("ALERT checksum of "+data.getAutoUpdateURL()+" is "+zipChck+" ("+data.getAutoUpdateCheckSum()+" was expected)");
					}

					if (this.isCancelled() || !ok) FileUtils.deleteDirectory(destinationFolder);
					if (this.isCancelled()) return null;
					if (!ok) throw new IOException("invalid checksum");
					return Boolean.TRUE;
				} catch (Exception e) {
					if (!auto) {
						DoShowDialog doShowDialog = new DoShowDialog();
						SwingUtilities.invokeAndWait(doShowDialog);
						if (!doShowDialog.proceedConfirmed) return null;
					}
				}
			}
		}
				
		private void progress(int size) {
			downloaded += size;
			int percent = (int) (downloaded*100/(data.getAutoUpdateSize()+data.getAutoUpdaterSize()));
			if (percent!=lastProgress) {
				this.publish(percent);
				lastProgress = percent;
			}
		}
		
		private String download(URL url, File file) throws UnknownHostException, IOException {
			// First, create a digest to verify the checksum
	  	MessageDigest digest;
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
			InputStream in = url.openConnection(Preferences.INSTANCE.getHttpProxy()).getInputStream();
			byte[] buffer = new byte[2048];
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file), buffer.length);
			try {
				int size;
				while ((size = in.read(buffer, 0, buffer.length)) != -1) {
					if (this.isCancelled()) break;
					bos.write(buffer, 0, size);
					if (size>0) {
						digest.update(buffer, 0, size);
						progress(size);
					}
				}
			} finally {
				bos.flush();
				bos.close();
			}
			return CheckSum.toString(digest.digest());
		}
		
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
						String message = MessageFormat.format(LocalizationData.get("Update.Downloaded.message"),data.getLastestRelease().toString());
						Object[] options = {LocalizationData.get("GenericButton.close"), LocalizationData.get("Update.Downloaded.quitNow")};
						int choice = JOptionPane.showOptionDialog(owner, message, LocalizationData.get("Update.Downloaded.title"), JOptionPane.OK_OPTION,
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
				ErrorManager.INSTANCE.log(e);
			}
		}

		private File getUpdaterFile() {
			return new File(Portable.getUpdateFileDirectory(),"updater.jar");
		}

		class DoShowDialog implements Runnable {
			boolean proceedConfirmed;

			public void run() {
				Object[] options = { LocalizationData.get("GenericButton.cancel"), LocalizationData.get("Update.DownloadFailed.retry") };
				int n = JOptionPane.showOptionDialog(owner, LocalizationData.get("Update.DownloadFailed.message"),
						LocalizationData.get("Update.DownloadFailed.title"), JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE,
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
