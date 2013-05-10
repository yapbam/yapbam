package net.yapbam.junit;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

import net.yapbam.data.Account;
import net.yapbam.data.Category;
import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Mode;
import net.yapbam.data.Transaction;
import net.yapbam.gui.dialogs.export.Exporter;
import net.yapbam.gui.dialogs.export.ExporterParameters;
import net.yapbam.gui.dialogs.export.Importer;
import net.yapbam.gui.dialogs.export.ImporterParameters;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExportTest {
	@Test
	public void test() throws IOException {
		GlobalData data = new GlobalData();
		Account account = new Account("toto", 100.0);
		data.add(account);
		String description = "A description without \"special\" chars, like quote and ;";
		Transaction t = new Transaction(new Date(), null, description,null,0.0,account,Mode.UNDEFINED,
				Category.UNDEFINED,new Date(), null, Collections.EMPTY_LIST);
		data.add(t);
		FilteredData fData = new FilteredData(data);
		ExporterParameters parameters = new ExporterParameters();
		Exporter exporter = new Exporter(parameters);
		File file = File.createTempFile("ExportTest", "txt");
		exporter.exportFile(file, fData);
		GlobalData rdata = new GlobalData();
		Importer importer = new Importer(file, new ImporterParameters(parameters.getSeparator(), parameters.isInsertHeader()?1:0, parameters.getExportedIndexes()), rdata, null);
		importer.importFile(rdata);
		
		assertEquals(1,rdata.getAccountsNumber());
		assertEquals(1,rdata.getTransactionsNumber());
		assertEquals(description, rdata.getTransaction(0).getDescription());
		assertEquals("toto", rdata.getAccount(0).getName());
		assertTrue(GlobalData.AMOUNT_COMPARATOR.compare(100.0, rdata.getAccount(0).getInitialBalance())==0);
	}
}
