package net.yapbam.gui.statementview;

import com.fathzer.jlocal.Formatter;

import lombok.AllArgsConstructor;
import net.yapbam.export.HtmlFormatWriter.HeaderAndFooterBuilder;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.export.ExporterParameters;

@AllArgsConstructor
class StatementHeaderAndFooterBuilder extends HeaderAndFooterBuilder {
	private ExporterParameters<ExtraExportData> params;
	@Override
	public String getHeader() {
		final StringBuilder builder = new StringBuilder();
		final ExtraExportData data = params.getDataExtension();
		if (data.isWithStatementId() || data.isWithStartBalance()) {
			builder.append("<div id=\"header\">");
			if (data.isWithStatementId()) {
				builder.append(get("statement-id", LocalizationData.get("TransactionDialog.statement")+" {0}", data.getStatementId()));
			}
			if (data.isWithStartBalance()) {
				builder.append(get("start-balance", LocalizationData.get("StatementView.startBalance"), data.getStartBalance()));
			}
			builder.append("</div>\n");
		}
		return builder.toString();
	}

	@Override
	public String getFooter() {
		if (params.getDataExtension().isWithEndBalance()) {
			return "\n<div id=\"footer\">"+get("end-balance", LocalizationData.get("StatementView.endBalance"), params.getDataExtension().getEndBalance())+"</div>";
		} else {
			return super.getFooter();
		}
	}
	
	private String get(String id, String contentFormat, Object value) {
		return "<div id=\""+id+"\">"+Formatter.format(contentFormat, "<span class=\"content\">"+params.format(value))+"</span></div>";
	}
}