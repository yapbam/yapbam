package net.yapbam.gui.graphics.balancehistory;

import java.awt.print.Printable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;

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
		this.setPanelTitle(LocalizationData.get("BalanceHistory.title"));
		this.panel = new BalanceHistoryPane(data);
		this.setPanelToolTip(LocalizationData.get("BalanceHistory.toolTip"));
		PropertyChangeListener listener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Date first = (Date) evt.getNewValue();
				setPanelIcon((first!=null?IconManager.ALERT:null));
				String tooltip;
				tooltip = LocalizationData.get("BalanceHistory.toolTip"); //$NON-NLS-1$
				if (first!=null) {
					String dateStr = DateFormat.getDateInstance(DateFormat.SHORT, LocalizationData.getLocale()).format(first);
					tooltip = tooltip.replace("'", "''"); // single quotes in message pattern are escape characters. So, we have to replace them with "double simple quote" //$NON-NLS-1$ //$NON-NLS-2$
					String pattern = "<html>"+tooltip+"<br>"+LocalizationData.get("BalanceHistory.alertTooltipAdd")+"</html>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					tooltip = MessageFormat.format(pattern, "<b>"+dateStr+"</b>"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				setPanelToolTip(tooltip);
			}
		};
		this.panel.addPropertyChangeListener(BalanceHistoryPane.FIRST_ALERT, listener);
		if (this.panel.getFirstAlert()!=null) {
			listener.propertyChange(new PropertyChangeEvent(this.panel, BalanceHistoryPane.FIRST_ALERT, null, this.panel.getFirstAlert()));
		}
		this.panel.addPropertyChangeListener(BalanceHistoryPane.SELECTED_PANEL, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setPrintingSupported((Integer)evt.getNewValue()==1);
			}
		});
	}
	
	@Override
	public void setDisplayed(boolean displayed) {
		super.setDisplayed(displayed);
		this.panel.changeDisplayed();
	}


	@Override
	public JPanel getPanel() {
		return panel;
	}

	@Override
	public void saveState() {
		this.panel.saveState();
	}

	@Override
	public void restoreState() {
		this.panel.restoreState();
	}

	@Override
	protected Printable getPrintable() {
		return panel.getPrintable();
	}
}
