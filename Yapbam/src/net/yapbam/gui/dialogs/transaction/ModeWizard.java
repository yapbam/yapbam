package net.yapbam.gui.dialogs.transaction;

import java.util.HashSet;
import java.util.Set;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Mode;
import net.yapbam.data.Transaction;
import net.yapbam.date.helpers.DateStepper;

public class ModeWizard extends EditionWizard<Mode> {
	private Set<String> allowedModes;

	public ModeWizard(GlobalData data, String description, Account account, boolean receipt) {
		super(data, description);
		// As mode are attached to accounts, it would be unsafe to try to
		// deduce modes on accounts different from the current one.
		// allowedModes will contains modes available for this account and the expense/receipt attribute of the transaction
		allowedModes = new HashSet<String>();
		for (int i = 0; i < account.getModesNumber(); i++) {
			Mode mode = account.getMode(i);
			DateStepper dateStepper = receipt ? mode.getReceiptVdc() : mode.getExpenseVdc();
			if (dateStepper!=null) {
				allowedModes.add(mode.getName());
			}
		}
	}

	@Override
	protected Mode getValue(Transaction transaction) {
		return allowedModes.contains(transaction.getMode().getName()) ? transaction.getMode() : null;
	}

}
