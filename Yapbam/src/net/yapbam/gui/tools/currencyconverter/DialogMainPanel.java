package net.yapbam.gui.tools.currencyconverter;

import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import java.awt.GridBagConstraints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.yapbam.currency.AbstractCurrencyConverter;
import net.yapbam.currency.CountryCurrencyMap;
import net.yapbam.currency.CurrencyNames;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.tools.Messages;
import net.yapbam.gui.widget.AutoSelectFocusListener;
import net.yapbam.gui.widget.CurrencyWidget;

import javax.swing.JLabel;

import java.awt.Insets;
import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.JButton;

import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.table.RowSorter;
import com.fathzer.soft.ajlib.swing.widget.ComboBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

public class DialogMainPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final CountryCurrencyMap MAP = new CountryCurrencyMap();

	private ComboBox currency1 = null;
	private ComboBox currency2 = null;
	private CurrencyWidget amount1 = null;
	private CurrencyWidget amount2 = null;
	
	private AbstractCurrencyConverter converter;
	private String[] codes;
	private JLabel errField = null;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private CurrenciesTableModel tableModel;
	private JButton swapButton;
	
	/**
	 * This is the default constructor
	 */
	public DialogMainPanel() {
		this.converter = null;
		initialize();
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
		gridBagConstraints12.gridy = 4;
		gridBagConstraints12.insets = new Insets(5, 5, 0, 0);
		gridBagConstraints12.weightx = 1.0;
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.anchor = GridBagConstraints.WEST;
		gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints6.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints6.gridwidth = 0;
		gridBagConstraints6.gridy = 2;
		GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
		gridBagConstraints31.gridx = 2;
		gridBagConstraints31.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints31.gridy = 1;
		JLabel jLabel = new JLabel();
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
		gridBagConstraints1.insets = new Insets(5, 5, 5, 0);
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
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.setLayout(gridBagLayout);
		this.add(getCurrency1(), gridBagConstraints2);
		this.add(getCurrency2(), gridBagConstraints1);
		this.add(getAmount1(), gridBagConstraints11);
		this.add(getAmount2(), gridBagConstraints21);
		this.add(jLabel, gridBagConstraints31);
		this.add(getErrField(), gridBagConstraints6);
		GridBagConstraints gbcSwapButton = new GridBagConstraints();
		gbcSwapButton.insets = new Insets(0, 0, 5, 0);
		gbcSwapButton.gridwidth = 0;
		gbcSwapButton.gridx = 0;
		gbcSwapButton.gridy = 2;
		add(getSwapButton(), gbcSwapButton);
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
	private ComboBox getCurrency1() {
		if (currency1 == null) {
			currency1 = new ComboBox();
			currency1.setToolTipText(Messages.getString("CurrencyConverterPanel.origin.toolTip")); //$NON-NLS-1$
			currency1.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					amount1.setCurrency(Currency.getInstance(codes[currency1.getSelectedIndex()]));
					if (tableModel!=null) {
						tableModel.setCurrency(codes[currency1.getSelectedIndex()]);
					}
					Utils.packColumns(getJTable(), 2);
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
	private ComboBox getCurrency2() {
		if (currency2 == null) {
			currency2 = new ComboBox();
			currency2.setToolTipText(Messages.getString("CurrencyConverterPanel.destination.toolTip")); //$NON-NLS-1$
			currency2.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String currencyCode = codes[currency2.getSelectedIndex()];
					int index = tableModel.indexOf(currencyCode);
					if (index<0) {
						getJTable().clearSelection();
					} else {
						index = getJTable().convertRowIndexToView(index);
						getJTable().getSelectionModel().setSelectionInterval(index, index);
						getJTable().scrollRectToVisible(getJTable().getCellRect(index, 0, true));
					}
					amount2.setCurrency(Currency.getInstance(currencyCode));
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
			amount1.addFocusListener(AutoSelectFocusListener.INSTANCE);
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
		if ((value!=null) && (codes!=null) && (currency1.getSelectedIndex()>=0) && (currency2.getSelectedIndex()>=0)) {
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
			tableModel = new CurrenciesTableModel();
			jTable = new com.fathzer.soft.ajlib.swing.table.JTable(tableModel) {
				private static final long serialVersionUID = 1L;

				@Override
				public String getToolTipText(MouseEvent event) {
					int row = jTable.convertRowIndexToModel(jTable.rowAtPoint(event.getPoint()));
					Set<String> countries = MAP.getCountries(tableModel.getCode(row));
					if (countries==null) {
						return super.getToolTipText(event);
					} else {
						List<String> lines = new ArrayList<String>(countries.size());
						Iterator<String> iter = countries.iterator();
						while (iter.hasNext()) {
							lines.add(new Locale("",iter.next()).getDisplayCountry()); //$NON-NLS-1$
						}
						Collections.sort(lines);
						StringBuilder buf = new StringBuilder();
						for (String line : lines) {
							if (buf.length()!=0) {
								buf.append("<br>"); //$NON-NLS-1$
							}
							buf.append(line);
						}
						return "<html>"+buf.toString()+"</html>"; //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
			};
			getJTable().setRowSorter(new RowSorter<TableModel>(getJTable().getModel()));
			getJTable().setDefaultRenderer(Double.class, new ConversionRateRenderer());
			Utils.packColumns(jTable, 2);
			getJTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			getJTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						int viewRow = getJTable().getSelectedRow();
						if (viewRow>=0) {
							int selectedRow = getJTable().convertRowIndexToModel(viewRow);
							String selectedCode = tableModel.getCode(selectedRow);
							getCurrency2().setSelectedItem(CurrencyNames.get(selectedCode));
						}
					}
				}
			});
		}
		return jTable;
	}
	private JButton getSwapButton() {
		if (swapButton == null) {
			swapButton = new JButton(Utils.createIcon(getClass().getResource("swap.png"), ((float)getFont().getSize())/32f)); //$NON-NLS-1$
			swapButton.setFocusable(false);
			swapButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int old1 = getCurrency1().getSelectedIndex();
					getCurrency1().setSelectedIndex(getCurrency2().getSelectedIndex());
					getCurrency2().setSelectedIndex(old1);
				}
			});
			swapButton.setToolTipText(Messages.getString("CurrencyConverterPanel.swapButton.toolTipText")); //$NON-NLS-1$
		}
		return swapButton;
	}

	void setConverter (AbstractCurrencyConverter converter) {
		this.converter = converter;
		
		// Sort the codes accordingly to their wordings
		this.codes = this.converter.getCurrencies();
		Arrays.sort(this.codes, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				String w1 = CurrencyNames.get(o1);
				String w2 = CurrencyNames.get(o2);
				return w1.compareToIgnoreCase(w2);
			}
		});
		
		// Fill the table model
		this.tableModel.setContent(this.converter, codes);
		
		// Fill currency combos
		getCurrency1().setActionEnabled(false);
		getCurrency2().setActionEnabled(false);
		getCurrency1().setSelectedItem(null);
		getCurrency2().setSelectedItem(null);
		for (int i = 0; i < codes.length; i++) {
			String symbol = CurrencyNames.get(this.codes[i]);
			getCurrency1().addItem(symbol);
			getCurrency2().addItem(symbol);
		}
		getCurrency1().setActionEnabled(true);
		getCurrency2().setActionEnabled(true);
		
		// Select available currencies
		Currency currency = Currency.getInstance(LocalizationData.getLocale());
		int index = Arrays.asList(this.codes).indexOf(currency.getCurrencyCode());
		if (index>=0) {
			getCurrency1().setSelectedIndex(index);
			getCurrency2().setSelectedIndex(index);
		}
	}
}
