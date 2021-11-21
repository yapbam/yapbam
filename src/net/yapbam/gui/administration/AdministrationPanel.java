package net.yapbam.gui.administration;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.administration.AccountAdministrationPanel.CheckBookAlertListener;
import net.yapbam.gui.administration.filter.FilterListPanel;
import net.yapbam.gui.widget.TabbedPane;
import net.yapbam.util.HtmlUtils;
import net.yapbam.gui.widget.PanelWithOverlay;

import javax.swing.JLayeredPane;
import javax.swing.JCheckBox;
import java.awt.BorderLayout;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class AdministrationPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	static final String ALERT_PROPERTY = "alert";

	private FilteredData data;
	private AbstractAdministrationPanel[] panels;

	private TabbedPane tabbedPane;
	private JLayeredPane layeredPane;
	private JCheckBox ignoreFilter;

	private PeriodicalTransactionListPanel periodicalTransactionsPanel;
	
	/**
	 * This is the constructor
	 */
	public AdministrationPanel(FilteredData filteredData) {
		super();
		this.data = filteredData;
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		add(getLayeredPane(), BorderLayout.CENTER);
		getTabbedPane().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				getIgnoreFilter().setVisible(getTabbedPane().getSelectedComponent() instanceof PeriodicalTransactionListPanel);
			}
		});
	}
	
	TabbedPane getTabbedPane() {
		if (tabbedPane==null) {
			tabbedPane = new TabbedPane();
			panels = getPanels();
			for (int i = 0; i < panels.length; i++) {
				tabbedPane.addTab(panels[i].getPanelTitle(), null, panels[i].getPanel(), panels[i].getPanelToolTip());
			}
		}
		return tabbedPane;
	}

	private AbstractAdministrationPanel[] getPanels() {
		if (panels == null) {
			final AccountAdministrationPanel accountAdministrationPanel = new AccountAdministrationPanel(data.getGlobalData());
			accountAdministrationPanel.addCheckBookAlert(new CheckBookAlertListener() {
				@Override
				public void process(boolean hasAlert) {
					final String alert = hasAlert ? LocalizationData.get("AdministrationPlugIn.toolTip.checkbookAlert") : null;
					final String basicToolTip =  accountAdministrationPanel.getPanelToolTip();
					final int accountPanelIndex = getAccountPanelIndex();
					String toolTipText = hasAlert ? HtmlUtils.linesToHtml(true, basicToolTip, alert) : basicToolTip;
					tabbedPane.setToolTipTextAt(accountPanelIndex, toolTipText);
					tabbedPane.setIconAt(accountPanelIndex, hasAlert ? IconManager.get(Name.ALERT) : null);
					firePropertyChange(ALERT_PROPERTY, null, alert);
				}
			});
			panels = new AbstractAdministrationPanel[] { getPeriodicalTransactionsPanel(),
					accountAdministrationPanel,
					new CategoryListPanel(data.getGlobalData()),
					new FilterListPanel(data.getGlobalData()) };
		}
		return panels;
	}
	
	private int getAccountPanelIndex() {
		for (int i=0 ; i<getPanels().length; i++) {
			if (getTabbedPane().getComponentAt(i) instanceof AccountAdministrationPanel) {
				return i;
			}
		}
		return -1;
	}

	private PeriodicalTransactionListPanel getPeriodicalTransactionsPanel() {
		if (periodicalTransactionsPanel==null) {
			periodicalTransactionsPanel = new PeriodicalTransactionListPanel(data);
		}
		return periodicalTransactionsPanel;
	}

	void saveState() {
		YapbamState.INSTANCE.saveState(getTabbedPane(), this.getClass().getCanonicalName());
		for (int i = 0; i < panels.length; i++) {
			panels[i].saveState();
		}
	}

	void restoreState() {
		YapbamState.INSTANCE.restoreState(getTabbedPane(), this.getClass().getCanonicalName());
		for (int i = 0; i < panels.length; i++) {
			panels[i].restoreState();
		}
	}
	private JLayeredPane getLayeredPane() {
		if (layeredPane == null) {
			layeredPane = new PanelWithOverlay(getTabbedPane(), getIgnoreFilter());
		}
		return layeredPane;
	}
	private JCheckBox getIgnoreFilter() {
		if (ignoreFilter == null) {
			ignoreFilter = new JCheckBox(LocalizationData.get("PeriodicalTransactionManager.ignoreFilter")); //$NON-NLS-1$
			ignoreFilter.setToolTipText(LocalizationData.get("PeriodicalTransactionManager.ignoreFilter.tooltip")); //$NON-NLS-1$
			ignoreFilter.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					getPeriodicalTransactionsPanel().setIgnoreFilter(ignoreFilter.isSelected());
				}
			});
			ignoreFilter.setSelected(true);
		}
		return ignoreFilter;
	}
}
