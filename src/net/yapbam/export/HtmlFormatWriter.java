package net.yapbam.export;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

public class HtmlFormatWriter implements ExportWriter {

	private Writer writer;
	private Charset encoding;
	private String statementId;
	private String startBalance;
	private String endBalance;
	private File css;

	public HtmlFormatWriter(OutputStream stream, Charset encoding) {
		this(stream, encoding, null);
	}
	
	public HtmlFormatWriter(OutputStream stream, Charset encoding, String statementId) {
		this(stream, encoding, statementId, null);
	}
	
	public HtmlFormatWriter(OutputStream stream, Charset encoding, String statementId, String startBalance) {
		this(stream, encoding, statementId, startBalance, null);
	}
	
	public HtmlFormatWriter(OutputStream stream, Charset encoding, String statementId, String startBalance, String endBalance) {
		this(stream, encoding, statementId, startBalance, endBalance, null);
	}
	
	public HtmlFormatWriter(OutputStream stream, Charset encoding, String statementId, String startBalance, String endBalance, File css) {
		this.writer = new OutputStreamWriter(stream, encoding);
		this.encoding = encoding;
		this.statementId = statementId;
		this.startBalance = startBalance;
		this.endBalance = endBalance;
		this.css = css;
	}

	@Override
	public void addHeader() throws IOException {
		this.writer.append("<!DOCTYPE html>\n");
		this.writer.append("<html>\n");
		this.writer.append("<head>\n");
		this.writer.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset="+encoding.name()+"\"/>");
		if(css != null && css.exists()) {
			this.writer.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"file://"+css.getPath()+ "\"/>\n");
		}
		this.writer.append("<style type=\"text/css\"> table, th, td {border: 1px solid black;border-collapse: collapse;} </style>\n");
		this.writer.append("</head>\n");
		this.writer.append("<body>\n");
		if(!StringUtils.isBlank(statementId)) {
			this.writer.append("<center>\n");
			this.writer.append(String.format("<p>%s</p>\n", statementId));
			this.writer.append("</center>\n");
		}
		this.writer.append("<table width=\"90%\" style=\"margin:0 auto;\">\n");
	}

	@Override
	public void addLineStart() throws IOException {
		this.writer.append("<tr>\n");
	}

	@Override
	public void addLineEnd() throws IOException {
		this.writer.append("</tr>\n");
		
	}

	@Override
	public void addValue(String value) throws IOException {
		this.writer.append(String.format("<td>%s</td>",StringEscapeUtils.escapeHtml4(value)));
	}
	
	@Override
	public void addFooter() throws IOException {
		this.writer.append("</table>");
		if (!StringUtils.isBlank(startBalance) || !StringUtils.isBlank(endBalance)) {
			this.writer.append("<center><p>\n");
			if (!StringUtils.isBlank(startBalance)) {
				this.writer.append(startBalance);

			}
			if (!StringUtils.isBlank(endBalance)) {
				this.writer.append(String.format("%s %s", !StringUtils.isBlank(startBalance) ? " -" : "", endBalance));

			}
			this.writer.append("</p></center>\n");
		}
		this.writer.append("</body>");
		this.writer.append("</html>");
	}

	@Override
	public void close() throws IOException {
		this.writer.close();
	}
}
