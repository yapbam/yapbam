package net.yapbam.data.xml;

import java.io.*;
import java.util.Date;
import java.util.HashSet;
import java.util.StringTokenizer;

import net.yapbam.data.*;
import net.yapbam.date.helpers.DateStepper;
import net.yapbam.date.helpers.DayDateStepper;
import net.yapbam.date.helpers.DeferredValueDateComputer;
import net.yapbam.date.helpers.MonthDateStepper;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.sax.*;

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
	static final String CHEQUE_BOOK_ATTRIBUTE = "chequeBook";

	static final String INITIAL_BALANCE_ATTRIBUTE = "initialBalance";
	static final String ID_ATTRIBUTE = "id";
	static final String NEXT_DATE_ATTRIBUTE = "next";
	static final String ANTICIPATION_ATTRIBUTE = "anticipation";
	static final String LAST_DATE_ATTRIBUTE = "last";
	static final String ENABLED_ATTRIBUTE = "enabled";

	static final String GLOBAL_DATA_TAG = "DATA";
	static final String CATEGORY_TAG = "CATEGORY";
	static final String ACCOUNT_TAG = "ACCOUNT";
	static final String MODE_TAG = "MODE";
	static final String EXPENSE_VDC_TAG = "EXPENSE";
	static final String RECEIPT_VDC_TAG = "RECEIPT";
	static final String PERIODICAL_TAG = "PERIODICAL";
	static final String DATE_STEPPER_TAG = "DATE_STEPPER";
	static final String TRANSACTION_TAG = "TRANSACTION";
	static final String SUBTRANSACTION_TAG = "SUBTRANSACTION";

	
	private AttributesImpl atts;
	private TransformerHandler hd;
	
	public static void write(GlobalData data, File file) throws IOException {
		try {
			new Serializer().serialize(data, file);
		} catch (TransformerConfigurationException e) {
			throw new IOException(e);
		} catch (SAXException e) {
			throw new IOException(e);
		}
	}

	public static void read(GlobalData data, File file) throws IOException {
		try {
			SAXParserFactory.newInstance().newSAXParser().parse(file, new GlobalDataHandler(data));
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	private Serializer () {
	}

	private void serialize (GlobalData data, File file) throws IOException, SAXException, TransformerConfigurationException {
		FileOutputStream fos = new FileOutputStream(file);
		StreamResult streamResult = new StreamResult(fos);
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
		fos.close();
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
		hd.endElement("","",ACCOUNT_TAG);
	}
	
	private void serialize(Mode mode) throws SAXException {
		atts.clear();
		atts.addAttribute("","",ID_ATTRIBUTE,"CDATA",mode.getName());
		if (mode.isUseChequeBook()) atts.addAttribute("", "", CHEQUE_BOOK_ATTRIBUTE, "CDATA", "true");
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
//		atts.addAttribute("", "", ID_ATTRIBUTE, "CDATA", periodicalTransaction.getId());
		atts.addAttribute("","",ACCOUNT_ATTRIBUTE,"CDATA",periodicalTransaction.getAccount().getName());
		String description = periodicalTransaction.getDescription();
		if (description!=null) atts.addAttribute("","",DESCRIPTION_ATTRIBUTE,"CDATA",description);
		atts.addAttribute("","",AMOUNT_ATTRIBUTE,"CDATA",Double.toString(periodicalTransaction.getAmount()));
		Mode mode = periodicalTransaction.getMode();
		if (!mode.equals(Mode.UNDEFINED)) atts.addAttribute("","",MODE_ATTRIBUTE,"CDATA",mode.getName());
		Category category = periodicalTransaction.getCategory();
		if (!category.equals(Category.UNDEFINED)) atts.addAttribute("","",CATEGORY_ATTRIBUTE,"CDATA",category.getName());
		atts.addAttribute("","",NEXT_DATE_ATTRIBUTE,"CDATA",toString(periodicalTransaction.getNextDate()));
		atts.addAttribute("","",ANTICIPATION_ATTRIBUTE,"CDATA",Integer.toString(periodicalTransaction.getAnticipation()));
		atts.addAttribute("","",ENABLED_ATTRIBUTE,"CDATA",Boolean.toString(periodicalTransaction.isEnabled()));
		hd.startElement("","",PERIODICAL_TAG,atts);
		serialize(periodicalTransaction.getNextDateBuilder());
		for (int i = 0; i < periodicalTransaction.getSubTransactionSize(); i++) {
			serialize(periodicalTransaction.getSubTransaction(i));
		}
		hd.endElement("","",PERIODICAL_TAG);
	}

	private void serialize(DateStepper stepper) throws SAXException {
		MonthDateStepper mds = (MonthDateStepper) stepper;
		atts.clear();
		atts.addAttribute("", "", DATE_STEPPER_KIND_ATTRIBUTE, "CDATA", MONTHLY_DATE_STEPPER_KIND);
		atts.addAttribute("", "", PERIOD_ATTRIBUTE, "CDATA", Integer.toString(mds.getPeriod()));
		atts.addAttribute("", "", DAY_ATTRIBUTE, "CDATA", Integer.toString(mds.getDay()));
		Date last = mds.getLastDate();
		if (last!=null) atts.addAttribute("", "", LAST_DATE_ATTRIBUTE, "CDATA", toString(last));
		hd.startElement("","",DATE_STEPPER_TAG, atts);
		hd.endElement("","",DATE_STEPPER_TAG);
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
