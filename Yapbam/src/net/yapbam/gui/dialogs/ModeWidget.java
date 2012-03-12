package net.yapbam.gui.dialogs;

import net.yapbam.data.GlobalData;
import net.yapbam.data.Mode;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.AbstractSelector;

@SuppressWarnings("serial")
/** A category selector widget. */
public class ModeWidget extends AbstractSelector<Mode, GlobalData> {
	public static final String MODE_PROPERTY = "mode"; //$NON-NLS-1$
	
	public ModeWidget(GlobalData data) {
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
			for (int i = 0; i < getParameters().getCategoriesNumber(); i++) {
				getCombo().addItem(getParameters().getCategory(i));
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
			return ModeDialog.open(getParameters(), null, CategoryDialog.getOwnerWindow(this)); //TODO
		} else {
			return null;
		}
	}
}
