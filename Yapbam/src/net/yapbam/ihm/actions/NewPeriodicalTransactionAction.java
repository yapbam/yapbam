package net.yapbam.ihm.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.yapbam.data.GlobalData;
import net.yapbam.ihm.IconManager;
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.dialogs.AbstractDialog;
import net.yapbam.ihm.dialogs.PeriodicalTransactionDialog;

@SuppressWarnings("serial")
public class NewPeriodicalTransactionAction extends AbstractAction {
	private GlobalData data;
	public NewPeriodicalTransactionAction(GlobalData data) {
		super(LocalizationData.get("GenericButton.new"), IconManager.NEW); //$NON-NLS-1$
        putValue(SHORT_DESCRIPTION, LocalizationData.get("PeriodicalTransactionManager.new.toolTip")); //$NON-NLS-1$
        this.data = data;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		PeriodicalTransactionDialog.open(data, AbstractDialog.getOwnerWindow((Component) e.getSource()), null);
	}
}