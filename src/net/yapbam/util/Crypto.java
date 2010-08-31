package net.yapbam.util;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/** This class provides utilities to encrypt data.
 * <BR>It uses a symetric AES algorithm. */
public class Crypto {
	// An interesting article to implement file encryption : http://java.sun.com/j2se/1.4.2/docs/guide/security/jce/JCERefGuide.html
	private Crypto(){}
	
	/** Decrypt a text
	 * @param key The secret key used for the encryption
	 * @param message The encrypted message we want to decrypt
	 * @return the decrypted message
	 */
	public static String decrypt(String key, String message) {
		SecretKeySpec skeySpec;
		Cipher cipher;
		skeySpec = new SecretKeySpec(asByteArray(key), "AES");
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			String original = new String(cipher.doFinal(asByteArray(message)));
			return original;
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}

	/** Encrypt a text
	 * @param key The secret key used for the encryption (oups secret key has to have some properties (128 bits long ?) ... but I don't remember)
	 * @param message The message we want to encrypt
	 * @return the encrypted message
	 */
	public static String encrypt(String key, String message) {
		SecretKey skeySpec = new SecretKeySpec(asByteArray(key), "AES");
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			String encrypted = asHex(cipher.doFinal(message.getBytes()));
			return encrypted;
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Turns array of bytes into string
	 * @param buf Array of bytes to convert to hex string
	 * @return Generated hex string
	 */
	private static String asHex(byte[] buf) {
		return new BigInteger(buf).toString(16);
	}
	
	private static byte[] asByteArray (String str) {
		return new BigInteger(str, 16).toByteArray();
	}
/*
	private static String generateKey() throws NoSuchAlgorithmException {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128); // 192 and 256 bits may not be available

		// Generate the secret key specs.
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		String key = asHex(raw);
		return key;
	}

	public static void main(String[] args) throws Exception {
		String message = "Source forge is great";
		String encrypt = encrypt(key, message);
		System.out.println("encrypted string: " + encrypt);
		String original = decrypt(key, encrypt);
		System.out.println("Original string: "+original);
	}*/
	
	private static final PBEParameterSpec pbeParamSpec = new PBEParameterSpec(new byte[]{ (byte)0xc7, (byte)0x23, (byte)0xa5, (byte)0xfc, (byte)0x7e, (byte)0x38, (byte)0xee, (byte)0x09}, 16);

	public static OutputStream getPasswordProtectedOutputStream (String password, OutputStream stream) {
		PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
		try {
			SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
			Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
			cipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
//			stream = new DeflaterOutputStream(stream);
			stream = new CipherOutputStream(stream, cipher);
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

	public static InputStream getPasswordProtectedInputStream (String password, InputStream stream) {
		PBEKeySpec pbeKeySpec;
		SecretKeyFactory keyFac;

		pbeKeySpec = new PBEKeySpec(password.toCharArray());
		try {
			keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
			Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
			cipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);
//			stream = new InflaterInputStream(stream);
			stream = new CipherInputStream(stream, cipher);
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
}
