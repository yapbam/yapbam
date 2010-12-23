package net.yapbam.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Window;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.*;

import net.yapbam.data.*;
import net.yapbam.gui.LocalizationData;

/** This dialog allows to create or edit a transaction */
public class PeriodicalTransactionDialog extends AbstractTransactionDialog {
	private static final long serialVersionUID = 1L;
	
	private GenerationPanel generationPanel;
	
	public static PeriodicalTransaction open(GlobalData data, Window frame, PeriodicalTransaction transaction, boolean edit) {
		if (data.getAccountsNumber()==0) {
			//Need to create an account first
			AccountDialog.open(data, frame, LocalizationData.get("TransactionDialog.needAccount")); //$NON-NLS-1$
			if (data.getAccountsNumber()==0) return null;
		}
		PeriodicalTransactionDialog dialog = new PeriodicalTransactionDialog(frame, data, transaction, edit);
		dialog.setVisible(true);
		PeriodicalTransaction newTransaction = dialog.getTransaction();
		if (newTransaction!=null) {
			if (transaction!=null) data.remove(transaction);
			data.add(newTransaction);
		}
		return newTransaction;
	}
	
	private PeriodicalTransactionDialog(Window owner, GlobalData data, PeriodicalTransaction transaction, boolean edit) {
		super(owner, (edit?LocalizationData.get("PeriodicalTransactionDialog.title.edit"):LocalizationData.get("PeriodicalTransactionDialog.title.new")), data, transaction); //$NON-NLS-1$ //$NON-NLS-2$
	}

	protected void setContent(AbstractTransaction transaction) {
		super.setContent(transaction);
		PeriodicalTransaction t = (PeriodicalTransaction) transaction;
		generationPanel.setActivated(t.isEnabled());
		generationPanel.setNextDate(t.getNextDate());
		generationPanel.setDateStepper(t.getNextDateBuilder());
	}

	@Override
	protected JPanel createCenterPane() {
		JPanel center = new JPanel(new BorderLayout());
		JPanel transactionPane = super.createCenterPane();
		transactionPane.setBorder(BorderFactory.createTitledBorder(LocalizationData.get("PeriodicalTransactionDialog.transactionBorderTitle"))); //$NON-NLS-1$
		center.add (transactionPane, BorderLayout.CENTER);
		center.add(buildPeriodicalPanel(),BorderLayout.NORTH);
		return center;
	}
	
	private JComponent buildPeriodicalPanel() {
		generationPanel = new GenerationPanel();
		generationPanel.setBorder(BorderFactory.createTitledBorder(LocalizationData.get("PeriodicalTransactionDialog.generationBorderTitle"))); //$NON-NLS-1$
		generationPanel.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateOkButtonEnabled();
			}
		});
		return generationPanel;
	}

	@Override
	protected Object buildResult() {
		double amount = getAmount();
		ArrayList<SubTransaction> subTransactions = new ArrayList<SubTransaction>();
		for (int i = 0; i < subtransactionsPanel.getSubtransactionsCount(); i++) {
			subTransactions.add(subtransactionsPanel.getSubtransaction(i));
		}
		return new PeriodicalTransaction(description.getText().trim(), amount, this.data.getAccount(selectedAccount),
				getCurrentMode(), categories.getCategory(), subTransactions, generationPanel.getNextDate(),
				generationPanel.isActivated(), generationPanel.getDateStepper());
	}
	
	protected void buildStatementFields(JPanel centerPane, FocusListener focusListener, GridBagConstraints c) {}

	protected void buildNumberField(JPanel centerPane, FocusListener focusListener, GridBagConstraints c) {}

	protected void buildDateField(JPanel centerPane, FocusListener focusListener, GridBagConstraints c) {}

	public PeriodicalTransaction getTransaction() {
		return (PeriodicalTransaction) super.getResult();
	}

	@Override
	protected String getOkDisabledCause() {
		String disabledCause = super.getOkDisabledCause(); 
		if ((disabledCause!=null) || !generationPanel.isActivated()) return disabledCause;
		if (generationPanel.getNextDate()==null) return LocalizationData.get("PeriodicalTransactionDialog.error.nextDate"); //$NON-NLS-1$
		if (generationPanel.getDateStepper()==null) return LocalizationData.get("PeriodicalTransactionDialog.error.dateStepper"); //$NON-NLS-1$
		return null;
	}
}
