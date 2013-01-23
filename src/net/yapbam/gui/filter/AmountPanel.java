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
	private JPanel panel_2;
	private JPanel panel_1;
	private JRadioButton amountAll;
	private CurrencyWidget maxAmountWidget;
	private CurrencyWidget minAmountWidget;
	private JRadioButton amountBetween;
	private JRadioButton amountEquals;
	
	/**
	 * Create the panel.
	 */
	public AmountPanel() {
		GridBagLayout gbl_amountPanel = new GridBagLayout();
		setLayout(gbl_amountPanel);
		setBorder(BorderFactory.createTitledBorder(null, LocalizationData.get("Transaction.amount"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION)); //$NON-NLS-1$
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 5;
		gridBagConstraints4.gridy = 0;
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.anchor = GridBagConstraints.WEST;
		gbc_panel_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_2.insets = new Insets(0, 0, 5, 5);
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 0;
		add(getPanel_2(), gbc_panel_2);
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.weightx = 1.0;
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.anchor = GridBagConstraints.WEST;
		gbc_panel_1.gridwidth = 3;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 0;
		add(getPanel_1(), gbc_panel_1);
	}
	private JPanel getPanel_2() {
		if (panel_2 == null) {
			panel_2 = new JPanel();
			GridBagLayout gbl_panel_2 = new GridBagLayout();
			panel_2.setLayout(gbl_panel_2);
			GridBagConstraints gbc_amountAll = new GridBagConstraints();
			gbc_amountAll.anchor = GridBagConstraints.WEST;
			gbc_amountAll.insets = new Insets(0, 0, 0, 5);
			gbc_amountAll.gridx = 0;
			gbc_amountAll.gridy = 0;
			panel_2.add(getAmountAll(), gbc_amountAll);
			GridBagConstraints gbc_amountEquals = new GridBagConstraints();
			gbc_amountEquals.anchor = GridBagConstraints.WEST;
			gbc_amountEquals.insets = new Insets(0, 0, 0, 5);
			gbc_amountEquals.gridx = 0;
			gbc_amountEquals.gridy = 1;
			panel_2.add(getAmountEquals(), gbc_amountEquals);
			GridBagConstraints gbc_amountBetween = new GridBagConstraints();
			gbc_amountBetween.anchor = GridBagConstraints.WEST;
			gbc_amountBetween.gridx = 0;
			gbc_amountBetween.gridy = 2;
			panel_2.add(getAmountBetween(), gbc_amountBetween);
			ButtonGroup group = new ButtonGroup();
			group.add(getAmountAll());
			group.add(getAmountEquals());
			group.add(getAmountBetween());
		}
		return panel_2;
	}
	private JPanel getPanel_1() {
		if (panel_1 == null) {
			panel_1 = new JPanel();
			GridBagLayout gbl_panel_1 = new GridBagLayout();
			panel_1.setLayout(gbl_panel_1);
			GridBagConstraints gbc_maxAmount = new GridBagConstraints();
			gbc_maxAmount.insets = new Insets(0, 5, 0, 0);
			gbc_maxAmount.weightx = 1.0;
			gbc_maxAmount.fill = GridBagConstraints.HORIZONTAL;
			gbc_maxAmount.gridx = 2;
			gbc_maxAmount.gridy = 0;
			panel_1.add(getMaxAmountWidget(), gbc_maxAmount);
			GridBagConstraints gbc_minAmount = new GridBagConstraints();
			gbc_minAmount.fill = GridBagConstraints.HORIZONTAL;
			gbc_minAmount.insets = new Insets(0, 0, 0, 5);
			gbc_minAmount.weightx = 1.0;
			gbc_minAmount.gridx = 0;
			gbc_minAmount.gridy = 0;
			panel_1.add(getMinAmountWidget(), gbc_minAmount);
			JLabel jLabel = new JLabel();
			GridBagConstraints gbc_jLabel = new GridBagConstraints();
			gbc_jLabel.gridx = 1;
			gbc_jLabel.gridy = 0;
			panel_1.add(jLabel, gbc_jLabel);
			jLabel.setText(LocalizationData.get("CustomFilterPanel.amount.to")); //$NON-NLS-1$
		}
		return panel_1;
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
		if (!getMinAmountWidget().isContentValid()) return LocalizationData.get("CustomFilterPanel.error.amountMimimum"); //$NON-NLS-1$
		if (!getMaxAmountWidget().isContentValid()) return LocalizationData.get("CustomFilterPanel.error.amountMaximum"); //$NON-NLS-1$
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
