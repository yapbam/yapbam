package net.yapbam.lineTitledTable;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LookAndFeel;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import java.awt.BorderLayout;
import java.awt.Dimension;

public class Test extends JPanel {
// Have a look at http://www.chka.de/swing/table/row-headers/
	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	/**
	 * This is the default constructor
	 */
	public Test() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setLayout(new BorderLayout());
		this.add(getJScrollPane(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane(getJTable());
			JTable rowHeader = new JTable(new TableModel() {
				
				@Override
				public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void removeTableModelListener(TableModelListener l) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public boolean isCellEditable(int rowIndex, int columnIndex) {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public Object getValueAt(int rowIndex, int columnIndex) {
					// TODO Auto-generated method stub
					return "title "+rowIndex;
				}
				
				@Override
				public int getRowCount() {
					// TODO Auto-generated method stub
					return getJTable().getRowCount();
				}
				
				@Override
				public String getColumnName(int columnIndex) {
					// TODO Auto-generated method stub
					return "Title";
				}
				
				@Override
				public int getColumnCount() {
					// TODO Auto-generated method stub
					return 1;
				}
				
				@Override
				public Class<?> getColumnClass(int columnIndex) {
					// TODO Auto-generated method stub
					return Object.class;
				}
				
				@Override
				public void addTableModelListener(TableModelListener l) {
					// TODO Auto-generated method stub
					
				}
			});
			jScrollPane.setRowHeaderView(rowHeader);
			jScrollPane.setViewportView(getJTable());
			LookAndFeel.installColorsAndFont (rowHeader, "TableHeader.background", "TableHeader.foreground", "TableHeader.font");
			
			Dimension d = rowHeader.getPreferredScrollableViewportSize();
			d.width = rowHeader.getPreferredSize().width;
			rowHeader.setPreferredScrollableViewportSize(d);
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTable() {
		if (jTable == null) {
			jTable = new JTable(new TableModel() {
				@Override
				public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void removeTableModelListener(TableModelListener l) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public boolean isCellEditable(int rowIndex, int columnIndex) {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public Object getValueAt(int rowIndex, int columnIndex) {
					// TODO Auto-generated method stub
					return "cell "+rowIndex+"x"+columnIndex;
				}
				
				@Override
				public int getRowCount() {
					// TODO Auto-generated method stub
					return 30;
				}
				
				@Override
				public String getColumnName(int columnIndex) {
					// TODO Auto-generated method stub
					return "column "+columnIndex;
				}
				
				@Override
				public int getColumnCount() {
					// TODO Auto-generated method stub
					return 5;
				}
				
				@Override
				public Class<?> getColumnClass(int columnIndex) {
					// TODO Auto-generated method stub
					return Object.class;
				}
				
				@Override
				public void addTableModelListener(TableModelListener l) {
					// TODO Auto-generated method stub
					
				}
			});
			jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}
		return jTable;
	}

}
