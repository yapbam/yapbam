package net.yapbam.gui.graphics.balancehistory;

import static j2html.TagCreator.body;
import static j2html.TagCreator.document;
import static j2html.TagCreator.h2;
import static j2html.TagCreator.html;
import static j2html.TagCreator.style;
import static j2html.TagCreator.table;
import static j2html.TagCreator.tbody;
import static j2html.TagCreator.td;
import static j2html.TagCreator.tr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.print.Printable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable.PrintMode;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.lang3.StringUtils;

import com.alexandriasoftware.swing.JSplitButton;
import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.dialog.FileChooser;
import com.fathzer.soft.ajlib.swing.table.JTableListener;
import com.fathzer.soft.ajlib.utilities.CSVWriter;
import com.fathzer.soft.ajlib.utilities.FileUtils;

import j2html.attributes.Attribute;
import j2html.tags.ContainerTag;
import net.yapbam.data.FilteredData;
import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.actions.ConvertToPeriodicalTransactionAction;
import net.yapbam.gui.actions.DeleteTransactionAction;
import net.yapbam.gui.actions.DuplicateTransactionAction;
import net.yapbam.gui.actions.EditTransactionAction;
import net.yapbam.gui.dialogs.export.ExportFormatType;
import net.yapbam.gui.util.FriendlyTable;
import net.yapbam.gui.util.FriendlyTable.ExportFormat;
import net.yapbam.gui.util.XTableColumnModel;

public class BalanceHistoryTablePane extends JPanel {
	// If you ask yourself why this pane can't be displayed by Eclipse Window Builder, it seems the problem is
	// with JSplitButton. If you replace the JSplitButton with a basic JButton, the component is displayed without any problem
	//TODO Investigate more on this.
	private static final long serialVersionUID = 1L;
	private static final String HIDE_INTERMEDIATE_BALANCE_KEY = BalanceHistoryTablePane.class.getPackage().getName()+".hideIntermediateBalance"; //$NON-NLS-1$

	private JLabel columnMenu;
	BalanceHistoryTable table;
	private FilteredData data;
	private JCheckBox hideIntermediateChkBx;

