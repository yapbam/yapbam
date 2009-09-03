package net.yapbam.ihm.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.MainFrame;

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