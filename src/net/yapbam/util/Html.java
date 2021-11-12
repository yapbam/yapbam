package net.yapbam.util;

//TODO Move this to yapbam commons
public class Html {
	private Html() {
		// Html is a utilit class
	}
	
	public static String linesToHtml(boolean withStartAndEnd, String... lines) {
		final StringBuilder builder = new StringBuilder();
		if (withStartAndEnd) {
			builder.append(HtmlUtils.START_TAG);
		}
		boolean first = true;
		for (String line:lines) {
			if (first) {
				first = false;
			} else {
				builder.append(HtmlUtils.NEW_LINE_TAG);
			}
			builder.append(HtmlUtils.toHtml(line));
		}
		if (withStartAndEnd) {
			builder.append(HtmlUtils.END_TAG);
		}
		return builder.toString();
	}
}
