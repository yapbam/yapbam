package net.yapbam.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** Utility to compute the checksum of a file.
 * @author Fathzer
 * <BR>License : GPL v3
 */
public final class CheckSum {
	
	private CheckSum() {}
	
	/** Gets a file's checksum.
	 * @param file The file to scan.
	 * @return The file's MD5 checksum
	 * @throws IOException
	 */
  public static byte[] createChecksum(File file) throws IOException {
  	InputStream is =  new FileInputStream(file);
  	try {
	  	byte[] buffer = new byte[1024];
	  	MessageDigest digest;
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
	  	int numRead;
	  	do {
	  		numRead = is.read(buffer);
	  		if (numRead > 0) {
	  			digest.update(buffer, 0, numRead);
	  		}
	  	} while (numRead != -1);
	  	return digest.digest();
  	} finally {
    	is.close();
  	}
  }
}
