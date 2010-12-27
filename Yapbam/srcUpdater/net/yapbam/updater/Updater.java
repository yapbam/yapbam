package net.yapbam.updater;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JOptionPane;

import net.yapbam.util.FileUtils;
import net.yapbam.util.Portable;

public class Updater {
	private static final String ZIP_FILE = "update.zip";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String message = "<html>Hi man, welcome to the install shutdown hook.<br>";
		File installDirectory = Portable.getLaunchDirectory();
		message = message + "The launch directory is "+installDirectory+"</html>";
		JOptionPane.showMessageDialog(null, message);
		// Uncompress the zip file
		try {
			// Creating input stream
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(new File(Portable.getUpdateFileDirectory(),ZIP_FILE))));
			ZipEntry entry;

			// Read each entry from the ZipInputStream until no more entry found
			// indicated by a null return value of the getNextEntry() method.
			//
			while ((entry = zis.getNextEntry()) != null) {
				File target = new File(installDirectory, entry.getName());
				if (entry.isDirectory()) {
					if (target.isFile()) target.delete();
					target.mkdirs();
				} else {
					int size;
					byte[] buffer = new byte[2048];
					if (target.isDirectory()) FileUtils.deleteDirectory(target);
					FileOutputStream fos = new FileOutputStream(target);
					BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);
					while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
						bos.write(buffer, 0, size);
					}
					bos.flush();
					bos.close();
				}
				if (entry.getName().endsWith(".sh")) target.setExecutable(true);
				JOptionPane.showMessageDialog(null, "Unzipped: " + (entry.isDirectory()?"directory ":"file ")+entry.getName()+" to "+target); //TODO
			}
			zis.close();
			JOptionPane.showMessageDialog(null,"Yapbam has been successfully updated");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"<html>An unexpected error has occured.<br>Maybe, your Yapbam copy is corrupted.<br>We strongly recommend you to reinstall Yapbam from the official download site : http://www.yapbam.net</html>");
		}
		FileUtils.deleteDirectory(Portable.getUpdateFileDirectory());
	}
}
