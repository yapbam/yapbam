package net.yapbam.gui.graphics.balancehistory;

import java.awt.Color;
import java.util.List;

import javax.swing.JTable;

import net.yapbam.data.Account;
import net.yapbam.data.AlertThreshold;
import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.data.event.AccountPropertyChangedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.gui.transactiontable.TransactionsPreferencePanel;
import net.yapbam.gui.util.CellRenderer;

@SuppressWarnings("serial")
public class BalanceHistoryCellRenderer extends CellRenderer {
	private Color alertColor;
	private Color receiptColor;
	private Color expenseColor;
	private AlertThreshold alertThreshold;
	private FilteredData data;
	
	public BalanceHistoryCellRenderer(FilteredData data) {
		this.data = data;
		this.alertColor = TablePreferencePanel.isHighlightAlerts() ? new Color(255, 100, 100) : null;
		if (TablePreferencePanel.isSameColors()) {
			Color[] colors = TransactionsPreferencePanel.getBackgroundColors();
			this.expenseColor = colors[0];
			this.receiptColor = colors[1];
		}
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
		if ((alertColor!=null) && !isSelected && (alertThreshold.getTrigger((Double)model.getValueAt(rowModel, model.getRemainingColumn()))!=0)) {
			return alertColor;
		} else {
			double amount = model.getTransaction(rowModel).getAmount();
			if (!isSelected && (receiptColor!=null) && (GlobalData.AMOUNT_COMPARATOR.compare(amount, 0.0)>0)) {
				return receiptColor;
			} else if (!isSelected && (expenseColor!=null) && (GlobalData.AMOUNT_COMPARATOR.compare(amount, 0.0)<=0)) {
				return expenseColor;
			} else {
				return super.getBackground(table, value, isSelected, hasFocus, rowModel, columnModel);
			}
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
