package net.yapbam.gui.graphics.balancehistory;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JTable;

import net.yapbam.data.Account;
import net.yapbam.data.AlertThreshold;
import net.yapbam.data.FilteredData;
import net.yapbam.data.event.AccountPropertyChangedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.gui.transactiontable.TransactionTablePainter;

final class BalanceHistoryTablePainter extends TransactionTablePainter {
	private static final long serialVersionUID = 1L;
	private Color alertColor;
	private AlertThreshold alertThreshold;
	private FilteredData data;
	

	BalanceHistoryTablePainter(FilteredData data) {
		this.alertColor = TablePreferencePanel.isHighlightAlerts() ? new Color(255, 100, 100) : null;
		this.data = data;
		refreshMinMax();
		if (data!=null) {
			data.addListener(new DataListener() {
				@Override
				public void processEvent(DataEvent event) {
					boolean needRefresh = (event instanceof EverythingChangedEvent) || (event instanceof AccountPropertyChangedEvent);
					if (needRefresh) {
						refreshMinMax();
					}
				}
			});
		}
	}
	
	private void refreshMinMax() {
		Account singleAccount = null;
		if (data==null) {
			// Do nothing, account is null
		} else if (data.getGlobalData().getAccountsNumber()==1) {
			singleAccount = data.getGlobalData().getAccount(0);
		} else {
			List<Account> validAccounts = data.getFilter().getValidAccounts();
			if ((validAccounts!=null) && (validAccounts.size()==1)) {
				singleAccount = validAccounts.get(0);
			}
		}
		if (singleAccount==null) {
			this.alertThreshold = new AlertThreshold(0.0, Double.MAX_VALUE);
		} else {
			this.alertThreshold = singleAccount.getAlertThreshold();
		}
	}

	@Override
	public void setRowLook(Component renderer, JTable table, int row, boolean isSelected) {
		super.setRowLook(renderer, table, row, isSelected);
		BalanceHistoryModel model = (BalanceHistoryModel) table.getModel();
		if ((alertColor!=null) && !isSelected && (alertThreshold.getTrigger(model.getDayBalance(row))!=0)) {
			renderer.setBackground(alertColor);
		}
		
	}
}