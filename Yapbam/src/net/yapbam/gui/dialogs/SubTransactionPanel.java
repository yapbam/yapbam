package net.yapbam.gui.dialogs;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;

import net.yapbam.data.Category;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.AmountWidget;
import net.yapbam.gui.widget.AutoSelectFocusListener;
import net.yapbam.gui.widget.PopupTextFieldList;

import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBox;

public class SubTransactionPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public static final String AMOUNT_PROPERTY = "amount";  //  @jve:decl-index=0: //$NON-NLS-1$
	public static final String DESCRIPTION_PROPERTY = "description";  //  @jve:decl-index=0: //$NON-NLS-1$
	private static final String CATEGORY_PROPERTY = "category"; //$NON-NLS-1$
	
	private JLabel jLabel = null;
	private PopupTextFieldList descriptionField = null;
	private JLabel jLabel1 = null;
	private AmountWidget amountField = null;
	private CategoryPanel categoryPanel = null;
	private JLabel jLabel2 = null;
	
	private String description;  //  @jve:decl-index=0:
	private Double amount;
	private JCheckBox jCheckBox = null;

	/**
	 * This is the default constructor
	 */
	public SubTransactionPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
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
		
		AutoSelectFocusListener focusListener = new AutoSelectFocusListener();
		this.description = descriptionField.getText();
		descriptionField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				String old = description;
				description = descriptionField.getText();
				SubTransactionPanel.this.firePropertyChange(DESCRIPTION_PROPERTY, old, description);
			}
		});
		descriptionField.addFocusListener(focusListener);
		this.amount = amountField.getValue();
		amountField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				Double old = amount;
				amount = amountField.getValue();
				if (!jCheckBox.isSelected() && (amount!=null)) amount = -amount;
				SubTransactionPanel.this.firePropertyChange(AMOUNT_PROPERTY, old, description);
			}
		});
		amountField.addFocusListener(focusListener);
	}

	/**
	 * This method initializes descriptionField	
	 * @return javax.swing.JTextField	
	 */
	private PopupTextFieldList getDescriptionField() {
		if (descriptionField == null) {
			descriptionField = new PopupTextFieldList();
			descriptionField.setToolTipText(LocalizationData.get("SubTransactionDialog.description.tooltip")); //$NON-NLS-1$
			descriptionField.setText(""); //$NON-NLS-1$
			descriptionField.setColumns(50);
		}
		return descriptionField;
	}

	/**
	 * This method initializes amountField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getAmountField() {
		if (amountField == null) {
			amountField = new AmountWidget(LocalizationData.getLocale());
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
	private CategoryPanel getCategoryPanel() {
		if (categoryPanel == null) {
			categoryPanel = new CategoryPanel();
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
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (amount!=0) {
						Double old = amount;
						amount = -amount;
						SubTransactionPanel.this.firePropertyChange(AMOUNT_PROPERTY, old, amount);
					}
				}
			});
		}
		return jCheckBox;
	}

	public void setData(GlobalData data) {
		this.getCategoryPanel().setData(data);
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
		return description;
	}
	
	public void setDescription(String description) {
		Object old = this.description;
		this.description = description;
		this.descriptionField.setText(description);
		this.firePropertyChange(DESCRIPTION_PROPERTY, old, description);
	}
	
	public Category getCategory() {
		return this.getCategoryPanel().getCategory();
	}
	
	public void setCategory (Category category) {
		Object old = this.getCategory();
		this.categoryPanel.setCategory(category);
		this.firePropertyChange(CATEGORY_PROPERTY, old, category);
	}
	
	public void setPredefined(String[] predefined, int[] groupSizes) {
		this.getDescriptionField().setPredefined(predefined, groupSizes);
	}
}  //  @jve:decl-index=0:visual-constraint="10,53"
