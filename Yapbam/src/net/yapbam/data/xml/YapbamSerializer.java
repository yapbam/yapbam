package net.yapbam.data.xml;

import java.io.*;
import java.net.URI;
import java.security.AccessControlException;
import java.util.zip.ZipOutputStream;

import com.fathzer.soft.ajlib.utilities.FileUtils;

import net.yapbam.data.*;

/** The class implements xml yapbam data serialization and deserialization to (or from) an URL.
 * Currently supported URL type are :<UL>
 * <LI> file.
 * </UL>
 */
public class YapbamSerializer {
	/** Saves the data in a file.
	 * @param data The data to save
	 * @param file The file where to save the data
	 * @param zipped true if the data should be wrapped in a zip container
	 * @param report a progress report
	 * @throws FileNotFoundException is file is not available for writing
	 * @throws IOException if something goes wrong while writing
	 */
	public static void write(GlobalData data, File file, boolean zipped, ProgressReport report) throws IOException {
		if ((file.exists() && !file.canWrite()) || !FileUtils.isWritable(file)) {
			throw new FileNotFoundException("writing to "+file+" is not allowed"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		// Proceed safely, it means not to erase the old version until the new version is written
		// Everything here is pretty ugly.
		//TODO Implement this stuff using the transactional File access in Apache Commons (http://commons.apache.org/transaction/file/index.html)
		File writed = file.exists()?getSafeTmpFile("yapbam", null, file.getParentFile()):file; //$NON-NLS-1$
		OutputStream out = new FileOutputStream(writed);
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
		if (report!=null) {
			report.setMax(-1);
		}
		if (!file.equals(writed)) {
			// Ok, not so safe as I want since we could lost the file between deleting and renaming
			// but I can't find a better way
			if (!file.delete()) {
				writed.delete();
				throw new FileNotFoundException();
			}
			FileUtils.move(writed, file);
		}
	}
	
	private static String getEntryName(String fileName) {
		String lowerCase = fileName.toLowerCase();
		if (lowerCase.endsWith(".zip")) {
			fileName = fileName.substring(0, fileName.length()-".zip".length());
			lowerCase = lowerCase.substring(0, lowerCase.length()-".zip".length());
		}
		return lowerCase.endsWith(".xml") ? fileName : fileName+".xml";
	}
	
	private static File getSafeTmpFile(String prefix, String suffix, File directory) throws IOException {
		File result;
		try {
			result = File.createTempFile(prefix, suffix, directory);
		} catch (IOException e) {
			result = File.createTempFile(prefix, suffix);
		}
		return result;
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
//		long start = System.currentTimeMillis();//TODO
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
//		System.out.println ("Data read in "+(System.currentTimeMillis()-start)+"ms");//TODO
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
