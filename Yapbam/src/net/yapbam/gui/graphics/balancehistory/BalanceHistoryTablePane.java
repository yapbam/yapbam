package net.yapbam.gui.graphics.balancehistory;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTable.PrintMode;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.actions.ConvertToPeriodicalTransactionAction;
import net.yapbam.gui.actions.DeleteTransactionAction;
import net.yapbam.gui.actions.DuplicateTransactionAction;
import net.yapbam.gui.actions.EditTransactionAction;
import net.yapbam.gui.util.FriendlyTable;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JButton;

import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.dialog.FileChooser;
import com.fathzer.soft.ajlib.swing.table.JTableListener;
import com.fathzer.soft.ajlib.utilities.CSVWriter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.print.Printable;
import java.io.File;
import java.io.IOException;

import javax.swing.JCheckBox;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class BalanceHistoryTablePane extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final String HIDE_INTERMEDIATE_BALANCE_KEY = BalanceHistoryTablePane.class.getPackage().getName()+".hideIntermediateBalance"; //$NON-NLS-1$

	private JLabel columnMenu;
	BalanceHistoryTable table;
	private FilteredData data;
	private JCheckBox hideIntermediateChkBx;

	/**
	 * Creates the panel.
	 * @param data the data to be displayed
	 */
	public BalanceHistoryTablePane(FilteredData data) {
		this.data = data;
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		
		GridBagConstraints gbcLabel = new GridBagConstraints();
		gbcLabel.insets = new Insets(0, 5, 5, 0);
		gbcLabel.anchor = GridBagConstraints.EAST;
		gbcLabel.gridx = 1;
		gbcLabel.gridy = 0;
		add(getColumnMenu(), gbcLabel);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbcScrollPane = new GridBagConstraints();
		gbcScrollPane.insets = new Insets(0, 0, 5, 0);
		gbcScrollPane.weighty = 1.0;
		gbcScrollPane.gridwidth = 0;
		gbcScrollPane.fill = GridBagConstraints.BOTH;
		gbcScrollPane.gridx = 0;
		gbcScrollPane.gridy = 1;
		add(scrollPane, gbcScrollPane);
		scrollPane.setViewportView(getTable());
		
/*
	JLabel lblSortBy = new JLabelMenu("Sort by:") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void fillPopUp(JPopupMenu popup) {
				boolean valueDateSelected = true; //TODO
				JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem("Value date", valueDateSelected);
				popup.add(menuItem);
				menuItem = new JRadioButtonMenuItem("Transaction's date", !valueDateSelected);
				popup.add(menuItem);
			}
		};

		lblSortBy.setToolTipText("Ce menu permet de trier les opérations par date ou date de valeur"); //LOCAL
		GridBagConstraints gbcLblSortBy = new GridBagConstraints();
		gbcLblSortBy.weightx = 1.0;
		gbcLblSortBy.anchor = GridBagConstraints.WEST;
		gbcLblSortBy.insets = new Insets(0, 5, 0, 5);
		gbcLblSortBy.gridx = 0;
		gbcLblSortBy.gridy = 0;
		add(lblSortBy, gbcLblSortBy);
		lblSortBy.setVisible(false); //TODO ... maybe
*/		
	
		final JButton btnExport = new JButton(LocalizationData.get("BudgetPanel.export")); //$NON-NLS-1$
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new FileChooser();
				chooser.setLocale(new Locale(LocalizationData.getLocale().getLanguage()));
				File file = chooser.showSaveDialog(Utils.getOwnerWindow(btnExport))==JFileChooser.APPROVE_OPTION?chooser.getSelectedFile():null; //$NON-NLS-1$
				if (file!=null) {
					try {
						getTable().export(file, new DefaultExporter(LocalizationData.getLocale()));
					} catch (IOException e1) {
						ErrorManager.INSTANCE.display(btnExport, e1);
					}
				}
			}
		});
		GridBagConstraints gbcChckbxHide = new GridBagConstraints();
		gbcChckbxHide.insets = new Insets(0, 0, 0, 5);
		gbcChckbxHide.gridx = 0;
		gbcChckbxHide.gridy = 2;
		add(getHideIntermediateChkBx(), gbcChckbxHide);
		btnExport.setToolTipText("BudgetPanel.export.toolTip"); //$NON-NLS-1$
		btnExport.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbcBtnExport = new GridBagConstraints();
		gbcBtnExport.anchor = GridBagConstraints.EAST;
		gbcBtnExport.weightx = 1.0;
		gbcBtnExport.gridx = 1;
		gbcBtnExport.gridy = 2;
		add(btnExport, gbcBtnExport);
	}

	private JCheckBox getHideIntermediateChkBx() {
		if (hideIntermediateChkBx==null) {
			hideIntermediateChkBx = new JCheckBox(LocalizationData.get("BalanceHistory.transaction.hideIntermediate")); //$NON-NLS-1$
			hideIntermediateChkBx.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					((BalanceHistoryModel)getTable().getModel()).setHideIntermediateBalances(e.getStateChange()==ItemEvent.SELECTED);
				}
			});
		}
		return hideIntermediateChkBx;
	}
	
	private JLabel getColumnMenu() {
		if (columnMenu==null) {
			columnMenu = new FriendlyTable.ShowHideColumsMenu(getTable(), LocalizationData.get("MainFrame.showColumns")); //$NON-NLS-1$
			columnMenu.setToolTipText(LocalizationData.get("MainFrame.showColumns.ToolTip")); //$NON-NLS-1$
			columnMenu.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return columnMenu;
	}
	
	private BalanceHistoryTable getTable() {
		if (table==null) {
			table = new BalanceHistoryTable(data);
			if (data!=null) {
				Action edit = new EditTransactionAction(table);
				Action delete = new DeleteTransactionAction(table);
				Action duplicate = new DuplicateTransactionAction(table);
				table.addMouseListener(new JTableListener(new Action[] { edit, duplicate,
						delete, null, new ConvertToPeriodicalTransactionAction(table) }, edit));
			}
		}
		return table;
	}

	public void saveState() {
		YapbamState.INSTANCE.saveState(getTable(), this.getClass().getCanonicalName());
		YapbamState.INSTANCE.put(HIDE_INTERMEDIATE_BALANCE_KEY, Boolean.toString(getHideIntermediateChkBx().isSelected()));
	}

	public void restoreState() {
		YapbamState.INSTANCE.restoreState(getTable(), this.getClass().getCanonicalName());
		getHideIntermediateChkBx().setSelected(Boolean.parseBoolean(YapbamState.INSTANCE.get(HIDE_INTERMEDIATE_BALANCE_KEY, "true"))); //$NON-NLS-1$
	}

	private final class DefaultExporter implements FriendlyTable.ExportFormat {
		private DateFormat dateFormater;
		private NumberFormat currencyFormat;

		private DefaultExporter (Locale locale) {
			dateFormater = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, locale);
			currencyFormat = CSVWriter.getDecimalFormater(locale);
		}
		
		@Override
		public boolean hasHeader() {
			return true;
		}

		@Override
		public char getSeparator() {
			return '\t';
		}

		@Override
		public String format(Object obj) {
			if (obj==null) {
				return ""; //$NON-NLS-1$
			} else if (obj instanceof Date) {
				return dateFormater.format(obj);
			} else if (obj instanceof Double) {
				return currencyFormat.format(obj);
			} else {
				return obj.toString();
			}
		}
	}

	public Printable getPrintable() {
		return getTable().getPrintable(PrintMode.FIT_WIDTH, null, null);
	}
}
