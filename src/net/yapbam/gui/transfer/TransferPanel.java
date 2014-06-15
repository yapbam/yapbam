package net.yapbam.gui.transfer;

import javax.swing.JPanel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.border.TitledBorder;
import javax.swing.JLabel;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.widget.date.DateWidget;
import com.fathzer.soft.ajlib.utilities.NullUtils;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.CategoryWidget;

import java.awt.GridLayout;

import net.yapbam.data.GlobalData;
import net.yapbam.data.SubTransaction;
import net.yapbam.data.Transaction;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import net.yapbam.gui.dialogs.SubtransactionListPanel;
import net.yapbam.gui.widget.AutoSelectFocusListener;
import net.yapbam.gui.widget.CurrencyWidget;

public class TransferPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public static final String OK_DISABLED_CAUSE_PROPERTY = "okDisabledCause"; //$NON-NLS-1$

	private JPanel upperPane;
	private FromOrToPane fromPane;
	private FromOrToPane toPane;
	private JLabel dateLabel;
	private DateWidget dateField;
	private JLabel amountLabel;
	private CurrencyWidget amountField;
	private JPanel mainPanel;
	private CategoryWidget categoryWidget;

	private GlobalData data;
	private String okDisabledCause;
	private PropertyChangeListener listener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (FromOrToPane.ACCOUNT_PROPERTY.equals(evt.getPropertyName())) {
				setDefaultDescription(evt.getSource()!=getFromPane());
			}
			updateOkDisabledCause();
		}
	};
	private SubtransactionListPanel subTransactionsPanel;
	
	private void setDefaultDescription(boolean from) {
		String format = LocalizationData.get(from?"TransferDialog.from.description": //$NON-NLS-1$
				"TransferDialog.to.description"); //$NON-NLS-1$
		String description = Formatter.format(format, (from ? getToPane() : getFromPane()).getAccountWidget().get().getName());
		(from ? getFromPane() : getToPane()).getDescriptionField().setText(description);
	}

	/**
	 * Create the panel.
	 */
	public TransferPanel(GlobalData data) {
		this.data = data;
		initialize();
		setDefaultDescription(true);
		setDefaultDescription(false);
		updateOkDisabledCause();
	}

	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbcUpperPane = new GridBagConstraints();
		gbcUpperPane.insets = new Insets(0, 5, 0, 5);
		gbcUpperPane.weightx = 1.0;
		gbcUpperPane.fill = GridBagConstraints.HORIZONTAL;
		gbcUpperPane.anchor = GridBagConstraints.NORTHWEST;
		gbcUpperPane.gridx = 0;
		gbcUpperPane.gridy = 0;
		add(getUpperPane(), gbcUpperPane);
		GridBagConstraints gbcSubTransactionsPanel = new GridBagConstraints();
		gbcSubTransactionsPanel.fill = GridBagConstraints.BOTH;
		gbcSubTransactionsPanel.gridx = 0;
		gbcSubTransactionsPanel.gridy = 2;
		add(getSubTransactionsPanel(), gbcSubTransactionsPanel);
		GridBagConstraints gbcMainPanel = new GridBagConstraints();
		gbcMainPanel.insets = new Insets(0, 0, 5, 0);
		gbcMainPanel.gridwidth = 0;
		gbcMainPanel.weighty = 1.0;
		gbcMainPanel.weightx = 1.0;
		gbcMainPanel.fill = GridBagConstraints.BOTH;
		gbcMainPanel.gridx = 0;
		gbcMainPanel.gridy = 1;
		add(getMainPanel(), gbcMainPanel);
	}

	private JPanel getUpperPane() {
		if (upperPane == null) {
			upperPane = new JPanel();
			GridBagLayout gblUpperPane = new GridBagLayout();
			upperPane.setLayout(gblUpperPane);
			GridBagConstraints gbcDateLabel = new GridBagConstraints();
			gbcDateLabel.insets = new Insets(0, 0, 0, 5);
			gbcDateLabel.anchor = GridBagConstraints.WEST;
			gbcDateLabel.gridx = 0;
			gbcDateLabel.gridy = 0;
			upperPane.add(getDateLabel(), gbcDateLabel);
			GridBagConstraints gbcDateField = new GridBagConstraints();
			gbcDateField.insets = new Insets(0, 0, 0, 5);
			gbcDateField.gridx = 1;
			gbcDateField.gridy = 0;
			upperPane.add(getDateField(), gbcDateField);
			GridBagConstraints gbcAmountLabel = new GridBagConstraints();
			gbcAmountLabel.insets = new Insets(0, 5, 0, 5);
			gbcAmountLabel.anchor = GridBagConstraints.EAST;
			gbcAmountLabel.gridx = 2;
			gbcAmountLabel.gridy = 0;
			upperPane.add(getAmountLabel(), gbcAmountLabel);
			GridBagConstraints gbcAmountField = new GridBagConstraints();
			gbcAmountField.anchor = GridBagConstraints.WEST;
			gbcAmountField.insets = new Insets(0, 0, 0, 5);
			gbcAmountField.gridx = 3;
			gbcAmountField.gridy = 0;
			upperPane.add(getAmountField(), gbcAmountField);
			GridBagConstraints gbcCategoryWidget = new GridBagConstraints();
			gbcCategoryWidget.weightx = 1.0;
			gbcCategoryWidget.fill = GridBagConstraints.HORIZONTAL;
			gbcCategoryWidget.gridwidth = 0;
			gbcCategoryWidget.gridx = 4;
			gbcCategoryWidget.gridy = 0;
			upperPane.add(getCategoryWidget(), gbcCategoryWidget);
		}
		return upperPane;
	}
	FromOrToPane getFromPane() {
		if (fromPane == null) {
			fromPane = new FromOrToPane(data, true);
			fromPane.getAccountWidget().setToolTipText(LocalizationData.get("TransferDialog.from.account.tooltip")); //$NON-NLS-1$
			fromPane.getValueDateField().setToolTipText(LocalizationData.get("TransferDialog.from.valueDate.tooltip")); //$NON-NLS-1$
			fromPane.setBorder(new TitledBorder(null, LocalizationData.get("TransferDialog.from.title"), TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null, null)); //$NON-NLS-1$
			fromPane.addPropertyChangeListener(FromOrToPane.ACCOUNT_PROPERTY, listener);
			fromPane.addPropertyChangeListener(FromOrToPane.VALUE_DATE_PROPERTY, listener);
			fromPane.setDate(getDateField().getDate());
		}
		return fromPane;
	}
	FromOrToPane getToPane() {
		if (toPane == null) {
			toPane = new FromOrToPane(data, false);
			if ((data!=null) && (data.getAccountsNumber()>1)) {
				toPane.setAccount(data.getAccount(1));
			}
			toPane.getAccountWidget().setToolTipText(LocalizationData.get("TransferDialog.to.account.tooltip")); //$NON-NLS-1$
			toPane.getValueDateField().setToolTipText(LocalizationData.get("TransferDialog.to.valueDate.tooltip")); //$NON-NLS-1$
			toPane.setBorder(new TitledBorder(null, LocalizationData.get("TransferDialog.to.title"), TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null, null)); //$NON-NLS-1$
			toPane.addPropertyChangeListener(FromOrToPane.ACCOUNT_PROPERTY, listener);
			toPane.addPropertyChangeListener(FromOrToPane.VALUE_DATE_PROPERTY, listener);
			toPane.setDate(getDateField().getDate());
		}
		return toPane;
	}
	private JLabel getDateLabel() {
		if (dateLabel == null) {
			dateLabel = new JLabel(LocalizationData.get("TransactionDialog.date")); //$NON-NLS-1$
		}
		return dateLabel;
	}
	private DateWidget getDateField() {
		if (dateField == null) {
			dateField = new DateWidget();
			dateField.setToolTipText(LocalizationData.get("TransactionDialog.date.tooltip")); //$NON-NLS-1$
			dateField.setColumns(10);
			dateField.addPropertyChangeListener(DateWidget.DATE_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					getFromPane().setDate(dateField.getDate());
					getToPane().setDate(dateField.getDate());
					updateOkDisabledCause();
				}
			});
			dateField.getDateField().addFocusListener(AutoSelectFocusListener.INSTANCE);
		}
		return dateField;
	}
	private JLabel getAmountLabel() {
		if (amountLabel == null) {
			amountLabel = new JLabel(LocalizationData.get("TransactionDialog.amount")); //$NON-NLS-1$
		}
		return amountLabel;
	}
	private CurrencyWidget getAmountField() {
		if (amountField == null) {
			amountField = new CurrencyWidget();
			amountField.setToolTipText(LocalizationData.get("TransferDialog.amount")); //$NON-NLS-1$
			amountField.setColumns(10);
			amountField.setValue(0.0);
			amountField.addPropertyChangeListener(CurrencyWidget.VALUE_PROPERTY, listener);
			amountField.addFocusListener(AutoSelectFocusListener.INSTANCE);
		}
		return amountField;
	}
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridLayout(0, 2, 0, 0));
			mainPanel.add(getFromPane());
			mainPanel.add(getToPane());
		}
		return mainPanel;
	}
	private CategoryWidget getCategoryWidget() {
		if (categoryWidget == null) {
			categoryWidget = new CategoryWidget(data);
		}
		return categoryWidget;
	}
	
	public String getOkDisabledCause() {
		return okDisabledCause;
	}
	
	private void updateOkDisabledCause() {
		String old = okDisabledCause;
		okDisabledCause = null;
		if (getDateField().getDate()==null) {
			okDisabledCause = LocalizationData.get("TransactionDialog.bad.date"); //$NON-NLS-1$
		} else if (getAmountField().getValue() == null) {
			okDisabledCause = LocalizationData.get("TransactionDialog.bad.amount"); //$NON-NLS-1$
		} else if (GlobalData.AMOUNT_COMPARATOR.compare(getAmountField().getValue(),0.0)<=0) {
			okDisabledCause = LocalizationData.get("TransferDialog.error.amountIsNotPositive"); //$NON-NLS-1$
		} else if (getFromPane().getAccountWidget().get().equals(getToPane().getAccountWidget().get())) {
			okDisabledCause = LocalizationData.get("TransferDialog.error.accountAreEquals"); //$NON-NLS-1$
		} else if (getFromPane().getValueDateField().getDate()==null) {
			okDisabledCause = Formatter.format(LocalizationData.get("TransferDialog.errorValueDate"), LocalizationData.get("TransferDialog.from.title")); //$NON-NLS-1$ //$NON-NLS-2$
		} else if (getToPane().getValueDateField().getDate()==null) {
			okDisabledCause = Formatter.format(LocalizationData.get("TransferDialog.errorValueDate"), LocalizationData.get("TransferDialog.to.title")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (!NullUtils.areEquals(old, okDisabledCause)) {
			firePropertyChange(OK_DISABLED_CAUSE_PROPERTY, old, okDisabledCause);
		}
	}

	private SubtransactionListPanel getSubTransactionsPanel() {
		if (subTransactionsPanel == null) {
			subTransactionsPanel = new SubtransactionListPanel(data);
			subTransactionsPanel.addPropertyChangeListener(SubtransactionListPanel.SUM_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if ((getAmountField().getValue() != null) && subTransactionsPanel.isAddToTransactionSelected()) {
						double diff = (Double) evt.getNewValue() - (Double) evt.getOldValue();
						double newValue = getAmountField().getValue() + diff;
						getAmountField().setValue(newValue);
					}
				}
			});
		}
		return subTransactionsPanel;
	}

	public Transaction[] getTransactions() {
		Transaction[] result = new Transaction[2];
		List<SubTransaction> subFrom = new ArrayList<SubTransaction>();
		List<SubTransaction> subTo = new ArrayList<SubTransaction>();
		for (int i = 0; i < getSubTransactionsPanel().getSubtransactionsCount(); i++) {
			SubTransaction sub = getSubTransactionsPanel().getSubtransaction(i);
			subTo.add(sub);
			subFrom.add(new SubTransaction(-sub.getAmount(), sub.getDescription(), sub.getCategory()));
		}
		result[0] = new Transaction(getDateField().getDate(), getFromPane().getNumberField().getNumber(), 
				getFromPane().getDescriptionField().getText(), getFromPane().getCommentField().getText(),
				-getAmountField().getValue(), getFromPane().getAccountWidget().get(), getFromPane().getModeWidget().get(),
				getCategoryWidget().get(), getFromPane().getValueDateField().getDate(),
				getFromPane().getStatementField().getText(), subFrom);
		result[1] = new Transaction(getDateField().getDate(), getToPane().getNumberField().getNumber(),
				getToPane().getDescriptionField().getText(), getToPane().getCommentField().getText(),
				getAmountField().getValue(), getToPane().getAccountWidget().get(), getToPane().getModeWidget().get(),
				getCategoryWidget().get(), getToPane().getValueDateField().getDate(),
				getToPane().getStatementField().getText(), subFrom);
		return result;
	}
}
