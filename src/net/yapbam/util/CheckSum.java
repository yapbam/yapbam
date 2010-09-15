package net.yapbam.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/** A utility class to compute a checksum for a file.
 */
public class CheckSum {
	
	/** Gets a file checksum.
	 * @param file the file to san
	 * @return its MD5 checksum
	 * @throws Exception
	 */
  public static byte[] createChecksum(File file) throws Exception {
  	InputStream is =  new FileInputStream(file);
  	try {
	  	byte[] buffer = new byte[1024];
	  	MessageDigest digest = MessageDigest.getInstance("MD5");
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
