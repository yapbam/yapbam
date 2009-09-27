package net.yapbam.ihm.transactiontable;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.yapbam.data.GlobalData;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.PeriodicalTransactionAddedEvent;
import net.yapbam.data.event.PeriodicalTransactionRemovedEvent;
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.dialogs.AbstractDialog;
import net.yapbam.ihm.dialogs.GeneratePeriodicalTransactionsDialog;

@SuppressWarnings("serial")
public class GeneratePeriodicalTransactionsAction extends AbstractAction {
	private TransactionTable table;
	
	public GeneratePeriodicalTransactionsAction(TransactionTable table) {
		super(LocalizationData.get("MainMenu.Transactions.Periodical")); //$NON-NLS-1$
        putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.Periodical.ToolTip")); //$NON-NLS-1$
        putValue(Action.MNEMONIC_KEY, (int)LocalizationData.getChar("MainMenu.Transactions.Periodical.Mnemonic")); //$NON-NLS-1$
        this.table = table;
        table.getGlobalData().addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if ((event instanceof EverythingChangedEvent) || (event instanceof PeriodicalTransactionRemovedEvent) ||
						(event instanceof PeriodicalTransactionAddedEvent)) {
			        setEnabled(((GlobalData)event.getSource()).getPeriodicalTransactionsNumber()!=0);
				}
			}
		});
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new GeneratePeriodicalTransactionsDialog(AbstractDialog.getOwnerWindow((Component) e.getSource()), table.getGlobalData()).setVisible(true);
	}
}