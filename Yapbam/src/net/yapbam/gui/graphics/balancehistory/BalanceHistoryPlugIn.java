package net.yapbam.gui.graphics.balancehistory;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;

public class BalanceHistoryPlugIn extends AbstractPlugIn {
	private BalanceHistoryPane panel;
	private FilteredData data;

	public BalanceHistoryPlugIn(FilteredData filteredData, Object restartData) {
		this.data = filteredData;
		this.panel = new BalanceHistoryPane(data);
		this.setPanelTitle(LocalizationData.get("BalanceHistory.title"));
		this.setPanelToolTip(LocalizationData.get("BalanceHistory.toolTip"));
		this.panel.addPropertyChangeListener(BalanceHistoryPane.FIRST_ALERT, new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setPanelIcon((evt.getNewValue()!=null?IconManager.ALERT:null));
				setPanelToolTip("TODO"); //TODO
			}
		});
	}
	
	@Override
	public void setDisplayed(boolean displayed) {
		super.setDisplayed(displayed);
		this.panel.setDisplayed(displayed);
	}


	@Override
	public JPanel getPanel() {
		return panel;
	}
}
