package net.yapbam.currency;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import net.astesana.ajlib.utilities.FileUtils;

public class FileCache implements CurrencyConverter.Cache {
	private File cacheFile;
	
	public FileCache(File file) {
		cacheFile = file;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
	}

	@Override
	public Writer getWriter() throws IOException {
		return new OutputStreamWriter(FileUtils.getHiddenCompliantStream(cacheFile));
	}

	@Override
	public Reader getReader() throws IOException {
		return new FileReader(cacheFile);
	}
}
