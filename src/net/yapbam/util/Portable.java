package net.yapbam.util;

import java.io.File;

import com.fathzer.soft.ajlib.utilities.FileUtils;


/** That class provides utilities that help to implement a portable application (with no installation)
 * in the portable apps format.
 * @see <a href="http://portableapps.com/development/portableapps.com_format">Portable apps format</a>
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */   
public final class Portable {
	//TODO This class is duplicated from Yapbam. We probably should make it a unique common class
	private static final String APPLICATION_NAME = "yapbam"; 
	private static final boolean IS_PORTABLE;
	private static final File DATA_DIRECTORY;

	static {
		File file = getLaunchDirectory();
		IS_PORTABLE = FileUtils.isWritable(file);
		if (IS_PORTABLE) {
			file = new File(file, "Data");
		} else {
			// Try with Windows app data variable
			String path = System.getenv("APPDATA");
			if (path==null) {
				// Try with windows user profile variable
				path = System.getenv("USERPROFILE");
			}
			if (path==null) {
				// Try with the user home directory
				path = System.getProperty("user.home");
			}
			if ((path!=null) && FileUtils.isWritable(new File(path))) {
				// If user data directory or user directory is ok. Use this one
				file = new File (path, "."+APPLICATION_NAME);
			} else {
				// Try with tmp directory
				path = System.getProperty("java.io.tmpdir");
				if ((path!=null) && FileUtils.isWritable(new File(path))) {
					// If tmp directory is ok. Use it
					file = new File (path, "."+APPLICATION_NAME);
				} else {
					// Damned there's really no way to write on this machine !
					file = null;
				}
			}
		}
		DATA_DIRECTORY = file;
	}
	
	private Portable() {
		// Hide constructor
	}
	
	/** Tests whether this application is portable or not.
	 * <br>An application is supposed to be be portable if it is able to write in its launch directory.
	 * If not, it is impossible for the application to store its preferences (for example) in a portable way.
	 * @return a boolean, true if the application is portable
	 */
	public static boolean isPortable() {
		return IS_PORTABLE;
	}
	
	/** Gets the directory where the application was launched.
	 * @return the directory from where the application is executed.
	 */
	public static File getLaunchDirectory() {
		return new File(System.getProperty("user.dir"));
	}
	
	/** Gets the applications's data directory.
	 * <br>Be aware that if the application is not portable (for example, installed in "C:/program files") this
	 * directory is probably not the better place to save preferences. In such a case, the standard solution is to use java.util.Preferences. 
	 * @return a directory where typically the portable application can store its data or null if there's nowhere where the application can write.
	 */
	public static File getDataDirectory() {
		return DATA_DIRECTORY;
	}
	
	/** Gets the applications's help directory.
	 * @return a directory where typically the portable application can store its help pages.
	 */
	public static File getHelpDirectory() {
		File file = getLaunchDirectory();
		file = new File(file,"Other/Help");
		// Once, I've made a mistake and named the directory "help" (with a small h), and, as subversion is a little bit too complex for me
		// (on windows), I've never found a way to fix this mistake in svn. So, Yapbam will stay with this ugly "help" directory.
		if (!file.exists()) {
			file = new File(getLaunchDirectory(),"Other/help");
		}
		return file;
	}

	/** Gets the directory where updates are temporarily stored, before to be applied.
	 * @return a directory
	 */
	public static File getUpdateFileDirectory() {
		return new File(getDataDirectory(),"update");
	}
	
	/** Gets the directory where patches are installed.
	 * @return a directory or null if not such directory is available
	 */
/*	public static File getJarDirectory() {
		if (IS_PORTABLE) return new File(getLaunchDirectory(), "App");
		return DATA_IS_TEMPORARY ? null : new File(getDataDirectory(), "patches");
	}*/
}
