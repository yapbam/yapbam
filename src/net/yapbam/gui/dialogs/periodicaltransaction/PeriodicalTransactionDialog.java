package net.yapbam.gui.dialogs.periodicaltransaction;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Window;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;

import net.yapbam.data.*;
import net.yapbam.date.helpers.DateStepper;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.AbstractTransactionDialog;
import net.yapbam.gui.dialogs.EditAccountDialog;
import net.yapbam.gui.dialogs.GenerationPanel;
import net.yapbam.gui.util.AutoUpdateOkButtonPropertyListener;

/** This dialog allows to create or edit a transaction */
public class PeriodicalTransactionDialog extends AbstractTransactionDialog<PeriodicalTransaction> {
	private static final long serialVersionUID = 1L;
	
	private GenerationPanel generationPanel;
	
	public static PeriodicalTransaction open(GlobalData data, Window frame, PeriodicalTransaction transaction, boolean edit) {
		if (data.getAccountsNumber()==0) {
			//Need to create an account first
			EditAccountDialog.open(data, frame, LocalizationData.get("TransactionDialog.needAccount")); //$NON-NLS-1$
			if (data.getAccountsNumber()==0) {
				return null;
			}
		}
		PeriodicalTransactionDialog dialog = new PeriodicalTransactionDialog(frame, data, transaction, edit);
		dialog.setVisible(true);
		PeriodicalTransaction newTransaction = dialog.getTransaction();
		if (newTransaction!=null) {
			if (transaction!=null) {
				data.remove(transaction);
			}
			data.add(newTransaction);
		}
		return newTransaction;
	}
	
	private PeriodicalTransactionDialog(Window owner, GlobalData data, PeriodicalTransaction transaction, boolean edit) {
		super(owner, (edit?LocalizationData.get("PeriodicalTransactionDialog.title.edit"):LocalizationData.get("PeriodicalTransactionDialog.title.new")), data, transaction); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
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
		generationPanel.addPropertyChangeListener(new AutoUpdateOkButtonPropertyListener(this));
		return generationPanel;
	}

	@Override
	protected PeriodicalTransaction buildResult() {
		double amount = getAmount();
		ArrayList<SubTransaction> subTransactions = new ArrayList<SubTransaction>();
		for (int i = 0; i < subtransactionsPanel.getSubtransactionsCount(); i++) {
			subTransactions.add(subtransactionsPanel.getSubtransaction(i));
		}
		return new PeriodicalTransaction(description.getText().trim(), comment.getText().trim(), amount, getAccount(),
				getCurrentMode(), categories.get(), subTransactions, generationPanel.getNextDate(),
				generationPanel.isActivated(), generationPanel.getDateStepper());
	}
	
	@Override
	protected void buildStatementFields(JPanel centerPane, FocusListener focusListener, GridBagConstraints c) {
	}

	@Override
	protected void buildNumberField(JPanel centerPane, FocusListener focusListener, GridBagConstraints c) {
	}

	@Override
	protected void buildDateField(JPanel centerPane, FocusListener focusListener, GridBagConstraints c) {
	}

	@Override
	public PeriodicalTransaction getTransaction() {
		return (PeriodicalTransaction) super.getResult();
	}

	@Override
	protected String getOkDisabledCause() {
		String disabledCause = super.getOkDisabledCause(); 
		if ((disabledCause!=null) || (generationPanel==null)) {
			return disabledCause;
		} else if (!generationPanel.getNextDateIsValid()) {
			return LocalizationData.get("PeriodicalTransactionDialog.error.nextDate"); //$NON-NLS-1$
		}
		Date nextDate = generationPanel.getNextDate();
		if ((nextDate==null) && generationPanel.isActivated()) {
			return LocalizationData.get("PeriodicalTransactionDialog.error.nextDate"); //$NON-NLS-1$
		}
		DateStepper dateStepper = generationPanel.getDateStepper();
		if (dateStepper==null) {
			return LocalizationData.get("PeriodicalTransactionDialog.error.dateStepper"); //$NON-NLS-1$
		} else if (!generationPanel.getLastDateIsValid()) {
			return LocalizationData.get("PeriodicalTransactionDialog.error.endDate"); //$NON-NLS-1$
		} else if ((dateStepper.getLastDate()!=null) && (nextDate!=null) && (dateStepper.getLastDate().compareTo(nextDate)<0)) {
			return LocalizationData.get("PeriodicalTransactionDialog.error.nextDateAfterEnd"); //$NON-NLS-1$
		} else {
			return null;
		}
	}
}
