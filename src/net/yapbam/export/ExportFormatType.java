package net.yapbam.export;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.fathzer.jlocal.Formatter;

import net.yapbam.export.HtmlFormatWriter.HeaderAndFooterBuilder;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.export.ExporterParameters;
import net.yapbam.gui.statementview.StatementExporterParameters;

public enum ExportFormatType {
	HTML("HyperText Markup Language", "html"),
	CSV("Comma Separated Values", "csv"),
	JSON("JavaScript Object Notation", "json");

	private final String description;
	private final String extension;

	ExportFormatType(String description, String extension) {
		this.description = description;
		this.extension = extension;
	}

	public String getDescription() {
		return description;
	}

	public String getExtension() {
		return extension;
	}

	public ExportWriter getTableExporter(OutputStream stream, ExporterParameters params) {
		if (ExportFormatType.CSV.equals(this)) {
			return new CsvFormatWriter(stream, params.getSeparator(), params.getPreferredEncoding());
		} else if (ExportFormatType.HTML.equals(this)) {
			if (params instanceof StatementExporterParameters) { //TODO
				final StatementExporterParameters p = (StatementExporterParameters) params;
				final HeaderAndFooterBuilder haf = new HeaderAndFooterBuilder() {
					@Override
					public String getHeader() {
						StringBuilder builder = new StringBuilder();
						if (p.isWithStatementId() || p.isWithStartBalance()) {
							builder.append("<div id=\"header\">");
							if (p.isWithStatementId()) {
								builder.append(get("statement-id", LocalizationData.get("TransactionDialog.statement")+" {0}", p.getStatementId(), p));
							}
							if (p.isWithStartBalance()) {
								builder.append(get("start-balance", LocalizationData.get("StatementView.startBalance"), p.getStartBalance(), p));
							}
							builder.append("</div>\n");
						}
						return builder.toString();
					}

					@Override
					public String getFooter() {
						if (p.isWithEndBalance()) {
							return "\n<div id=\"footer\">"+get("end-balance", LocalizationData.get("StatementView.endBalance"), p.getEndBalance(), p)+"</div>";
						} else {
							return super.getFooter();
						}
					}
					
					private String get(String id, String contentFormat, Object value, ExporterParameters p) {
						return "<div id=\""+id+"\">"+Formatter.format(contentFormat, "<span class=\"content\">"+p.format(value))+"</span></div>";
					}
				};
				return new HtmlFormatWriter(stream, StandardCharsets.UTF_8, haf, p.getCss());
			}
			return new HtmlFormatWriter(stream, StandardCharsets.UTF_8);
		} else if(ExportFormatType.JSON.equals(this)) {
			return new JsonFormatWriter(stream, StandardCharsets.UTF_8);
		} else {
			throw new IllegalStateException(); // Ouch we forgot a format !
		}
	}
}
