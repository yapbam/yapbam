package net.yapbam.gui.tools;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.ListSelectionModel;

import java.awt.GridBagConstraints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Currency;

import net.astesana.ajlib.swing.Utils;
import net.astesana.ajlib.swing.table.RowSorter;
import net.astesana.ajlib.swing.widget.CurrencyWidget;
import net.yapbam.currency.CurrencyConverter;
import net.yapbam.gui.LocalizationData;

import javax.swing.JLabel;
import java.awt.Insets;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

public class CurrencyConverterPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JComboBox currency1 = null;
	private JComboBox currency2 = null;
	private CurrencyWidget amount1 = null;
	private CurrencyWidget amount2 = null;
	private JLabel jLabel = null;
	
	private CurrencyConverter converter;
	private String[] codes;
	private JLabel title = null;
	private JLabel errField = null;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private CurrencyTableModel tableModel;
	
	/**
	 * This is the default constructor
	 */
	public CurrencyConverterPanel(CurrencyConverter converter) {
		this.converter = converter;
		if (this.converter!=null) {
			this.codes = this.converter.getCurrencies();
			// Sort the codes accordingly to their wordings
			Arrays.sort(this.codes, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					String w1 = CurrencyNames.getString(o1);
					String w2 = CurrencyNames.getString(o2);
					return w1.compareToIgnoreCase(w2);
				}
			});
		}
		initialize();
		if (this.converter!=null) {
			Currency currency = Currency.getInstance(LocalizationData.getLocale());
			int index = Arrays.asList(this.codes).indexOf(currency.getCurrencyCode());
			if (index>=0) {
				getCurrency1().setSelectedIndex(index);
				getCurrency2().setSelectedIndex(index);
				String title = MessageFormat.format(Messages.getString("CurrencyConverterPanel.topMessage"), this.converter.getReferenceDate()); //$NON-NLS-1$
				this.title.setText(title);
			} else {
				//The locale is unknown
				this.title.setText("?"); //TODO
			}
		}
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.fill = GridBagConstraints.BOTH;
		gridBagConstraints12.weighty = 1.0;
		gridBagConstraints12.gridwidth = 0;
		gridBagConstraints12.gridx = 0;
		gridBagConstraints12.gridy = 3;
		gridBagConstraints12.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints12.weightx = 1.0;
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.anchor = GridBagConstraints.WEST;
		gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints6.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints6.gridwidth = 0;
		gridBagConstraints6.gridy = 2;
		getErrField();
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.gridy = 0;
		gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints4.anchor = GridBagConstraints.WEST;
		gridBagConstraints4.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints4.gridwidth = GridBagConstraints.REMAINDER;
		title = new JLabel();
		title.setText(""); //$NON-NLS-1$
		GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
		gridBagConstraints31.gridx = 2;
		gridBagConstraints31.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints31.gridy = 1;
		jLabel = new JLabel();
		jLabel.setText("="); //$NON-NLS-1$
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints21.gridy = 1;
		gridBagConstraints21.weightx = 1.0;
		gridBagConstraints21.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints21.gridx = 3;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints11.gridy = 1;
		gridBagConstraints11.weightx = 1.0;
		gridBagConstraints11.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints11.gridx = 0;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints1.gridy = 1;
		gridBagConstraints1.weightx = 0.0D;
		gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints1.gridx = 4;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridx = 0;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints3.gridy = 0;
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.gridx = 2;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.weightx = 0.0D;
		gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints2.gridx = 1;
		this.setSize(1000, 400);
		this.setLayout(new GridBagLayout());
		this.add(getCurrency1(), gridBagConstraints2);
		this.add(getCurrency2(), gridBagConstraints1);
		this.add(getAmount1(), gridBagConstraints11);
		this.add(getAmount2(), gridBagConstraints21);
		this.add(jLabel, gridBagConstraints31);
		this.add(title, gridBagConstraints4);
		this.add(errField, gridBagConstraints6);
		this.add(getJScrollPane(), gridBagConstraints12);
	}

	private JLabel getErrField() {
		if (errField==null) {
			errField = new JLabel();
			errField.setForeground(new Color(255, 64, 64));
		}
		return this.errField;
	}

	/**
	 * This method initializes currency1	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCurrency1() {
		if (currency1 == null) {
			currency1 = new JComboBox();
			currency1.setToolTipText(Messages.getString("CurrencyConverterPanel.origin.toolTip")); //$NON-NLS-1$
			if (codes!=null) {
				for (int i = 0; i < codes.length; i++) {
					String symbol = CurrencyNames.getString(this.codes[i]);
					currency1.addItem(symbol);
				}
			}
			currency1.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					amount1.setCurrency(Currency.getInstance(codes[currency1.getSelectedIndex()]));
					if (tableModel!=null) tableModel.setCurrency(codes[currency1.getSelectedIndex()]);
					doConvert();
				}
			});
		}
		return currency1;
	}

	/**
	 * This method initializes currency2	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCurrency2() {
		if (currency2 == null) {
			currency2 = new JComboBox();
			currency2.setToolTipText(Messages.getString("CurrencyConverterPanel.destination.toolTip")); //$NON-NLS-1$
			if (codes!=null) {
				for (int i = 0; i < codes.length; i++) {
					String symbol = CurrencyNames.getString(this.codes[i]);
					currency2.addItem(symbol);
				}
			}
			currency2.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					amount2.setCurrency(Currency.getInstance(codes[currency2.getSelectedIndex()]));
					doConvert();
				}
			});
		}
		return currency2;
	}

	/**
	 * This method initializes amount1	
	 * 	
	 * @return net.yapbam.gui.widget.AmountWidget	
	 */
	private CurrencyWidget getAmount1() {
		if (amount1 == null) {
			amount1 = new CurrencyWidget(LocalizationData.getLocale());
			amount1.setColumns(10);
			amount1.setToolTipText(Messages.getString("CurrencyConverterPanel.amount.toolTip")); //$NON-NLS-1$
			amount1.setValue(0.0);
			amount1.addPropertyChangeListener(CurrencyWidget.VALUE_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					doConvert();
				}
			});
		}
		return amount1;
	}

	/**
	 * This method initializes amount2	
	 * 	
	 * @return net.yapbam.gui.widget.AmountWidget	
	 */
	private CurrencyWidget getAmount2() {
		if (amount2 == null) {
			amount2 = new CurrencyWidget(LocalizationData.getLocale());
			amount2.setColumns(10);
			amount2.setToolTipText(Messages.getString("CurrencyConverterPanel.result.toolTip")); //$NON-NLS-1$
			amount2.setEditable(false);
		}
		return amount2;
	}

	private void doConvert() {
		Double value = amount1.getValue();
		if ((value!=null) && (codes!=null)) {
			String from = codes[currency1.getSelectedIndex()];
			String to = codes[currency2.getSelectedIndex()];
			amount2.setValue(this.converter.convert(value, from, to));
		}
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTable());
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
			tableModel = new CurrencyTableModel(this.converter, this.codes);
			jTable = new JTable(tableModel);
			getJTable().setRowSorter(new RowSorter<TableModel>(getJTable().getModel()));
			getJTable().setDefaultRenderer(Double.class, new ConversionRateRenderer());
			Utils.packColumns(jTable, 2);
			getJTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			getJTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						int selectedRow = getJTable().convertRowIndexToModel(getJTable().getSelectedRow());
						String selectedCode = tableModel.getCode(selectedRow);
						getCurrency2().setSelectedItem(CurrencyNames.getString(selectedCode));
					}
				}
			});
		}
		return jTable;
	}
}
