package net.yapbam.ihm.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class DeletePeriodicalTransactionAction extends AbstractAction {//LOCAL
	
	public DeletePeriodicalTransactionAction() {
		super("Supprimer");
        putValue(SHORT_DESCRIPTION, "Supprime l'opération périodique sélectionnée");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		; //TODO
	}
}