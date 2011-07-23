package net.yapbam.data.xml;

import java.util.ArrayList;
import java.util.Date;

import net.yapbam.data.*;
import net.yapbam.util.ArrayUtils;
import net.yapbam.util.TextMatcher;
import net.yapbam.util.TextMatcher.Kind;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class FilterHandler extends DefaultHandler {
	private GlobalData data;
	private Filter filter;
	private TextMatcher descriptionMatcher;
	private TextMatcher numberMatcher;
	private TextMatcher statementMatcher;
	private int property;
	

	public FilterHandler(GlobalData data) {
		super();
		this.data = data;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equals(Serializer.FILTER_TAG)) {
			filter = new Filter();
			Date dateFrom = Serializer.toDate(attributes.getValue(Serializer.FILTER_DATE_FROM_ATTRIBUTE));
			Date dateTo = Serializer.toDate(attributes.getValue(Serializer.FILTER_DATE_TO_ATTRIBUTE));
			filter.setDateFilter(dateFrom, dateTo);
			Date valueDateFrom = Serializer.toDate(attributes.getValue(Serializer.FILTER_VALUE_DATE_FROM_ATTRIBUTE));
			Date valueDateTo = Serializer.toDate(attributes.getValue(Serializer.FILTER_VALUE_DATE_TO_ATTRIBUTE));
			filter.setValueDateFilter(valueDateFrom, valueDateTo);
			String amountFrom = attributes.getValue(Serializer.FILTER_AMOUNT_FROM_ATTRIBUTE);
			String amountTo = attributes.getValue(Serializer.FILTER_AMOUNT_TO_ATTRIBUTE);
			String filterString = attributes.getValue(Serializer.FILTER_ATTRIBUTE);
			property = filterString==null?Filter.ALL:Integer.parseInt(filterString);
			filter.setAmountFilter(property, amountFrom==null?0.0:Double.parseDouble(amountFrom), amountFrom==null?Double.POSITIVE_INFINITY:Double.parseDouble(amountTo));
			String accountsString = attributes.getValue(Serializer.ACCOUNT_ATTRIBUTE);
			if (accountsString!= null) {
				String[] names = ArrayUtils.parseStringArray(accountsString);
				ArrayList<Account> accounts = new ArrayList<Account>();
				for (String name: names) {
					Account account = data.getAccount(name);
					if (account != null) accounts.add(account);
				}
				if ((accounts.size()!=0) && (accounts.size()!=data.getAccountsNumber())) filter.setValidAccounts(accounts);
			}
		} else if (qName.equals(Serializer.TEXT_MATCHER_TAG)) {
			String id = attributes.getValue(Serializer.ID_ATTRIBUTE);
			String kindString = attributes.getValue(Serializer.KIND_ATTRIBUTE);
			Kind kind = null;
			if (kindString.equals(Serializer.CONTAINS)) {
				kind = TextMatcher.CONTAINS;
			} else if (kindString.equals(Serializer.EQUALS)) {
				kind = TextMatcher.EQUALS;
			} else if (kindString.equals(Serializer.REGULAR)) {
				kind = TextMatcher.REGULAR;
			}
			String filter = Serializer.decode(attributes.getValue(Serializer.FILTER_ATTRIBUTE));
			String bString = attributes.getValue(Serializer.CASE_SENSITIVE_ATTRIBUTE);
			boolean caseSensitive = bString==null?false:Boolean.parseBoolean(bString);
			bString = attributes.getValue(Serializer.DIACRITICAL_SENSITIVE_ATTRIBUTE);
			boolean diacriticalSensitive = bString==null?false:Boolean.parseBoolean(bString);
			TextMatcher textMatcher = new TextMatcher(kind, filter, caseSensitive, diacriticalSensitive);
			if (id.equals(Serializer.FILTER_DESCRIPTION_ID)) {
				this.descriptionMatcher = textMatcher;
			} else if (id.equals(Serializer.FILTER_NUMBER_ID)) {
				this.numberMatcher = textMatcher;
			} else if (id.equals(Serializer.FILTER_STATEMENT_ID)) {
				this.statementMatcher = textMatcher;
			}
		} else {
			throw new IllegalArgumentException ("Unknown tag "+qName);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equals(Serializer.FILTER_TAG)) {
			filter.setDescriptionMatcher(this.descriptionMatcher);
			filter.setNumberMatcher(this.numberMatcher);
			filter.setStatementFilter(property, this.statementMatcher);
		} else if (qName.equals(Serializer.TEXT_MATCHER_TAG)) {
		} else {
			System.err.println ("Unknown tag "+qName);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String str = new String(ch, start, length);
		str = str.trim();
		if (str.length()!=0) {
			System.err.println ("strange, characters is called : "+str);
		}
	}

	public Filter getFilter() {
		return filter;
	}
}
