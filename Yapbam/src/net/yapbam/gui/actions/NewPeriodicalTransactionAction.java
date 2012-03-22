package net.yapbam.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.astesana.ajlib.swing.dialog.AbstractDialog;
import net.yapbam.data.FilteredData;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.PeriodicalTransactionDialog;

@SuppressWarnings("serial")
public class NewPeriodicalTransactionAction extends AbstractAction {
	private FilteredData data;
	public NewPeriodicalTransactionAction(FilteredData data) {
		super(LocalizationData.get("GenericButton.new"), IconManager.NEW); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, LocalizationData.get("PeriodicalTransactionManager.new.toolTip")); //$NON-NLS-1$
		this.data = data;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		PeriodicalTransactionDialog.open(data, AbstractDialog.getOwnerWindow((Component) e.getSource()), null, false);
	}
}