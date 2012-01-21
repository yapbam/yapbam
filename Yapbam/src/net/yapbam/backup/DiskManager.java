package net.yapbam.backup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Calendar;

class DiskManager implements AbstractBackupManager {

	@Override
	public void createFolder(URI folder) throws IOException {
		File file = new File(folder);
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
	public void copy(InputStream originalStream, Calendar originalDate, String folderPath, String backupFileName) throws IOException {
		// TODO Auto-generated method stub
		
	}
}
