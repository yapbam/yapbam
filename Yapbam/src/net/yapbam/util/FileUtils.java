package net.yapbam.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import sun.awt.shell.ShellFolder;

/** Utility to perform some operations on files.
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public class FileUtils {
	private static final String ACCESS_DENIED_MESSAGE = "What's the right message ?";

	private FileUtils() {}
	
	/** Gets the canonical file of a file even on windows where links are ignored by File.getCanonicalPath().
	 * <br>Even if the link is broken, the method returns the linked file. You should use File.exists() to test if the returned file is available.
	 * <br>If the link is a link to a link to a file, this method returns the final file. 
	 * @param file the file to test
	 * @return a File
	 */
	@SuppressWarnings("unchecked")
	public static File getCanonical(File file) throws IOException {
		try {
			// The following lines are equivalent to sf = new sun.awt.shell.Win32ShellFolderManager2().createShellFolder(file);
			// We use reflection in order the code to compile on non Windows platform (where new sun.awt.shell.Win32ShellFolderManager2
			// is a unknown class.
			ShellFolder sf;
			@SuppressWarnings("rawtypes")
			Class cl = Class.forName("sun.awt.shell.Win32ShellFolderManager2");
			Object windowsFolderManager = cl.newInstance();
			sf = (ShellFolder) cl.getMethod("createShellFolder", File.class).invoke(windowsFolderManager, file);
			if (sf.isLink()) return sf.getLinkLocation();
		} catch (ClassNotFoundException e) {
			// We're not on a windows platform
			// We also ignore other errors that may not happen.
			// Ok, errors always happens ... In such a case, we can do we have already done our best effort and
			// we will let file.CanonicalFile do better.
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		} catch (NoSuchMethodException e) {
		} catch (InstantiationException e) {
		}
		return file.getCanonicalFile();
	}
	
	/** Move a file from one path to another.
	 * <br>Unlike java.io.File.renameTo, this method always moves the file (if it doesn't fail).
	 * If the source and the destination paths are not on the same file system, the file is copied to the new file system
	 * and then erased from the old one. 
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
				// Now, deletes the src file
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
	
	/** Deletes recursively a directory.
	 * <br>This means that the directory and all of its files or subfolders are deleted.
	 * @param path the directory to be deleted.
	 * @return true if the directory has been successfully deleted.
	 */
	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return path.delete();
	}

	/** Tests whether a file is contained in a directory.
	 * <BR>The directory and the file may not exists, the search is done on the absolute paths.
	 * @param file The file to be tested
	 * @param directory The tested directory
	 * @return true if the file is contained in the directory.
	 */
	public static boolean isIncluded(File file, File directory) {
		for (File parent = file.getParentFile(); parent!=null; parent = parent.getParentFile()) {
			if (parent.equals(directory)) return true;
		}
		return false;
	}
	
	/** Gets a FileOutputStream even on a windows hidden file.
	 * <br>Under windows, it is impossible to write directly in a hidden file with Java.
	 * You have to make the file visible first. That's what this method try to do.
	 * <br>When the file was initially hidden, the close method of the returned stream hide it again.
	 * @param file The file to be opened for writing
	 * @return a new stream
	 * @throws IOException
	 */
	public static FileOutputStream getHiddenCompliantStream(File file) throws IOException {
		if (file.isHidden() && System.getProperty("os.name", "?").startsWith("Windows")) {
			try {
				Process process = Runtime.getRuntime().exec("attrib -H \""+file.getAbsolutePath()+"\"");
				try {
					int result = process.waitFor();
					if (result==0) return new HiddenFileOutputStream(file);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			} catch (IOException e) {
				// This try catch block is empty because this exception, in this context, means that the attrib command is not available.
				// In such a case, we just have to do ... nothing: If the OutputStream creation fails, an IOException will be thrown
			}
		}
		// If the file was not hidden, or if making the file visible failed, try to open a classic stream.
		return new FileOutputStream(file);
	}
	
	/** The FileOutputStream returned by getHiddenCompliantStream method when it is called on a Windows hidden file.
	 * @see FileUtils#getHiddenCompliantStream(File)
	 */
	private static class HiddenFileOutputStream extends FileOutputStream {
		private File file;

		public HiddenFileOutputStream(File file) throws FileNotFoundException {
			super(file);
			this.file = file;
		}
		
		public void close() throws IOException {
			super.close();
			Process process = Runtime.getRuntime().exec("attrib +H \""+file.getAbsolutePath()+"\"");
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
