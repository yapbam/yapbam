package net.yapbam.statementview;

import javax.swing.JPanel;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.AbstractPlugIn;

public class StatementViewPlugin extends AbstractPlugIn {
	
	private StatementViewPanel panel;

	public StatementViewPlugin(FilteredData data, Object state) {
		this.panel = new StatementViewPanel(data.getGlobalData());
		this.setPanelTitle("Relevés de comptes"); //LOCAL
		this.setPanelToolTip("Vue relevés de comptes"); //TODO
	}

	@Override
	public JPanel getPanel() {
		return this.panel;
	}

}
