package net.yapbam.gui.graphics.balancehistory;

import java.awt.Color;
import java.util.List;

import javax.swing.JTable;

import net.yapbam.data.Account;
import net.yapbam.data.AlertThreshold;
import net.yapbam.data.FilteredData;
import net.yapbam.data.event.AccountPropertyChangedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.gui.util.CellRenderer;

@SuppressWarnings("serial")
public class BalanceHistoryCellRenderer extends CellRenderer {
	private static final Color ALERT_COLOR = new Color(255, 100, 100);
	private AlertThreshold alertThreshold;
	private FilteredData data;
	
	public BalanceHistoryCellRenderer(FilteredData data) {
		this.data = data;
		refreshMinMax();
		data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				boolean needRefresh = ((event instanceof EverythingChangedEvent) || (event instanceof AccountPropertyChangedEvent));
				if (needRefresh) {
					refreshMinMax();
				}
			}
		});
	}
	
	@Override
	protected Color getBackground(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowModel, int columnModel) {
		BalanceHistoryModel model = (BalanceHistoryModel) table.getModel();
		if (!isSelected && (alertThreshold.getTrigger((Double)model.getValueAt(rowModel, 9))!=0)) {
			return ALERT_COLOR;
		} else {
			return super.getBackground(table, value, isSelected, hasFocus, rowModel, columnModel);
		}
	}

	private void refreshMinMax() {
		Account singleAccount = null;
		if (data.getGlobalData().getAccountsNumber()==1) {
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
}
