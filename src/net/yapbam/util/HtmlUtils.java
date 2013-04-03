package net.yapbam.util;

/** Some utility method on html Strings.
 * 
 * @author Jean-Marc Astesana
 * License GPL v3
 */
public abstract class HtmlUtils {
	private static final String HTML_START_TAG = "<HTML>";
	private static final String HTML_END_TAG = "</HTML>";

	private HtmlUtils() {}
	
	/** Removes the <html> and </html> tag respectively at the beginning and the end of a string.
	 * @param text The string to process
	 * @return the string without the html tags, or the trimmed string if it doesn't not contains the tags.
	 */
	public static String removeHtmlTags (String text) {
		text = text.trim();
		String upper = text.toUpperCase();
		if (upper.startsWith(HTML_START_TAG) && upper.endsWith(HTML_END_TAG)) {
			text = text.substring(HTML_START_TAG.length());
			text = text.substring(0, text.length()-HTML_END_TAG.length());
		}
		return text;
	}
}
