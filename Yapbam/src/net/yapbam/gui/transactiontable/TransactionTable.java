package net.yapbam.gui.transactiontable;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;
import net.yapbam.gui.MainMenuBar;

public class TransactionTable extends JTable {
	private static final long serialVersionUID = 1L;
	private static final Cursor CHECK_CURSOR;
	
	private FilteredData data;

	static {
	    URL imgURL = MainMenuBar.class.getResource("images/checkCursor.png"); //$NON-NLS-1$
	    Toolkit toolkit = Toolkit.getDefaultToolkit();
		CHECK_CURSOR = toolkit.createCustomCursor(toolkit.getImage(imgURL), new Point(5, 13), "checked"); //$NON-NLS-1$
	}

	public TransactionTable(FilteredData data) {
		super();
		this.data = data;
		
		this.setModel(new TransactionsTableModel(this, data));
		this.setDefaultRenderer(Date.class, new DateRenderer());
		this.setDefaultRenderer(double[].class, new AmountRenderer());
		this.setDefaultRenderer(SpreadState.class, new SpreadStateRenderer());
		this.setDefaultRenderer(Object.class, new ObjectRenderer());
		this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.addMouseListener(new SpreadableMouseAdapter());
	}

	public Transaction getSelectedTransaction() {
		int index = getSelectionModel().getMinSelectionIndex();
		return index < 0 ? null : data.getTransaction(index);
	}

	public void setCheckMode(boolean checkMode) {
		Cursor cursor = checkMode ? CHECK_CURSOR:Cursor.getDefaultCursor();
		setCursor(cursor);
	}
	
	public GlobalData getGlobalData() {
		return data.getGlobalData();
	}
	
	FilteredData getFilteredData() {
		return data;
	}
}