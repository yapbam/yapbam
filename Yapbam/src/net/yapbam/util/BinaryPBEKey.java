package net.yapbam.util;

import java.security.spec.*;
import java.util.*;
import javax.crypto.*;

/** A PBEWithMD5AndDES key that allows non ascii char in password.
 * <br>This class is copied from a <a href="http://stackoverflow.com/questions/809590/how-can-i-decode-a-pkcs5-encrypted-pkcs8-private-key-in-java">stackoverflow question</a> 
 */
@SuppressWarnings("serial")
public class BinaryPBEKey implements SecretKey {
	private final byte[] key;

	/**
	 * Creates a PBE key from a given binary key.
	 * 
	 * @param key
	 *          The key.
	 */
	public BinaryPBEKey(byte[] key) throws InvalidKeySpecException {
		if (key == null) {
			this.key = new byte[0];
		} else {
			this.key = (byte[]) key.clone();
		}
		Arrays.fill(key, (byte) 0);
	}

	public byte[] getEncoded() {
		return (byte[]) key.clone();
	}

	public String getAlgorithm() {
		return "PBEWithMD5AndDES";
	}

	public String getFormat() {
		return "RAW";
	}

	/**
	 * Calculates a hash code value for the object. Objects that are equal will
	 * also have the same hashcode.
	 */
	public int hashCode() {
		int ret = 0;

		for (int xa = 1; xa < this.key.length; xa++) {
			ret += (this.key[xa] * xa);
		}
		return (ret ^= getAlgorithm().toLowerCase().hashCode());
	}

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}

		BinaryPBEKey oth = (BinaryPBEKey) obj;

		if (!(oth.getAlgorithm().equalsIgnoreCase(getAlgorithm()))) {
			return false;
		}

		byte[] othkey = oth.getEncoded();
		boolean ret = Arrays.equals(key, othkey);
		Arrays.fill(othkey, (byte) 0);
		return ret;
	}

	public void destroy() {
		Arrays.fill(this.key, (byte) 0);
	}

	/**
	 * Ensure that the password bytes of this key are zeroed out when there are no
	 * more references to it.
	 */
	protected void finalize() throws Throwable {
		try {
			destroy();
		} finally {
			super.finalize();
		}
	}
}