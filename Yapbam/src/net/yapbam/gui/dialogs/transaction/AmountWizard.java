package net.yapbam.gui.dialogs.transaction;

import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.preferences.EditionWizardSettings;
import net.yapbam.gui.preferences.EditionWizardSettings.Mode;
import net.yapbam.gui.preferences.EditionWizardSettings.Source;

public class AmountWizard extends EditionWizard<Double> {
	private Double lastAmount;
	private int lastDate = 0;
	private Source source;
	private boolean isReceipt;

	public AmountWizard(GlobalData data, String description, double currentAmount, boolean isReceipt) {
		super(data, description);
		EditionWizardSettings settings = Preferences.INSTANCE.getEditionSettings().getEditionWizardSettings();
		Mode mode = settings.getMode();
		if (Mode.WHEN_NULL.equals(mode) && (GlobalData.AMOUNT_COMPARATOR.compare(currentAmount, 0.0)!=0)) {
			mode = Mode.NEVER;
		}
		source = Mode.NEVER.equals(mode) ? null : settings.getSource();
		this.isReceipt = isReceipt;
	}

	@Override
	protected Double getValue(Transaction transaction) {
		if ((source==null) || (isReceipt!=transaction.getAmount()>0)) {
			return null;
		} else if (Source.MOST_PROBABLE.equals(source)) {
			return transaction.getAmount();
		} else {
			if (lastDate<transaction.getDateAsInteger()) {
				lastAmount = transaction.getAmount();
			}
			return null;
		}
	}

	@Override
	public Double get() {
		if (source==null) {
			return null;
		} else {
			// super.get should be called in order to have transactions inspected using the getValue method
			Double mostPropable = super.get();
			return Source.MOST_PROBABLE.equals(source) ? mostPropable : lastAmount;
		}
	}
}
