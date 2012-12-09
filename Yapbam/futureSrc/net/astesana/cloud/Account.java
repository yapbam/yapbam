package net.astesana.cloud;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.CharEncoding;

/** An account in the Cloud, cached in a local folder.
 * @see Service
 */
public abstract class Account<T> {
	private File root;
	protected T connectionData;
	
	protected Account(File file) throws IOException {
		if (!file.isDirectory()) throw new IllegalArgumentException();
		this.root = file;
		FileInputStream stream = new FileInputStream(new File(this.root, ".info"));
		try {
			deserializeConnectionData(stream);
		} finally {
			stream.close();
		}
	}
	
	protected Account(Service<? extends Account<T>> service, String name, T connectionData) throws IOException {
		this.connectionData = connectionData;
		try {
			this.root = new File(service.getCacheRoot(), URLEncoder.encode(name, CharEncoding.UTF_8));
			if (this.root.isFile()) this.root.delete();
			this.root.mkdirs();
			if (this.root.isDirectory()) throw new IOException();
			File connectionDataFile = new File(this.root, ".info");
			FileOutputStream stream = new FileOutputStream(connectionDataFile);
			try {
				serializeConnectionData(stream);
			} finally {
				stream.close();
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getName() {
		try {
			return URLDecoder.decode(root.getName(), CharEncoding.UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public T getConnectionData() {
		return this.connectionData;
	}

	protected abstract void serializeConnectionData(OutputStream stream) throws IOException;

	protected abstract void deserializeConnectionData(InputStream stream) throws IOException;
}
