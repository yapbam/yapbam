package net.yapbam.updater;

import javax.swing.JOptionPane;

import net.yapbam.util.FileUtils;
import net.yapbam.util.Portable;

public class Updater {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JOptionPane.showMessageDialog(null, "Hi man, welcome to the install shutdown hook");
		//TODO Uncompress the zip file
		FileUtils.deleteDirectory(Portable.getUpdateFileDirectory());
	}
}
