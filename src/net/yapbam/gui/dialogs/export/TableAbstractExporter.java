package net.yapbam.gui.dialogs.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.fathzer.soft.ajlib.utilities.CSVWriter;
import com.fathzer.soft.ajlib.utilities.FileUtils;

import net.yapbam.gui.util.FriendlyTable;
import net.yapbam.gui.util.XTableColumnModel;

public abstract class TableAbstractExporter<F extends IExportableFormat> implements FriendlyTable.ExportFormat {

	private F format;
	private DateFormat dateFormater;
	private NumberFormat currencyFormat;

	public TableAbstractExporter(F exporter, Locale locale) {
		format = exporter;
		dateFormater = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, locale);
		currencyFormat = CSVWriter.getDecimalFormater(locale);
	}
	
	@Override
	public char getSeparator() {
		return 0;
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

	@Override
	public void export(FriendlyTable table, File onFile) throws IOException {
		if (table != null && onFile != null) {
			Writer fileWriter = null;
			try {
				fileWriter = new FileWriter(FileUtils.getCanonical(onFile));
				format.addHeader();
				format.addLineStart();
				int[] modelIndexes = new int[table.getColumnCount(false)];
				for (int colIndex = 0; colIndex < table.getColumnCount(false); colIndex++) {
					if (table.isColumnVisible(colIndex)) {
						modelIndexes[colIndex] = ((XTableColumnModel) table.getColumnModel()).getColumn(colIndex, false)
								.getModelIndex();
						if (hasHeader()) {
							format.addValue(table.getModel().getColumnName(modelIndexes[colIndex]));
						}
					}
				}
				format.addLineEnd();
				for (int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
					int modelRowIndex = table.convertRowIndexToModel(rowIndex);
					format.addLineStart();
					for (int colIndex = 0; colIndex < table.getColumnCount(false); colIndex++) {
						if (table.isColumnVisible(colIndex)) {
							Object obj = table.getModel().getValueAt(modelRowIndex, modelIndexes[colIndex]);
							format.addValue(format(obj));
						}
					}
					format.addLineEnd();
				}
				format.addFooter();
				fileWriter.append(format.flushAndGetResultl());
			} finally {
				if (fileWriter != null) {
					fileWriter.close();
				}
			}
		}
	}

}
