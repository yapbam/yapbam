package net.yapbam.gui.transactiontable;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.Utils;

import net.yapbam.data.GlobalData;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.PeriodicalTransactionsAddedEvent;
import net.yapbam.data.event.PeriodicalTransactionsRemovedEvent;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.periodicaltransaction.GeneratePeriodicalTransactionsDialog;

@SuppressWarnings("serial")
public class GeneratePeriodicalTransactionsAction extends AbstractAction {
	private GlobalData data;
	
	public GeneratePeriodicalTransactionsAction(GlobalData data, boolean isMenu) {
		super(LocalizationData.get("MainMenu.Transactions.Periodical")); //$NON-NLS-1$
		if (isMenu) {
			putValue(Action.MNEMONIC_KEY, (int) LocalizationData.getChar("MainMenu.Transactions.Periodical.Mnemonic")); //$NON-NLS-1$
		}
		this.data = data;
		if (data!=null) {
			data.addListener(new DataListener() {
				@Override
				public void processEvent(DataEvent event) {
					if ((event instanceof EverythingChangedEvent) || (event instanceof PeriodicalTransactionsRemovedEvent) ||
							(event instanceof PeriodicalTransactionsAddedEvent)) {
				        refreshEnabled();
					}
				}
			});
			refreshEnabled();
		}
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		new GeneratePeriodicalTransactionsDialog(Utils.getOwnerWindow((Component) e.getSource()), data).setVisible(true);
	}

	private void refreshEnabled() {
		boolean enabled = data.getPeriodicalTransactionsNumber()!=0;
		setEnabled(enabled);
		StringBuilder toolTip = new StringBuilder();
		toolTip.append("<html>"); //$NON-NLS-1$
		toolTip.append(LocalizationData.get("MainMenu.Transactions.Periodical.ToolTip")); //$NON-NLS-1$
		Icon icon = null;
		if (!enabled) {
			toolTip.append("<br>").append(LocalizationData.get("MainMenu.Transactions.Periodical.disabled.tooltip.line1")); //$NON-NLS-1$ //$NON-NLS-2$
			toolTip.append("<br>").append(Formatter.format(LocalizationData.get("MainMenu.Transactions.Periodical.disabled.tooltip.line2"), //$NON-NLS-1$ //$NON-NLS-2$
					LocalizationData.get("PeriodicalTransactionManager.title"),LocalizationData.get("AdministrationPlugIn.title"))); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			if (data.hasPendingPeriodicalTransactions(new Date())) {
				toolTip.append("<br>").append(LocalizationData.get("GeneratePeriodicalTransactionsDialog.alert")); //$NON-NLS-1$ //$NON-NLS-2$
				icon = IconManager.get(Name.ALERT);
			}
		}
		toolTip.append("</html>"); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, toolTip.toString());
		this.putValue(SMALL_ICON, icon);
		this.putValue(LARGE_ICON_KEY, icon);
	}
}