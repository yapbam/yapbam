package net.yapbam.backup;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Calendar;

interface AbstractBackupManager {
	public void createFolder(URI folder) throws IOException;
	public void copy(InputStream originalStream, Calendar originalDate, String folderPath, String backupFileName) throws IOException;
	public void close();
}
