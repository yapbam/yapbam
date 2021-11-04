package net.yapbam.gui.administration;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fathzer.jlocal.Formatter;

import net.yapbam.data.Account;
import net.yapbam.data.FilteredData;
import net.yapbam.data.event.AccountAddedEvent;
import net.yapbam.data.event.AccountPropertyChangedEvent;
import net.yapbam.data.event.AccountRemovedEvent;
import net.yapbam.data.event.CheckbookAddedEvent;
import net.yapbam.data.event.CheckbookPropertyChangedEvent;
import net.yapbam.data.event.CheckbookRemovedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.TransactionsAddedEvent;
import net.yapbam.data.event.TransactionsRemovedEvent;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.administration.filter.FilterListPanel;
import net.yapbam.gui.widget.PanelWithOverlay;
import net.yapbam.gui.widget.TabbedPane;

public class AdministrationPanel extends JPanel implements AbstractAlertPanel {
	private static final long serialVersionUID = 1L;

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
		this.data.getGlobalData().addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if ((event instanceof EverythingChangedEvent) || (event instanceof AccountAddedEvent)
						|| (event instanceof AccountRemovedEvent) || (event instanceof AccountPropertyChangedEvent)
						|| (event instanceof TransactionsAddedEvent) || (event instanceof TransactionsRemovedEvent)
						|| (event instanceof CheckbookAddedEvent) || (event instanceof CheckbookRemovedEvent)
						|| (event instanceof CheckbookPropertyChangedEvent)) {
					hasAlert();
				}
			}
		});
		hasAlert();
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
			panels = new AbstractAdministrationPanel[] { getPeriodicalTransactionsPanel(),
					new AccountAdministrationPanel(data.getGlobalData()), new CategoryListPanel(data.getGlobalData()),
					new FilterListPanel(data.getGlobalData()) };
		}
		return panels;
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

	@Override
	public boolean hasAlert() {
		boolean result = false;
		if (this.data != null) {
			for (int i = 0; i < this.data.getGlobalData().getAccountsNumber() && !result; i++) {
				Account account = this.data.getGlobalData().getAccount(i);
				if (account != null) {
					for (int c = 0; c < account.getCheckbooksNumber() && !result; c++) {
						if (account.getCheckbook(c).getRemaining() > 0 && //
								account.getCheckbook(c).getRemaining() <= account.getCheckNumberAlertThreshold()) {
							result = true;
						}
					}
				}
			}
		}
		Icon panelIcon = result ? IconManager.get(Name.ALERT) : null;
		for (int i = 0; i < getPanels().length; i++) {
			if (getPanels()[i] instanceof AccountAdministrationPanel) {
				getTabbedPane().setIconAt(i, panelIcon);
				getTabbedPane().setToolTipTextAt(i, result ? //
						Formatter.format(LocalizationData.get("AccountManager.toolTip.checkbookAlert"), LocalizationData.get("AccountManager.toolTip")) : //
						getPanels()[i].getPanelToolTip() //
				);
			}
		}
		return result;
	}

}
