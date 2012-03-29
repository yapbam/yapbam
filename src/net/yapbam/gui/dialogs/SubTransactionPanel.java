package net.yapbam.gui.dialogs;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;

import net.astesana.ajlib.swing.widget.CurrencyWidget;
import net.astesana.ajlib.swing.widget.TextWidget;
import net.yapbam.data.Category;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.AutoSelectFocusListener;

import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JCheckBox;

public class SubTransactionPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public static final String AMOUNT_PROPERTY = "amount";  //  @jve:decl-index=0: //$NON-NLS-1$
	public static final String DESCRIPTION_PROPERTY = "description";  //  @jve:decl-index=0: //$NON-NLS-1$
	private static final String CATEGORY_PROPERTY = "category"; //$NON-NLS-1$
	
	private JLabel jLabel = null;
	private TextWidget descriptionField = null;
	private JLabel jLabel1 = null;
	private CurrencyWidget amountField = null;
	private CategoryWidget categoryPanel = null;
	private JLabel jLabel2 = null;
	private JCheckBox jCheckBox = null;
	
	private GlobalData data;
	private Double amount;
	
	private PredefinedDescriptionUpdater updater;

	public interface PredefinedDescriptionUpdater {
		public double getAmount(String description);
		public Category getCategory(String description);
	}

	/**
	 * This is the default constructor
	 */
	public SubTransactionPanel(GlobalData data) {
		this.data = data;
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.gridx = 2;
		gridBagConstraints12.anchor = GridBagConstraints.WEST;
		gridBagConstraints12.gridy = 1;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints11.gridy = 2;
		jLabel2 = new JLabel();
		jLabel2.setText(LocalizationData.get("TransactionDialog.category")); //$NON-NLS-1$
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 1;
		gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints4.weightx = 1.0D;
		gridBagConstraints4.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints4.gridwidth = 2;
		gridBagConstraints4.gridy = 2;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints3.gridy = 1;
		gridBagConstraints3.weightx = 0.0D;
		gridBagConstraints3.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints3.gridx = 1;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.anchor = GridBagConstraints.WEST;
		gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints2.gridy = 1;
		jLabel1 = new JLabel();
		jLabel1.setText(LocalizationData.get("TransactionDialog.amount")); //$NON-NLS-1$
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.gridwidth = 2;
		gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints1.gridx = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.gridy = 0;
		jLabel = new JLabel();
		jLabel.setText(LocalizationData.get("TransactionDialog.description")); //$NON-NLS-1$
		this.setSize(333, 106);
		this.setLayout(new GridBagLayout());
		this.add(jLabel, gridBagConstraints);
		this.add(getDescriptionField(), gridBagConstraints1);
		this.add(jLabel1, gridBagConstraints2);
		this.add(getAmountField(), gridBagConstraints3);
		this.add(getCategoryPanel(), gridBagConstraints4);
		this.add(jLabel2, gridBagConstraints11);
		this.add(getJCheckBox(), gridBagConstraints12);
		
		descriptionField.addPropertyChangeListener(TextWidget.TEXT_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				SubTransactionPanel.this.firePropertyChange(DESCRIPTION_PROPERTY, evt.getOldValue(), evt.getNewValue());
			}
		});
		descriptionField.addFocusListener(AutoSelectFocusListener.INSTANCE);
		this.amount = amountField.getValue();
		amountField.addPropertyChangeListener(CurrencyWidget.VALUE_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Double old = amount;
				amount = amountField.getValue();
				if (!jCheckBox.isSelected() && (amount!=null)) amount = -amount;
				SubTransactionPanel.this.firePropertyChange(AMOUNT_PROPERTY, old, amount);
			}
		});
		amountField.addFocusListener(AutoSelectFocusListener.INSTANCE);
	}

	/**
	 * This method initializes descriptionField	
	 * @return javax.swing.JTextField	
	 */
	private TextWidget getDescriptionField() {
		if (descriptionField == null) {
			descriptionField = new TextWidget();
			descriptionField.setToolTipText(LocalizationData.get("SubTransactionDialog.description.tooltip")); //$NON-NLS-1$
			descriptionField.setColumns(50);
			descriptionField.addPropertyChangeListener(TextWidget.PREDEFINED_VALUE, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if ((updater!=null) && (evt.getNewValue()!=null)) {
						setAmount(updater.getAmount((String) evt.getNewValue()));
						setCategory(updater.getCategory((String) evt.getNewValue()));
					}
				}
			});
		}
		return descriptionField;
	}
	
	public void setUpdater(PredefinedDescriptionUpdater updater) {
		this.updater = updater;
	}

	/**
	 * This method initializes amountField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private CurrencyWidget getAmountField() {
		if (amountField == null) {
			amountField = new CurrencyWidget(LocalizationData.getLocale());
			amountField.setToolTipText(LocalizationData.get("SubTransactionDialog.amount.tooltip")); //$NON-NLS-1$
			amountField.setColumns(10);
			amountField.setValue(new Double(0));
		}
		return amountField;
	}

	/**
	 * This method initializes categoryPanel	
	 * @return net.astesana.comptes.ihm.dialogs.CategoryPanel	
	 */
	private CategoryWidget getCategoryPanel() {
		if (categoryPanel == null) {
			categoryPanel = new CategoryWidget(data);
			categoryPanel.getJLabel().setVisible(false);
			categoryPanel.addPropertyChangeListener(CategoryWidget.CATEGORY_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					firePropertyChange(CATEGORY_PROPERTY, evt.getOldValue(), evt.getNewValue());
				}
			});
		}
		return categoryPanel;
	}

	/**
	 * This method initializes jCheckBox	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBox() {
		if (jCheckBox == null) {
			jCheckBox = new JCheckBox();
			jCheckBox.setText(LocalizationData.get("TransactionDialog.receipt")); //$NON-NLS-1$
			jCheckBox.setToolTipText(LocalizationData.get("SubTransactionDialog.receipt.tooltip")); //$NON-NLS-1$
			jCheckBox.addItemListener(new java.awt.event.ItemListener() {
				@Override
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if ((amount!=null) && (amount!=0)) {
						Double old = amount;
						amount = -amount;
						SubTransactionPanel.this.firePropertyChange(AMOUNT_PROPERTY, old, amount);
					}
				}
			});
		}
		return jCheckBox;
	}

	public Double getAmount() {
		return amount;
	}
	
	public void setAmount(Double amount) {
		Object old = this.amount;
		this.jCheckBox.setSelected(amount>=0);
		this.amountField.setValue(Math.abs(amount));
		this.amount = amount;
		this.firePropertyChange(AMOUNT_PROPERTY, old, amount);
	}
	
	public String getDescription() {
		return descriptionField.getText();
	}
	
	public void setDescription(String description) {
		this.descriptionField.setText(description);
	}
	
	public Category getCategory() {
		return this.getCategoryPanel().get();
	}
	
	public void setCategory (Category category) {
		this.categoryPanel.set(category);
	}
	
	public void setPredefined(String[] predefined, int unsortedSize) {
		this.getDescriptionField().setPredefined(predefined, unsortedSize);
	}
}  //  @jve:decl-index=0:visual-constraint="10,53"
