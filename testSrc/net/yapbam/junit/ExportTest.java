package net.yapbam.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Date;

import org.junit.Test;
import org.w3c.tidy.Tidy;
import org.w3c.tidy.TidyMessage;
import org.w3c.tidy.TidyMessageListener;

import net.yapbam.data.Account;
import net.yapbam.data.Category;
import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Mode;
import net.yapbam.data.SubTransaction;
import net.yapbam.data.Transaction;
import net.yapbam.gui.dialogs.export.ExporterCsvFormat;
import net.yapbam.gui.dialogs.export.ExporterHtmlFormat;
import net.yapbam.gui.dialogs.export.Exporter;
import net.yapbam.gui.dialogs.export.ExporterParameters;
import net.yapbam.gui.dialogs.export.Importer;
import net.yapbam.gui.dialogs.export.ImporterParameters;

public class ExportTest {
	@Test
	public void testCSV() throws IOException {
		GlobalData data = new GlobalData();
		Account account = new Account("toto", 100.0);
		data.add(account);
		String description = "A description with \"special\" chars, like quote and ;";
		Transaction t = new Transaction(new Date(), null, description,null,0.0,account,Mode.UNDEFINED,
				Category.UNDEFINED,new Date(), null, Collections.<SubTransaction>emptyList());
		data.add(t);
		FilteredData fData = new FilteredData(data);
		ExporterParameters parameters = new ExporterParameters();
		Exporter<ExporterCsvFormat> exporter = new Exporter<ExporterCsvFormat>(parameters);
		File file = File.createTempFile("ExportTest", ".txt");
		OutputStream outputStream = new FileOutputStream(file);
		try {
			exporter.exportFile(new ExporterCsvFormat(outputStream, parameters.getSeparator(), parameters.getEncoding()), fData);
		} finally {
			outputStream.close();
		}
		GlobalData rdata = new GlobalData();
		DecimalFormat format = (DecimalFormat) NumberFormat.getNumberInstance();
		char decimalSeparator = format.getDecimalFormatSymbols().getDecimalSeparator();
		Importer importer = new Importer(file, new ImporterParameters(parameters.getSeparator(), decimalSeparator, parameters.isInsertHeader()?1:0, parameters.getExportedIndexes()), rdata, null);
		importer.importFile(rdata);
		
		assertEquals(1,rdata.getAccountsNumber());
		assertEquals(1,rdata.getTransactionsNumber());
		assertEquals(description, rdata.getTransaction(0).getDescription());
		assertEquals("toto", rdata.getAccount(0).getName());
		assertTrue(GlobalData.AMOUNT_COMPARATOR.compare(100.0, rdata.getAccount(0).getInitialBalance())==0);
	}
	
	@Test
	public void testHTML() throws IOException {
		GlobalData data = new GlobalData();
		Account account = new Account("toto", 100.0);
		data.add(account);
		String description = "A description with html tags like </td> </tr> </table> &;";
		Transaction t = new Transaction(new Date(), null, description,null,0.0,account,Mode.UNDEFINED,
				Category.UNDEFINED,new Date(), null, Collections.<SubTransaction>emptyList());
		data.add(t);
		FilteredData fData = new FilteredData(data);
		ExporterParameters parameters = new ExporterParameters();
		Exporter<ExporterHtmlFormat> exporter = new Exporter<ExporterHtmlFormat>(parameters);
		File file = File.createTempFile("ExportTest", ".html");
		OutputStream outputStream = new FileOutputStream(file);
		try {
			exporter.exportFile(new ExporterHtmlFormat(outputStream, parameters.getEncoding()), fData);
		} finally {
			outputStream.close();
		}
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
}
