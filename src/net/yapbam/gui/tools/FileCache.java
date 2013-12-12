package net.yapbam.gui.tools;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import net.yapbam.currency.CurrencyConverter;

public class FileCache implements CurrencyConverter.Cache {
	private File cacheFile;
	
	public FileCache(File file) {
		cacheFile = file;
	}

	@Override
	public Writer getWriter() throws IOException {
		return new FileWriter(getTmpFile());
	}

	private File getTmpFile() {
		return new File(cacheFile.getAbsolutePath()+".tmp");
	}

	@Override
	public Reader getReader(boolean tmp) throws IOException {
		return new FileReader(tmp?getTmpFile():cacheFile);
	}

	@Override
	public void commit() {
		if (getTmpFile().exists() && cacheFile.exists()) {
			cacheFile.delete();
		}
		getTmpFile().renameTo(cacheFile);
	}
}
