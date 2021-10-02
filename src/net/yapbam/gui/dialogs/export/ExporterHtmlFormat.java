package net.yapbam.gui.dialogs.export;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import org.apache.commons.lang3.StringEscapeUtils;

public class ExporterHtmlFormat implements IExportableFormat {

	private Writer writer;

	public ExporterHtmlFormat(OutputStream stream, Charset encoding) {
		this.writer = new OutputStreamWriter(stream, encoding);
	}

	@Override
	public void addHeader() throws IOException {
		this.writer.append("<html>\n");
		this.writer.append("<body>\n");
		this.writer.append("<style> table, th, td {border: 1px solid black;border-collapse: collapse;} </style>\n");
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
		this.writer.append(String.format( //
				"<td>%s</td>", //
				StringEscapeUtils.escapeHtml4(value)) //
		);
	}
	
	@Override
	public void addFooter() throws IOException {
		this.writer.append("</table>");
		this.writer.append("</body>");
		this.writer.append("</html>");
	}

	@Override
	public void close() throws IOException {
		this.writer.close();
	}

}
