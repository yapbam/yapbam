package net.yapbam.util;

import java.util.StringTokenizer;

/** Utility to work with arrays.
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public final class ArrayUtils {
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
}
