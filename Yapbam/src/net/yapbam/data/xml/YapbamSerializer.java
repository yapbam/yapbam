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
	private static final String XML_EXTENSION = ".xml"; //$NON-NLS-1$
	private static final String ZIP_EXTENSION = ".zip"; //$NON-NLS-1$
	private static final String CLASSPATH_SCHEME = "classpath"; //$NON-NLS-1$

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
			throw new FileNotFoundException(MessageFormat.format("Writing to {0} is not allowed",file)); //$NON-NLS-1$
		}
		// Proceed safely, it means backup the old version before to write the new one
		File backupFile = file.exists() && file.isFile() ? getBackupFile(file) : null;
		if (backupFile!=null) {
			FileUtils.copy(file, backupFile, true);
		}
		doWrite(data, file, zipped, report);
		if (report!=null) {
			report.setMax(-1);
		}
		if (backupFile!=null && !backupFile.delete()) {
			// Unable to delete the backup file.
			// It seems strange that we were able to write to a file and we can't delete it.
			// It's strange, but possible (tested under Windows 8).
			// In such a case, we will empty the file
			OutputStream out = new FileOutputStream(backupFile);
			out.close();
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
			out.flush();
		} finally {
			out.close();
		}
	}

	private static File getBackupFile(File file) {
		File parent = file.getParentFile();
		String root = FileUtils.getRootName(file);
		String extension = FileUtils.getExtension(file);
		if (extension==null) {
			extension = "";
		}
		for (int index = 1; ; index++) {
			File candidate = new File(parent, root+"_backup"+index+extension); //$NON-NLS-1$
			if (!candidate.exists() || (candidate.length()==0)) {
				return candidate;
			}
		}
	}
	
	private static String getEntryName(String fileName) {
		String lowerCase = fileName.toLowerCase();
		if (lowerCase.endsWith(ZIP_EXTENSION)) {
			fileName = fileName.substring(0, fileName.length()-ZIP_EXTENSION.length());
			lowerCase = lowerCase.substring(0, lowerCase.length()-ZIP_EXTENSION.length());
		}
		return lowerCase.endsWith(XML_EXTENSION) ? fileName : fileName+XML_EXTENSION;
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
		InputStream in = getStream(uri);
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
	}
	
	/** Tests whether a password is the right one for an uri.
	 * @param uri The URI containing Yapbam data
	 * @param password A password (null for no password)
	 * @throws IOException If an I/O error occurred
	 */
	public static boolean isPasswordOk(URI uri, String password) throws IOException {
		InputStream global = getStream(uri);
		try {
			return new Serializer().isPasswordOk(global, password);
		} finally {
			global.close();
		}
	}
	
	private static InputStream getStream(URI uri) throws IOException {
		String scheme = uri.getScheme();
		if ("file".equals(scheme) || "ftp".equals(scheme)) { //$NON-NLS-1$ //$NON-NLS-2$
			return uri.toURL().openStream();
		} else if (CLASSPATH_SCHEME.equals(scheme)) {
			String resourcePath = uri.toString().substring(CLASSPATH_SCHEME.length()+1);
			return YapbamSerializer.class.getResourceAsStream(resourcePath);
		} else {
			throw new IOException("Unsupported protocol: "+scheme); //$NON-NLS-1$
		}
	}
}
