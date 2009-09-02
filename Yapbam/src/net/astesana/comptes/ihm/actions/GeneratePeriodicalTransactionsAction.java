package net.astesana.comptes.ihm.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.astesana.comptes.ihm.LocalizationData;
import net.astesana.comptes.ihm.MainFrame;

@SuppressWarnings("serial")
public class GeneratePeriodicalTransactionsAction extends AbstractAction {
	private MainFrame frame;
	
	public GeneratePeriodicalTransactionsAction(MainFrame frame) {
		super(LocalizationData.get("MainMenu.Transactions.Periodical"));
        putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.Periodical.ToolTip"));
        this.frame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println ("not already implemented ... but it will come very, very soon"); //TODO
	}
}