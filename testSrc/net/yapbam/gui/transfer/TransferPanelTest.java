package net.yapbam.gui.transfer;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import net.yapbam.data.Account;
import net.yapbam.data.Category;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Mode;
import net.yapbam.data.SubTransaction;
import net.yapbam.data.Transaction;

public class TransferPanelTest {

	@Test
	public void toTransactionKeepsItsOwnSubTransactionsNotTheFromOnesNegated() {
		GlobalData data = new GlobalData();
		Account from = new Account("from", 0.0); //$NON-NLS-1$
		Account to = new Account("to", 0.0); //$NON-NLS-1$
		data.add(from);
		data.add(to);

		TransferPanel panel = new TransferPanel(data);

		List<SubTransaction> subTransactions = new ArrayList<SubTransaction>();
		subTransactions.add(new SubTransaction(30.0, "food", Category.UNDEFINED)); //$NON-NLS-1$
		Transaction fixture = new Transaction(0, null, "fixture", null, 100.0, from, Mode.UNDEFINED, //$NON-NLS-1$
				Category.UNDEFINED, 0, null, subTransactions);
		panel.getSubTransactionsPanel().fill(fixture);

		Transaction[] result = panel.getTransactions();

		assertEquals("The 'from' transaction sub-amount must be negated", //$NON-NLS-1$
				-30.0, result[0].getSubTransaction(0).getAmount(), 0.0001);
		assertEquals("The 'to' transaction must keep its own (non-negated) sub-amount", //$NON-NLS-1$
				30.0, result[1].getSubTransaction(0).getAmount(), 0.0001);
	}
}
