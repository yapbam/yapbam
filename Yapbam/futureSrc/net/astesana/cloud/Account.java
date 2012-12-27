package net.astesana.cloud;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
public class Account {
	private static final String INFO_FILENAME = ".info";
	private File root;
	Service service;
	private String displayName;
	private String id;
	protected Serializable connectionData;
	protected long quota;
	protected long used;
	
	protected Account(Service service, File file) throws IOException {
		if (!file.isDirectory()) throw new IllegalArgumentException();
		this.root = file;
		try {
			this.id = URLDecoder.decode(file.getName(), CharEncoding.UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedEncodingException();
		}
		ObjectInputStream stream = new ObjectInputStream(new FileInputStream(new File(this.root, INFO_FILENAME)));
		try {
			this.displayName = (String) stream.readObject();
			this.connectionData = (Serializable) stream.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		} finally {
			stream.close();
		}
		this.service = service;
		this.quota = -1;
		this.used = -1;
	}
	
	public Account(Service service, String id, String displayName, Serializable connectionData, long quota, long used) throws IOException {
		this.service = service;
		this.id = id;
		this.displayName = displayName;
		this.connectionData = connectionData;
		this.quota = quota;
		this.used = used;
		try {
			this.root = new File(service.getCacheRoot(), URLEncoder.encode(id, CharEncoding.UTF_8));
			if (this.root.isFile()) this.root.delete();
			this.root.mkdirs();
			if (!this.root.isDirectory()) throw new IOException();
			File connectionDataFile = new File(this.root, INFO_FILENAME);
			ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(connectionDataFile));
			try {
				stream.writeObject(this.displayName);
				stream.writeObject(this.connectionData);
			} finally {
				stream.close();
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/** Gets this account's display name.
	 * @return a String
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/** Gets the unique account id.
	 * @return a String
	 */
	public String getId() {
		return this.id;
	}

	/** Gets the service that hosted this account. 
	 * @return A service
	 */
	public Service getService() {
		return this.service;
	}
	
	public Serializable getConnectionData() {
		return this.connectionData;
	}

//	public abstract Collection<Entry> getRemoteFiles(Cancellable task) throws UnreachableHostException;

	/** Gets the account quota in bytes.
	 * <br>Please note that this method should return quickly. This means, it should not connect with the server
	 * in order to have the information. This method should return a negative number until the remote data is initialized
	 * by getRemoteFiles.
	 * @return The quota in bytes or a negative number if the service is not able to give this information
	 */
	public long getQuota() {
		return quota;
	}

	/** Gets the size used in bytes.
	 * <br>Please note that this method should return quickly. This means, it should not connect with the server
	 * in order to have the information. This method should return a negative number until the remote data is initialized
	 * by getRemoteFiles.
	 * @return The used size in bytes or a negative number if the service is not able to give this information
	 */
	public long getUsed() {
		return used;
	}
	
	/** Deletes the local data about this account.
	 */
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
				result.add(new Entry(this, file.getName()));
			}
		}
		return result;
	}
	
	public Collection<Entry> getRemoteFiles(Cancellable task) throws UnreachableHostException {
		return this.service.getRemoteFiles(this, task);
	}

}
