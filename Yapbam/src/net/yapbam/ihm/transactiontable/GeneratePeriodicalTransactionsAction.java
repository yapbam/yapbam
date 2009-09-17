package net.yapbam.ihm.transactiontable;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.yapbam.ihm.LocalizationData;

@SuppressWarnings("serial")
public class GeneratePeriodicalTransactionsAction extends AbstractAction {
	private TransactionTable table;
	
	public GeneratePeriodicalTransactionsAction(TransactionTable table) {
		super(LocalizationData.get("MainMenu.Transactions.Periodical")); //$NON-NLS-1$
        putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.Periodical.ToolTip")); //$NON-NLS-1$
        putValue(Action.MNEMONIC_KEY, (int)LocalizationData.getChar("MainMenu.Transactions.Periodical.Mnemonic")); //$NON-NLS-1$
        this.table = table;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println ("not already implemented ... but it will come very, very soon"); //TODO
	}
}