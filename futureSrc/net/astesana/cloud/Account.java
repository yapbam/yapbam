package net.astesana.cloud;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;

import net.astesana.cloud.exceptions.UnreachableHostException;
import net.yapbam.gui.persistence.Cancellable;

import org.apache.commons.codec.CharEncoding;

/** An account in the Cloud, cached in a local folder.
 * @see Service
 */
public abstract class Account<T> {
	private static final String INFO_FILENAME = ".info";
	private File root;
	Service<? extends Account<T>> service;
	private String displayName;
	protected T connectionData;
	
	protected Account(Service<? extends Account<T>> service, File file) throws IOException {
		if (!file.isDirectory()) throw new IllegalArgumentException();
		this.root = file;
		FileInputStream stream = new FileInputStream(new File(this.root, INFO_FILENAME));
		try {
			BufferedReader b = new BufferedReader(new InputStreamReader(stream));
			String line = b.readLine(); //FIXME This reads all the file. deserializeConnectionData has no more characters in the stream
			this.displayName = URLDecoder.decode(line, CharEncoding.UTF_8);
			deserializeConnectionData(stream);
		} finally {
			stream.close();
		}
		this.service = service;
	}
	
	protected Account(Service<? extends Account<T>> service, String id, String displayName, T connectionData) throws IOException {
		this.service = service;
		this.displayName = displayName;
		this.connectionData = connectionData;
		try {
			this.root = new File(service.getCacheRoot(), URLEncoder.encode(id, CharEncoding.UTF_8));
			if (this.root.isFile()) this.root.delete();
			this.root.mkdirs();
			if (!this.root.isDirectory()) throw new IOException();
			File connectionDataFile = new File(this.root, INFO_FILENAME);
			FileOutputStream stream = new FileOutputStream(connectionDataFile);
			try {
				BufferedWriter b = new BufferedWriter(new OutputStreamWriter(stream));
				b.write(URLEncoder.encode(displayName, CharEncoding.UTF_8)); b.newLine(); b.flush();
				serializeConnectionData(stream);
			} finally {
				stream.close();
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public Service<? extends Account<T>> getService() {
		return this.service;
	}
	
	public T getConnectionData() {
		return this.connectionData;
	}

	protected abstract void serializeConnectionData(OutputStream stream) throws IOException;

	protected abstract void deserializeConnectionData(InputStream stream) throws IOException;

	public abstract Collection<Entry> getRemoteFiles(Cancellable task) throws UnreachableHostException;

	/** Gets the account quota in bytes.
	 * <br>By default, this method return -1.
	 * <br>Please note that this method should return quickly. This means, it should not connect with the server
	 * in order to have the information. We recommend to return a negative number until the remote data is initialized
	 * by getRemoteFiles.
	 * @return The quota in bytes or a negative number if the service is not able to give this information
	 */
	public long getQuota() {
		return -1;
	}
	
	/** Gets the size used in bytes.
	 * <br>By default, this method return -1.
	 * <br>Please note that this method should return quickly. This means, it should not connect with the server
	 * in order to have the information. We recommend to return a negative number until the remote data is initialized
	 * by getRemoteFiles.
	 * @return The used size in bytes or a negative number if the service is not able to give this information
	 */
	public long getUsed() {
		return -1;
	}
	

	public void delete() {
		delete (this.root);
	}
	
	private static void delete(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File child : files) {
				delete(child);
			}
		}
		file.delete();
	}
	
	public Collection<Entry> getLocalFiles() {
		Collection<Entry> result = new ArrayList<Entry>();
		for (File file : this.root.listFiles()) {
			if (file.isDirectory()) {
				result.add(new Entry(file.getName()));
			}
		}
		return result;
	}
}
