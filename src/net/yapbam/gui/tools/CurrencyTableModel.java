package net.yapbam.gui.tools;

import java.text.MessageFormat;

import javax.swing.table.AbstractTableModel;

import net.yapbam.currency.CurrencyConverter;
import net.yapbam.currency.CurrencyNames;

@SuppressWarnings("serial")
public class CurrencyTableModel extends AbstractTableModel {
	private int currentCurrency;
	private CurrencyConverter converter;
	private String[] codes;
	
	public CurrencyTableModel(CurrencyConverter converter, String[] codes) {
		this.converter = converter;
		if (this.converter!=null) {
			this.codes = codes;
			this.currentCurrency = 0;
		} else {
			this.currentCurrency = -1;
		}
	}
	
	public void setCurrency(String currencyCode) {
		if (this.converter!=null) {
			this.currentCurrency = -1;
			for (int i = 0; i < this.codes.length; i++) {
				if (currencyCode.equals(this.codes[i])) {
					this.currentCurrency = i;
					break;
				}
			}
			this.fireTableDataChanged();
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if ((columnIndex==2)||(columnIndex==3)) {
			return Double.class;
		}
		return String.class;
	}

	@Override
	public int getColumnCount() {
		if ((this.converter==null) || (this.codes.length==0)) {
			return 0;
		} else {
			return 4;
		}
	}

	@Override
	public int getRowCount() {
		return this.currentCurrency<0?0:this.codes.length-1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String currencyCode = getCode(rowIndex);
		if (columnIndex==0) {
			return CurrencyNames.get(currencyCode);
		} else if (columnIndex==1) {
			return currencyCode;
		} else if (columnIndex==2) {
			return this.converter.convert(1.0, this.codes[currentCurrency], currencyCode);
		} else if (columnIndex==3) {
			return this.converter.convert(1.0, currencyCode, this.codes[currentCurrency]);
		}
		throw new IllegalArgumentException();
	}

	@Override
	public String getColumnName(int column) {
		if (column==0) {
			return Messages.getString("CurrencyConverterPanel.CurrencyColumnName"); //$NON-NLS-1$
		} else if (column==1) {
			return Messages.getString("CurrencyConverterPanel.CodeColumnName"); //$NON-NLS-1$
		}
		String currencyName = CurrencyNames.get(this.codes[currentCurrency]);
		if (column==2) {
			return MessageFormat.format(Messages.getString("CurrencyConverterPanel.ToCurrencyColumnName"), currencyName); //$NON-NLS-1$
		} else if (column==3) {
			return MessageFormat.format(Messages.getString("CurrencyConverterPanel.CurrencyToColumnName"),currencyName); //$NON-NLS-1$
		} else {
			return super.getColumnName(column);
		}
	}

	public String getCode(int rowIndex) {
		return this.codes[(rowIndex<this.currentCurrency)?rowIndex:rowIndex+1];
	}

	public int indexOf(String currencyCode) {
		for (int i = 0; i < getRowCount(); i++) {
			if (currencyCode.equals(getValueAt(i, 1))) {
				return i; 
			}
		}
		return -1;
	}
}
