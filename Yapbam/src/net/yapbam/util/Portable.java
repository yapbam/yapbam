package net.yapbam.util;

import java.io.File;

/** That class provides utilities that help to implement a portable application (with no installation) */   
public class Portable {
	private Portable() {}
	
	/** Get the directory where the application was launched.
	 * @return a directory where typically the portable application can store its preference data
	 */
	public static File getLaunchDirectory() {
		File file = new File(Portable.class.getProtectionDomain().getCodeSource().getLocation().getFile());
		if (!file.isDirectory()) file = file.getParentFile();
		else file = new File("").getAbsoluteFile();
		return file;
	}
}
