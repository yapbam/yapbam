package net.yapbam.gui.tools;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import net.yapbam.currency.CurrencyConverter;
import net.yapbam.currency.CurrencyConverter.Cache;

public class FileCache implements CurrencyConverter.Cache {
	private File cacheFile;
	private boolean isNotCommited;
	
	public FileCache(File file) {
		cacheFile = file;
		this.isNotCommited = false;
	}

	@Override
	public Writer getWriter() throws IOException {
		isNotCommited = true;
		return new FileWriter(getTmpFile());
	}

	private File getTmpFile() {
		return new File(cacheFile.getAbsolutePath()+".tmp");
	}

	@Override
	public Reader getReader() throws IOException {
		return new FileReader(isNotCommited?getTmpFile():cacheFile);
	}

	@Override
	public void commit() {
		if (cacheFile.exists()) cacheFile.delete();
		isNotCommited = !getTmpFile().renameTo(cacheFile);
	}
}
