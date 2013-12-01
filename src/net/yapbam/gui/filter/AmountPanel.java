package net.yapbam.gui.filter;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.AutoSelectFocusListener;
import net.yapbam.gui.widget.CurrencyWidget;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class AmountPanel extends ConsistencyCheckedPanel {
	private static final long serialVersionUID = 1L;
	private JPanel panel2;
	private JPanel panel1;
	private JRadioButton amountAll;
	private CurrencyWidget maxAmountWidget;
	private CurrencyWidget minAmountWidget;
	private JRadioButton amountBetween;
	private JRadioButton amountEquals;
	
	/**
	 * Create the panel.
	 */
	public AmountPanel() {
		GridBagLayout gblAmountPanel = new GridBagLayout();
		setLayout(gblAmountPanel);
		setBorder(BorderFactory.createTitledBorder(null, LocalizationData.get("Transaction.amount"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION)); //$NON-NLS-1$
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 5;
		gridBagConstraints4.gridy = 0;
		GridBagConstraints gbcPanel2 = new GridBagConstraints();
		gbcPanel2.anchor = GridBagConstraints.WEST;
		gbcPanel2.fill = GridBagConstraints.HORIZONTAL;
		gbcPanel2.insets = new Insets(0, 0, 5, 5);
		gbcPanel2.gridx = 0;
		gbcPanel2.gridy = 0;
		add(getPanel2(), gbcPanel2);
		GridBagConstraints gbcPanel1 = new GridBagConstraints();
		gbcPanel1.weightx = 1.0;
		gbcPanel1.fill = GridBagConstraints.BOTH;
		gbcPanel1.anchor = GridBagConstraints.WEST;
		gbcPanel1.gridwidth = 3;
		gbcPanel1.gridx = 1;
		gbcPanel1.gridy = 0;
		add(getPanel1(), gbcPanel1);
	}
	private JPanel getPanel2() {
		if (panel2 == null) {
			panel2 = new JPanel();
			GridBagLayout gblPanel2 = new GridBagLayout();
			panel2.setLayout(gblPanel2);
			GridBagConstraints gbcAmountAll = new GridBagConstraints();
			gbcAmountAll.anchor = GridBagConstraints.WEST;
			gbcAmountAll.insets = new Insets(0, 0, 0, 5);
			gbcAmountAll.gridx = 0;
			gbcAmountAll.gridy = 0;
			panel2.add(getAmountAll(), gbcAmountAll);
			GridBagConstraints gbcamountEquals = new GridBagConstraints();
			gbcamountEquals.anchor = GridBagConstraints.WEST;
			gbcamountEquals.insets = new Insets(0, 0, 0, 5);
			gbcamountEquals.gridx = 0;
			gbcamountEquals.gridy = 1;
			panel2.add(getAmountEquals(), gbcamountEquals);
			GridBagConstraints gbcamountBetween = new GridBagConstraints();
			gbcamountBetween.anchor = GridBagConstraints.WEST;
			gbcamountBetween.gridx = 0;
			gbcamountBetween.gridy = 2;
			panel2.add(getAmountBetween(), gbcamountBetween);
			ButtonGroup group = new ButtonGroup();
			group.add(getAmountAll());
			group.add(getAmountEquals());
			group.add(getAmountBetween());
		}
		return panel2;
	}
	private JPanel getPanel1() {
		if (panel1 == null) {
			panel1 = new JPanel();
			GridBagLayout gblPanel1 = new GridBagLayout();
			panel1.setLayout(gblPanel1);
			GridBagConstraints gbcMaxAmount = new GridBagConstraints();
			gbcMaxAmount.insets = new Insets(0, 5, 0, 0);
			gbcMaxAmount.weightx = 1.0;
			gbcMaxAmount.fill = GridBagConstraints.HORIZONTAL;
			gbcMaxAmount.gridx = 2;
			gbcMaxAmount.gridy = 0;
			panel1.add(getMaxAmountWidget(), gbcMaxAmount);
			GridBagConstraints gbcMinAmount = new GridBagConstraints();
			gbcMinAmount.fill = GridBagConstraints.HORIZONTAL;
			gbcMinAmount.insets = new Insets(0, 0, 0, 5);
			gbcMinAmount.weightx = 1.0;
			gbcMinAmount.gridx = 0;
			gbcMinAmount.gridy = 0;
			panel1.add(getMinAmountWidget(), gbcMinAmount);
			JLabel jLabel = new JLabel();
			GridBagConstraints gbcJLabel = new GridBagConstraints();
			gbcJLabel.gridx = 1;
			gbcJLabel.gridy = 0;
			panel1.add(jLabel, gbcJLabel);
			jLabel.setText(LocalizationData.get("CustomFilterPanel.amount.to")); //$NON-NLS-1$
		}
		return panel1;
	}
	/**
	 * This method initializes amountEquals	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getAmountEquals() {
		if (amountEquals == null) {
			amountEquals = new JRadioButton();
			amountEquals.setText(LocalizationData.get("CustomFilterPanel.amount.equals")); //$NON-NLS-1$
			amountEquals.setToolTipText(LocalizationData.get("CustomFilterPanel.amount.equals.toolTip")); //$NON-NLS-1$
			amountEquals.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (amountEquals.isSelected()) {
						getMinAmountWidget().setEnabled(true);
						getMaxAmountWidget().setEnabled(false);
						getMaxAmountWidget().setValue(getMinAmountWidget().getValue());
					}
				}
			});
		}
		return amountEquals;
	}

	/**
	 * This method initializes amountBetween	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getAmountBetween() {
		if (amountBetween == null) {
			amountBetween = new JRadioButton();
			amountBetween.setText(LocalizationData.get("CustomFilterPanel.amout.between")); //$NON-NLS-1$
			amountBetween.setToolTipText(LocalizationData.get("CustomFilterPanel.amout.between.toolTip")); //$NON-NLS-1$
			amountBetween.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (amountBetween.isSelected()) {
						getMinAmountWidget().setEnabled(true);
						getMaxAmountWidget().setEnabled(true);
					}
				}
			});
		}
		return amountBetween;
	}

	/**
	 * This method initializes minAmount	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private CurrencyWidget getMinAmountWidget() {
		if (minAmountWidget == null) {
			minAmountWidget = new CurrencyWidget(LocalizationData.getLocale());
			minAmountWidget.setColumns(6);
			minAmountWidget.setEmptyAllowed(true);
			minAmountWidget.setMinValue(0.0);
			minAmountWidget.setToolTipText(LocalizationData.get("CustomFilterPanel.amout.minimum")); //$NON-NLS-1$
			minAmountWidget.addPropertyChangeListener(CurrencyWidget.VALUE_PROPERTY, consistencyChecker);
			minAmountWidget.addPropertyChangeListener(CurrencyWidget.CONTENT_VALID_PROPERTY, consistencyChecker);
			minAmountWidget.addFocusListener(AutoSelectFocusListener.INSTANCE);
		}
		return minAmountWidget;
	}

	/**
	 * This method initializes maxAmount	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private CurrencyWidget getMaxAmountWidget() {
		if (maxAmountWidget == null) {
			maxAmountWidget = new CurrencyWidget(LocalizationData.getLocale());
			maxAmountWidget.setColumns(6);
			maxAmountWidget.setEmptyAllowed(true);
			maxAmountWidget.setMinValue(0.0);
			maxAmountWidget.setToolTipText(LocalizationData.get("CustomFilterPanel.amount.maximum")); //$NON-NLS-1$
			maxAmountWidget.addPropertyChangeListener(CurrencyWidget.VALUE_PROPERTY, consistencyChecker);
			maxAmountWidget.addPropertyChangeListener(CurrencyWidget.CONTENT_VALID_PROPERTY, consistencyChecker);
			maxAmountWidget.addFocusListener(AutoSelectFocusListener.INSTANCE);
		}
		return maxAmountWidget;
	}

	/**
	 * This method initializes amountAll	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getAmountAll() {
		if (amountAll == null) {
			amountAll = new JRadioButton();
			amountAll.setText(LocalizationData.get("CustomFilterPanel.amount.all")); //$NON-NLS-1$
			amountAll.setToolTipText(LocalizationData.get("CustomFilterPanel.amount.all.toolTip")); //$NON-NLS-1$
			amountAll.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (amountAll.isSelected()) {
						getMinAmountWidget().setValue(null);
						getMaxAmountWidget().setValue(null);
						getMinAmountWidget().setEnabled(false);
						getMaxAmountWidget().setEnabled(false);
					}
				}
			});
		}
		return amountAll;
	}

	@Override
	protected String computeInconsistencyCause() {
		if (!getMinAmountWidget().isContentValid()) {
			return LocalizationData.get("CustomFilterPanel.error.amountMimimum"); //$NON-NLS-1$
		}
		if (!getMaxAmountWidget().isContentValid()) {
			return LocalizationData.get("CustomFilterPanel.error.amountMaximum"); //$NON-NLS-1$
		}
		if ((getMinAmountWidget().getValue()!=null) && (getMaxAmountWidget().getValue()!=null)
				&& (getMinAmountWidget().getValue()>getMaxAmountWidget().getValue())) {
			return LocalizationData.get("CustomFilterPanel.error.amountFromHigherThanTo"); //$NON-NLS-1$
		}
		return null;
	}

	public double getMinAmount() {
		return getMinAmountWidget().getValue()==null?0.0:getMinAmountWidget().getValue();
	}
	
	public double getMaxAmount() {
		return getAmountEquals().isSelected()?getMinAmount():(getMaxAmountWidget().getValue()==null?Double.POSITIVE_INFINITY:getMaxAmountWidget().getValue());
	}
	
	public void clear() {
		getAmountAll().setSelected(true);
	}
	
	public void setAmounts(double min, double max) {
		amountEquals.setSelected(min==max);
		amountBetween.setSelected(((min!=0.0) || (max!=Double.POSITIVE_INFINITY)) && (max!=min));
		minAmountWidget.setValue(min==0.0?null:min);
		maxAmountWidget.setValue(max==Double.POSITIVE_INFINITY?null:max);
		amountAll.setSelected((min==0.0) && (max==Double.POSITIVE_INFINITY));
	}
}
