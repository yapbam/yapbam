package net.astesana.ajlib.swing.widget;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.astesana.ajlib.swing.framework.Application;
import net.astesana.ajlib.swing.framework.SafeJFileChooser;
import net.astesana.ajlib.swing.framework.Utils;
import net.astesana.ajlib.swing.table.RowHeaderRenderer;
import net.astesana.ajlib.swing.table.TitledRowsTableModel;
import net.astesana.ajlib.utilities.CSVExporter;

/** A widget with a JTable and a button that is able to save it in csv format.*/ 
public class ExcelPane extends JPanel {
	private static final long serialVersionUID = 1L;

	private JScrollPane scrollPane;
	private JButton button;
	private JTable table;
	private JTable rowView;

	private CSVExporter exporter;

	/**
	 * Create the panel.
	 */
	public ExcelPane() {
		exporter = new CSVExporter('\t',false);
		initialize();
	}
	
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{1.0, 0.0};
		gridBagLayout.columnWeights = new double[]{1.0};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		add(getScrollPane(), gbc_scrollPane);
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.insets = new Insets(0, 0, 5, 0);
		gbc_button.anchor = GridBagConstraints.EAST;
		gbc_button.gridx = 0;
		gbc_button.gridy = 1;
		add(getButton(), gbc_button);
	}

	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
	    scrollPane.setRowHeaderView(getRowView());
			scrollPane.setViewportView(getTable());
		}
		return scrollPane;
	}
	
	private JTable getRowView() {
		if (rowView==null) {
			rowView = new JTable();
			rowView.setDefaultRenderer(Object.class, new RowHeaderRenderer(true));
			LookAndFeel.installColorsAndFont (rowView, "TableHeader.background", "TableHeader.foreground", "TableHeader.font"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			setRowViewSize(rowView);
		}
		return rowView;
	}
	
	private void setRowViewSize(final JTable rowView) {
		int width = (rowView.getColumnCount()>0)?Utils.packColumn(rowView, 0, 2):0;
		Dimension d = rowView.getPreferredScrollableViewportSize();
		d.width = width;
		rowView.setPreferredScrollableViewportSize(d);
	}
	
	private JButton getButton() {
		if (button == null) {
			button = new JButton(Application.getString("ExcelPane.save")); //$NON-NLS-1$
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser chooser = new SafeJFileChooser(Application.getString("ExcelPane.dialog.title")); //$NON-NLS-1$
					File file = chooser.showSaveDialog(ExcelPane.this)==JFileChooser.APPROVE_OPTION?chooser.getSelectedFile():null;
					if (file!=null) {
						try {
							BufferedWriter writer = new BufferedWriter(new FileWriter(file));
							try {
								exporter.export(writer, table.getModel(), true);
							} finally {
								writer.close();
							}
						} catch(IOException ex) {
							String message = MessageFormat.format(Application.getString("ExcelPane.error.message"), ex.toString()); //$NON-NLS-1$
							JOptionPane.showMessageDialog(ExcelPane.this, message, Application.getString("ExcelPane.error.title"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
						}
					}
				}
			});
		}
		return button;
	}

	private JTable getTable() {
		if (table == null) {
			table = new JTable();
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}
		return table;
	}
	
	@SuppressWarnings("serial")
	public void setModel(TableModel model) {
		table.setModel(model);
		if (model instanceof TitledRowsTableModel) {
			TableModel rowHeaderModel = new AbstractTableModel() {
				@Override
				public Object getValueAt(int rowIndex, int columnIndex) {
					return ((TitledRowsTableModel)table.getModel()).getRowName(rowIndex);
				}
				
				@Override
				public int getRowCount() {
					return table.getModel().getRowCount();
				}
				
				@Override
				public int getColumnCount() {
					return 1;
				}
			};
			getRowView().setModel(rowHeaderModel);
			rowHeaderModel.addTableModelListener(new TableModelListener() {
				@Override
				public void tableChanged(TableModelEvent e) {
					setRowViewSize(rowView);
				}
			});
			setRowViewSize(getRowView());
		}
	}
	
	public void packTable() {
		Utils.packColumns(table, 2);
	}
	
	/** Gets the CSVExporter used to export the data.
	 * @return a CSVExporter
	 */
	public CSVExporter getCSVExporter() {
		return this.exporter;
	}
}