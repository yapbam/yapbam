package net.yapbam.updater;

import javax.swing.JOptionPane;

import net.yapbam.util.FileUtils;
import net.yapbam.util.Portable;

public class Updater {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String message = "<html>Hi man, welcome to the install shutdown hook.<br>";
		message = message + "The launch directory is "+Portable.getLaunchDirectory()+"</html>";
		JOptionPane.showMessageDialog(null, message);
		//TODO Uncompress the zip file
		FileUtils.deleteDirectory(Portable.getUpdateFileDirectory());
	}
}
