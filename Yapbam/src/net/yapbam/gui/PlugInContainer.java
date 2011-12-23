package net.yapbam.gui;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

import net.yapbam.data.FilteredData;

public class PlugInContainer {
//	private File location;
	private Class<? extends AbstractPlugIn> plugin;
	private boolean isActivated;
	private Throwable e;
	
	PlugInContainer (File file) {
//		this.location = file;
		try {
			JarFile jar = new JarFile(file);
			Attributes attributes = jar.getManifest().getMainAttributes();
			String className = attributes.getValue("Plugin-Class");
			if (className!=null) {
				this.plugin = (Class<AbstractPlugIn>) new URLClassLoader(new URL[]{file.toURI().toURL()}).loadClass(className);
			}
			this.isActivated = true;
		} catch (Exception e) {
			this.e = e;
		}
	}

	PlugInContainer(Class<? extends AbstractPlugIn> plugin) {
//		this.location = null;
		this.plugin = plugin;
		this.isActivated = true;
	}

	public AbstractPlugIn build(FilteredData filteredData, Object restartData) {
		if (this.plugin==null) return null;
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

	/** Gets the exception that occured during the plugin instanciation.
	 * @return a throwable or null if no error occured.
	 */
	public Throwable getInstanciationException() {
		return e;
	}
}
