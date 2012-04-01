package net.yapbam.gui.dialogs;

import net.astesana.ajlib.swing.Utils;
import net.yapbam.data.Account;
import net.yapbam.data.Mode;
import net.yapbam.date.helpers.DateStepper;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.AbstractSelector;

@SuppressWarnings("serial")
/** A category selector widget. */
public class ModeWidget extends AbstractSelector<Mode, ModeWidgetParams> {
	public static final String MODE_PROPERTY = "mode"; //$NON-NLS-1$
	
	public ModeWidget(ModeWidgetParams data) {
		super(data);
	}

	@Override
	protected String getLabel() {
		return LocalizationData.get("TransactionDialog.mode");
	}
	
	@Override
	protected String getComboTip() {
		return LocalizationData.get("TransactionDialog.mode.tooltip");
	}
	
	@Override
	protected String getNewButtonTip() {
		return LocalizationData.get("TransactionDialog.mode.new.tooltip");
	}

	@Override
	protected String getPropertyName() {
		return MODE_PROPERTY;
	}

	@Override
	protected void populateCombo() {
		if (getParameters()!=null) {
			Account account = getParameters().getAccount();
			for (int i = 0; i < account.getModesNumber(); i++) {
				Mode mode = account.getMode(i);
				DateStepper ds = getParameters().isExpense()?mode.getExpenseVdc():mode.getReceiptVdc();
				if (ds!=null)	getCombo().addItem(mode);
			}
		}
	}
		
	@Override
	protected Object getDefaultRenderedValue(Mode mode) {
		return mode==null ? mode : mode.getName();
	}

	@Override
	protected Mode createNew() {
		if (getParameters()!=null) {
			return ModeDialog.open(getParameters().getGlobalData(), getParameters().getAccount(), Utils.getOwnerWindow(this));
		} else {
			return null;
		}
	}
//
//	@Override
//	protected void setSelectionAfterRefresh(Mode old) {
//		super.setSelectionAfterRefresh(old);
//		if (getCombo().contains(old)) {
//			firePropertyChange(MODE_PROPERTY, old, getCombo().getSelectedItem());
//		}
//	}
}
