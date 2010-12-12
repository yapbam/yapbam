package net.yapbam.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/** Utility to perform some operations on files.
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public class FileUtils {
	private static final String ACCESS_DENIED_MESSAGE = "What's the right message ?";

	private FileUtils() {}
	
	/** Move a file from one path to another.
	 * <br>Unlike java.io.File.renameTo, this method always moves the file (if it doesn't fail).
	 * If the source and the destination paths are not on the same file system, the file is copied to the new file system
	 * and then erase from the old one. 
	 * @param src The src path
	 * @param dest The dest path
	 * @throws IOException If the move fails
	 */
	public static void move(File src, File dest) throws IOException {
		// Check whether the destination directory exists or not.
		// If not, create it.
		File parent = dest.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		// Try to simply rename the file
		boolean result = src.renameTo(dest);
		if (result==false) {
			// renameTo may fail if src and dest files are not on the same file system.
			// We then copy the src file, it's really ugly ... but I don't know how to do that
			// Before, we will ensure we will be able to erase the src file after the copy
			if (src.canWrite()) {
				FileReader in = new FileReader(src);
				FileWriter out = new FileWriter(dest);
				int c;
				while ((c = in.read()) != -1) out.write(c);
				in.close();
				out.close();
				// Now, deletes the tmp file
				if (!src.delete()) {
					// Oh ... we were thinking we had the right to delete the file ... but we can't
					// delete the dest file
					dest.delete();
					//FIXME Test that on Spot PC
					throw new SecurityException(ACCESS_DENIED_MESSAGE);
				}
			} else {
				throw new SecurityException(ACCESS_DENIED_MESSAGE);
			}
		}
	}
}
