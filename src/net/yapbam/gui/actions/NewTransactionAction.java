package net.yapbam.gui.actions;

import java.awt.event.ActionEvent;

import net.yapbam.data.FilteredData;

@SuppressWarnings("serial")
public class NewTransactionAction extends DynamicNewTransactionAction {
	private boolean massMode;
	
	public NewTransactionAction(FilteredData data, boolean massMode) {
		super(data);
		this.massMode = massMode;
	}

	@Override
	protected boolean getMassMode(ActionEvent e) {
		return massMode;
	}
}