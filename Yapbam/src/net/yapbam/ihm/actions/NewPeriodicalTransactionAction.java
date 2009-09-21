package net.yapbam.ihm.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.yapbam.data.GlobalData;
import net.yapbam.ihm.dialogs.AbstractDialog;
import net.yapbam.ihm.dialogs.PeriodicalTransactionDialog;

@SuppressWarnings("serial")
public class NewPeriodicalTransactionAction extends AbstractAction {//LOCAL
	private GlobalData data;
	public NewPeriodicalTransactionAction(GlobalData data) {
		super("Ajouter");
        putValue(SHORT_DESCRIPTION, "Ouvre le dialogue de création d'une opération périodique");
        this.data = data;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		PeriodicalTransactionDialog.open(data, AbstractDialog.getOwnerWindow((Component) e.getSource()), null);
	}
}