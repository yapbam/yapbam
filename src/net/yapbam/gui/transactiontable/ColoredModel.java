package net.yapbam.gui.transactiontable;

import java.awt.Component;

import javax.swing.JTable;

public interface ColoredModel {
	public abstract void setRowLook (Component renderer, JTable table, int row, boolean isSelected, boolean hasFocus);
	
	public abstract int getAlignment(int column);
}
