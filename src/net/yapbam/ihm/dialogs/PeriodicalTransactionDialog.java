package net.yapbam.ihm.dialogs;

import java.awt.GridBagConstraints;
import java.awt.Window;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;

import net.yapbam.data.*;
import net.yapbam.date.helpers.DateStepper;
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.widget.DateWidget;

/** This dialog allows to create or edit a transaction */
public class PeriodicalTransactionDialog extends AbstractTransactionDialog {
	private static final long serialVersionUID = 1L;
	
	public static PeriodicalTransaction open(GlobalData data, Window frame, PeriodicalTransaction transaction) {
		if (data.getAccountsNumber()==0) {
			//Need to create an account first
			AccountDialog.open(data, frame, LocalizationData.get("TransactionDialog.needAccount")); //$NON-NLS-1$
			if (data.getAccountsNumber()==0) return null;
		}
		PeriodicalTransactionDialog dialog = new PeriodicalTransactionDialog(frame, data, transaction);
		dialog.setVisible(true);
		PeriodicalTransaction newTransaction = dialog.getTransaction();
		if (newTransaction!=null) {
			if (transaction!=null) data.remove(transaction);
			data.add(newTransaction);
		}
		return newTransaction;
	}
	
	private PeriodicalTransactionDialog(Window owner, GlobalData data, PeriodicalTransaction transaction) {
		super(owner, data, transaction);
	}

	protected void setContent(AbstractTransaction transaction) {
		super.setContent(transaction);
		PeriodicalTransaction t = (PeriodicalTransaction) transaction;
		//TODO
	}

	@Override
	protected Object buildResult() {
		double amount = Math.abs(((Number)this.amount.getValue()).doubleValue());
		if (!this.receipt.isSelected()) amount = -amount;
		ArrayList<SubTransaction> subTransactions = new ArrayList<SubTransaction>();
		for (int i = 0; i < subtransactionsPanel.getSubtransactionsCount(); i++) {
			subTransactions.add(subtransactionsPanel.getSubtransaction(i));
		}
		return new PeriodicalTransaction(description.getText().trim(), amount, this.data.getAccount(selectedAccount),
				getCurrentMode(), categories.getCategory(), subTransactions, new Date(), true, 0, DateStepper.IMMEDIATE); //TODO
	}
	
	protected void buildStatementFields(JPanel centerPane, FocusListener focusListener, KeyListener listener, GridBagConstraints c) {}

	protected void buildNumberField(JPanel centerPane, FocusListener focusListener, GridBagConstraints c) {}

	protected void buildDateField(JPanel centerPane, FocusListener focusListener, KeyListener listener, GridBagConstraints c) {}

	public PeriodicalTransaction getTransaction() {
		return (PeriodicalTransaction) super.getResult();
	}

	@Override
	protected String getOkDisabledCause() {
		String disabledCause = super.getOkDisabledCause(); 
		if (disabledCause!=null) return disabledCause;
		return null;
	}
}
