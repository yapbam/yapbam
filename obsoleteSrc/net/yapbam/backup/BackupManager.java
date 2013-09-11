package net.yapbam.backup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.fathzer.soft.ajlib.utilities.StringUtils;

/**
 * A class that backup files to a folder on the machine or to a ftp server.
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public class BackupManager {
	private URL backupFolder;
	private long sizeLimit;
	private AbstractBackupManager manager;
	
	/** Constructor.
	 * @param backupFolder The place to store the backups
	 * @param sizeLimit The space allocated to backups (0 or less if there's no limit) in MBytes
	 * @throws IOException 
	 */
	public BackupManager(URL backupFolder, long sizeLimit) throws IOException {
		this.backupFolder = backupFolder;
		this.sizeLimit = sizeLimit;
		String scheme = backupFolder.getProtocol();
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
	public synchronized void backup(URL originalFile, boolean compress) throws IOException {
		// Ensure that the backup folder is created.
		this.manager.createFolder(backupFolder);
		// Get the backup file name
		String backupFileName = getBackupFileName(originalFile, compress);
		// Open the original file
		InputStream in = originalFile.openStream();
		try {
			OutputStream out = this.manager.getOutputStream(backupFolder, backupFileName);
			try {
				if (compress) {
					// Create the ZIP file
					ZipOutputStream zipOut = new ZipOutputStream(out);
					try {
						zipOut.setLevel(9); // Sets maximum compression level
						zip(getFileName(originalFile), getTimeStamp(originalFile).getTimeInMillis(), in, zipOut);
					} finally {
						// Complete the ZIP file
						zipOut.close();
					}
				} else {
					copy(in, out);
				}
			} finally {
				this.manager.close(out);
			}
			this.manager.setTime(backupFolder, backupFileName, getTimeStamp(originalFile));
		} finally {
			in.close();
		}
//TODO		purgeBackupFolder();
	}
	
	private void copy(InputStream in, OutputStream out) throws IOException {
		// Create a buffer for reading the files
		byte[] buf = new byte[1024];
		// Transfer bytes from the file to the ZIP file
		int len;
		long total = 0; 
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
			total += len;
			setBytesCopied(total);
		}
	}
	
	/** This method is called each time a bunch of bytes are copied.
	 * <br>This method does nothing but you can override it to produce an progress report.
	 * @param bytesCopied The number of bytes that have been copied since backup was called.
	 */
	protected void setBytesCopied (long bytesCopied) {}
	
	private ZipEntry zip(String entryName, long time, InputStream in, ZipOutputStream out) throws IOException {
		// Add ZIP entry to output stream.
		ZipEntry entry = new ZipEntry(entryName);
		entry.setTime(time);
		out.putNextEntry(entry);
		// Copy input to the entry
		copy(in, out);
		// Complete the entry
		out.closeEntry();
		return entry;
	}

	/** Gets the file name of the backup file. 
	 * @param originalContent the url of the original content
	 * @param compress 
	 * @return a file name. This file name includes date information in order to be able to purge the older files (if it is needed), even if the server
	 * doesn't support setting the time stamp of the remote files. 
	 */
	private String getBackupFileName(URL originalContent, boolean compress) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
		if ((originalContent.getProtocol()==null) || (originalContent.getProtocol().equalsIgnoreCase("file"))) {
			String fileName = format.format(getTimeStamp(originalContent).getTime())+"_"+getFileName(originalContent);
			if (compress) fileName = fileName+".zip";
			return fileName;
		} else {
			throw new IllegalArgumentException("only files are currently supported");
		}
	}
	
	private String getFileName(URL url) {
		String[] path = StringUtils.split(url.getPath(), '/');
		return path[path.length-1];
	}
		
	private Calendar getTimeStamp(URL url) {
		if ((url.getProtocol()==null) || (url.getProtocol().equalsIgnoreCase("file"))) {
			File file = new File(url.getPath());
			Calendar result = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.US);
			result.setTimeInMillis(file.lastModified());
			return result;
		} else {
			throw new IllegalArgumentException("only files are currently supported");
		}
	}
	
	/** Closes the backup manager.
	 * If the bakcup is performed to a remote server, closes the connection to that server.
	 */
	public synchronized void close() {
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
			BackupManager bkMgr = new VerboseBM(new URL(args[0]), Long.parseLong(args[1]));
			try {
				bkMgr.backup(new URL(args[2]), Boolean.parseBoolean(args[3]));
			} finally {
				bkMgr.close();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	private static class VerboseBM extends BackupManager {
		public VerboseBM(URL backupFolder, long sizeLimit) throws IOException {
			super(backupFolder, sizeLimit);
		}

		@Override
		protected void setBytesCopied(long bytesCopied) {
			System.out.println (bytesCopied+" bytes were copied");
		}
	}
}
