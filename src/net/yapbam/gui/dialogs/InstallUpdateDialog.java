package net.yapbam.gui.dialogs;

import java.awt.Window;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JPanel;
import javax.swing.SwingWorker;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;
import net.yapbam.update.UpdateInformation;
import net.yapbam.util.CheckSum;
import net.yapbam.util.FileUtils;
import net.yapbam.util.Portable;

public class InstallUpdateDialog extends LongTaskDialog<UpdateInformation> {
	private static final long serialVersionUID = 1L;

	private WaitPanel waitPanel;

	public InstallUpdateDialog(Window owner, UpdateInformation data) {
		super(owner, LocalizationData.get("Update.Dowloading.title"), data);
	}

	@Override
	protected SwingWorker<?, ?> getWorker() {
		return new DownloadSwingWorker(this);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getOkDisabledCause() {
		// TODO Auto-generated method stub
		return null;
	}

	// A SwingWorker that performs the update availability check
	class DownloadSwingWorker extends SwingWorker<String, Integer> {
		private Window owner;

		DownloadSwingWorker(Window owner) {
			this.owner = owner;
		}
		
		@Override
		protected String doInBackground() throws Exception {
			int last = 0;
			File destinationFolder = Portable.getUpdateFileDirectory();
			// delete the destination directory
			FileUtils.deleteDirectory(destinationFolder);
			// create it again
			destinationFolder.mkdirs();
			// Store the checksum in a file in order to be able to verify the file checksum, when the application
			// will restart
			BufferedWriter out = new BufferedWriter(new FileWriter(new File(destinationFolder, "checksum.txt")));
			out.write(data.getAutoUpdateCheckSum());
			out.flush();
			out.close();
			
			// First, create a digest to verify the checksum
	  	MessageDigest digest;
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
			// Download the file
			InputStream in = data.getAutoUpdateURL().openConnection(Preferences.INSTANCE.getHttpProxy()).getInputStream();
			byte[] buffer = new byte[2048];
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(destinationFolder,"update.zip")), buffer.length);
			long totalSize = 0;
			last = 0;
			int size;
			while ((size = in.read(buffer, 0, buffer.length)) != -1) {
				if (this.isCancelled()) break;
				bos.write(buffer, 0, size);
				totalSize += size;
				if (size>0) digest.update(buffer, 0, size);
				int percent = (int) (totalSize*100/data.getAutoUpdateSize());
				if (percent>last) {
					this.publish(percent);
					last = percent;
				}
			}
			bos.flush();
			bos.close();
			if (this.isCancelled()) {
				FileUtils.deleteDirectory(destinationFolder);
				return null;
			} else {
				return CheckSum.toString(digest.digest());
			}
		}
		
		public void done() {
			InstallUpdateDialog.this.setVisible(false);
			try {
				System.out.println("downloaded : "+this.get()+". expected : "+data.getAutoUpdateCheckSum());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void process(List<Integer> chunks) {
			Integer value = chunks.get(chunks.size()-1);
			waitPanel.setValue(value);
		}
	}

}
