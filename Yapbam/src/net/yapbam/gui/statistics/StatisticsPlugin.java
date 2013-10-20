package net.yapbam.gui.statistics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.util.ResourceBundleWrapper;

import net.yapbam.data.Category;
import net.yapbam.data.FilteredData;
import net.yapbam.data.SubTransaction;
import net.yapbam.data.Transaction;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.filter.FilterView;
import net.yapbam.gui.widget.TabbedPane;

public class StatisticsPlugin extends AbstractPlugIn {
	private FilteredData data;
	private boolean displayed;
	private TreeMap<Category, Summary> categoryToAmount;
	private PieChartPanel pie;
	private BarChartPanel bar;
	private TabbedPane tabbedPane;
	private JCheckBox groupSubCategories;
	
	public StatisticsPlugin(FilteredData filteredData, Object restartData) {
		this.data = filteredData;
		categoryToAmount = new TreeMap<Category, Summary>();
		this.data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if (displayed) buildSummaries();
			}
		});
		this.setPanelTitle(LocalizationData.get("StatisticsPlugin.title")); //$NON-NLS-1$
		this.setPanelToolTip(LocalizationData.get("StatisticsPlugin.tooltip")); //$NON-NLS-1$
		this.setPrintingSupported(true);
		// Hack to set the right Locale for JFreeChart panels (it seems there's a bug in JFreeChart: setLocale doesn't refresh the popup menus).
		new SimpleChartPanel();
		// FIXME The properties dialog is not localized if the locale has just been set using the preference panel.
	}

	@SuppressWarnings("serial")
	private static class SimpleChartPanel extends ChartPanel {
		public SimpleChartPanel() {
			super(null);
			localizationResources = ResourceBundleWrapper.getBundle("org.jfree.chart.LocalizationBundle", LocalizationData.getLocale()); //$NON-NLS-1$
		}
	}
	
	@Override
	public JPanel getPanel() {
		tabbedPane = new TabbedPane();
		this.bar = new BarChartPanel(categoryToAmount);
		tabbedPane.addTab(LocalizationData.get("StatisticsPlugin.bar.tabname"), null, this.bar, LocalizationData.get("StatisticsPlugin.bar.tooltip")); //$NON-NLS-1$ //$NON-NLS-2$
		this.pie = new PieChartPanel(categoryToAmount);
		tabbedPane.addTab(LocalizationData.get("StatisticsPlugin.pie.tabname"), null, this.pie, LocalizationData.get("StatisticsPlugin.pie.tooltip")); //$NON-NLS-1$ //$NON-NLS-2$
		buildSummaries();
		
/*
// 	An implementation based on a JSplitPane
		JPanel result = new JPanel(new BorderLayout());
		FilterView filterView = new FilterView(data);
		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, filterView, tabbedPane);
		result.add(BorderLayout.CENTER, splitPane);
		filterView.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				splitPane.setDividerLocation(-1);
				System.out.println (splitPane.getSize());
				System.out.println (splitPane.getComponent(0).getPreferredSize());
//				System.out.println (splitPane.getComponent(1).getPreferredSize());
//				splitPane.getComponent(1).setMinimumSize(minimumSize);
			}
		});
		return result;
*/		
		
		JPanel result = new JPanel(new BorderLayout());
//		result.add(new FilterView(data), BorderLayout.WEST);
		result.add(tabbedPane, BorderLayout.CENTER);
		JPanel southPane = new JPanel(new BorderLayout());
		southPane.add(getGroupSubCategories(), BorderLayout.WEST);
		southPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
		result.add(southPane, BorderLayout.SOUTH);
		return result;
	}
	
	private JCheckBox getGroupSubCategories() {
		if (groupSubCategories==null) {
			groupSubCategories = new JCheckBox(LocalizationData.get("Subcategories.groupButton.title")); //$NON-NLS-1$
			groupSubCategories.setToolTipText(LocalizationData.get("Subcategories.groupButton.tooltip")); //$NON-NLS-1$
			groupSubCategories.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					buildSummaries();
				}
			});
		}
		return groupSubCategories;
	}

	@Override
	protected Printable getPrintable() {
		return new Printable() {
			@Override
			public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
				ChartPanel pane = (ChartPanel) tabbedPane.getSelectedComponent();
				return pane.print(graphics, pageFormat, pageIndex);
			}
		};
	}

	private void buildSummaries() {
		categoryToAmount.clear();
		for (int i = 0; i < this.data.getGlobalData().getCategoriesNumber(); i++) {
			categoryToAmount.put(this.data.getGlobalData().getCategory(i), new Summary());
		}
		for (int i = 0; i < this.data.getTransactionsNumber(); i++) {
			Transaction transaction = this.data.getTransaction(i);
			if (this.data.isOk(transaction)) {
				for (int j = 0; j < transaction.getSubTransactionSize(); j++) {
					SubTransaction subTransaction = transaction.getSubTransaction(j);
					if (this.data.isOk(subTransaction)) categoryToAmount.get(subTransaction.getCategory()).add(subTransaction.getAmount());
				}
				Category category = transaction.getCategory();
				if (this.data.isComplementOk(transaction)) categoryToAmount.get(category).add(transaction.getComplement());
			}
		}
		if (getGroupSubCategories().isSelected()) {
			groupBySuperCategory();
		}
		pie.updateDataSet();
		bar.updateDataSet();
	}

	private void groupBySuperCategory() {
		// First extract the list of keys of categoryToAmount
		// We can't directly work with the categoryToAmount key set, because we will
		// add and remove some keys (ConcurrentModificationException is thrown when 
		// adding/removing a key while iterating on the key set.
		ArrayList<Category> keys = new ArrayList<Category>(categoryToAmount.size());
		for (Category category : categoryToAmount.keySet()) {
			keys.add(category);
		}
		for (Category category : keys) {
			Category superCategory = category.getSuperCategory(data.getGlobalData().getSubCategorySeparator());
			if (!category.equals(superCategory)) {
				Summary amounts = categoryToAmount.remove(category);
				Summary superAmounts = categoryToAmount.get(superCategory);
				if (superAmounts==null) {
					superAmounts = new Summary();
					categoryToAmount.put(superCategory, superAmounts);
				}
				superAmounts.add(amounts);
			}
		}
	}

	@Override
	public void setDisplayed(boolean displayed) {
		super.setDisplayed(displayed);
		this.displayed = displayed;
		if (displayed) buildSummaries();
	}

	@Override
	public void saveState() {
		YapbamState.INSTANCE.saveState(tabbedPane, this.getClass().getCanonicalName());
		YapbamState.INSTANCE.put(getGroupSubCategoriesStateKey(), Boolean.toString(getGroupSubCategories().isSelected()));
	}

	@Override
	public void restoreState() {
		YapbamState.INSTANCE.restoreState(tabbedPane, this.getClass().getCanonicalName());
		getGroupSubCategories().setSelected(Boolean.parseBoolean(YapbamState.INSTANCE.get(getGroupSubCategoriesStateKey(), "false")));
	}

	private String getGroupSubCategoriesStateKey() {
		return this.getClass().getCanonicalName()+".groupSubCategories";
	}

}
