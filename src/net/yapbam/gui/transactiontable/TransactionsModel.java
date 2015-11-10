package net.yapbam.gui.transactiontable;

import net.yapbam.data.AbstractTransaction;

public interface TransactionsModel {
	AbstractTransaction getTransaction(int rowIndex);
}
