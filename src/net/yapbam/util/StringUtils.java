package net.yapbam.util;

import java.util.LinkedList;
import java.util.List;

/** Utility to parse the fields separated by a char in a string.
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public abstract class StringUtils {
	private StringUtils() {}
	
	/** Splits a string into fields.
	 * <br>The main advantage vs String#split is that the developer has not to deal with separators that
	 * are reserved characters of the regular expressions syntax
	 * <br>Some examples:<ul>
	 * <li>split("",',') -> {""}</li>
	 * <li>split(",a,",',') -> {"","a",""}</li>
	 * </ul>
	 * @param string The String to split
	 * @param separator The field separator
	 * @return an array of Strings.
	 */
	public static String[] split(String string, char separator) {
		List<String> result = new LinkedList<String>();
		StringBuilder buffer = new StringBuilder(); 
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i)==separator) {
				result.add(buffer.toString());
				if (buffer.length()>0) buffer.delete(0, buffer.length());
			} else {
				buffer.append(string.charAt(i));
			}
		}
		result.add(buffer.toString());
		return result.toArray(new String[result.size()]);
	}
}
