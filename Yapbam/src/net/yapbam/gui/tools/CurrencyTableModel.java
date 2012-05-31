package net.yapbam.gui.tools;

import java.text.MessageFormat;
import java.util.Arrays;

import javax.swing.table.AbstractTableModel;

import net.yapbam.currency.CurrencyConverter;

@SuppressWarnings("serial")
public class CurrencyTableModel extends AbstractTableModel {
	private int currentCurrency;
	private CurrencyConverter converter;
	private String[] codes;
	
	public CurrencyTableModel(CurrencyConverter converter) {
		this.converter = converter;
		if (this.converter!=null) {
			this.codes = this.converter.getCurrencies();
			Arrays.sort(this.codes);
			this.currentCurrency = 0;
		} else {
			this.currentCurrency = -1;
		}
	}
	
	public void setCurrency(String currencyCode) {
		if (this.converter!=null) {
			this.currentCurrency = Arrays.binarySearch(this.codes, currencyCode);
			this.fireTableDataChanged();
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if ((columnIndex==2)||(columnIndex==3)) return Double.class;
		return String.class;
	}

	@Override
	public int getColumnCount() {
		if ((this.converter==null) || (this.codes.length==0)) return 0;
		else return 4;
	}

	@Override
	public int getRowCount() {
		return this.currentCurrency<0?0:this.codes.length-1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String currencyCode = this.codes[(rowIndex<this.currentCurrency)?rowIndex:rowIndex+1];
		if (columnIndex==0) return currencyCode;
		if (columnIndex==1) return CurrencyNames.getString(currencyCode);
		if (columnIndex==2) return this.converter.convert(1.0, this.codes[currentCurrency], currencyCode);
		if (columnIndex==3) return this.converter.convert(1.0, currencyCode, this.codes[currentCurrency]);
		throw new IllegalArgumentException();
	}

	@Override
	public String getColumnName(int column) {
		if (column==0) return Messages.getString("CurrencyConverterPanel.CodeColumnName"); //$NON-NLS-1$
		if (column==1) return Messages.getString("CurrencyConverterPanel.CurrencyColumnName"); //$NON-NLS-1$
		String currencyName = CurrencyNames.getString(this.codes[currentCurrency]);
		if (column==2) return MessageFormat.format(Messages.getString("CurrencyConverterPanel.ToCurrencyColumnName"), currencyName); //$NON-NLS-1$
		if (column==3) return MessageFormat.format(Messages.getString("CurrencyConverterPanel.CurrencyToColumnName"),currencyName); //$NON-NLS-1$
		return super.getColumnName(column);
	}
}
