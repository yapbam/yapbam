package net.yapbam.backup;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Calendar;

interface AbstractBackupManager {
	public void createFolder(URL url) throws IOException;
	public OutputStream getOutputStream(URL folder, String fileName) throws IOException;
	public void setTime(URL folder, String fileName, Calendar date) throws IOException;
	public void close(OutputStream out) throws IOException;
	public void close();
}
