package net.yapbam.gui.transfer;

import java.awt.Window;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JPanel;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;
import net.yapbam.gui.util.AutoUpdateOkButtonPropertyListener;
import net.yapbam.util.DateUtils;

public class TransferDialog extends AbstractDialog<GlobalData, Transaction[]> {
	private static final long serialVersionUID = 1L;
	
	private TransferPanel transferPanel;

	public TransferDialog(Window owner, String title, GlobalData data) {
		super(owner, title, data);
	}

	@Override
	protected JPanel createCenterPane() {
		transferPanel = new TransferPanel(data);
		transferPanel.addPropertyChangeListener(TransferPanel.OK_DISABLED_CAUSE_PROPERTY, new AutoUpdateOkButtonPropertyListener(this));
		return transferPanel;
	}

	@Override
	protected Transaction[] buildResult() {
		return transferPanel.getTransactions();
	}

	@Override
	protected String getOkDisabledCause() {
		return transferPanel.getOkDisabledCause();
	}

	public void setFromAccount(Account selectedAccount) {
		transferPanel.getFromPane().setAccount(selectedAccount);
		// Find which destination is the most probable
		// We will ignore transactions older than one year
		//TODO
//		Calendar oneYearBefore = new GregorianCalendar();
//		oneYearBefore.roll(Calendar.YEAR, false);
//		int timeLimit = DateUtils.dateToInteger(oneYearBefore.getTime());
//		ArrayList<Transaction> srcExpenses = new ArrayList<Transaction>();
//		ArrayList<Transaction> destinationReceipts = new ArrayList<Transaction>();
//		int lastDay = 0;
//		for (int i = 0; i < data.getTransactionsNumber(); i++) {
//			// We assume the transactions are sorted by date
//			Transaction transaction = data.getTransaction(i);
//			int day = transaction.getDateAsInteger();
//			if (day<lastDay) {
//				//FIXME transaction are sorted by value date, not by date !!!
//				throw new IllegalStateException("Date order is wrong");
//			}
//			if (day>=timeLimit) {
//				if (day == lastDay) {
//					// We are still in the same day transactions
//					srcExpenses.add(transaction); //TODO only if receipt is another account and expense if the source account
//				}
//			}
//			lastDay = day;
//		}
//		//TODO
//		Account account = transferPanel.getToPane().getAccountWidget().get();
	}
}
