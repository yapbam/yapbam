package net.yapbam.data.xml;

import java.io.*;
import java.math.BigInteger;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.AccessControlException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import net.astesana.ajlib.utilities.FileUtils;
import net.astesana.ajlib.utilities.TextMatcher;
import net.yapbam.data.*;
import net.yapbam.date.helpers.DateStepper;
import net.yapbam.date.helpers.DayDateStepper;
import net.yapbam.date.helpers.DeferredValueDateComputer;
import net.yapbam.date.helpers.MonthDateStepper;
import net.yapbam.util.ArrayUtils;
import net.yapbam.util.Crypto;

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
	private static final boolean SLOW_WRITING = Boolean.getBoolean("slowDataWriting"); //$NON-NLS-1$

	private static final byte[] PASSWORD_ENCODED_FILE_HEADER;
	private static final byte[] MAGIC_ZIP_BYTES = new byte[]{0x50, 0x4B, 0x03, 0x04};

	private static final String EMPTY = ""; //$NON-NLS-1$
	private static final String CDATA = "CDATA"; //$NON-NLS-1$
	private static final String DATE_DELIM = "/"; //$NON-NLS-1$
	private static final String TRUE = "true"; //$NON-NLS-1$
	private static final String UTF8 = "UTF-8"; //$NON-NLS-1$

	static {
		// An ugly implementation due to problem with the compiler and the final
		// status and the fact the UnsupportedEncodingException may be thrown when
		// an encoding is specified
		String magicString = "<Yapbam password encoded file 1.0>"; //$NON-NLS-1$
		byte[] bytes;
		try {
			bytes = magicString.getBytes(UTF8);
		} catch (UnsupportedEncodingException e) {
			bytes = magicString.getBytes();
		}
		PASSWORD_ENCODED_FILE_HEADER = bytes;
	}

	static final String SUBCATEGORY_SEPARATOR_ATTRIBUTE = "subCategorySeparator"; //$NON-NLS-1$
	static final String NB_TRANSACTIONS_ATTRIBUTE = "nbTransactions"; //$NON-NLS-1$
	static final String STATEMENT_ATTRIBUTE = "statement"; //$NON-NLS-1$
	static final String VALUE_DATE_ATTRIBUTE = "valueDate"; //$NON-NLS-1$
	static final String CATEGORY_ATTRIBUTE = "category"; //$NON-NLS-1$
	static final String NUMBER_ATTRIBUTE = "number"; //$NON-NLS-1$
	static final String MODE_ATTRIBUTE = "mode"; //$NON-NLS-1$
	static final String AMOUNT_ATTRIBUTE = "amount"; //$NON-NLS-1$
	static final String DATE_ATTRIBUTE = "date"; //$NON-NLS-1$
	static final String DESCRIPTION_ATTRIBUTE = "description"; //$NON-NLS-1$
	static final String COMMENT_ATTRIBUTE = "comment"; //$NON-NLS-1$
	static final String ACCOUNT_ATTRIBUTE = "account"; //$NON-NLS-1$

	static final String DEBT_DAY_ATTRIBUTE = "debtDay"; //$NON-NLS-1$
	static final String STOP_DAY_ATTRIBUTE = "stopDay"; //$NON-NLS-1$
	static final String DAY_ATTRIBUTE = "day"; //$NON-NLS-1$
	static final String PERIOD_ATTRIBUTE = "period"; //$NON-NLS-1$
	static final String KIND_ATTRIBUTE = "kind"; //$NON-NLS-1$
	static final String IMMEDIATE_DATE_STEPPER_KIND = "immediate"; //$NON-NLS-1$
	static final String MONTHLY_DATE_STEPPER_KIND = "monthly"; //$NON-NLS-1$
	static final String DEFERRED_DATE_STEPPER_KIND = "deferred"; //$NON-NLS-1$
	static final String RELATIVE_DATE_STEPPER_KIND = "daily"; //$NON-NLS-1$
	static final String CHECKBOOK_ATTRIBUTE = "checkbook"; //$NON-NLS-1$
	
	static final String PREFIX_ATTRIBUTE = "prefix"; //$NON-NLS-1$
	static final String FIRST_NUMBER_ATTRIBUTE = "first"; //$NON-NLS-1$
	static final String SIZE_ATTRIBUTE = "size"; //$NON-NLS-1$
	static final String NEXT_NUMBER_ATTRIBUTE = "next"; //$NON-NLS-1$

	static final String INITIAL_BALANCE_ATTRIBUTE = "initialBalance"; //$NON-NLS-1$
	static final String ALERT_THRESHOLD_LESS = "alertThresholdLess"; //$NON-NLS-1$
	static final String ALERT_THRESHOLD_MORE = "alertThresholdMore"; //$NON-NLS-1$
	static final String ALERT_IGNORE = "no"; //$NON-NLS-1$
	static final String ID_ATTRIBUTE = "id"; //$NON-NLS-1$
	static final String NEXT_DATE_ATTRIBUTE = "next"; //$NON-NLS-1$
	static final String LAST_DATE_ATTRIBUTE = "last"; //$NON-NLS-1$
	static final String ENABLED_ATTRIBUTE = "enabled"; //$NON-NLS-1$

	static final String GLOBAL_DATA_TAG = "DATA"; //$NON-NLS-1$
	static final String CATEGORY_TAG = "CATEGORY"; //$NON-NLS-1$
	static final String ACCOUNT_TAG = "ACCOUNT"; //$NON-NLS-1$
	static final String MODE_TAG = "MODE"; //$NON-NLS-1$
	static final String CHECKBOOK_TAG = "CHECKBOOK"; //$NON-NLS-1$
	static final String EXPENSE_VDC_TAG = "EXPENSE"; //$NON-NLS-1$
	static final String RECEIPT_VDC_TAG = "RECEIPT"; //$NON-NLS-1$
	static final String PERIODICAL_TAG = "PERIODICAL"; //$NON-NLS-1$
	static final String DATE_STEPPER_TAG = "DATE_STEPPER"; //$NON-NLS-1$
	static final String TRANSACTION_TAG = "TRANSACTION"; //$NON-NLS-1$
	static final String SUBTRANSACTION_TAG = "SUBTRANSACTION"; //$NON-NLS-1$
	
	static final String FILTER_TAG = "FILTER"; //$NON-NLS-1$
	static final String FILTER_DATE_FROM_ATTRIBUTE = "dateFrom"; //$NON-NLS-1$
	static final String FILTER_DATE_TO_ATTRIBUTE = "dateTo"; //$NON-NLS-1$
	static final String FILTER_VALUE_DATE_FROM_ATTRIBUTE = "valueDateFrom"; //$NON-NLS-1$
	static final String FILTER_VALUE_DATE_TO_ATTRIBUTE = "valueDateTo"; //$NON-NLS-1$
	static final String FILTER_AMOUNT_FROM_ATTRIBUTE = "amountFrom"; //$NON-NLS-1$
	static final String FILTER_AMOUNT_TO_ATTRIBUTE = "amountTo"; //$NON-NLS-1$
	static final String FILTER_ATTRIBUTE = "filter"; //$NON-NLS-1$
	static final String FILTER_DESCRIPTION_ID = DESCRIPTION_ATTRIBUTE;
	static final String FILTER_COMMENT_ID = COMMENT_ATTRIBUTE;
	static final String FILTER_NUMBER_ID = NUMBER_ATTRIBUTE;
	static final String FILTER_STATEMENT_ID = STATEMENT_ATTRIBUTE;

	static final String TEXT_MATCHER_TAG = "TEXT_MATCHER"; //$NON-NLS-1$
	static final String CONTAINS = "contains"; //$NON-NLS-1$
	static final String EQUALS = "equals"; //$NON-NLS-1$
	static final String REGULAR = "regular"; //$NON-NLS-1$
	static final String DIACRITICAL_SENSITIVE_ATTRIBUTE = "diacriticalSensitive"; //$NON-NLS-1$
	static final String CASE_SENSITIVE_ATTRIBUTE = "caseSensitive"; //$NON-NLS-1$

	private AttributesImpl atts;
	private TransformerHandler hd;
	private OutputStream os;
	
	public static void write(GlobalData data, File file, boolean zipped, ProgressReport report) throws IOException {
		if (file.exists() && !file.canWrite()) throw new IOException("writing to "+file+" is not allowed"); //$NON-NLS-1$ //$NON-NLS-2$
		// Proceed safely, it means not to erase the old version until the new version is written
		// Everything here is pretty ugly.
		//TODO Implement this stuff using the transactional File access in Apache Commons (http://commons.apache.org/transaction/file/index.html)
		File writed = file.exists()?File.createTempFile("yapbam", "cpt"):file; //$NON-NLS-1$ //$NON-NLS-2$
		OutputStream out = new FileOutputStream(writed);
		try {
			ZipEntry entry = null;
			if (zipped) {
				out = new ZipOutputStream(out);
	      entry = new ZipEntry(file.getName());
				((ZipOutputStream)out).putNextEntry(entry);
			}
			write (data, out, report);
			if (out instanceof ZipOutputStream) {
				((ZipOutputStream) out).closeEntry();
			}
		} finally {
			out.close();
		}
		if (report!=null) report.setMax(-1);
		if (!file.equals(writed)) {
			// Ok, not so safe as I want since we could lost the file between deleting and renaming
			// but I can't find a better way
			if (!file.delete()) {
				writed.delete();
				throw new IOException(MessageFormat.format("Unable to delete previous version of {0}",file)); //LOCAL
			}
			FileUtils.move(writed, file);
		}
	}
	
	private static void write(GlobalData data, OutputStream os, ProgressReport report) throws IOException {
		Serializer serializer = new Serializer(data.getPassword(), os);
		serializer.serialize(data, report);
		serializer.closeDocument(data.getPassword());
	}

	/** Creates a new XML Serializer.
	 * <br>The serializer outputs the xml header. After all elements are output, you should call closedocument in order
	 * to close the xml document
	 * @param password The password used to protect the stream (null if no password protection).
	 * @param os The output stream on which to write the xml document
	 * @throws IOException if something wrong happens
	 * @see #closeDocument()
	 */
	public Serializer (String password, OutputStream os) throws IOException {
		try {
			this.os = os;
			if (password!=null) {
				// If the file has to be protected by a password
				// outputs the magic bytes that will allow Yapbam to recognize the file is crypted.
				this.os.write(PASSWORD_ENCODED_FILE_HEADER);
				// replace the output stream by a new encoded stream
				this.os = Crypto.getPasswordProtectedOutputStream(password, os);
			}
			StreamResult streamResult = new StreamResult(this.os);
			SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
			hd = tf.newTransformerHandler();
			Transformer serializer = hd.getTransformer();
			serializer.setOutputProperty(OutputKeys.ENCODING, UTF8);
			serializer.setOutputProperty(OutputKeys.INDENT,"yes"); //$NON-NLS-1$
			hd.setResult(streamResult);
			this.atts = new AttributesImpl();
			hd.startDocument();
		} catch (TransformerConfigurationException e) {
			throw new IOException(e);
		} catch (SAXException e) {
			throw new IOException(e);
		}
	}
	
	public void closeDocument(String password) throws IOException {
		try {
			hd.endDocument();
			if (password!=null) {
				((DeflaterOutputStream)this.os).finish();
			}
		} catch (SAXException e) {
			throw new IOException(e);
		}
	}

	/** Reads data from an uri.
	 * @param uri
	 * @param password
	 * @param report 
	 * @return The GlobalData that was located at the uri
	 * @throws IOException
	 * @throws AccessControlException
	 */
	public static GlobalData read(URI uri, String password, ProgressReport report) throws IOException, AccessControlException {
//		long start = System.currentTimeMillis();//TODO
		if (uri.getScheme().equals("file") || uri.getScheme().equals("ftp")) { //$NON-NLS-1$ //$NON-NLS-2$
			InputStream is = getUnzippedInputStream(uri);
			try {
				GlobalData redData = read(password, is, report);
				redData.setURI(uri);
				redData.setChanged(false);
				return redData;
			} finally {
				is.close();
			}
		} else {
			throw new IOException("Unsupported protocol: "+uri.getScheme()); //$NON-NLS-1$
		}
//		System.out.println ("Data read in "+(System.currentTimeMillis()-start)+"ms");//TODO
	}
	
	private static InputStream getUnzippedInputStream(URI uri) throws IOException {
		InputStream in = new BufferedInputStream(uri.toURL().openStream());
		in.mark(MAGIC_ZIP_BYTES.length);
		boolean isZipped = true;
		for (int i = 0; i < MAGIC_ZIP_BYTES.length-1; i++) {
			if (in.read()!=MAGIC_ZIP_BYTES[i]) {
				isZipped = false;
				break;
			}
		}
		in.reset();
		
		if (isZipped) {
			in = new ZipInputStream(in);
	    ((ZipInputStream)in).getNextEntry();
		}
		return in;
	}
	
	/** Tests whether a password is the right one for an uri.
	 * @param uri The URI containing Yapbam data
	 * @param password A password (null for no password)
	 * @throws IOException If an I/O error occurred
	 */
	public static boolean isPasswordOk(URI uri, String password) throws IOException {
		InputStream is = getUnzippedInputStream(uri);
		try {
			getDecryptedStream(password, is);
			return true;
		} catch (AccessControlException e) {
			return false;
		} finally {
			is.close();
		}
	}

	static GlobalData read(String password, InputStream is, ProgressReport report) throws IOException, AccessControlException {
		is = getDecryptedStream(password, is);
		GlobalData data = read(is, report);
		data.setPassword(password);
		return data;
	}

	/** Gets a stream to read in an encrypted stream.
	 * @param password The password used for the encryption, or null if stream is not encrypted.
	 * @param stream The encrypted (or not if password is null) stream.
	 * @return A new stream that automatically decrypt the original stream.
	 * @throws IOException
	 */
	public static InputStream getDecryptedStream(String password, InputStream stream) throws IOException, AccessControlException {
		if (password!=null) {
			// Pass the header
			for (int i = 0; i < PASSWORD_ENCODED_FILE_HEADER.length; i++) {
				stream.read();
			}
			// Create the decoder input stream
			stream = Crypto.getPasswordProtectedInputStream(password, stream);
		}
		return stream;
	}
	
	/** Gets the data about the uri (what is its version, is it encoded or not, etc...).
	 * @param uri the uniform resource locator to test.
	 * @return A SerializationData instance
	 * @throws IOException
	 */
	public static SerializationData getSerializationData(URI uri) throws IOException {
		InputStream in = getUnzippedInputStream(uri);
		try {
			boolean isEncoded = true;
			for (int i = 0; i < PASSWORD_ENCODED_FILE_HEADER.length-1; i++) {
				if (in.read()!=PASSWORD_ENCODED_FILE_HEADER[i]) {
					isEncoded = false;
					break;
				}
			}
			return new SerializationData(isEncoded);
		} finally {
			in.close();
		}
	}
	
	public static class SerializationData {
		private boolean isPasswordRequired;
		private SerializationData(boolean isEncoded) {
			this.isPasswordRequired = isEncoded;
		}
		public boolean isPasswordRequired() {
			return isPasswordRequired;
		}
	}

	private static GlobalData read(InputStream is, ProgressReport report) throws IOException {
		try {
			GlobalDataHandler dh = new GlobalDataHandler(report);
			SAXParserFactory.newInstance().newSAXParser().parse(is, dh);
			return dh.getData();
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	void serialize (GlobalData data, ProgressReport report) throws IOException {
		try {
			atts.clear();
			atts.addAttribute(EMPTY, EMPTY, "nbAccounts", CDATA, Integer.toString(data.getAccountsNumber())); //$NON-NLS-1$
			atts.addAttribute(EMPTY, EMPTY, "nbCategories", CDATA, Integer.toString(data.getCategoriesNumber())); //$NON-NLS-1$
			atts.addAttribute(EMPTY, EMPTY, SUBCATEGORY_SEPARATOR_ATTRIBUTE, CDATA, Character.toString(data.getSubCategorySeparator())); //$NON-NLS-1$
			atts.addAttribute(EMPTY, EMPTY, "nbPeriodicalTransactions", CDATA, Integer.toString(data.getPeriodicalTransactionsNumber())); //$NON-NLS-1$
			atts.addAttribute(EMPTY, EMPTY, NB_TRANSACTIONS_ATTRIBUTE, CDATA, Integer.toString(data.getTransactionsNumber()));
			hd.startElement(EMPTY,EMPTY,GLOBAL_DATA_TAG,atts);
			
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
			if (report!=null) report.setMax(data.getTransactionsNumber());
			//Transactions
			for (int i=0;i<data.getTransactionsNumber();i++) {
				if (SLOW_WRITING) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {}
				}
				serialize(data.getTransaction(i));
				if (report!=null) report.reportProgress(i+1);
			}
			hd.endElement(EMPTY,EMPTY,GLOBAL_DATA_TAG);
		} catch (SAXException e) {
			throw new IOException(e);
		}
	}
	
	public void serialize(Filter filter) throws SAXException {
		atts.clear();
		if (filter.getDateFrom()!=null) atts.addAttribute(EMPTY,EMPTY,FILTER_DATE_FROM_ATTRIBUTE,CDATA,toString(filter.getDateFrom()));
		if (filter.getDateTo()!=null) atts.addAttribute(EMPTY,EMPTY,FILTER_DATE_TO_ATTRIBUTE,CDATA,toString(filter.getDateTo()));
		if (filter.getValueDateTo()!=null) atts.addAttribute(EMPTY,EMPTY,FILTER_VALUE_DATE_TO_ATTRIBUTE,CDATA,toString(filter.getValueDateTo()));
		if (filter.getValueDateFrom()!=null) atts.addAttribute(EMPTY,EMPTY,FILTER_VALUE_DATE_FROM_ATTRIBUTE,CDATA,toString(filter.getValueDateFrom()));
		if (filter.getMinAmount()!=0.0) atts.addAttribute(EMPTY,EMPTY,FILTER_AMOUNT_FROM_ATTRIBUTE,CDATA,Double.toString(filter.getMinAmount()));
		if (filter.getMaxAmount()!=Double.POSITIVE_INFINITY) atts.addAttribute(EMPTY,EMPTY,FILTER_AMOUNT_TO_ATTRIBUTE,CDATA,Double.toString(filter.getMaxAmount()));
		List<Account> accounts = filter.getValidAccounts();
		if (accounts!=null) {
			String[] strings = new String[accounts.size()];
			for (int i = 0; i < strings.length; i++) {
				strings[i] = accounts.get(i).getName();
			}
			atts.addAttribute(EMPTY, EMPTY, ACCOUNT_ATTRIBUTE, CDATA, ArrayUtils.toString(strings));
		}
		List<String> modes = filter.getValidModes();
		if (modes!=null) {
			atts.addAttribute(EMPTY, EMPTY, MODE_ATTRIBUTE, CDATA, ArrayUtils.toString(modes.toArray(new String[modes.size()])));
		}
		List<Category> categories = filter.getValidCategories();
		if (categories!=null) {
			String[] strings = new String[categories.size()];
			for (int i = 0; i < strings.length; i++) {
				strings[i] = categories.get(i).equals(Category.UNDEFINED)?EMPTY:categories.get(i).getName();
			}
			atts.addAttribute(EMPTY, EMPTY, CATEGORY_ATTRIBUTE, CDATA, ArrayUtils.toString(strings));
		}
		int mask = 0;
		if (filter.isOk(Filter.RECEIPTS)) mask += Filter.RECEIPTS;
		if (filter.isOk(Filter.EXPENSES)) mask += Filter.EXPENSES;
		if (filter.isOk(Filter.CHECKED)) mask += Filter.CHECKED;
		if (filter.isOk(Filter.NOT_CHECKED)) mask += Filter.NOT_CHECKED;
		if (mask!=(Filter.ALL)) atts.addAttribute(EMPTY, EMPTY, FILTER_ATTRIBUTE, CDATA, Integer.toString(mask));
		hd.startElement(EMPTY, EMPTY, FILTER_TAG, atts);
		if (filter.getDescriptionMatcher()!=null) serialize(filter.getDescriptionMatcher(), FILTER_DESCRIPTION_ID);
		if (filter.getCommentMatcher()!=null) serialize(filter.getCommentMatcher(), FILTER_COMMENT_ID);
		if (filter.getNumberMatcher()!=null) serialize(filter.getDescriptionMatcher(), FILTER_NUMBER_ID);
		if (filter.getStatementMatcher()!=null) serialize(filter.getDescriptionMatcher(), FILTER_STATEMENT_ID);
		hd.endElement(EMPTY,EMPTY,FILTER_TAG);
	}

	private void serialize(TextMatcher matcher, String id) throws SAXException {
		atts.clear();
		atts.addAttribute(EMPTY, EMPTY, ID_ATTRIBUTE, CDATA, id);
		String kind = null;
		if (matcher.getKind().equals(TextMatcher.Kind.CONTAINS)) {
			kind = CONTAINS;
		} else if (matcher.getKind().equals(TextMatcher.Kind.EQUALS)) {
			kind = EQUALS;
		} else if (matcher.getKind().equals(TextMatcher.Kind.REGULAR)) {
			kind = REGULAR;
		} else {
			throw new IllegalArgumentException();
		}
		atts.addAttribute(EMPTY, EMPTY, KIND_ATTRIBUTE, CDATA, kind);
		atts.addAttribute(EMPTY, EMPTY, FILTER_ATTRIBUTE, CDATA, encode(matcher.getFilter()));
		if (matcher.isCaseSensitive()) atts.addAttribute(EMPTY, EMPTY, CASE_SENSITIVE_ATTRIBUTE, CDATA, TRUE);
		if (matcher.isDiacriticalSensitive()) atts.addAttribute(EMPTY, EMPTY, DIACRITICAL_SENSITIVE_ATTRIBUTE, CDATA, TRUE);
		hd.startElement(EMPTY,EMPTY,TEXT_MATCHER_TAG, atts);
		hd.endElement(EMPTY,EMPTY,TEXT_MATCHER_TAG);
	}
	
	public static String encode(String string) {
		if (string==null) return string;
		try {
			return URLEncoder.encode(string, UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String decode(String string) {
		if (string==null) return string;
		try {
			return URLDecoder.decode(string, UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	private void serialize(Account account) throws SAXException {
		atts.clear();
		atts.addAttribute(EMPTY,EMPTY,ID_ATTRIBUTE,CDATA,account.getName());
		atts.addAttribute(EMPTY,EMPTY,INITIAL_BALANCE_ATTRIBUTE,CDATA,Double.toString(account.getInitialBalance()));
		AlertThreshold alertThreshold = account.getAlertThreshold();
		if (!alertThreshold.equals(AlertThreshold.DEFAULT)) {
			atts.addAttribute(EMPTY, EMPTY, ALERT_THRESHOLD_LESS, CDATA, Double.toString(alertThreshold.getLessThreshold()));
			atts.addAttribute(EMPTY, EMPTY, ALERT_THRESHOLD_MORE, CDATA, Double.toString(alertThreshold.getMoreThreshold()));
		}
		
		hd.startElement(EMPTY,EMPTY,ACCOUNT_TAG,atts);
		for (int i = 0; i < account.getModesNumber(); i++) {
			Mode mode = account.getMode(i);
			if (!mode.equals(Mode.UNDEFINED)) serialize(mode);
		}
		for (int i = 0; i < account.getCheckbooksNumber(); i++) {
			serialize(account.getCheckbook(i));
		}
		hd.endElement(EMPTY,EMPTY,ACCOUNT_TAG);
	}
	
	private void serialize(Mode mode) throws SAXException {
		atts.clear();
		atts.addAttribute(EMPTY,EMPTY,ID_ATTRIBUTE,CDATA,mode.getName());
		if (mode.isUseCheckBook()) atts.addAttribute(EMPTY, EMPTY, CHECKBOOK_ATTRIBUTE, CDATA, TRUE);
		hd.startElement(EMPTY,EMPTY,MODE_TAG,atts);
		DateStepper expense = mode.getExpenseVdc();
		if (expense!=null) {
			setAttributes(expense);
			hd.startElement(EMPTY, EMPTY, EXPENSE_VDC_TAG, atts);
			hd.endElement(EMPTY, EMPTY, EXPENSE_VDC_TAG);
		}
		DateStepper receipt = mode.getReceiptVdc();
		if (receipt!=null) {
			setAttributes(receipt);
			hd.startElement(EMPTY, EMPTY, RECEIPT_VDC_TAG, atts);
			hd.endElement(EMPTY, EMPTY, RECEIPT_VDC_TAG);
		}
		hd.endElement(EMPTY,EMPTY,MODE_TAG);
	}

	private void serialize(Checkbook book) throws SAXException {
		atts.clear();
		atts.addAttribute(EMPTY,EMPTY,PREFIX_ATTRIBUTE,CDATA,book.getPrefix());
		atts.addAttribute(EMPTY,EMPTY,FIRST_NUMBER_ATTRIBUTE,CDATA,book.getFirst().toString());
		atts.addAttribute(EMPTY,EMPTY,SIZE_ATTRIBUTE,CDATA,Integer.toString(book.size()));
		if (!book.isEmpty()) atts.addAttribute(EMPTY, EMPTY, NEXT_NUMBER_ATTRIBUTE, CDATA, book.getFirst().add(BigInteger.valueOf(book.getUsed())).toString());
		hd.startElement(EMPTY,EMPTY,CHECKBOOK_TAG,atts);
		hd.endElement(EMPTY,EMPTY,CHECKBOOK_TAG);
	}

	private void setAttributes(DateStepper dateStepper) {
		atts.clear();
		String kind;
		if (dateStepper instanceof DayDateStepper) {
			kind = RELATIVE_DATE_STEPPER_KIND;
			atts.addAttribute(EMPTY, EMPTY, PERIOD_ATTRIBUTE, CDATA, Integer.toString(((DayDateStepper)dateStepper).getStep()));
		} else if (dateStepper instanceof DeferredValueDateComputer) {
			kind = DEFERRED_DATE_STEPPER_KIND;
			atts.addAttribute(EMPTY, EMPTY, STOP_DAY_ATTRIBUTE, CDATA, Integer.toString(((DeferredValueDateComputer)dateStepper).getStopDay()));
			atts.addAttribute(EMPTY, EMPTY, DEBT_DAY_ATTRIBUTE, CDATA, Integer.toString(((DeferredValueDateComputer)dateStepper).getDebtDay()));
		} else if (dateStepper.equals(DateStepper.IMMEDIATE)) {
			kind = IMMEDIATE_DATE_STEPPER_KIND;
		} else {
			throw new RuntimeException("Unsupported ValueDateComputer class : "+dateStepper.getClass().getName()); //$NON-NLS-1$
		}
		atts.addAttribute(EMPTY, EMPTY, KIND_ATTRIBUTE, CDATA, kind);
	}

	private void serialize(Category category) throws SAXException {
		if (!category.equals(Category.UNDEFINED)) {
			atts.clear();
			atts.addAttribute(EMPTY,EMPTY,ID_ATTRIBUTE,CDATA,category.getName());
			hd.startElement(EMPTY,EMPTY,CATEGORY_TAG,atts);
			hd.endElement(EMPTY,EMPTY,CATEGORY_TAG);
		}
	}
	
	private void serialize(Transaction transaction) throws SAXException {
		atts.clear();
		atts.addAttribute(EMPTY,EMPTY,ACCOUNT_ATTRIBUTE,CDATA,transaction.getAccount().getName());
		String description = transaction.getDescription();
		if (description!=null) atts.addAttribute(EMPTY,EMPTY,DESCRIPTION_ATTRIBUTE,CDATA,description);
		String comment = transaction.getComment();
		if (comment!=null) atts.addAttribute(EMPTY,EMPTY,COMMENT_ATTRIBUTE,CDATA,comment);
		atts.addAttribute(EMPTY,EMPTY,DATE_ATTRIBUTE,CDATA,toString(transaction.getDate()));
		atts.addAttribute(EMPTY,EMPTY,AMOUNT_ATTRIBUTE,CDATA,Double.toString(transaction.getAmount()));
		Mode mode = transaction.getMode();
		if (!mode.equals(Mode.UNDEFINED)) atts.addAttribute(EMPTY,EMPTY,MODE_ATTRIBUTE,CDATA,mode.getName());
		String number = transaction.getNumber();
		if ((number!=null) && (number.length()>0)) atts.addAttribute(EMPTY,EMPTY,NUMBER_ATTRIBUTE,CDATA,number);
		Category category = transaction.getCategory();
		if (!category.equals(Category.UNDEFINED)) atts.addAttribute(EMPTY,EMPTY,CATEGORY_ATTRIBUTE,CDATA,category.getName());
		atts.addAttribute(EMPTY,EMPTY,VALUE_DATE_ATTRIBUTE,CDATA,toString(transaction.getValueDate()));
		String statement = transaction.getStatement();
		if (statement!=null) atts.addAttribute(EMPTY,EMPTY,STATEMENT_ATTRIBUTE,CDATA,statement);
		hd.startElement(EMPTY,EMPTY,TRANSACTION_TAG,atts);
		for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
			serialize(transaction.getSubTransaction(i));
		}
		hd.endElement(EMPTY,EMPTY,TRANSACTION_TAG);
	}

	private void serialize(SubTransaction subTransaction) throws SAXException {
		atts.clear();
		atts.addAttribute(EMPTY, EMPTY, DESCRIPTION_ATTRIBUTE, CDATA, subTransaction.getDescription());
		atts.addAttribute(EMPTY, EMPTY, AMOUNT_ATTRIBUTE, CDATA, Double.toString(subTransaction.getAmount()));
		Category category = subTransaction.getCategory();
		if (!category.equals(Category.UNDEFINED)) atts.addAttribute(EMPTY, EMPTY, CATEGORY_ATTRIBUTE, CDATA,category.getName());
		hd.startElement(EMPTY,EMPTY,SUBTRANSACTION_TAG,atts);
		hd.endElement(EMPTY,EMPTY,SUBTRANSACTION_TAG);
	}
	
	private void serialize(PeriodicalTransaction periodicalTransaction) throws SAXException {
		atts.clear();
		atts.addAttribute(EMPTY,EMPTY,ACCOUNT_ATTRIBUTE,CDATA,periodicalTransaction.getAccount().getName());
		String description = periodicalTransaction.getDescription();
		if (description!=null) atts.addAttribute(EMPTY,EMPTY,DESCRIPTION_ATTRIBUTE,CDATA,description);
		String comment = periodicalTransaction.getComment();
		if (comment!=null) atts.addAttribute(EMPTY,EMPTY,COMMENT_ATTRIBUTE,CDATA,comment);
		atts.addAttribute(EMPTY,EMPTY,AMOUNT_ATTRIBUTE,CDATA,Double.toString(periodicalTransaction.getAmount()));
		Mode mode = periodicalTransaction.getMode();
		if (!mode.equals(Mode.UNDEFINED)) atts.addAttribute(EMPTY,EMPTY,MODE_ATTRIBUTE,CDATA,mode.getName());
		Category category = periodicalTransaction.getCategory();
		if (!category.equals(Category.UNDEFINED)) atts.addAttribute(EMPTY,EMPTY,CATEGORY_ATTRIBUTE,CDATA,category.getName());
		atts.addAttribute(EMPTY,EMPTY,ENABLED_ATTRIBUTE,CDATA,Boolean.toString(periodicalTransaction.isEnabled()));
		Date nextDate = periodicalTransaction.getNextDate();
		if (nextDate!=null) atts.addAttribute(EMPTY,EMPTY,NEXT_DATE_ATTRIBUTE,CDATA,toString(nextDate));
		hd.startElement(EMPTY,EMPTY,PERIODICAL_TAG,atts);
		DateStepper nextDateBuilder = periodicalTransaction.getNextDateBuilder();
		if (nextDateBuilder!=null) serialize(nextDateBuilder);
		for (int i = 0; i < periodicalTransaction.getSubTransactionSize(); i++) {
			serialize(periodicalTransaction.getSubTransaction(i));
		}
		hd.endElement(EMPTY,EMPTY,PERIODICAL_TAG);
	}

	private void serialize(DateStepper stepper) throws SAXException {
		if (stepper instanceof MonthDateStepper) {
			MonthDateStepper mds = (MonthDateStepper) stepper;
			atts.clear();
			atts.addAttribute(EMPTY, EMPTY, KIND_ATTRIBUTE, CDATA, MONTHLY_DATE_STEPPER_KIND);
			atts.addAttribute(EMPTY, EMPTY, PERIOD_ATTRIBUTE, CDATA, Integer.toString(mds.getPeriod()));
			atts.addAttribute(EMPTY, EMPTY, DAY_ATTRIBUTE, CDATA, Integer.toString(mds.getDay()));
			Date last = mds.getLastDate();
			if (last!=null) atts.addAttribute(EMPTY, EMPTY, LAST_DATE_ATTRIBUTE, CDATA, toString(last));
			hd.startElement(EMPTY,EMPTY,DATE_STEPPER_TAG, atts);
			hd.endElement(EMPTY,EMPTY,DATE_STEPPER_TAG);
		} else if (stepper instanceof DayDateStepper) {
			DayDateStepper dds = (DayDateStepper) stepper;
			atts.addAttribute(EMPTY, EMPTY, KIND_ATTRIBUTE, CDATA, RELATIVE_DATE_STEPPER_KIND);
			atts.addAttribute(EMPTY, EMPTY, PERIOD_ATTRIBUTE, CDATA, Integer.toString(dds.getStep()));
			Date last = dds.getLastDate();
			if (last!=null) atts.addAttribute(EMPTY, EMPTY, LAST_DATE_ATTRIBUTE, CDATA, toString(last));
			hd.startElement(EMPTY,EMPTY,DATE_STEPPER_TAG, atts);
			hd.endElement(EMPTY,EMPTY,DATE_STEPPER_TAG);
			atts.clear();
		} else throw new IllegalArgumentException("This stepper class is not supported : "+stepper.getClass()); //$NON-NLS-1$
	}

	@SuppressWarnings("deprecation")
	private String toString(Date date) {
		int month = date.getMonth()+1;
		int year = date.getYear()+1900;
		return year + DATE_DELIM + month + DATE_DELIM + date.getDate();
	}
	
	@SuppressWarnings("deprecation")
	static Date toDate(String value) {
		if (value==null) return null;
		StringTokenizer tokens = new StringTokenizer(value,DATE_DELIM);
		int year = Integer.parseInt(tokens.nextToken())-1900;
		int month = Integer.parseInt(tokens.nextToken())-1;
		int day = Integer.parseInt(tokens.nextToken());
		return new Date(year, month, day);
	}
}
