package net.yapbam.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class AsymetricEncryption {
	
	public static void main(String[] args) {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(512, random);
			KeyPair pair = keyGen.generateKeyPair();
			
			KeyFactory fact = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec pub = fact.getKeySpec(pair.getPublic(), RSAPublicKeySpec.class);
			RSAPrivateKeySpec priv = fact.getKeySpec(pair.getPrivate(), RSAPrivateKeySpec.class);
			
			BigInteger pubModulus = pub.getModulus();
			BigInteger pubExponent = pub.getPublicExponent();
			BigInteger privModulus = priv.getModulus();
			BigInteger privExponent = priv.getPrivateExponent();
			System.out.println ("public : "+pubModulus.toString(16) + " / " +pubExponent.toString(16));
			System.out.println ("private : "+privModulus.toString(16) + " / " +privExponent.toString(16));
			
			StringBuffer buf = new StringBuffer();
			for (int i=0;i<200;i++) {
				buf.append("Hello world repeated many times is a very long secret phrase.");
			}
			String secret = buf.toString();

			
		    Key pubKey = fact.generatePublic(new RSAPublicKeySpec(pubModulus, pubExponent));
			Key privKey = fact.generatePrivate(new RSAPrivateKeySpec(privModulus, privExponent));
		    		
			byte[] encoded = cipher (secret.getBytes(Charset.forName("UTF-8")), pubKey, Cipher.ENCRYPT_MODE, 53);
			System.out.println (secret.length() + "->" + encoded.length/* + " : "+new String(encoded)*/);
			String decoded = new String(cipher (encoded, privKey, Cipher.DECRYPT_MODE, 64),"UTF-8");
			System.out.println (decoded);
			
			encoded = cipher (secret.getBytes(Charset.forName("UTF-8")), privKey, Cipher.ENCRYPT_MODE, 53);
			System.out.println (secret.length() + "->" + encoded.length/* + " : "+new String(encoded)*/);
			decoded = new String(cipher (encoded, pubKey, Cipher.DECRYPT_MODE, 64),"UTF-8");
			System.out.println (decoded);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private static byte[] cipher(byte[] source, Key key, int mode, int blockSize) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			InputStream in = new ByteArrayInputStream(source);
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		    byte[] b = new byte[blockSize];
		    int i = in.read(b);
		    while (i != -1) {
				cipher.init(mode, key);
		    	byte[] encrypted = cipher.doFinal(b,0,i);
				bytes.write(encrypted);
				i = in.read(b);
		    }
		    bytes.close();
			return bytes.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e);
		}
	}
}
