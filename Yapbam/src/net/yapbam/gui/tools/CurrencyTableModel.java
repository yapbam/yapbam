package net.yapbam.gui.tools;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Arrays;

import javax.swing.table.AbstractTableModel;

import net.yapbam.currency.CurrencyConverter;

@SuppressWarnings("serial")
public class CurrencyTableModel extends AbstractTableModel {
	private int currentCurrency;
	private String[] codes;
	
	public CurrencyTableModel(String currencyCode) throws IOException, ParseException {
		this.codes = CurrencyConverter.getInstance().getCurrencies();
		Arrays.sort(this.codes);
		this.currentCurrency = Arrays.binarySearch(this.codes, currencyCode);
	}
	
	public void setCurrency(String currencyCode) {
		this.currentCurrency = Arrays.binarySearch(this.codes, currencyCode);
		this.fireTableStructureChanged();
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public int getRowCount() {
		return this.currentCurrency<0?0:this.codes.length-1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String currencyCode = this.codes[(rowIndex<this.currentCurrency)?rowIndex:rowIndex+1];
		if (columnIndex==0) return CurrencyNames.getString(currencyCode);
		try {
			if (columnIndex==1) return CurrencyConverter.getInstance().convert(1.0, this.codes[currentCurrency], currencyCode);
			if (columnIndex==2) return CurrencyConverter.getInstance().convert(1.0, currencyCode, this.codes[currentCurrency]);
			throw new IllegalArgumentException();
		} catch (IOException e) {
			return "?"; //$NON-NLS-1$
		} catch (ParseException e) {
			return "!"; //$NON-NLS-1$
		}
	}

	@Override
	public String getColumnName(int column) {
		if (column==0) return Messages.getString("CurrencyConverterPanel.CurrencyColumnName"); //$NON-NLS-1$
		String currencyName = CurrencyNames.getString(this.codes[currentCurrency]);
		if (column==1) return MessageFormat.format(Messages.getString("CurrencyConverterPanel.ToCurrencyColumnName"), currencyName); //$NON-NLS-1$
		if (column==2) return MessageFormat.format(Messages.getString("CurrencyConverterPanel.CurrencyToColumnName"),currencyName); //$NON-NLS-1$
		return super.getColumnName(column);
	}
}
