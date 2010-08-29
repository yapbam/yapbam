package net.yapbam.util;

import java.io.File;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map.Entry;

/** That class provides utilities that help to implement a portable application (with no installation)
 * in the portable apps format.
 * @see http://portableapps.com/development/portableapps.com_format
 */   
public class Portable {
	private Portable() {}
	
	/** Get the directory where the application was launched.
	 * @return a directory where typically the portable application can store its preference data
	 */
	@SuppressWarnings("deprecation")
	public static File getLaunchDirectory() {
		String fileName = Portable.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		File file = new File(URLDecoder.decode(fileName));
		if (file.isDirectory()) {
			file = new File("").getAbsoluteFile();
		} else {
			file = file.getParentFile();
		}
		return file;
	}
	
//	public static File getDataDirectory() {
//		
//	}
}
