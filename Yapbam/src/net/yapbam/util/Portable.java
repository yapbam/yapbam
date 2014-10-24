package net.yapbam.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.fathzer.soft.ajlib.utilities.FileUtils;
import com.fathzer.soft.ajlib.utilities.NullUtils;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** That class provides utilities that help to implement a portable application (with no installation)
 * in the portable apps format.
 * @see <a href="http://portableapps.com/development/portableapps.com_format">Portable apps format</a>
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */   
public final class Portable {
	private static ApplicationDefinition appDefinition = null;
	private static boolean IS_PORTABLE;
	private static File APP_DIRECTORY;
	private static File DATA_DIRECTORY;
	private static boolean IS_JNLP;
	private static boolean inited;
	
	public interface ApplicationDefinition {
		public File getAppDirectory();
		public String getApplicationName();
	}
	
	static {
		InputStream in = Portable.class.getClassLoader().getResourceAsStream("applicationDefinition.properties");
		if (in!=null) {
			Logger logger = LoggerFactory.getLogger(Portable.class);
			logger.trace("Application definition is specified");
			try {
				initDefFromPropertyStream(in);
			} catch (IOException e) {
				logger.warn("Unable to read property resource file", e);
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					logger.warn("Unable to close property resource file", e);
				}
			}
		}
		inited = false;
	}

	private static void initDefFromPropertyStream(InputStream in) throws IOException {
		Logger logger = LoggerFactory.getLogger(Portable.class);
		Properties properties = new Properties();
		properties.load(in);
		String className = properties.getProperty("class");
		if (className==null) {
			logger.warn("No class specified in property resource file");
		} else {
			try {
				Portable.appDefinition = (ApplicationDefinition) Class.forName(className).newInstance();
			} catch (InstantiationException e) {
				logger.warn("Unable to create application description ("+className+")", e);
			} catch (IllegalAccessException e) {
				logger.warn("Error while creating application description ("+className+")", e);
			} catch (ClassNotFoundException e) {
				logger.warn("Unable to find application description class ("+className+")", e);
			}
		}
	}

	/** Sets the application definition.
	 * <br>The application definition must be set before methods of this class to be called.
	 * <br>Another way to set the application definition is to define a property file named <b>applicationDefinition</b>
	 * in the default package. This file must contains a property named <b>class</b> that contains the name of a class that implements
	 * the {@link ApplicationDefinition}. This class must have a public no argument constructor.
	 * @param definition The application definition.
	 */
	public static void setDefinition(ApplicationDefinition definition) {
		if (!NullUtils.areEquals(definition, Portable.appDefinition)) {
			APP_DIRECTORY = null;
			Portable.appDefinition = definition;
		}
	}
	
	private static void init() {
		if (inited) {
			return;
		}
		if (Portable.appDefinition==null) {
			throw new IllegalStateException();
		}
		APP_DIRECTORY = Boolean.getBoolean("simulateJNLP") ? null : Portable.appDefinition.getAppDirectory();
		IS_JNLP = APP_DIRECTORY==null;
		IS_PORTABLE = (APP_DIRECTORY!=null) && FileUtils.isWritable(APP_DIRECTORY);
		File file = APP_DIRECTORY;
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
				file = new File (path, "."+Portable.appDefinition.getApplicationName());
			} else {
				// Try with tmp directory
				path = System.getProperty("java.io.tmpdir");
				if ((path!=null) && FileUtils.isWritable(new File(path))) {
					// If tmp directory is ok. Use it
					file = new File (path, "."+Portable.appDefinition.getApplicationName());
				} else {
					// Damned there's really no way to write on this machine !
					file = null;
				}
			}
		}
		DATA_DIRECTORY = file;
		inited = true;
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
		init();
		return IS_PORTABLE;
	}
	
	/** Tests whether this application is launched by Java Web Start.
	 * <br>A Java Web started application is not portable because the application can't write to its launch directory.
	 * @return a boolean, true if the application is launched through Java Web Start
	 */
	public static boolean isWebStarted() {
		init();
		return IS_JNLP;
	}
	
	/** Gets the application's installation directory.
	 * @return a File.
	 */
	public static File getApplicationDirectory() {
		init();
		return APP_DIRECTORY;
	}
	
	/** Gets the applications's data directory.
	 * <br>Be aware that if the application is not portable (for example, installed in "C:/program files") this
	 * directory is probably not the better place to save preferences. In such a case, the standard solution is to use java.util.Preferences. 
	 * @return a directory where typically the portable application can store its data or null if there's nowhere where the application can write.
	 */
	public static File getDataDirectory() {
		init();
		return DATA_DIRECTORY;
	}
	
	/** Gets the applications's help directory.
	 * @return a directory where typically the portable application can store its help pages.
	 */
	public static File getHelpDirectory() {
		init();
		File file = getApplicationDirectory();
		file = new File(file,"Other/Help");
		// Once, I've made a mistake and named the directory "help" (with a small h), and, as subversion is a little bit too complex for me
		// (on windows), I've never found a way to fix this mistake in svn. So, Yapbam will stay with this ugly "help" directory.
		if (!file.exists()) {
			file = new File(getApplicationDirectory(),"Other/help");
		}
		return file;
	}

	/** Gets the directory where updates are temporarily stored, before to be applied.
	 * @return a directory
	 */
	public static File getUpdateFileDirectory() {
		init();
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
