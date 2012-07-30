package net.yapbam.backup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Calendar;

class DiskManager implements AbstractBackupManager {

	@Override
	public void createFolder(URL folder) throws IOException {
		File file = new File(folder.getPath());
		if (file.exists()) {
			if (!file.isDirectory()) throw new IOException(folder + " is not a directory");
		} else {
			if (!file.mkdirs()) throw new IOException("Unable to create folder "+folder);
		}
	}

	@Override
	public void close() {
		// Nothing to do
	}

	@Override
	public OutputStream getOutputStream(URL folder, String fileName) throws IOException {
		return new FileOutputStream(new File(folder.getPath(), fileName));
	}

	@Override
	public void setTime(URL folder, String fileName, Calendar date) throws IOException {
		new File(folder.getPath(), fileName).setLastModified(date.getTimeInMillis());
	}

	@Override
	public void close(OutputStream out) throws IOException {
		out.close();
	}
}
