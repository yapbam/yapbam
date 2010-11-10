package net.yapbam.gui;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;

import javax.swing.JOptionPane;

public class Launcher {
	private final static String REQUIRED_JAVA_VERSION = "1.6";
	private final static String NO_JAR_PROPERTY = "noJar";
	private final static File JAR_FILE = new File("App/program.jar");
	private static String CLASS_NAME = "net.yapbam.gui.MainFrame";
	
	public static void main(String[] args) {
		String current = System.getProperty("java.specification.version");
		if (Float.parseFloat(current)<Float.parseFloat(REQUIRED_JAVA_VERSION)) {
			String message = "Your current java version is "+current+
					"\nYapbam requires Java "+REQUIRED_JAVA_VERSION+" or more.\nPlease have a look at http://java.sun.com";
			error(message);
		} else {
			try {
				Class mainClass;
				if (!Boolean.getBoolean(NO_JAR_PROPERTY)) {
					if (!JAR_FILE.exists()) {
						error("<html>A program file is missing ("+JAR_FILE.getAbsolutePath()+").<br><br>You should install Yapbam again in order to fix it.<br></html>");
					}
					mainClass = new URLClassLoader(new URL[]{JAR_FILE.toURI().toURL()}).loadClass(CLASS_NAME);
				} else {
					mainClass = Class.forName(CLASS_NAME);
				}
				Method method = mainClass.getMethod("main", new Class[]{String[].class});
				method.invoke(null, new Object[]{args});
			} catch (MalformedURLException e) {
				error(e);
			} catch (ClassNotFoundException e) {
				error(e);
			} catch (SecurityException e) {
				error(e);
			} catch (NoSuchMethodException e) {
				error(e);
			} catch (IllegalArgumentException e) {
				error(e);
			} catch (IllegalAccessException e) {
				error(e);
			} catch (InvocationTargetException e) {
				error(e);
			}
		}
	}

	private static void error(Throwable e) {
		String pattern = "<html>A fatal error occurs ({0}).<br>Maybe a program file ("+JAR_FILE.getAbsolutePath()+") is corrupted.<br>" +
		"<br>You should install Yapbam again in order to fix it.<br></html>";
		error(MessageFormat.format(pattern, new Object[]{e.toString()}));
	}

	private static void error(String message) {
		JOptionPane.showMessageDialog(null, message, "Sorry, unable to launch Yapbam", JOptionPane.ERROR_MESSAGE);
		System.exit(-1);
	}
}
