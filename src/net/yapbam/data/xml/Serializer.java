package net.yapbam.data.xml;

import java.io.*;
import java.math.BigInteger;
import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.StringTokenizer;

import net.yapbam.data.*;
import net.yapbam.date.helpers.DateStepper;
import net.yapbam.date.helpers.DayDateStepper;
import net.yapbam.date.helpers.DeferredValueDateComputer;
import net.yapbam.date.helpers.MonthDateStepper;
import net.yapbam.gui.Preferences;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.sax.*;

/** The class implements xml yapbam data serialization and deserialization to (or from) an URL.
 * Currently supported URL type are :<UL>
 * <LI> file.
 * </UL>
 */
public class Serializer {
	static final String ENCODING = "UTF-8";

	static final String STATEMENT_ATTRIBUTE = "statement";
	static final String VALUE_DATE_ATTRIBUTE = "valueDate";
	static final String CATEGORY_ATTRIBUTE = "category";
	static final String NUMBER_ATTRIBUTE = "number";
	static final String MODE_ATTRIBUTE = "mode";
	static final String AMOUNT_ATTRIBUTE = "amount";
	static final String DATE_ATTRIBUTE = "date";
	static final String DESCRIPTION_ATTRIBUTE = "description";
	static final String ACCOUNT_ATTRIBUTE = "account";

	static final String DEBT_DAY_ATTRIBUTE = "debtDay";
	static final String STOP_DAY_ATTRIBUTE = "stopDay";
	static final String DAY_ATTRIBUTE = "day";
	static final String PERIOD_ATTRIBUTE = "period";
	static final String DATE_STEPPER_KIND_ATTRIBUTE = "kind";
	static final String IMMEDIATE_DATE_STEPPER_KIND = "immediate";
	static final String MONTHLY_DATE_STEPPER_KIND = "monthly";
	static final String DEFERRED_DATE_STEPPER_KIND = "deferred";
	static final String RELATIVE_DATE_STEPPER_KIND = "daily";
	static final String CHECKBOOK_ATTRIBUTE = "checkbook";
	
	static final String PREFIX_ATTRIBUTE = "prefix";
	static final String FIRST_NUMBER_ATTRIBUTE = "first";
	static final String SIZE_ATTRIBUTE = "size";
	static final String NEXT_NUMBER_ATTRIBUTE = "next";

	static final String INITIAL_BALANCE_ATTRIBUTE = "initialBalance";
	static final String ID_ATTRIBUTE = "id";
	static final String NEXT_DATE_ATTRIBUTE = "next";
	static final String LAST_DATE_ATTRIBUTE = "last";
	static final String ENABLED_ATTRIBUTE = "enabled";

	static final String GLOBAL_DATA_TAG = "DATA";
	static final String CATEGORY_TAG = "CATEGORY";
	static final String ACCOUNT_TAG = "ACCOUNT";
	static final String MODE_TAG = "MODE";
	static final String CHECKBOOK_TAG = "CHECKBOOK";
	static final String EXPENSE_VDC_TAG = "EXPENSE";
	static final String RECEIPT_VDC_TAG = "RECEIPT";
	static final String PERIODICAL_TAG = "PERIODICAL";
	static final String DATE_STEPPER_TAG = "DATE_STEPPER";
	static final String TRANSACTION_TAG = "TRANSACTION";
	static final String SUBTRANSACTION_TAG = "SUBTRANSACTION";

	private AttributesImpl atts;
	private TransformerHandler hd;
	
	private static void write(GlobalData data, File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		try {
			new Serializer().serialize(data, fos);
		} finally {
			fos.close();
		}
	}
	
	public static void write(GlobalData data, URI uri) throws IOException {
		if (uri.getScheme().equals("file")) {
			File file = new File(uri);
			if (file.exists() && !file.canWrite()) throw new IOException("writing to "+file+" is not allowed");
			// Proceed safely, it means not to erase the old version until the new version is written
			// Everything here is pretty ugly.
			//TODO Implement this stuff using the transactional File access in JCommon
			File writed = file.exists()?File.createTempFile("yapbam", "cpt"):file;
			write(data, writed);
			if (!file.equals(writed)) {
				// Ok, not so safe as I want since we could lost the file between deleting and renaming
				// but I can't find a better way
				if (!file.delete()) {
					writed.delete();
					throw new IOException("Unable to delete old copy of "+file);
				}
				boolean result = writed.renameTo(file);
				if (result==false) {
					// renameTo may fail if tmpFile and file are not on the same file system.
					// We then copy the tmp file, it's really ugly ... but I don't know how to do that
					FileReader in = new FileReader(writed);
					FileWriter out = new FileWriter(file);
					int c;
					while ((c = in.read()) != -1) out.write(c);
					in.close();
					out.close();
					writed.delete(); // Deletes the tmp file
				}
			}
		} else if (uri.getScheme().equals("ftp")) {
			// FTP URL has to be like this one : ftp://user:password@server/file;type=i
			// Currently this functionality isn't implemented in the gui
			// Probably, it means implementing an ftp client to create directories and save copy
			OutputStream os = uri.toURL().openConnection(Preferences.INSTANCE.getHttpProxy()).getOutputStream();
			try {
				new Serializer().serialize(data, os);
			} finally {
				os.close();
			}
		} else {
			throw new IOException("Unsupported protocol: "+uri.getScheme());
		}
	}

