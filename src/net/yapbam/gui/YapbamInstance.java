package net.yapbam.gui;

import java.awt.Window;

import net.yapbam.gui.actions.TransactionSelector;

public interface YapbamInstance {
	public TransactionSelector getCurrentTransactionSelector();
	public Window getApplicationWindow();
}
