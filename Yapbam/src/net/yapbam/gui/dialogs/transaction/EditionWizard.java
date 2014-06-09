package net.yapbam.gui.dialogs.transaction;

import net.yapbam.data.AbstractTransactionWizard;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;

public abstract class EditionWizard<T> extends AbstractTransactionWizard<T> {
	private String description;
	
	protected EditionWizard(GlobalData data, String description) {
		super(data);
		this.description = description;
	}

	@Override
	protected final boolean isValid(Transaction transaction) {
		return transaction.getDescription().equalsIgnoreCase(description);
	}
}
