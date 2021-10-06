package net.yapbam.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.w3c.tidy.Tidy;
import org.w3c.tidy.TidyMessage;
import org.w3c.tidy.TidyMessageListener;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.yapbam.data.Account;
import net.yapbam.data.Category;
import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Mode;
import net.yapbam.data.SubTransaction;
import net.yapbam.data.Transaction;
import net.yapbam.export.ExportFormatType;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.export.DataExporter;
import net.yapbam.gui.dialogs.export.DataExporterParameters;
import net.yapbam.gui.dialogs.export.ExportComponent;
import net.yapbam.gui.dialogs.export.Importer;
import net.yapbam.gui.dialogs.export.ImporterParameters;

public class ExportTest {
	@Test
	public void testCSV() throws IOException {
		String description = "A description with \"special\" chars, like quote and ;";
		DataExporterParameters parameters = new DataExporterParameters();
		// Test that Export parameters are taken in account
		parameters.setSeparator('\t');
		File file = exportNewData(ExportFormatType.CSV, description, parameters);

		GlobalData rdata = new GlobalData();
		DecimalFormat format = (DecimalFormat) NumberFormat.getNumberInstance();
		char decimalSeparator = format.getDecimalFormatSymbols().getDecimalSeparator();
		Importer importer = new Importer(file, new ImporterParameters(parameters.getSeparator(), decimalSeparator, DateFormat.getDateInstance(DateFormat.SHORT, LocalizationData.getLocale()), parameters.isInsertHeader()?1:0, parameters.getExportedIndexes()), rdata, null);
		importer.importFile(rdata);
		
		assertEquals(1,rdata.getAccountsNumber());
		assertEquals(1,rdata.getTransactionsNumber());
		assertEquals(description, rdata.getTransaction(0).getDescription());
		assertEquals("toto", rdata.getAccount(0).getName());
		assertEquals(0, GlobalData.AMOUNT_COMPARATOR.compare(100.0, rdata.getAccount(0).getInitialBalance()));
	}

	private File exportNewData(ExportFormatType type, String description, DataExporterParameters parameters)
			throws IOException {
		GlobalData data = new GlobalData();
		Account account = new Account("toto", 100.0);
		data.add(account);
		Transaction t = new Transaction(new Date(), null, description,null,0.0,account,Mode.UNDEFINED,
				Category.UNDEFINED,new Date(), null, Collections.<SubTransaction>emptyList());
		data.add(t);
		FilteredData fData = new FilteredData(data);
		DataExporter exporter = new DataExporter(parameters);
		File file = File.createTempFile("ExportTest", "."+type.getExtension());
		ExportComponent.export(fData, exporter, file, type);
		return file;
	}
	
	@Test
	public void testHTML() throws IOException {
		String description = "A description with html tags like </td> </tr> </table> &;";
		File file = exportNewData(ExportFormatType.HTML, description, new DataExporterParameters());

		final Tidy tidy = new Tidy();
		// Let's ignore the absence of title as its hard to set a valuable title to the document
		final NoTitleDetector listener = new NoTitleDetector();
		tidy.setMessageListener(listener);
		final StringWriter out    = new StringWriter();
	    tidy.setErrout(new PrintWriter(out));
		final InputStream input = new FileInputStream(file); 
		try {
			tidy.parse(input, (OutputStream)null);
		} finally {
			input.close();
		}
		assertEquals (out.toString(), listener.hasNoTitle ? 1 : 0, tidy.getParseWarnings()+tidy.getParseErrors());
	}
	
	private static class NoTitleDetector implements TidyMessageListener {
		boolean hasNoTitle;
		
		@Override
		public void messageReceived(TidyMessage message) {
			if (message.getErrorCode()==17) {
				hasNoTitle = true;
			}
		}
	}
	
	@Test
	public void testJSON() throws IOException {
		String description = "A description with json reserved chars like \", { or ] and accent like אחי";
		File file = exportNewData(ExportFormatType.JSON, description, new DataExporterParameters());

		final ObjectMapper parser = new ObjectMapper();
		@SuppressWarnings("unchecked")
		Map<String, Object> obj = parser.readValue(file, Map.class);
		assertTrue(obj.containsKey("values"));
		@SuppressWarnings("unchecked")
		List<List<String>> lines = (List<List<String>>) obj.get("values");
		assertEquals(3,lines.size());
		List<String> transaction = lines.get(2);
		assertEquals(description, transaction.get(2));
	}
}