	public static void read(GlobalData data, URI uri) throws IOException {
		if (uri.getScheme().equals("file") || uri.getScheme().equals("ftp")) {
			InputStream is = uri.toURL().openStream();
			try {
				read(data, is);
			} finally {
				is.close();
			}
		} else {
			throw new IOException("Unsupported protocol: "+uri.getScheme());
		}
	}

	private static void read(GlobalData data, InputStream is) throws IOException {
		try {
			SAXParserFactory.newInstance().newSAXParser().parse(is, new GlobalDataHandler(data));
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	private Serializer () {
	}

	private void serialize (GlobalData data, OutputStream os) throws IOException {
		try {
			StreamResult streamResult = new StreamResult(os);
			SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
			hd = tf.newTransformerHandler();
			Transformer serializer = hd.getTransformer();
			serializer.setOutputProperty(OutputKeys.ENCODING,ENCODING);
			serializer.setOutputProperty(OutputKeys.INDENT,"yes");
			hd.setResult(streamResult);
			hd.startDocument();
			
			this.atts = new AttributesImpl();
			hd.startElement("","",GLOBAL_DATA_TAG,atts);
			
			// Accounts.
			for (int i=0;i<data.getAccountsNumber();i++)
			{
				serialize(data.getAccount(i));
			}
			// Categories
			for (int i=0;i<data.getCategoriesNumber();i++)
			{
				serialize(data.getCategory(i));
			}
			// Periodical transactions
			for (int i = 0; i < data.getPeriodicalTransactionsNumber(); i++) {
				serialize(data.getPeriodicalTransaction(i));
			}
			//Transactions
			for (int i=0;i<data.getTransactionsNumber();i++)
			{
				serialize(data.getTransaction(i));
			}		
			hd.endElement("","",GLOBAL_DATA_TAG);
			
			hd.endDocument();
		} catch (TransformerConfigurationException e) {
			throw new IOException(e);
		} catch (SAXException e) {
			throw new IOException(e);
		}
	}

	private void serialize(Account account) throws SAXException {
		atts.clear();
		atts.addAttribute("","",ID_ATTRIBUTE,"CDATA",account.getName());
		atts.addAttribute("","",INITIAL_BALANCE_ATTRIBUTE,"CDATA",Double.toString(account.getInitialBalance()));
		hd.startElement("","",ACCOUNT_TAG,atts);
		HashSet<String> saved = new HashSet<String>();
		// for loops start with index 1 because the default mode doesn't need to be saved
		for (int i = 0; i < account.getModesNumber(true); i++) {
			Mode mode = account.getMode(i, true);
			if (!mode.equals(Mode.UNDEFINED)) serialize(mode);
			saved.add(mode.getName());
		}
		for (int i = 0; i < account.getModesNumber(false); i++) {
			Mode mode = account.getMode(i, false);
			if (!mode.equals(Mode.UNDEFINED) && !saved.contains(mode.getName())) serialize(mode);
		}
		for (int i = 0; i < account.getCheckbooksNumber(); i++) {
			serialize(account.getCheckbook(i));
		}
		hd.endElement("","",ACCOUNT_TAG);
	}
	
	private void serialize(Mode mode) throws SAXException {
		atts.clear();
		atts.addAttribute("","",ID_ATTRIBUTE,"CDATA",mode.getName());
		if (mode.isUseCheckBook()) atts.addAttribute("", "", CHECKBOOK_ATTRIBUTE, "CDATA", "true");
		hd.startElement("","",MODE_TAG,atts);
		DateStepper expense = mode.getExpenseVdc();
		if (expense!=null) {
			setAttributes(expense);
			hd.startElement("", "", EXPENSE_VDC_TAG, atts);
			hd.endElement("", "", EXPENSE_VDC_TAG);
		}
		DateStepper receipt = mode.getReceiptVdc();
		if (receipt!=null) {
			setAttributes(receipt);
			hd.startElement("", "", RECEIPT_VDC_TAG, atts);
			hd.endElement("", "", RECEIPT_VDC_TAG);
		}
		hd.endElement("","",MODE_TAG);
	}

	private void serialize(Checkbook book) throws SAXException {
		atts.clear();
		atts.addAttribute("","",PREFIX_ATTRIBUTE,"CDATA",book.getPrefix());
		atts.addAttribute("","",FIRST_NUMBER_ATTRIBUTE,"CDATA",book.getFirst().toString());
		atts.addAttribute("","",SIZE_ATTRIBUTE,"CDATA",Integer.toString(book.size()));
		if (!book.isEmpty()) atts.addAttribute("", "", NEXT_NUMBER_ATTRIBUTE, "CDATA", book.getFirst().add(BigInteger.valueOf(book.getUsed())).toString());
		hd.startElement("","",CHECKBOOK_TAG,atts);
		hd.endElement("","",CHECKBOOK_TAG);
	}

	private void setAttributes(DateStepper dateStepper) {
		atts.clear();
		String kind;
		if (dateStepper instanceof DayDateStepper) {
			kind = RELATIVE_DATE_STEPPER_KIND;
			atts.addAttribute("", "", PERIOD_ATTRIBUTE, "CDATA", Integer.toString(((DayDateStepper)dateStepper).getStep()));
		} else if (dateStepper instanceof DeferredValueDateComputer) {
			kind = DEFERRED_DATE_STEPPER_KIND;
			atts.addAttribute("", "", STOP_DAY_ATTRIBUTE, "CDATA", Integer.toString(((DeferredValueDateComputer)dateStepper).getStopDay()));
			atts.addAttribute("", "", DEBT_DAY_ATTRIBUTE, "CDATA", Integer.toString(((DeferredValueDateComputer)dateStepper).getDebtDay()));
		} else if (dateStepper.equals(DateStepper.IMMEDIATE)) {
			kind = IMMEDIATE_DATE_STEPPER_KIND;
		} else {
			throw new RuntimeException("Unsupported ValueDateComputer class : "+dateStepper.getClass().getName());
		}
		atts.addAttribute("", "", DATE_STEPPER_KIND_ATTRIBUTE, "CDATA", kind);
	}

	private void serialize(Category category) throws SAXException {
		if (!category.equals(Category.UNDEFINED)) {
			atts.clear();
			atts.addAttribute("","",ID_ATTRIBUTE,"CDATA",category.getName());
			hd.startElement("","",CATEGORY_TAG,atts);
			hd.endElement("","",CATEGORY_TAG);
		}
	}
	
	private void serialize(Transaction transaction) throws SAXException {
		atts.clear();
		atts.addAttribute("","",ACCOUNT_ATTRIBUTE,"CDATA",transaction.getAccount().getName());
		String description = transaction.getDescription();
		if (description!=null) atts.addAttribute("","",DESCRIPTION_ATTRIBUTE,"CDATA",description);
		atts.addAttribute("","",DATE_ATTRIBUTE,"CDATA",toString(transaction.getDate()));
		atts.addAttribute("","",AMOUNT_ATTRIBUTE,"CDATA",Double.toString(transaction.getAmount()));
		Mode mode = transaction.getMode();
		if (!mode.equals(Mode.UNDEFINED)) atts.addAttribute("","",MODE_ATTRIBUTE,"CDATA",mode.getName());
		String number = transaction.getNumber();
		if ((number!=null) && (number.length()>0)) atts.addAttribute("","",NUMBER_ATTRIBUTE,"CDATA",number);
		Category category = transaction.getCategory();
		if (!category.equals(Category.UNDEFINED)) atts.addAttribute("","",CATEGORY_ATTRIBUTE,"CDATA",category.getName());
		atts.addAttribute("","",VALUE_DATE_ATTRIBUTE,"CDATA",toString(transaction.getValueDate()));
		String statement = transaction.getStatement();
		if (statement!=null) atts.addAttribute("","",STATEMENT_ATTRIBUTE,"CDATA",statement);
		hd.startElement("","",TRANSACTION_TAG,atts);
		for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
			serialize(transaction.getSubTransaction(i));
		}
		hd.endElement("","",TRANSACTION_TAG);
	}

	private void serialize(SubTransaction subTransaction) throws SAXException {
		atts.clear();
		atts.addAttribute("", "", DESCRIPTION_ATTRIBUTE, "CDATA", subTransaction.getDescription());
		atts.addAttribute("", "", AMOUNT_ATTRIBUTE, "CDATA", Double.toString(subTransaction.getAmount()));
		Category category = subTransaction.getCategory();
		if (!category.equals(Category.UNDEFINED)) atts.addAttribute("", "", CATEGORY_ATTRIBUTE, "CDATA",category.getName());
		hd.startElement("","",SUBTRANSACTION_TAG,atts);
		hd.endElement("","",SUBTRANSACTION_TAG);
	}
	
	private void serialize(PeriodicalTransaction periodicalTransaction) throws SAXException {
		atts.clear();
		atts.addAttribute("","",ACCOUNT_ATTRIBUTE,"CDATA",periodicalTransaction.getAccount().getName());
		String description = periodicalTransaction.getDescription();
		if (description!=null) atts.addAttribute("","",DESCRIPTION_ATTRIBUTE,"CDATA",description);
		atts.addAttribute("","",AMOUNT_ATTRIBUTE,"CDATA",Double.toString(periodicalTransaction.getAmount()));
		Mode mode = periodicalTransaction.getMode();
		if (!mode.equals(Mode.UNDEFINED)) atts.addAttribute("","",MODE_ATTRIBUTE,"CDATA",mode.getName());
		Category category = periodicalTransaction.getCategory();
		if (!category.equals(Category.UNDEFINED)) atts.addAttribute("","",CATEGORY_ATTRIBUTE,"CDATA",category.getName());
		atts.addAttribute("","",ENABLED_ATTRIBUTE,"CDATA",Boolean.toString(periodicalTransaction.isEnabled()));
		Date nextDate = periodicalTransaction.getNextDate();
		if (nextDate!=null) atts.addAttribute("","",NEXT_DATE_ATTRIBUTE,"CDATA",toString(nextDate));
		hd.startElement("","",PERIODICAL_TAG,atts);
		DateStepper nextDateBuilder = periodicalTransaction.getNextDateBuilder();
		if (nextDateBuilder!=null) serialize(nextDateBuilder);
		for (int i = 0; i < periodicalTransaction.getSubTransactionSize(); i++) {
			serialize(periodicalTransaction.getSubTransaction(i));
		}
		hd.endElement("","",PERIODICAL_TAG);
	}

	private void serialize(DateStepper stepper) throws SAXException {
		if (stepper instanceof MonthDateStepper) {
			MonthDateStepper mds = (MonthDateStepper) stepper;
			atts.clear();
			atts.addAttribute("", "", DATE_STEPPER_KIND_ATTRIBUTE, "CDATA", MONTHLY_DATE_STEPPER_KIND);
			atts.addAttribute("", "", PERIOD_ATTRIBUTE, "CDATA", Integer.toString(mds.getPeriod()));
			atts.addAttribute("", "", DAY_ATTRIBUTE, "CDATA", Integer.toString(mds.getDay()));
			Date last = mds.getLastDate();
			if (last!=null) atts.addAttribute("", "", LAST_DATE_ATTRIBUTE, "CDATA", toString(last));
			hd.startElement("","",DATE_STEPPER_TAG, atts);
			hd.endElement("","",DATE_STEPPER_TAG);
		} else if (stepper instanceof DayDateStepper) {
			DayDateStepper dds = (DayDateStepper) stepper;
			atts.addAttribute("", "", DATE_STEPPER_KIND_ATTRIBUTE, "CDATA", RELATIVE_DATE_STEPPER_KIND);
			atts.addAttribute("", "", PERIOD_ATTRIBUTE, "CDATA", Integer.toString(dds.getStep()));
			Date last = dds.getLastDate();
			if (last!=null) atts.addAttribute("", "", LAST_DATE_ATTRIBUTE, "CDATA", toString(last));
			hd.startElement("","",DATE_STEPPER_TAG, atts);
			hd.endElement("","",DATE_STEPPER_TAG);
			atts.clear();
		} else throw new IllegalArgumentException("This stepper class is not supported : "+stepper.getClass());
	}

	@SuppressWarnings("deprecation")
	private String toString(Date date) {
		int month = date.getMonth()+1;
		int year = date.getYear()+1900;
		return year + "/" + month + "/" + date.getDate();
	}
	
	@SuppressWarnings("deprecation")
	static Date toDate(String value) {
		StringTokenizer tokens = new StringTokenizer(value,"/");
		int year = Integer.parseInt(tokens.nextToken())-1900;
		int month = Integer.parseInt(tokens.nextToken())-1;
		int day = Integer.parseInt(tokens.nextToken());
		return new Date(year, month, day);
	}
}
