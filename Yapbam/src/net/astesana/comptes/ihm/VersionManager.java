package net.astesana.comptes.ihm;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class VersionManager {
	private static Properties properties;
	
	static {
		InputStream inStream = ClassLoader.getSystemResourceAsStream("version.txt");
		properties = new Properties();
		try {
			properties.load(inStream);
		} catch (IOException e) {
		} finally {
			try {
				inStream.close();
			} catch (IOException e) {}
		}
	}
	
	public static String getVersion() {
		return properties.getProperty("version", "Inconnue"); //LOCAL
	}

}
