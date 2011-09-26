package net.yapbam.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.StringTokenizer;

/** Utility to work with arrays.
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public final class ArrayUtils {
	private static final String ENCODING = "UTF-8";

	private ArrayUtils() {}
	
	/** Converts an int array to a string.
	 * @param array an int array
	 * @return a String representing this array
	 * @see #parseIntArray(String)
	 */
	public static String toString(int[] array) {
		StringBuilder builder = new StringBuilder("[");
		for (int i = 0; i < array.length; i++) {
			if (i!=0) builder.append(", ");
			builder.append(array[i]);
		}
		builder.append("]");
		return builder.toString();
	}
	
	/** Converts a string to an int array.
	 * @param string a string (format [int, int, int])
	 * @return an int array.
	 */
	public static int[] parseIntArray(String string) {
		StringTokenizer tokens = new StringTokenizer(string, "[,]");
		int[] result = new int[tokens.countTokens()];
		for (int i = 0; i < result.length; i++) {
			result[i] = Integer.parseInt(tokens.nextToken().trim());
		}
		return result;
	}
	
	/** Converts a String array to a string.
	 * @param array a String array
	 * @return a String representing this array
	 * @see #parseStringArray(String)
	 */
	public static String toString(String[] array) {
		if (array.length==0) return "";
		StringBuilder builder = new StringBuilder("[");
		for (int i = 0; i < array.length; i++) {
			if (i!=0) builder.append(",");
			try {
				builder.append(URLEncoder.encode(array[i], ENCODING));
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
		builder.append("]");
		return builder.toString();
	}
	
	/** Converts a string to a String array.
	 * @param string a string (format [String,String,String])
	 * @return a String array.
	 */
	public static String[] parseStringArray(String string) {
		if (string.length()==0) return new String[0];
		if (!string.startsWith("[") || !string.endsWith("]")) throw new IllegalArgumentException();
		string = string.substring(1, string.length()-1);
		String[] array = string.split(",");
		for (int i = 0; i < array.length; i++) {
			try {
				array[i] = URLDecoder.decode(array[i], ENCODING);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
		// Unfortunately, String.split ignore last empty strings !!!
		// So, we have to deal with them
		int extraEmptyStrings = 0;
		for (int index=string.length()-1;(index>=0) && (string.charAt(index)==',');index--) {
			extraEmptyStrings++;
		}
		if (extraEmptyStrings!=0) {
			String[] result = new String[array.length+extraEmptyStrings];
			System.arraycopy(array, 0, result, 0, array.length);
			for (int i=array.length;i<result.length;i++) {
				result[i] = "";
			}
			return result;
		} else {
			return array;
		}
	}
	
	/** Builds an integer array.
	 * @param length The length of the int array
	 * @param start The value of the first element of the array
	 * @param step The increment between two elements (result[i+1] - result[i])
	 * @return an integer array
	 */
	public static int[] buildIntArray(int length, int start, int step) {
		int[] result = new int[length];
		if (length>0) {
			result[0] = start;
			for (int i = 1; i < result.length; i++) {
				result[i] = result[i-1]+step; 
			}
		}
		return result;
	}
	
	/** Builds a boolean array.
	 * @param length The length of the array
	 * @param value the initial value of the elements of the array
	 * @return a boolean array.
	 */
	public static boolean[] buildBooleanArray(int length, boolean value) {
		boolean result[] = new boolean[length];
		if (value) Arrays.fill(result, value);
		return result;
	}
}
