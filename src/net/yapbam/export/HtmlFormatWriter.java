package net.yapbam.export;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

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
	private Writer writer;
	private Charset encoding;
	private HeaderAndFooterBuilder headAndFoot;
	private URL css;

	public HtmlFormatWriter(OutputStream stream, Charset encoding) {
		this(stream, encoding, new HeaderAndFooterBuilder(), null);
	}
	
	public HtmlFormatWriter(OutputStream stream, Charset encoding, HeaderAndFooterBuilder headAndFoot, URL css) {
		this.tableRowIndex = new AtomicInteger(0);
		this.writer = new OutputStreamWriter(stream, encoding);
		this.encoding = encoding;
		this.headAndFoot = headAndFoot;
		this.css = css;
	}

	@Override
	public void addHeader() throws IOException {
		this.writer.append("<!DOCTYPE html>\n");
		this.writer.append("<html>\n");
		this.writer.append("<head>\n");
		this.writer.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset="+encoding.name()+"\"/>");
		if(css != null) {
			try {
				this.writer.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + css.toURI() + "\"/>\n");
			} catch (URISyntaxException ex) {
				throw new IOException(ex.getMessage());
			}
		} else {
			this.writer.append("<style type=\"text/css\"> table, th, td {border: 1px solid black;border-collapse: collapse;} #transaction-table {margin:0 auto;width:90%;}</style>\n");
		}
		this.writer.append("</head>\n");
		this.writer.append("<body>\n");
		final String header = headAndFoot==null ? null : headAndFoot.getHeader();
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
		this.writer.append(String.format("<td%s>%s</td>", classes, StringEscapeUtils.escapeHtml4(value)));
	}
	
	@Override
	public void addFooter() throws IOException {
		this.writer.append("</table>");
		final String footer = headAndFoot==null ? null : headAndFoot.getFooter();
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
