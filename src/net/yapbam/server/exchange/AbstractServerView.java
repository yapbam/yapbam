package net.yapbam.server.exchange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public abstract class AbstractServerView {
	/** The server URL */
	protected static final URL SERVER_URL;
	
	private Proxy proxy;
	private String eMail;
	private String password;

	static {
		URL tmp;
		try {
			tmp = new URL("http://localhost:8888/yapbamserver");
//			tmp = new URL("http://yapbamnet.appspot.com/yapbamserver");
		} catch (MalformedURLException e) {
			tmp = null;
		}
		SERVER_URL = tmp;
	}
	
	public AbstractServerView(Proxy proxy, String eMail, String password) {
		super();
		this.proxy = proxy;
		this.eMail = eMail;
		this.password = password;
	}

	protected Serializable toServer(String command, Serializable[] data) throws IOException {
		//TODO Use a encrypter to encode all the stuff that goes on the Internet
		HttpURLConnection connection = (HttpURLConnection) SERVER_URL.openConnection(proxy);
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);
		connection.setRequestProperty("Content-Type", "application/x-java-serialized-object");  
		ObjectOutputStream out = new ObjectOutputStream(new DeflaterOutputStream(connection.getOutputStream()));
		try {
			out.writeObject(this.eMail);
			out.writeObject(this.password);
			out.writeObject(command);
			for (int i = 0; i < data.length; i++) {
				out.writeObject(data[i]);
			}
			out.flush();
		} finally {
			out.close();
		}
		int errorCode = connection.getResponseCode();
		if (errorCode==HttpURLConnection.HTTP_OK) {
			ObjectInputStream in = new ObjectInputStream(new InflaterInputStream(connection.getInputStream()));
			try {
				Integer errCode = (Integer) in.readObject();
				codeToException (errCode);
				Serializable result = (Serializable) in.readObject();
				return result;
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new IOException("http error "+errorCode);
		}
	}
	
	protected abstract void codeToException(int errCode);
}