	public BalanceHistoryTablePane(FilteredData data) {
		this.data = data;
		setLayout(new BorderLayout());

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(getTable());

		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BorderLayout());
		northPanel.add(getColumnMenu(), BorderLayout.EAST);
		northPanel.add(Box.createHorizontalGlue(), BorderLayout.CENTER);

		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BorderLayout());
		southPanel.add(getHideIntermediateChkBx(), BorderLayout.WEST);
		southPanel.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
		southPanel.add(getBtnExport(), BorderLayout.EAST);

		add(northPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
	}
	
	private JButton getBtnExport() {
		final JSplitButton btnExport = new JSplitButton(LocalizationData.get("BudgetPanel.export")); //$NON-NLS-1$
		btnExport.setPreferredSize(new Dimension(150, 40));
		btnExport.setToolTipText(LocalizationData.get("BudgetPanel.export.toolTip")); //$NON-NLS-1$

		ActionListener exportActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (StringUtils.isNotBlank(e.getActionCommand())) {

					ExportFormatType exportType = ExportFormatType.valueOf(e.getActionCommand());

					JFileChooser chooser = new FileChooser();
					chooser.setAcceptAllFileFilterUsed(false);
					chooser.setFileFilter(new FileNameExtensionFilter( //
							exportType.getDescription(), //
							exportType.getExtension() //
					));
					chooser.setLocale(new Locale(LocalizationData.getLocale().getLanguage()));

					File file = chooser.showSaveDialog(Utils.getOwnerWindow(btnExport)) == JFileChooser.APPROVE_OPTION
							? chooser.getSelectedFile()
							: null;

					if (file != null) {

						if (!file.getPath().endsWith(exportType.getExtension()))
							file = new File(file.getPath() + "." + exportType.getExtension());

						try {
							ExportFormat exportFormat = null;
							if (ExportFormatType.CSV.equals(exportType)) {
								exportFormat = new DefaultExporter(LocalizationData.getLocale());
							} else if (ExportFormatType.HTML.equals(exportType)) {
								exportFormat = new HtmlExporter(LocalizationData.getLocale());
							}
							if (exportFormat != null)
								exportFormat.export(BalanceHistoryTablePane.this.table, file);
						} catch (IOException ex) {
							ErrorManager.INSTANCE.display(btnExport, ex);
						}
					}
				}
			}
		};

		JPopupMenu exportMenu = new JPopupMenu();
		for (ExportFormatType formatType : ExportFormatType.values()) {
			JMenuItem menuItem = new JMenuItem(Formatter.format(LocalizationData.get("BudgetPanel.exportAs"),  //$NON-NLS-1$
					formatType.getDescription(), formatType.getExtension()) //
			);
			menuItem.setActionCommand(formatType.name());
			menuItem.addActionListener(exportActionListener);
			exportMenu.add(menuItem);
		}
		btnExport.setPopupMenu(exportMenu);
		return btnExport;
	}

	private JCheckBox getHideIntermediateChkBx() {
		if (hideIntermediateChkBx == null) {
			hideIntermediateChkBx = new JCheckBox(LocalizationData.get("BalanceHistory.transaction.hideIntermediate")); //$NON-NLS-1$
			hideIntermediateChkBx.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					((BalanceHistoryModel)getTable().getModel()).setHideIntermediateBalances(e.getStateChange()==ItemEvent.SELECTED);
				}
			});
		}
		return hideIntermediateChkBx;
	}

	private JLabel getColumnMenu() {
		if (columnMenu == null) {
			columnMenu = new FriendlyTable.ShowHideColumsMenu(getTable(), LocalizationData.get("MainFrame.showColumns")); //$NON-NLS-1$
			columnMenu.setToolTipText(LocalizationData.get("MainFrame.showColumns.ToolTip")); //$NON-NLS-1$
			columnMenu.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return columnMenu;
	}

	private BalanceHistoryTable getTable() {
		if (table == null) {
			table = new BalanceHistoryTable(data);
			if (data != null) {
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

	private abstract class AbstractExporter implements FriendlyTable.ExportFormat {

		private DateFormat dateFormater;
		private NumberFormat currencyFormat;

		private AbstractExporter(Locale locale) {
			dateFormater = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, locale);
			currencyFormat = CSVWriter.getDecimalFormater(locale);
		}

		@Override
		public boolean hasHeader() {
			return true;
		}

		@Override
		public String format(Object obj) {
			if (obj == null) {
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

	private final class DefaultExporter extends AbstractExporter {

		private DefaultExporter(Locale locale) {
			super(locale);
		}

		@Override
		public char getSeparator() {
			return '\t';
		}

		@Override
		public void export(FriendlyTable table, File onFile) throws IOException {
			if (table != null && onFile != null) {
				Writer fileWriter = new FileWriter(FileUtils.getCanonical(onFile));
				try {
					CSVWriter out = new CSVWriter(fileWriter);
					out.setSeparator(getSeparator());
					int[] modelIndexes = new int[table.getColumnCount(false)];
					for (int colIndex = 0; colIndex < table.getColumnCount(false); colIndex++) {
						if (table.isColumnVisible(colIndex)) {
							modelIndexes[colIndex] = ((XTableColumnModel) table.getColumnModel())
									.getColumn(colIndex, false).getModelIndex();
							if (hasHeader()) {
								out.writeCell(table.getModel().getColumnName(modelIndexes[colIndex]));
							}
						}
					}
					out.newLine();
					for (int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
						int modelRowIndex = table.convertRowIndexToModel(rowIndex);
						for (int colIndex = 0; colIndex < table.getColumnCount(false); colIndex++) {
							if (table.isColumnVisible(colIndex)) {
								Object obj = table.getModel().getValueAt(modelRowIndex, modelIndexes[colIndex]);
								out.writeCell(format(obj));
							}
						}
						out.newLine();
					}
					out.flush();
				} finally {
					fileWriter.close();
				}
			}
		}
	}

	private final class HtmlExporter extends AbstractExporter {

		private HtmlExporter(Locale locale) {
			super(locale);
		}

		@Override
		public char getSeparator() {
			return '\0';
		}

		@Override
		public void export(FriendlyTable table, File onFile) throws IOException {

			ContainerTag body = body();
			ContainerTag tBody = tbody();

			ContainerTag style = style();
			style.withText("table, th, td {border: 1px solid black;border-collapse: collapse;}");
			body.with(style);

			ContainerTag title = h2(format(new Date()));
			title.attr(new Attribute("align", "center"));
			body.with(title);

			if (table != null && onFile != null) {
				Writer fileWriter = new FileWriter(FileUtils.getCanonical(onFile));
				try {
					int[] modelIndexes = new int[table.getColumnCount(false)];
					ContainerTag columnTr = tr();
					for (int colIndex = 0; colIndex < table.getColumnCount(false); colIndex++) {
						if (table.isColumnVisible(colIndex)) {
							modelIndexes[colIndex] = ((XTableColumnModel) table.getColumnModel())
									.getColumn(colIndex, false).getModelIndex();
							if (hasHeader()) {
								columnTr.with(td(table.getModel().getColumnName(modelIndexes[colIndex])));
							}
						}
					}
					tBody.with(columnTr);
					for (int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
						ContainerTag tr = tr();
						int modelRowIndex = table.convertRowIndexToModel(rowIndex);
						for (int colIndex = 0; colIndex < table.getColumnCount(false); colIndex++) {
							if (table.isColumnVisible(colIndex)) {
								Object obj = table.getModel().getValueAt(modelRowIndex, modelIndexes[colIndex]);
								tr.with(td(format(obj)));
							}
						}
						tBody.with(tr);
					}

					ContainerTag htmlTable = table(tBody);
					htmlTable.attr(new Attribute("width", "90%"));
					htmlTable.attr(new Attribute("style", "margin:0 auto;"));

					body.with(htmlTable);

					fileWriter.append(document(html(body)));
					fileWriter.flush();
				} finally {
					fileWriter.close();
				}
			}
		}
	}

	public Printable getPrintable() {
		return getTable().getPrintable(PrintMode.FIT_WIDTH, null, null);
	}
}
