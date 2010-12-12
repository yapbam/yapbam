package net.yapbam.server.exchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.RSAPublicKeySpec;
import java.util.ResourceBundle;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 */
public abstract class AbstractServerView {
	/** The server URL */
	private static final URL SERVER_URL;
	private static final Key YAPBAM_PUBLIC_KEY;
	
	private Proxy proxy;
	private String eMail;
	private String password;
	private SecretKey sessionKey;
	private byte[] encryptedSessionKey;

	static {
		URL tmp;
		try {
//			tmp = new URL("http://localhost:8888/yapbamserver");
			tmp = new URL("http://yapbamnet.appspot.com/yapbamserver");
		} catch (MalformedURLException e) {
			tmp = null;
		}
		SERVER_URL = tmp;
		
		
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("net.yapbam.server.exchange.key"); //$NON-NLS-1$
			BigInteger modulus = new BigInteger(bundle.getString("modulus"),16);
			BigInteger exponent = new BigInteger(bundle.getString("exponent"),16);
			KeyFactory fact = KeyFactory.getInstance("RSA");
		    YAPBAM_PUBLIC_KEY = fact.generatePublic(new RSAPublicKeySpec(modulus, exponent));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public AbstractServerView(Proxy proxy, String eMail, String password) {
		super();
		this.proxy = proxy;
		this.eMail = eMail;
		this.password = password;
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(128);
			this.sessionKey = keyGen.generateKey();
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, YAPBAM_PUBLIC_KEY);
			this.encryptedSessionKey = cipher.doFinal(this.sessionKey.getEncoded());
//			System.out.println ("Encoded key : "+new BigInteger(this.encryptedSessionKey).toString(16)+" length "+this.encryptedSessionKey.length);//TODO
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException(e);
		} catch (BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}
		
	protected Serializable toServer(String command, Serializable[] data) throws IOException {
		//TODO Use a encrypter to encode all the stuff that goes on the Internet
		HttpURLConnection connection = (HttpURLConnection) SERVER_URL.openConnection(proxy);
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);
		connection.setRequestProperty("Content-Type", "application/x-java-serialized-object");  
		OutputStream outputStream = connection.getOutputStream();
		outputStream.write(this.encryptedSessionKey);
		ObjectOutputStream out = new ObjectOutputStream(getEncryptedStream(outputStream));
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
			ObjectInputStream in = new ObjectInputStream(getDecryptedStream(connection.getInputStream()));
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
	
	private OutputStream getEncryptedStream(OutputStream stream) throws IOException {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, this.sessionKey);
			return new CipherOutputStream(new DeflaterOutputStream(stream),cipher);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		}
	}
	
	private InputStream getDecryptedStream(InputStream stream) throws IOException {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, this.sessionKey);
			return new CipherInputStream(new InflaterInputStream(stream),cipher);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		}
	}

}
