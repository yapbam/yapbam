package net.yapbam.ihm.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.MainFrame;
import net.yapbam.ihm.dialogs.PeriodicTransactionsManagementDialog;

@SuppressWarnings("serial")
public class ManagePeriodicalTransactionsAction extends AbstractAction {
	private MainFrame frame;
	
	public ManagePeriodicalTransactionsAction(MainFrame frame) {
		super(LocalizationData.get("MainMenu.Transactions.ManagePeriodicals")); //$NON-NLS-1$
        putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.ManagePeriodicals.ToolTip")); //$NON-NLS-1$
        putValue(Action.MNEMONIC_KEY, (int)LocalizationData.getChar("MainMenu.Transactions.ManagePeriodicals.Mnemonic")); //$NON-NLS-1$
        this.frame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new PeriodicTransactionsManagementDialog(frame).setVisible(true); //TODO
	}
}