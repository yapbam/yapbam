package net.yapbam.util;

import java.io.IOException;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/** That class provides utilities dealing with java.util.prefs.
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */   
public final class PreferencesUtils {
	private PreferencesUtils() {}

	/** Saves properties to a Preferences node.
	 * @param pref The preferences node in which the properties will be saved.
	 * @param properties The properties to save. Be aware that these properties can only contains strings
	 * @throws IOException If it fails writing the preferences.
	 */
	public static void toPreferences(Preferences pref, Properties properties) throws IOException {
		for (Object obj : properties.keySet()) {
			String key = (String)obj;
			String value = properties.getProperty(key);
			pref.put(key, value);
		}
		try {
			pref.flush();
		} catch (BackingStoreException e) {
			throw new IOException(e);
		}
	}

	/** Reads properties in a Preferences node.
	 * @param pref The preferences node where to read the properties.
	 * @param properties The properties where the properties will be put.
	 * @throws IOException If it fails reading the properties.
	 */
	public static void fromPreferences(Preferences pref, Properties properties) throws IOException {
		try {
			for (String key : pref.keys()) {
				String value = pref.get(key, null);
				properties.put(key, value);
			}
		} catch (BackingStoreException e) {
			throw new IOException(e);
		}
	}
	
	/** Tests whether a Preferences node is empty (contains no key).
	 * @param pref The preferences node to test.
	 * @throws IOException If it fails reading the properties.
	 */
	public static boolean isEmpty(Preferences pref) throws IOException {
		try {
			return pref.keys().length==0;
		} catch (BackingStoreException e) {
			throw new IOException(e);
		}
	}
}