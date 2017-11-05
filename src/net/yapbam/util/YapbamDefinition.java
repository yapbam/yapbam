package net.yapbam.util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import net.yapbam.util.Portable.ApplicationDefinition;

public class YapbamDefinition implements ApplicationDefinition {
	private File appDirectory;

	public YapbamDefinition() {
		try {
			URI location = Portable.class.getProtectionDomain().getCodeSource().getLocation().toURI();
			if ("file".equals(location.getScheme())) { //$NON-NLS-1$
				appDirectory = new File(location);
				if (appDirectory.isFile()) {
					appDirectory = appDirectory.getParentFile();
					if (appDirectory.getParentFile() != null) {
						appDirectory = appDirectory.getParentFile();
					}
				} else {
					appDirectory = new File(System.getProperty("user.dir")); //$NON-NLS-1$
				}
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
		return "yapbam";  //$NON-NLS-1$
	}
}
