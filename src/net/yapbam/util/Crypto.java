package net.yapbam.util;
import java.math.BigInteger;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/** This class provides utilities to encrypt data.
 * <BR>It uses a symetric AES algorithm. */
public class Crypto {
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
		SecretKeySpec skeySpec = new SecretKeySpec(asByteArray(key), "AES");
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
}
