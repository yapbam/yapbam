package net.yapbam.gui.transactiontable;

import javax.swing.JPanel;
import javax.swing.JToggleButton;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import net.yapbam.data.BalanceData;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.ButtonGroup;

public class BalanceReportPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public enum Selection {NONE, CURRENT, FINAL, CHECKED};
	
	private BalanceData balance;

	private BalanceReportField currentBalance;
	private BalanceReportField finalBalance;
	private BalanceReportField checkedBalance;
	private ButtonGroup group;

	public BalanceReportPanel(BalanceData balance) {
		this.balance = balance;
		balance.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				updateBalances();
			}
		});
		
		setLayout(new GridLayout(1, 3, 0, 0));
		
		currentBalance = new BalanceReportField(LocalizationData.get("MainFrame.CurrentBalance")); //$NON-NLS-1$
		finalBalance = new BalanceReportField(LocalizationData.get("MainFrame.FinalBalance")); //$NON-NLS-1$
		checkedBalance = new BalanceReportField(LocalizationData.get("MainFrame.CheckedBalance")); //$NON-NLS-1$
		add(currentBalance);
		add(finalBalance);
		add(checkedBalance);
		group = new ButtonGroup();
		group.add(checkedBalance);
		group.add(currentBalance);
		group.add(finalBalance);
		group.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				updateBalances();
			}
		});
		updateBalances();
	}

	private void updateBalances() {
		JToggleButton selected = group.getSelected();
		if (selected==null) {
			currentBalance.setValue(balance.getCurrentBalance(), true);
			finalBalance.setValue(balance.getFinalBalance(), true);
			checkedBalance.setValue(balance.getCheckedBalance(), true);
		} else if (selected==currentBalance) {
			currentBalance.setValue(balance.getCurrentBalance(), true);
			finalBalance.setValue(balance.getFinalBalance()-balance.getCurrentBalance(), false);
			checkedBalance.setValue(balance.getCheckedBalance()-balance.getCurrentBalance(), false);
		} else if (selected==finalBalance) {
			currentBalance.setValue(balance.getCurrentBalance()-balance.getFinalBalance(), false);
			finalBalance.setValue(balance.getFinalBalance(), true);
			checkedBalance.setValue(balance.getCheckedBalance()-balance.getFinalBalance(), false);
		} else if (selected==checkedBalance) {			
			currentBalance.setValue(balance.getCurrentBalance()-balance.getCheckedBalance(), false);
			finalBalance.setValue(balance.getFinalBalance()-balance.getCheckedBalance(), false);
			checkedBalance.setValue(balance.getCheckedBalance(), true);
		}
		currentBalance.setToolTipText(getTooltip(currentBalance, LocalizationData.get("MainFrame.CurrentBalance.ToolTip"))); //$NON-NLS-1$
		finalBalance.setToolTipText(getTooltip(finalBalance,LocalizationData.get("MainFrame.FinalBalance.ToolTip"))); //$NON-NLS-1$
		checkedBalance.setToolTipText(getTooltip(checkedBalance,LocalizationData.get("MainFrame.CheckedBalance.ToolTip"))); //$NON-NLS-1$
	}
	
	private String getTooltip(JToggleButton button, String baseTip) {
		StringBuilder b = new StringBuilder("<html>").append(baseTip).append("<br>"); //$NON-NLS-1$ //$NON-NLS-2$
		JToggleButton selected = group.getSelected();
		if ((selected!=null)&&(selected!=button)) b.append(LocalizationData.get("MainFrame.BalancePanel.Relative.ToolTip")).append("<br><br>");
		b.append(LocalizationData.get(selected==button?"MainFrame.BalancePanel.Selected.ToolTip":"MainFrame.BalancePanel.NotSelected.ToolTip")); //$NON-NLS-1$ //$NON-NLS-2$
		b.append("</html>"); //$NON-NLS-1$
		return b.toString();
	}
	
	public Selection getSelected() {
		JToggleButton selected = group.getSelected();
		if (selected==currentBalance) return Selection.CURRENT;
		if (selected==checkedBalance) return Selection.CHECKED;
		if (selected==finalBalance) return Selection.FINAL;
		return Selection.NONE;
	}

	public void setSelected(Selection selected) {
		JToggleButton button = null;
		if (selected==Selection.CHECKED) {
			button = checkedBalance;
		} else if (selected==Selection.CURRENT) {
			button = currentBalance;
		} else if (selected==Selection.FINAL) {
			button = finalBalance;
		}
		group.setSelected(button);
	}
}
