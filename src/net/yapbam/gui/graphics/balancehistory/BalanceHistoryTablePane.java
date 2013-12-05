package net.yapbam.gui.graphics.balancehistory;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTable.PrintMode;

import java.awt.GridBagLayout;
import javax.swing.JLabel;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.util.FriendlyTable;
import net.yapbam.gui.widget.JLabelMenu;

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
import com.fathzer.soft.ajlib.utilities.CSVWriter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.print.Printable;
import java.io.File;
import java.io.IOException;

public class BalanceHistoryTablePane extends JPanel {
	private static final long serialVersionUID = 1L;
	BalanceHistoryTable table;

	/**
	 * Creates the panel.
	 * @param data the data to be displayed
	 */
	public BalanceHistoryTablePane(FilteredData data) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbcScrollPane = new GridBagConstraints();
		gbcScrollPane.insets = new Insets(0, 0, 0, 0);
		gbcScrollPane.weighty = 1.0;
		gbcScrollPane.gridwidth = 3;
		gbcScrollPane.fill = GridBagConstraints.BOTH;
		gbcScrollPane.gridx = 0;
		gbcScrollPane.gridy = 0;
		add(scrollPane, gbcScrollPane);
		
		table = new BalanceHistoryTable(data);
		scrollPane.setViewportView(table);
		
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
		gbcLblSortBy.insets = new Insets(0, 5, 0, 5);
		gbcLblSortBy.gridx = 0;
		gbcLblSortBy.gridy = 1;
		add(lblSortBy, gbcLblSortBy);
		lblSortBy.setVisible(false); //TODO ... maybe
		
		JLabel label = table.getShowHideColumnsMenu(LocalizationData.get("MainFrame.showColumns")); //$NON-NLS-1$
		label.setToolTipText(LocalizationData.get("MainFrame.showColumns.ToolTip")); //$NON-NLS-1$
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbcLabel = new GridBagConstraints();
		gbcLabel.insets = new Insets(0, 5, 0, 5);
		gbcLabel.anchor = GridBagConstraints.NORTHWEST;
		gbcLabel.gridx = 1;
		gbcLabel.gridy = 1;
		add(label, gbcLabel);
		
		final JButton btnExport = new JButton(LocalizationData.get("BudgetPanel.export")); //$NON-NLS-1$
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new FileChooser();
				chooser.setLocale(new Locale(LocalizationData.getLocale().getLanguage()));
				File file = chooser.showSaveDialog(Utils.getOwnerWindow(btnExport))==JFileChooser.APPROVE_OPTION?chooser.getSelectedFile():null; //$NON-NLS-1$
				if (file!=null) {
					try {
						table.export(file, new DefaultExporter(LocalizationData.getLocale()));
					} catch (IOException e1) {
						ErrorManager.INSTANCE.display(btnExport, e1);
					}
				}
			}
		});
		btnExport.setToolTipText("BudgetPanel.export.toolTip"); //$NON-NLS-1$
		btnExport.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbcBtnExport = new GridBagConstraints();
		gbcBtnExport.anchor = GridBagConstraints.EAST;
		gbcBtnExport.weightx = 1.0;
		gbcBtnExport.insets = new Insets(0, 0, 5, 5);
		gbcBtnExport.gridx = 2;
		gbcBtnExport.gridy = 1;
		add(btnExport, gbcBtnExport);
	}

	public void saveState() {
		YapbamState.INSTANCE.saveState(table, this.getClass().getCanonicalName());
	}

	public void restoreState() {
		YapbamState.INSTANCE.restoreState(table, this.getClass().getCanonicalName());
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
				return "";
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
		return table.getPrintable(PrintMode.FIT_WIDTH, null, null);
	}
}
