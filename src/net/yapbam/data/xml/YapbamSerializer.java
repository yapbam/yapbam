package net.yapbam.data.xml;

import java.io.*;
import java.net.URI;
import java.security.AccessControlException;
import java.text.MessageFormat;
import java.util.zip.ZipOutputStream;

import com.fathzer.soft.ajlib.utilities.FileUtils;

import net.yapbam.data.*;

/** The class implements xml yapbam data serialization and deserialization to (or from) an URL.
 * Currently supported URL type are :<UL>
 * <LI> file.</LI>
 * </UL>
 */
public class YapbamSerializer {
	private YapbamSerializer() {
		// To prevent instantiation
	}
	
	/** Saves the data in a file.
	 * @param data The data to save
	 * @param file The file where to save the data
	 * @param zipped true if the data should be wrapped in a zip container
	 * @param report a progress report
	 * @throws FileNotFoundException is file is not available for writing
	 * @throws IOException if something goes wrong while writing
	 */
	public static void write(GlobalData data, File file, boolean zipped, ProgressReport report) throws IOException {
		if (!FileUtils.isWritable(file)) {
			throw new FileNotFoundException(MessageFormat.format("Writing to {} is not allowed",file)); //$NON-NLS-1$
		}
		// Proceed safely, it means backup the old version before to write the new one
		File backupFile = file.exists() && file.isFile() ? getBackupFile(file) : null;
		if (backupFile!=null) {
			FileUtils.copy(file, backupFile, false);
		}
		doWrite(data, file, zipped, report);
		if (report!=null) {
			report.setMax(-1);
		}
		if (backupFile!=null && !backupFile.delete()) {
			throw new IOException(MessageFormat.format("Unable to delete backup file {0}", file));
		}
	}

	private static void doWrite(GlobalData data, File file, boolean zipped, ProgressReport report) throws IOException {
		OutputStream out = new FileOutputStream(file);
		try {
			if (zipped) {
				out = new ZipOutputStream(out);
				new Serializer().writeToZip(data, (ZipOutputStream) out, getEntryName(file.getName()), report);
			} else {
				new Serializer().write(data, out, report);
			}
		} finally {
			out.flush();
			out.close();
		}
	}

	private static File getBackupFile(File file) {
		File parent = file.getParentFile();
		String root = FileUtils.getRootName(file);
		String extension = FileUtils.getExtension(file);
		for (int i = 1; i >0; i++) {
			File candidate = new File(parent, root+"_backup"+i+extension);
			if (!candidate.exists()) {
				return candidate;
			}
		}
		return null;
	}
	
	private static String getEntryName(String fileName) {
		String lowerCase = fileName.toLowerCase();
		if (lowerCase.endsWith(".zip")) {
			fileName = fileName.substring(0, fileName.length()-".zip".length());
			lowerCase = lowerCase.substring(0, lowerCase.length()-".zip".length());
		}
		return lowerCase.endsWith(".xml") ? fileName : fileName+".xml";
	}
	
	/** Reads data from an uri.
	 * @param uri
	 * @param password
	 * @param report 
	 * @return The GlobalData that was located at the uri, null if reading was cancelled (using report in another thread) 
	 * @throws IOException
	 * @throws AccessControlException
	 */
	public static GlobalData read(URI uri, String password, ProgressReport report) throws IOException, AccessControlException {
		if ("file".equals(uri.getScheme()) || "ftp".equals(uri.getScheme())) { //$NON-NLS-1$ //$NON-NLS-2$
			InputStream in = uri.toURL().openStream();
			try {
				GlobalData redData = new Serializer().read(password, in, report);
				if (redData!=null) {
					redData.setURI(uri);
					redData.setChanged(false);
				}
				return redData;
			} finally {
				in.close();
			}
		} else {
			throw new IOException("Unsupported protocol: "+uri.getScheme()); //$NON-NLS-1$
		}
	}
	
	/** Tests whether a password is the right one for an uri.
	 * @param uri The URI containing Yapbam data
	 * @param password A password (null for no password)
	 * @throws IOException If an I/O error occurred
	 */
	public static boolean isPasswordOk(URI uri, String password) throws IOException {
		InputStream global = uri.toURL().openStream();
		try {
			return new Serializer().isPasswordOk(global, password);
		} finally {
			global.close();
		}
	}
}
