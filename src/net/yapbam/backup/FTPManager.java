package net.yapbam.backup;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import net.yapbam.util.StringUtils;

import org.apache.commons.net.ftp.FTPClient;

class FTPManager implements AbstractBackupManager {
	private FTPClient client;
	
	public FTPManager(URI location) throws IOException {
		this.client = new FTPClient();
		String host = location.getHost();
		int port = location.getPort();
		if (port<0) {
			this.client.connect(host);
		} else {
			this.client.connect(host, port);
		}
		String[] login = StringUtils.split(location.getRawUserInfo(), ':');
		this.client.login(login[0], login.length>1?login[1]:"");
	}
	
	@Override
	public void createFolder(URI folder) throws IOException {
		String path = folder.getPath();
		while (path.charAt(0)=='/') { path=path.substring(1);}
		String[] paths = StringUtils.split(path,'/');
		for (String p : paths) {
			if (!this.client.changeWorkingDirectory(p)) {
				// Not able to enter the directory ... probably when need to create it
				if (!this.client.makeDirectory(p)) throw new IOException("unable to create folder "+p);
				if (!this.client.changeWorkingDirectory(p)) throw new IOException("unable to access folder "+p);
			}
		}
//		this.client.changeWorkingDirectory("/");
//		FTPFile[] files = this.client.listFiles();
//		for (FTPFile file : files) {
//			System.out.println(file.getName() + " : " + file.getSize());
//		}
	}

	@Override
	public void close() {
		if (this.client.isConnected()) try {
			this.client.disconnect();
		} catch (IOException e) {
			// We can close ... ok, what can we do except leave it opened ?
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}

	@Override
	public void copy(InputStream originalStream, Calendar originalDate, String folderPath, String backupFileName) throws IOException {
		if ((folderPath.length()==0) || (folderPath.charAt(0)!='/')) folderPath = "/" + folderPath;
		if (!this.client.changeWorkingDirectory(folderPath)) throw new IOException("Unable to access to folder "+folderPath);
		if (!this.client.storeFile(backupFileName, originalStream)) throw new IOException("Unable to copy the file into folder "+folderPath);
		// Change the remote file last modification date
		String timeVal = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(originalDate.getTime());
		this.client.setModificationTime(backupFileName, timeVal); // It doesn't really matters if it succeed or not, so, we do not test the returned value
	}
}