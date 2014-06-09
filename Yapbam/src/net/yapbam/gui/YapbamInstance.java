package net.yapbam.gui;

import java.awt.Window;

public interface YapbamInstance extends AccountSelector {
	public TransactionSelector getCurrentTransactionSelector();
	public Window getApplicationWindow();
}
