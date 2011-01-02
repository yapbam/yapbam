package net.yapbam.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

/** Utility to compute the checksum of a file.
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public final class CheckSum {
	private CheckSum() {}
	
	/** Gets a file's checksum.
	 * @param file The file to scan.
	 * @return The file's MD5 checksum
	 * @throws IOException
	 */
  public static byte[] getChecksum(File file) throws IOException {
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
  
	/** Gets the checksums of different versions of a file.
	 * <br>One usage of checksum is to delete obsolete files, for instance when installing a new version of software.
	 * <br>The problem is then "What if the user modified some files, for instance sample files ?"
	 * <br>A solution is to not removed a file if it was changed. Checksum provide an "acceptable" evaluation of the version of the file.
	 * <br>The other problem is to be able to delete not only the file as it was in the last release ... but also in previous releases.
	 * <br>This mean that not only a checksum is valid, the checksum of all the versions of a file are valid.
	 * @param versionFolders The folders that contains all the version folders. A version folder contains a specific version of the set of files.
	 * <br>Be aware that when the number of possible checksums grow, the probability to find a "fake unmodified file" grows
	 *  (see <a href="http://en.wikipedia.org/wiki/Birthday_problem">Birthday paradox</a>).
	 * @param filePath The path of the file we want to get the checksums. This argument contains path relative to each version folder. 
	 * @return The different checksums of the versions of the file. If a versionFolder doesn't contain the file, it is ignored.
	 * All checksums in the returned table are different.
	 * @throws IOException 
	 */
	public static String[] getCheckSums(File[] versionFolders, String filePath) throws IOException {
			Set<String> checkSums = new HashSet<String>();
			for (File folder : versionFolders) {
				File file = new File(folder, filePath);
				if (file.exists()) checkSums.add(CheckSum.toString(CheckSum.getChecksum(file)));
			}
			return checkSums.toArray(new String[checkSums.size()]);
	}

	/** Turns an array of bytes into a string
	 * @param bytes Array of bytes to convert into a hexadecimal string
	 * @return a positive hexadecimal string
	 */
	public static String toString(byte[] bytes) {
		String brute = new BigInteger(1,bytes).toString(16);
		if (brute.length() != bytes.length*2) {
			StringBuilder builder = new StringBuilder(bytes.length*2);
			for (int i = brute.length(); i < bytes.length*2; i++) {
				builder.append('0');
			}
			builder.append(brute);
			return builder.toString();
		} else {
			return brute;
		}
	}
	
	/** Turns a hexadecimal String into an array of bytes
	 * @param string an hexadecimal string
	 * @return an hexadecimal string
	 */
	public static byte[] toBytes (String string) {
		byte[] byteArray = new BigInteger(string, 16).toByteArray();
		int nbDigits = string.length();
		if (string.startsWith("-")) nbDigits--;
		int resultSize = nbDigits/2;
		if (nbDigits % 2 != 0) {
			resultSize++;
		}
		if (byteArray.length==resultSize) return byteArray;
		byte[] result = new byte[resultSize];
		if (byteArray.length<resultSize) {
			System.arraycopy(byteArray, 0, result, resultSize-byteArray.length, byteArray.length);
		} else {
			System.arraycopy(byteArray, byteArray.length-resultSize, result, 0, resultSize);
		}
		return result;
	}
}
