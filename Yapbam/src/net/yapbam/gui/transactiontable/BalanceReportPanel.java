package net.yapbam.gui.transactiontable;

import javax.swing.JPanel;
import javax.swing.JToggleButton;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import net.yapbam.data.BalanceData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.ButtonGroup;

public class BalanceReportPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private BalanceReportField currentBalance;
	private BalanceReportField finalBalance;
	private BalanceReportField checkedBalance;

	public BalanceReportPanel() {
		setLayout(new GridLayout(1, 3, 0, 0));
		
		currentBalance = new BalanceReportField(LocalizationData.get("MainFrame.CurrentBalance")); //$NON-NLS-1$
		currentBalance.setToolTipText(LocalizationData.get("MainFrame.CurrentBalance.ToolTip")); //$NON-NLS-1$
		finalBalance = new BalanceReportField(LocalizationData.get("MainFrame.FinalBalance")); //$NON-NLS-1$
		finalBalance.setToolTipText(LocalizationData.get("MainFrame.FinalBalance.ToolTip")); //$NON-NLS-1$
		checkedBalance = new BalanceReportField(LocalizationData.get("MainFrame.CheckedBalance")); //$NON-NLS-1$
		checkedBalance.setToolTipText(LocalizationData.get("MainFrame.CheckedBalance.ToolTip")); //$NON-NLS-1$
		add(currentBalance);
		add(finalBalance);
		add(checkedBalance);
		ButtonGroup group = new ButtonGroup();
		group.add(checkedBalance);
		group.add(currentBalance);
		group.add(finalBalance);
		group.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				JToggleButton b = (JToggleButton) arg;
				System.out.println ((b==null?"No button":b.getText())+" is selected");
			}
		});
	}

	public void updateBalances(BalanceData balance) {
		currentBalance.setValue(balance.getCurrentBalance());
		finalBalance.setValue(balance.getFinalBalance());
		checkedBalance.setValue(balance.getCheckedBalance());
	}
}
