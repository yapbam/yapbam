package net.yapbam.gui.administration;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.widget.TabbedPane;
import net.yapbam.gui.widget.TabbedPaneWithOption;

import javax.swing.JLayeredPane;
import javax.swing.JCheckBox;
import java.awt.BorderLayout;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class AdministrationPanel extends JPanel {
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
	}
	
	TabbedPane getTabbedPane() {
		if (tabbedPane==null) {
			tabbedPane = new TabbedPane();
			panels = new AbstractAdministrationPanel[]{
					getPeriodicalTransactionsPanel(),
					new AccountAdministrationPanel(data.getGlobalData()),
					new CategoryListPanel(data.getGlobalData())
			};
			for (int i = 0; i < panels.length; i++) {
				tabbedPane.addTab(panels[i].getPanelTitle(), null, panels[i].getPanel(), panels[i].getPanelToolTip());
			}
		}
		return tabbedPane;
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
			layeredPane = new TabbedPaneWithOption(getTabbedPane(), getIgnoreFilter());
		}
		return layeredPane;
	}
	private JCheckBox getIgnoreFilter() {
		if (ignoreFilter == null) {
			ignoreFilter = new JCheckBox("Ignore filter");
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
