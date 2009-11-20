package net.yapbam.gui.transactiontable;

import java.awt.Color;
import java.awt.Component;
import java.util.HashSet;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import net.yapbam.data.AbstractTransaction;

@SuppressWarnings("serial")
public abstract class GenericTransactionTableModel extends AbstractTableModel implements SpreadableTableModel, ColoredModel {
	static Color CASHIN = new Color(240,255,240);
	static Color CASHOUT = new Color(255,240,240);
	private HashSet<Long> spreadTransactionId;

	protected GenericTransactionTableModel() {
		this.spreadTransactionId = new HashSet<Long>();
	}
	
	@Override
	public void setRowLook(Component renderer, JTable table, int row, boolean isSelected, boolean hasFocus) {
		if (isSelected) {
	        renderer.setBackground(table.getSelectionBackground());
	        renderer.setForeground(table.getSelectionForeground());
	    } else {
	        boolean expense = this.getTransaction(row).getAmount()<0;  	
	        renderer.setForeground(table.getForeground());
	        renderer.setBackground(expense?CASHOUT:CASHIN);
	    }
	}

	@Override
	public boolean isSpreadable(int row) {
		return this.getTransaction(row).getSubTransactionSize()>0;
	}

	@Override
	public boolean isSpread(int rowIndex) {
		return spreadTransactionId.contains(this.getTransaction(rowIndex).getId());
	}

	@Override
	public void setSpread(int rowIndex, boolean spread) {
		this.setSpread(this.getTransaction(rowIndex), spread);
	}

	@Override
	public int getSpreadColumnNumber() {
		return 0;
	}

	@Override
	public int getSpreadLines(int row) {
		int lines = this.getTransaction(row).getSubTransactionSize()+1;
		if (getTransaction(row).getComplement()!=0) lines++;
		return lines;
	}
	
	protected void clearSpreadData() {
		this.spreadTransactionId.clear();
	}
	
	protected void setSpread (AbstractTransaction transaction, boolean spread) {
		if (spread) {
			this.spreadTransactionId.add(transaction.getId());
		} else {
			this.spreadTransactionId.remove(transaction.getId());
		}
	}
	
	protected abstract AbstractTransaction getTransaction(int rowIndex);
}