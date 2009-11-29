package net.yapbam.gui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.Window;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.ArrayList;

import javax.swing.*;

import net.yapbam.data.*;
import net.yapbam.date.helpers.DateStepper;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.DateWidget;

/** This dialog allows to create or edit a transaction */
public class TransactionDialog extends AbstractTransactionDialog {
	private static final long serialVersionUID = 1L;
	
	private DateWidget date;
	private JTextField transactionNumber;
	private DateWidget defDate;
	private JTextField statement;
	
	/** Display the creation dialog, if the creation is confirmed, add the transaction to the global data 
	 * @param data the global data
	 * @param owner the dialog's parent frame
	 * @param transaction the transaction we want to edit, or null if we want to create a new transaction
	 * @param edit True if we edit an existing transaction, false if we edit a new transaction. Note : Maybe you're thinking
	 * that this argument is redundant with transaction!=null, but it is not. You have to think of transaction argument as a model
	 * to create a new transaction.
	 * @param autoAdd if true, the created or edited transaction is automatically added to the global data instance.
	 * @return the new transaction or the edited one
	 */
	public static Transaction open(GlobalData data, Window owner, Transaction transaction, boolean edit, boolean autoAdd) {
		if (data.getAccountsNumber()==0) {
			//Need to create an account first
			AccountDialog.open(data, owner, LocalizationData.get("TransactionDialog.needAccount")); //$NON-NLS-1$
			if (data.getAccountsNumber()==0) return null;
		}
		TransactionDialog dialog = new TransactionDialog(owner, data, transaction, edit);
		dialog.setVisible(true);
		Transaction newTransaction = dialog.getTransaction();
		if ((newTransaction!=null) && autoAdd) {
			if (transaction!=null) data.remove(transaction);
			data.add(newTransaction);
		}
		return newTransaction;
	}
	
	public TransactionDialog(Window owner, GlobalData data, Transaction transaction, boolean edit) {
		super(owner, (edit?LocalizationData.get("TransactionDialog.title.edit"):LocalizationData.get("TransactionDialog.title.new")), data, transaction);
	}

	public void setTransactionDate (Date date) {
		this.date.setDate(date);
	}
	
	protected void setContent(AbstractTransaction transaction) {
		super.setContent(transaction);
		Transaction t = (Transaction) transaction;
		date.setDate(t.getDate());
		transactionNumber.setText(t.getNumber());
		defDate.setDate(t.getValueDate());
		statement.setText(t.getStatement());
	}

	@Override
	protected Object buildResult() {
		double amount = Math.abs(((Number)this.amount.getValue()).doubleValue());
		if (!this.receipt.isSelected()) amount = -amount;
		String statementId = statement.getText().trim();
		if (statementId.length()==0) statementId = null;
		String number = transactionNumber.getText().trim();
		if (number.length()==0) number = null;
		ArrayList<SubTransaction> subTransactions = new ArrayList<SubTransaction>();
		for (int i = 0; i < subtransactionsPanel.getSubtransactionsCount(); i++) {
			subTransactions.add(subtransactionsPanel.getSubtransaction(i));
		}
		return new Transaction(date.getDate(), number, description.getText().trim(), amount,
				this.data.getAccount(selectedAccount), getCurrentMode(), categories.getCategory(),
				defDate.getDate(), statementId, subTransactions);
	}
	
	protected void buildStatementFields(JPanel centerPane, FocusListener focusListener, GridBagConstraints c) {
		centerPane.add(new JLabel(LocalizationData.get("TransactionDialog.valueDate")), c); //$NON-NLS-1$
		defDate = new DateWidget(new Date());
		defDate.addFocusListener(focusListener);
		defDate.addPropertyChangeListener(DateWidget.DATE_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateOkButtonEnabled();
			}
		});
        c.gridx=1; c.weightx=0; c.fill = GridBagConstraints.HORIZONTAL;
		centerPane.add(defDate,c);
        c.gridx=2; c.fill=GridBagConstraints.NONE; c.weightx = 0;
        centerPane.add(new JLabel(LocalizationData.get("TransactionDialog.statement")), c); //$NON-NLS-1$
        statement = new JTextField(15);
        statement.addFocusListener(focusListener);
        c.gridx=3;
        centerPane.add(statement, c);
	}

	protected void buildNumberField(JPanel centerPane, FocusListener focusListener, GridBagConstraints c) {
		centerPane.add(new JLabel(LocalizationData.get("TransactionDialog.number")), c); //$NON-NLS-1$
        transactionNumber = new JTextField(15);
        transactionNumber.addFocusListener(focusListener);
        c.gridx++;
        centerPane.add(transactionNumber, c);
        c.gridx++;
	}

	protected void buildDateField(JPanel centerPane, FocusListener focusListener, GridBagConstraints c) {
		JLabel titleDate = new JLabel(LocalizationData.get("TransactionDialog.date")); //$NON-NLS-1$
		centerPane.add(titleDate, c);
		date = new DateWidget(new Date());
		date.addFocusListener(focusListener);
		date.addPropertyChangeListener(DateWidget.DATE_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getNewValue()!=null) {
					Mode m = getCurrentMode();
					DateStepper vdc = receipt.isSelected()?m.getReceiptVdc():m.getExpenseVdc();
					defDate.setDate(vdc.getNextStep(date.getDate()));
				}
				updateOkButtonEnabled();
			}
		});
        c.gridx++; c.weightx=0; c.fill = GridBagConstraints.HORIZONTAL;
		centerPane.add(date,c);
        c.gridx++;
	}

	public Transaction getTransaction() {
		return (Transaction) super.getResult();
	}

	protected void optionnalUpdatesOnModeChange() {
		boolean expense = !receipt.isSelected();
		Mode mode = getCurrentMode();
		//TODO transaction number may depend on the new selected mode
		transactionNumber.setText(""); //$NON-NLS-1$
		DateStepper vdc = expense?mode.getExpenseVdc():mode.getReceiptVdc();
		defDate.setDate(vdc.getNextStep(date.getDate()));
	}


	@Override
	protected String getOkDisabledCause() {
		String disabledCause = super.getOkDisabledCause(); 
		if (disabledCause!=null) return disabledCause;
		if (this.date.getDate()==null) return LocalizationData.get("TransactionDialog.bad.date"); //$NON-NLS-1$
		if (this.defDate.getDate()==null) return LocalizationData.get("TransactionDialog.bad.valueDate"); //$NON-NLS-1$
		return null;
	}
}
