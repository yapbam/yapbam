package net.yapbam.util;

import java.io.File;

/** That class provides utilities that help to implement a portable application (with no installation)
 * in the portable apps format.
 * @see <a href="http://portableapps.com/development/portableapps.com_format">Portable apps format</a>
 */   
public class Portable {
	private Portable() {}
	
	/** Gets the directory where the application was launched.
	 * @return the directory from where the application is executed.
	 */
	public static File getLaunchDirectory() {
		return new File(System.getProperty("user.dir"));
//		String fileName = Portable.class.getProtectionDomain().getCodeSource().getLocation().getFile();
//		File file = new File(URLDecoder.decode(fileName));
//		if (file.isDirectory()) {
//			file = new File("").getAbsoluteFile();
//		} else {
//			file = file.getParentFile();
//		}
//		return file;
	}
	
	/** Gets the applications's data directory.
	 * @return a directory where typically the portable application can store its preference data.
	 */
	public static File getDataDirectory() {
		File file = getLaunchDirectory();
		file = new File(file,"Data");
		return file;
	}
	
	/** Gets the applications's help directory.
	 * @return a directory where typically the portable application can store its help pages.
	 */
	public static File getHelpDirectory() {
		File file = getLaunchDirectory();
		file = new File(file,"Other/Help");
		return file;
	}
}
