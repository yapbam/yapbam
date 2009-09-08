package net.yapbam.ihm.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class NewPeriodicalTransactionAction extends AbstractAction {//LOCAL
	
	public NewPeriodicalTransactionAction() {
		super("Ajouter");
        putValue(SHORT_DESCRIPTION, "Ouvre le dialogue de création d'une opération");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		; //TODO
	}
}