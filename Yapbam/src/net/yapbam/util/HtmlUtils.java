package net.yapbam.util;

/** Some utility method on html Strings.
 * 
 * @author Jean-Marc Astesana
 * License GPL v3
 */
public abstract class HtmlUtils {
	public static final String START_TAG = "<HTML>";
	public static final String END_TAG = "</HTML>";
	public static final String NEW_LINE_TAG = "<BR>";

	private HtmlUtils() {}
	
	/** Removes the <html> and </html> tag respectively at the beginning and the end of a string.
	 * @param text The string to process
	 * @return the string without the html tags, or the trimmed string if it doesn't not contains the tags.
	 */
	public static String removeHtmlTags (String text) {
		text = text.trim();
		String upper = text.toUpperCase();
		if (upper.startsWith(START_TAG) && upper.endsWith(END_TAG)) {
			text = text.substring(START_TAG.length());
			text = text.substring(0, text.length()-END_TAG.length());
		}
		return text;
	}
}
