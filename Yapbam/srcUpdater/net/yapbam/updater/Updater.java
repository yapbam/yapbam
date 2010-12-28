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
	private static final String ZIP_FILE = "update.zip"; //$NON-NLS-1$

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File installDirectory = Portable.getLaunchDirectory();
		// Uncompress the zip file
		try {
			// Creating input stream
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(new File(Portable.getUpdateFileDirectory(),ZIP_FILE))));
			ZipEntry entry;

			// Read each entry from the ZipInputStream until no more entry found
			// indicated by a null return value of the getNextEntry() method.
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
				if (entry.getName().endsWith(".sh")) target.setExecutable(true); //$NON-NLS-1$
			}
			zis.close();
			JOptionPane.showMessageDialog(null,Messages.getString("Updater.Install.success")); //$NON-NLS-1$
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,Messages.getString("Updater.Install.failure")); //$NON-NLS-1$
		}
		FileUtils.deleteDirectory(Portable.getUpdateFileDirectory());
	}
}
