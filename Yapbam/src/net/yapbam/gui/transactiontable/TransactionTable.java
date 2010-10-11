package net.yapbam.gui.transactiontable;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;
import java.util.Comparator;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableRowSorter;

import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;
import net.yapbam.gui.MainMenuBar;
import net.yapbam.gui.util.XTableColumnModel;

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
		
		TransactionsTableModel model = new TransactionsTableModel(this, data);
		this.setModel(model);
		this.setDefaultRenderer(Date.class, new DateRenderer());
		this.setDefaultRenderer(double[].class, new AmountRenderer());
		this.setDefaultRenderer(SpreadState.class, new SpreadStateRenderer());
		this.setDefaultRenderer(Object.class, new ObjectRenderer());
		this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.addMouseListener(new SpreadableMouseAdapter());
		TableRowSorter<TransactionsTableModel> sorter = new TableRowSorter<TransactionsTableModel>(model);
		sorter.setComparator(4, new Comparator<double[]>() {
			@Override
			public int compare(double[] o1, double[] o2) {
				return (int) Math.signum(o1[0]-o2[0]);
			}
		});
		this.setRowSorter(sorter);
		this.setColumnModel(new XTableColumnModel());
		this.createDefaultColumnsFromModel();
	}

	public Transaction getSelectedTransaction() {
		int index = getSelectedRow();
		return index < 0 ? null : data.getTransaction(this.convertRowIndexToModel(index));
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
	
	boolean isColumnVisible(int index) {
		XTableColumnModel model = (XTableColumnModel)getColumnModel();
		return model.isColumnVisible(model.getColumnByModelIndex(index));
	}
	
	void setColumnVisible(int index, boolean visible) {
		XTableColumnModel model = (XTableColumnModel)getColumnModel();
		model.setColumnVisible(model.getColumnByModelIndex(index), visible);		
	}

	public int getColumnCount(boolean onlyVisible) {
		XTableColumnModel model = (XTableColumnModel)getColumnModel();
		return model.getColumnCount(onlyVisible);
	}
	
	public String getColumnName (int index, boolean onlyVisible) {
		XTableColumnModel model = (XTableColumnModel)getColumnModel();
		if (onlyVisible) {
			return getColumnName(index);
		} else {
			return this.getModel().getColumnName(model.getColumn(index, onlyVisible).getModelIndex());
		}
	}
}