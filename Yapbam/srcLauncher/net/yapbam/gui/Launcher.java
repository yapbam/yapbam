package net.yapbam.gui;

import javax.swing.JOptionPane;

import net.yapbam.gui.MainFrame;

public class Launcher {
	private final static String REQUIRED_JAVA_VERSION = "1.6";
	
	public static void main(String[] args) {
		String current = System.getProperty("java.specification.version");
		if (Float.parseFloat(current)<Float.parseFloat(REQUIRED_JAVA_VERSION)) {
			JOptionPane.showMessageDialog(null, "Your current java version is "+current+
					"\nYapbam requires Java "+REQUIRED_JAVA_VERSION+" or more.\nPlease have a look at http://java.sun.com",
					"Sorry, unable to launch Yapbam", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		} else {
			MainFrame.main(args);
		}
	}
}
