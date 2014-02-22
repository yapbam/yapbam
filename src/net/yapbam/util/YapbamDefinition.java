package net.yapbam.util;

import java.io.File;
import java.net.URISyntaxException;

import net.yapbam.util.Portable.ApplicationDefinition;

public class YapbamDefinition implements ApplicationDefinition {
	private File appDirectory;

	public YapbamDefinition() {
		try {
			appDirectory = new File(Portable.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			if (appDirectory.isFile()) {
				appDirectory = appDirectory.getParentFile();
				if (appDirectory.getParentFile() != null) {
					appDirectory = appDirectory.getParentFile();
				}
			} else {
				appDirectory = new File(System.getProperty("user.dir"));
			}
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public File getAppDirectory() {
		return this.appDirectory;
	}

	@Override
	public String getApplicationName() {
		return "yapbam"; 
	}
}
