package net.yapbam.gui;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

import net.yapbam.data.FilteredData;

public class PlugInContainer {
	private Class<? extends AbstractPlugIn> plugin;
	private boolean isActivated;
	private Throwable e;
	
	PlugInContainer (File file) {
		try {
			JarFile jar = new JarFile(file);
			try {
				Attributes attributes = jar.getManifest().getMainAttributes();
				String className = attributes.getValue("Plugin-Class"); //$NON-NLS-1$
				if (className!=null) {
					URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()});
					try {
						this.plugin = (Class<AbstractPlugIn>) classLoader.loadClass(className);
					} finally {
						//JDK6 has no close method
						// classLoader.close();
						try {
							classLoader.getClass().getMethod("close").invoke(classLoader);
						} catch (Exception e) {
							// Method does not exist we're in JDK6.
						}
					}
				}
				this.isActivated = true;
			} finally {
				jar.close();
			}
		} catch (Exception e) {
			this.e = e;
		}
	}

	PlugInContainer(Class<? extends AbstractPlugIn> plugin) {
		this.plugin = plugin;
		this.isActivated = true;
	}

	public AbstractPlugIn build(FilteredData filteredData, Object restartData) {
		if (this.plugin==null) {
			return null;
		}
		try {
			Constructor<? extends AbstractPlugIn> constructor = this.plugin.getConstructor(FilteredData.class, Object.class);
			return (AbstractPlugIn) constructor.newInstance(filteredData, restartData);
		} catch (Throwable e) {
			this.e = e;
			this.isActivated = false;
			return null;
		}
	}

	public boolean isActivated() {
		return this.isActivated;
	}

	@Override
	public String toString() {
		return this.plugin.getCanonicalName();
	}

	/** Gets the exception that occurred during the plugin instanciation.
	 * @return a throwable or null if no error occurred.
	 */
	public Throwable getInstanciationException() {
		return e;
	}
}
