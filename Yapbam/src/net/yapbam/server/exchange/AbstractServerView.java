package net.yapbam.server.exchange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
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
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * @author Jean-Marc
 *
 */
public abstract class AbstractServerView {
	/** The server URL */
	private static final URL SERVER_URL;
	private static final Key YAPBAM_PUBLIC_KEY;
	
	private Proxy proxy;
	private String eMail;
	private String password;
	private SecretKey sessionKey;
	private BigInteger encryptedSessionKey;

	static {
		URL tmp;
		try {
			tmp = new URL("http://localhost:8888/yapbamserver");
//			tmp = new URL("http://yapbamnet.appspot.com/yapbamserver");
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
			this.encryptedSessionKey = new BigInteger(cipher.doFinal(this.sessionKey.getEncoded()));
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
	
	private BigInteger encrypt(String message) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, this.sessionKey);
			return new BigInteger(cipher.doFinal(message.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
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
		ObjectOutputStream out = new ObjectOutputStream(new DeflaterOutputStream(connection.getOutputStream()));
		try {
			out.writeObject(this.encryptedSessionKey);
			out.writeObject(encrypt(this.eMail));
			out.writeObject(encrypt(this.password));
			out.writeObject(encrypt(command));
			for (int i = 0; i < data.length; i++) {
				out.writeObject(data[i]); //TODO How to encrypt that ? Probably need to use a encrypted ObjectOutputStream, then send the byte array ?
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
