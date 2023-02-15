package net.yapbam.export;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import net.yapbam.util.HtmlUtils;

public class HtmlFormatWriter implements ExportWriter {
	public static class HeaderAndFooterBuilder {
		public String getHeader() {
			return null;
		}
		public String getFooter() {
			return null;
		}
	}
	
	private AtomicInteger tableRowIndex;
	private final Writer writer;
	private final HtmlExportParameters exportParameters;

	public HtmlFormatWriter(OutputStream stream, HtmlExportParameters parameters) {
		this.tableRowIndex = new AtomicInteger(0);
		this.writer = new OutputStreamWriter(stream, Charset.forName("UTF-8"));
		this.exportParameters = parameters;
	}

	@Override
	public void addHeader() throws IOException {
		this.writer.append("<!DOCTYPE html>\n");
		this.writer.append("<html>\n");
		this.writer.append("<head>\n");
		this.writer.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset="+StandardCharsets.UTF_8.name()+"\"/>");
		if(exportParameters.getCss() != null) {
			try {
				this.writer.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + exportParameters.getCss().toURI() + "\"/>\n");
			} catch (URISyntaxException ex) {
				throw new IOException(ex.getMessage());
			}
		} else {
			this.writer.append("<style type=\"text/css\"> table, th, td {border: 1px solid black;border-collapse: collapse;} #transaction-table {margin:0 auto;width:90%;}</style>\n");
		}
		this.writer.append("</head>\n");
		this.writer.append("<body>\n");
		final String header = exportParameters.getHeadAndFoot().getHeader();
		if (header!=null) {
			this.writer.append(header);
		}
		this.writer.append("<table id=\"transaction-table\">\n");
	}

	@Override
	public void addLineStart() throws IOException {
		this.writer.append(String.format("<tr id=\"row-%d\">", getTableRowIndex()));
	}

	@Override
	public void addLineEnd() throws IOException {
		this.writer.append("</tr>\n");
	}

	@Override
	public void addValue(String value, String... styles) throws IOException {
		final String classes = (styles!=null && styles.length > 0) ? String.format(" class=\"%s\"", StringUtils.join(styles, ';')) : "";
		this.writer.append(String.format("<td%s>%s</td>", classes, HtmlUtils.escape(value)));
	}
	
	@Override
	public void addFooter() throws IOException {
		this.writer.append("</table>");
		final String footer = exportParameters.getHeadAndFoot().getFooter();
		if (footer!=null) {
			this.writer.append(footer);
		}
		this.writer.append("</body>");
		this.writer.append("</html>");
	}

	@Override
	public void close() throws IOException {
		this.writer.close();
	}
	
	private Integer getTableRowIndex()  {
		return tableRowIndex.incrementAndGet();
	}
}
