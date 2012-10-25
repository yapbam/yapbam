package net.yapbam.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.AccessControlException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/** Utilities to encrypt data.
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public final class Crypto {
	// An interesting article to implement file encryption : http://java.sun.com/j2se/1.4.2/docs/guide/security/jce/JCERefGuide.html
	private Crypto(){}
	
	/** Decrypt a text previous crypted by the encrypt method.
	 * @param key The secret key used for the encryption
	 * @param message The encrypted message we want to decrypt
	 * @return the decrypted message
	 * @see #encrypt(String, String)
	 */
	public static String decrypt(String key, String message) {
		SecretKeySpec skeySpec;
		Cipher cipher;
		skeySpec = new SecretKeySpec(CheckSum.toBytes(key), "AES");
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			String original = new String(cipher.doFinal(CheckSum.toBytes(message)));
			return original;
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}

	/** Encrypt a text.
	 * <BR>It uses a symmetric AES algorithm.
	 * @param key The secret key used for the encryption (oups secret key has to have some properties (128 bits long ?) ... but I don't remember)
	 * @param message The message we want to encrypt
	 * @return the encrypted message
	 * @see #decrypt(String, String)
	 */
	public static String encrypt(String key, String message) {
		SecretKey skeySpec = new SecretKeySpec(CheckSum.toBytes(key), "AES");
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			String encrypted = CheckSum.toString(cipher.doFinal(message.getBytes()));
			return encrypted;
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static final byte[] SALT = new byte[]{ (byte)0xc7, (byte)0x23, (byte)0xa5, (byte)0xfc, (byte)0x7e, (byte)0x38, (byte)0xee, (byte)0x09};
	private static final PBEParameterSpec pbeParamSpec = new PBEParameterSpec(SALT, 16);

	/** Encrypt an output stream.
	 * <br>The returned stream is deflated and encrypted with the password accordingly to the PBEWithMD5AndDES algorithm.
	 * @param password The password used to encrypt the stream.
	 * @param stream The stream to encrypt.
	 * @return A new stream that encrypt data written to it.
	 * @see #getPasswordProtectedInputStream(String, InputStream)
	 * @throws IOException
	 */
	public static OutputStream getPasswordProtectedOutputStream (String password, OutputStream stream) throws IOException {
		stream.write(getDigest(password));
		try {
			SecretKey pbeKey = new BinaryPBEKey(password.getBytes("UTF-8"));
			Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
			cipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
			CipherOutputStream cstream = new CipherOutputStream(stream, cipher);
			stream = new DeflaterOutputStream(cstream) {
				/* (non-Javadoc)
				 * @see java.util.zip.DeflaterOutputStream#finish()
				 */
				@Override
				public void finish() throws IOException {
					super.finish();
					((CipherOutputStream)out).finish();
				}
			};
			return stream;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeySpecException e) {
			throw new RuntimeException(e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e);
		}
	}

	/** Decrypt an input stream.
	 * <br>The data in the stream may have been encoded using a stream return by getPasswordProtectedOutputStream.
	 * @param password The password used to encrypt the data.
	 * @param stream The stream on the encrypted data
	 * @return a new InputStream, data read from this stream is decrypted.
	 * @see #getPasswordProtectedOutputStream(String, OutputStream)
	 * @throws IOException
	 * @throws AccessControlException
	 */
	public static InputStream getPasswordProtectedInputStream (String password, InputStream stream) throws IOException, AccessControlException {
		verifyPassword(stream, password);
		try {
			SecretKey pbeKey = new BinaryPBEKey(password.getBytes("UTF-8"));
			Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
			cipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);
			stream = new CipherInputStream(stream, cipher);
			stream = new InflaterInputStream(stream);
			return stream;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeySpecException e) {
			throw new RuntimeException(e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static void verifyPassword(InputStream stream, String password) throws IOException, AccessControlException {
		byte[] digest = getDigest(password);
		byte[] fileDigest = new byte[digest.length];
		for (int missing=fileDigest.length; missing>0; ) {
			missing -= stream.read(fileDigest, fileDigest.length-missing, missing);
		}
		if (!MessageDigest.isEqual(digest, fileDigest)) throw new AccessControlException("invalid password");
	}

	/** Gets the SHA digest of a password.
	 * @param password The password
	 * @return The password digest.
	 */
	public static byte[] getDigest(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA");
			digest.update(SALT);
			return digest.digest(password.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
