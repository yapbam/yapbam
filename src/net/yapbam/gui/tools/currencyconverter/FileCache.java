package net.yapbam.gui.tools.currencyconverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.yapbam.remote.Cache;

public class FileCache implements Cache {
	private File cacheFile;
	
	public FileCache(File file) {
		cacheFile = file;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return new FileOutputStream(getTmpFile());
	}

	private File getTmpFile() {
		return new File(cacheFile.getAbsolutePath()+".tmp"); //$NON-NLS-1$
	}

	@Override
	public InputStream getInputStream(boolean tmp) throws IOException {
		return new FileInputStream(tmp?getTmpFile():cacheFile);
	}

	@Override
	public void commit() throws IOException {
		if (getTmpFile().exists() && cacheFile.exists()) {
			cacheFile.delete();
		}
		if (!getTmpFile().renameTo(cacheFile)) {
			throw new IOException("Unable to commit cache"); //$NON-NLS-1$
		}
	}

	@Override
	public boolean isEmpty() {
		return !getTmpFile().exists() && !cacheFile.exists();
	}

	@Override
	public long getTimeStamp() {
		return cacheFile.exists() ? cacheFile.lastModified() : -1;
	}
}
