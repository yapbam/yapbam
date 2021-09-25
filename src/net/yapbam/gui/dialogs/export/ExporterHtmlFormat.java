package net.yapbam.gui.dialogs.export;

public class ExporterHtmlFormat implements IExportableFormat {

	private StringBuffer writer;

	public ExporterHtmlFormat() {
		this.writer = new StringBuffer();
	}

	@Override
	public void addHeader() {
		this.writer.append("<html>\n");
		this.writer.append("<body>\n");
		this.writer.append("<style> table, th, td {border: 1px solid black;border-collapse: collapse;} </style>\n");
		this.writer.append("<table width=\"90%\" style=\"margin:0 auto;\">\n");
	}

	@Override
	public void addLineStart() {
		this.writer.append("<tr>\n");
	}

	@Override
	public void addLineEnd() {
		this.writer.append("</tr>\n");
	}

	@Override
	public void addValue(String value) {
		this.writer.append(String.format("<td>%s</td>", value));
	}

	@Override
	public void addValueSeparator() {
		return;
	}

	@Override
	public void addFooter() {
		this.writer.append("</table>");
		this.writer.append("</body>");
		this.writer.append("</html>");
	}

	@Override
	public String flushAndGetResultl() {
		return this.writer.toString();
	}

}
