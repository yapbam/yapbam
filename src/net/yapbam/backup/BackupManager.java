package net.yapbam.backup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A class that backup files to a folder on the machine or to a ftp server.
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public class BackupManager {
	private URI backupFolder;
	private long sizeLimit;
	private AbstractBackupManager manager;
	
	/** Constructor.
	 * @param backupFolder The place to store the backups
	 * @param sizeLimit The space allocated to backups (0 or less if there's no limit) in MBytes
	 * @throws IOException 
	 */
	public BackupManager(URI backupFolder, long sizeLimit) throws IOException {
		this.backupFolder = backupFolder;
		this.sizeLimit = sizeLimit;
		String scheme = backupFolder.getScheme();
		if ((scheme==null) || ("file".equalsIgnoreCase(scheme))) {
			this.manager = new DiskManager();
		} else if ("ftp".equalsIgnoreCase(scheme)) {
			this.manager = new FTPManager(backupFolder);
		} else {
			throw new IllegalArgumentException("only ftp and file URI are supported");
		}
	}
	
	/** Backups a file.
	 * @param originalFile The file to be saved
	 * @param compress true to have a zip backup file
	 * @throws IOException 
	 */
	public void backup(URI originalFile, boolean compress) throws IOException {
		// Ensure that the backup folder is created.
		this.manager.createFolder(backupFolder);
		InputStream originalStream = originalFile.toURL().openStream();
		try {
			this.manager.copy(originalStream, getTimeStamp(originalFile), backupFolder.getPath(), getBackupFileName(originalFile));
		} finally {
			originalStream.close();
		}
//		File f = this.createFile();
//		purgeBackupFolder();
	}
	
	private String getBackupFileName(URI originalContent) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
		if ((originalContent.getScheme()==null) || (originalContent.getScheme().equalsIgnoreCase("file"))) {
			File file = new File(originalContent);
			return format.format(new Date(file.lastModified()))+"_"+file.getName();
		} else {
			throw new IllegalArgumentException("only files are currently supported");
		}
	}
	
	private Calendar getTimeStamp(URI uri) {
		if ((uri.getScheme()==null) || (uri.getScheme().equalsIgnoreCase("file"))) {
			File file = new File(uri);
			Calendar result = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			result.setTimeInMillis(file.lastModified());
			return result;
		} else {
			throw new IllegalArgumentException("only files are currently supported");
		}
	}
	
	/** Closes the backup manager.
	 * If the bakcup is performed to a remote server, closes the connection to that server.
	 */
	public void close() {
		if (this.manager!=null) this.manager.close();
	}
	
	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			BackupManager bkMgr = new BackupManager(new URI(args[0]), Long.parseLong(args[1]));
			try {
				bkMgr.backup(new URI(args[2]), Boolean.parseBoolean(args[3]));
			} finally {
				bkMgr.close();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
