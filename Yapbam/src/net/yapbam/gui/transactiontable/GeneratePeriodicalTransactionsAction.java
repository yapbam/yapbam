package net.yapbam.gui.transactiontable;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.yapbam.data.FilteredData;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.PeriodicalTransactionsAddedEvent;
import net.yapbam.data.event.PeriodicalTransactionsRemovedEvent;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.GeneratePeriodicalTransactionsDialog;
import net.yapbam.gui.util.AbstractDialog;

@SuppressWarnings("serial")
public class GeneratePeriodicalTransactionsAction extends AbstractAction {
	private FilteredData data;
	
	public GeneratePeriodicalTransactionsAction(FilteredData filteredData) {
		super(LocalizationData.get("MainMenu.Transactions.Periodical")); //$NON-NLS-1$
		putValue(Action.MNEMONIC_KEY, (int) LocalizationData.getChar("MainMenu.Transactions.Periodical.Mnemonic")); //$NON-NLS-1$
		this.data = filteredData;
		filteredData.getGlobalData().addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if ((event instanceof EverythingChangedEvent) || (event instanceof PeriodicalTransactionsRemovedEvent) ||
						(event instanceof PeriodicalTransactionsAddedEvent)) {
			        refreshEnabled(false);
				}
			}
		});
		refreshEnabled(true);
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		new GeneratePeriodicalTransactionsDialog(AbstractDialog.getOwnerWindow((Component) e.getSource()), data).setVisible(true);
	}

	private void refreshEnabled(boolean forced) {
		boolean enabled = data.getGlobalData().getPeriodicalTransactionsNumber()!=0;
		if (forced || (enabled!=this.isEnabled())) {
			setEnabled(enabled);
			StringBuilder toolTip = new StringBuilder();
			toolTip.append("<html>");
			toolTip.append(LocalizationData.get("MainMenu.Transactions.Periodical.ToolTip")); //$NON-NLS-1$
			if (!enabled) {
				toolTip.append("<br>").append("Damned, it is grayed out");
			}
			toolTip.append("</html>");
			putValue(SHORT_DESCRIPTION, toolTip.toString());
		}
	}
}