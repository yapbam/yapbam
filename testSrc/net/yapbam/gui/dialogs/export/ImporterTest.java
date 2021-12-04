package net.yapbam.gui.dialogs.export;

import net.yapbam.data.Account;
import net.yapbam.data.BalanceHistory;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;
import net.yapbam.gui.YapbamState;
import net.yapbam.util.PreferencesUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ImporterTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void testImportFile() throws Exception {
        File f = createBoursoramaFile();
        Account account = new Account("test", 0.0);
        GlobalData data = new GlobalData();
        data.add(account);
        Importer importer = new Importer(f, createBoursoramaParameters(), data, account);

        ImportError[] errors = importer.importFile(data);

        assertNotNull(errors);
        assertEquals("Number of errors", 0, errors.length);

        BalanceHistory history = account.getBalanceData().getBalanceHistory();
        assertEquals("Number of transactions", 2, history.getTransactionsNumber());
        assertTransaction(history, 0, "27/03/2014", "28/03/2014", "PAIEMENT CARTE 260314 75 MONOP'", -4.68);
        assertTransaction(history, 1, "10/04/2014", "11/04/2014", "VIR appro", 370.0);
    }

    private void assertTransaction(BalanceHistory history, int transactionIndex, String operationDate, String valueDate, String description, double amount) throws ParseException {
        Transaction transaction = history.getTransaction(transactionIndex);
        DateFormat dateFormatter = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, Locale.FRANCE);

        String message = "transaction[" + transactionIndex + "].";
        assertEquals(message + ".date", dateFormatter.parse(operationDate), transaction.getDate());
        assertEquals(message + ".valueDate", dateFormatter.parse(valueDate), transaction.getValueDate());
        assertEquals(message + ".description", description, transaction.getDescription());
        assertEquals(message + ".amount", amount, transaction.getAmount(), 0.001);
    }

    private File createBoursoramaFile() throws IOException {
        File f = temporaryFolder.newFile();
        FileWriter fileWriter = new FileWriter(f);
        fileWriter.write("\"*** P?riode : 27/03/2014 - 27/04/2014\"\n");
        fileWriter.write("\"*** Compte : 40618-80263-00040489785    -EUR \"\n");
        fileWriter.write("\n");
        fileWriter.write("\"DATE OPERATION\"\t\"DATE VALEUR\"\t\"LIBELLE\"\t\"MONTANT\"\t\"DEVISE\"\n");
        fileWriter.write("\" 27/03/2014\"\t\" 28/03/2014\"\t\"PAIEMENT CARTE 260314 75 MONOP'         \"\t-00000000004.68\t\"EUR \"\n");
        fileWriter.write("\" 10/04/2014\"\t\" 11/04/2014\"\t\"VIR appro                               \"\t+00000000370.00\t\"EUR \"\n");
        fileWriter.close();
        return f;
    }

    private ImporterParameters createBoursoramaParameters() {
        return new ImporterParameters('\t', '.', DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRENCH), 4, new int[]{-1, 0, 2, -1, 3, -1, -1, -1, 1, -1});
    }
    
    @Test
    public void testParametersArePreferenceStorageCompatible() {
    	new YapbamState() {
			@Override
			public void put(String key, String value) {
				PreferencesUtils.verifyPreferencesCompliance(key, value);
			}
    	}.save("justATest", createBoursoramaParameters());;
    }
}