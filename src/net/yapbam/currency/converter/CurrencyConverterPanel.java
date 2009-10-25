package net.yapbam.currency.converter;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import java.awt.GridBagConstraints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Currency;


import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.AmountWidget;
import javax.swing.JLabel;
import java.awt.Insets;
import java.awt.Color;

public class CurrencyConverterPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JComboBox currency1 = null;
	private JComboBox currency2 = null;
	private AmountWidget amount1 = null;
	private AmountWidget amount2 = null;
	private JLabel jLabel = null;
	
	private String[] codes;
	private JLabel title = null;
	private JLabel errField = null;
	
	/**
	 * This is the default constructor
	 */
	public CurrencyConverterPanel() {
		super();
		initialize();
		try {
			this.codes = CurrencyConverter.getInstance().getCurrencies();
			for (int i = 0; i < codes.length; i++) {
				String symbol = Currency.getInstance(this.codes[i]).getSymbol();
				currency1.addItem(symbol);
				currency2.addItem(symbol);
			}
			Currency currency = Currency.getInstance(LocalizationData.getLocale());
			int index = Arrays.asList(this.codes).indexOf(currency.getCurrencyCode());
			currency1.setSelectedIndex(index);
			currency2.setSelectedIndex(index);
			String title = MessageFormat.format("Les taux de conversion utilisés sont ceux de la European Central Bank au {0,date,long}", CurrencyConverter.getInstance().getReferenceDate());
			this.title.setText(title);
		} catch (Exception e) {
			errField.setText(MessageFormat.format("L''erreur \"{0}\" est survenue.", e.toString()));
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.anchor = GridBagConstraints.WEST;
		gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints6.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints6.gridwidth = 0;
		gridBagConstraints6.gridy = 2;
		errField = new JLabel();
		errField.setText("");
		errField.setForeground(new Color(255, 64, 64));
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.gridy = 0;
		gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints4.anchor = GridBagConstraints.WEST;
		gridBagConstraints4.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints4.gridwidth = GridBagConstraints.REMAINDER;
		title = new JLabel();
		title.setText("");
		GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
		gridBagConstraints31.gridx = 2;
		gridBagConstraints31.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints31.gridy = 1;
		jLabel = new JLabel();
		jLabel.setText("=");
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
	}

	/**
	 * This method initializes currency1	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCurrency1() {
		if (currency1 == null) {
			currency1 = new JComboBox();
			currency1.setToolTipText("Sélectionnez la devise d'origine");
			currency1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					amount1.setCurrency(Currency.getInstance(codes[currency1.getSelectedIndex()]));
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
			currency2.setToolTipText("Sélectionnez la devise de destination");
			currency2.addActionListener(new java.awt.event.ActionListener() {
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
	private AmountWidget getAmount1() {
		if (amount1 == null) {
			amount1 = new AmountWidget(LocalizationData.getLocale());
			amount1.setColumns(5);
			amount1.setToolTipText("Tapez le montant à convertir ici, terminez par la touche <Tab>");
			amount1.setValue(0.0);
			amount1.addPropertyChangeListener(AmountWidget.VALUE_PROPERTY, new PropertyChangeListener() {
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
	private AmountWidget getAmount2() {
		if (amount2 == null) {
			amount2 = new AmountWidget(LocalizationData.getLocale());
			amount2.setColumns(5);
			amount2.setToolTipText("Ce champ contient le montant converti");
			amount2.setEditable(false);
		}
		return amount2;
	}

	private void doConvert() {
		Double value = amount1.getValue();
		if (value!=null) {
			String from = codes[currency1.getSelectedIndex()];
			String to = codes[currency2.getSelectedIndex()];
			try {
				amount2.setValue(CurrencyConverter.getInstance().convert(value, from, to));
			} catch (Exception e) {
				errField.setText(MessageFormat.format("L''erreur \"{0}\" est survenue.", e.toString()));
			}
		}
	}
}
