package net.yapbam.gui.transactiontable;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import net.yapbam.data.FilteredData;
import net.yapbam.data.Transaction;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.PeriodicalTransactionsAddedEvent;
import net.yapbam.data.event.PeriodicalTransactionsRemovedEvent;
import net.yapbam.gui.IconManager;
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
		setEnabled(enabled);
		StringBuilder toolTip = new StringBuilder();
		toolTip.append("<html>"); //$NON-NLS-1$
		toolTip.append(LocalizationData.get("MainMenu.Transactions.Periodical.ToolTip")); //$NON-NLS-1$
		Icon icon = null;
		if (!enabled) {
			toolTip.append("<br>").append(LocalizationData.get("MainMenu.Transactions.Periodical.disabled.tooltip.line1")); //$NON-NLS-1$ //$NON-NLS-2$
			toolTip.append("<br>").append(MessageFormat.format(LocalizationData.get("MainMenu.Transactions.Periodical.disabled.tooltip.line2"), //$NON-NLS-1$ //$NON-NLS-2$
					LocalizationData.get("PeriodicalTransactionManager.title"),LocalizationData.get("AdministrationPlugIn.title"))); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			Transaction[] transactions = data.getGlobalData().generateTransactionsFromPeriodicals(new Date());
			if (transactions.length!=0) {
				toolTip.append("<br>").append(LocalizationData.get("GeneratePeriodicalTransactionsDialog.alert")); //$NON-NLS-1$ //$NON-NLS-2$
				icon = IconManager.ALERT;
			}
		}
		toolTip.append("</html>"); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, toolTip.toString());
		this.putValue(SMALL_ICON, icon);
		this.putValue(LARGE_ICON_KEY, icon);
	}
}