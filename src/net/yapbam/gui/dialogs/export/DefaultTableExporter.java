package net.yapbam.gui.dialogs.export;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import com.fathzer.soft.ajlib.utilities.CSVWriter;

import net.yapbam.gui.util.FriendlyTable;
import net.yapbam.gui.util.XTableColumnModel;

public class DefaultTableExporter {
	private IExportableFormat format;
	private DateFormat dateFormater;
	private NumberFormat currencyFormat;

	public DefaultTableExporter(IExportableFormat exporter, Locale locale) {
		format = exporter;
		dateFormater = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		currencyFormat = CSVWriter.getDecimalFormater(locale);
	}

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

	public void export(final FriendlyTable table, File onFile) throws IOException {
		if (table != null && onFile != null && format != null) {
			try {
				final int[] modelIndexes = buildModelIndex(table);
				format.addHeader();
				ValueGetter vg = new ValueGetter() {
					@Override
					public String get(int colIndex) {
						return table.getModel().getColumnName(modelIndexes[colIndex]);
					}
				}; 

				writeLine(table, vg);
				for (int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
					final int modelRowIndex = table.convertRowIndexToModel(rowIndex);
					vg = new ValueGetter() {
						@Override
						public String get(int colIndex) {
							return format(table.getModel().getValueAt(modelRowIndex, modelIndexes[colIndex]));
						}
					};
					writeLine(table, vg);
				}
				format.addFooter();
			} finally {
				format.close();
			}
		}
	}

	private void writeLine(final FriendlyTable table, ValueGetter vg) throws IOException {
		format.addLineStart();
		for (int colIndex = 0; colIndex < table.getColumnCount(false); colIndex++) {
			if (table.isColumnVisible(colIndex)) {
				format.addValue(vg.get(colIndex));
			}
		}
		format.addLineEnd();
	}
	
	private int[] buildModelIndex(FriendlyTable table) {
		final int[] modelIndexes = new int[table.getColumnCount(false)];
		for (int colIndex = 0; colIndex < table.getColumnCount(false); colIndex++) {
			modelIndexes[colIndex] = ((XTableColumnModel) table.getColumnModel()).getColumn(colIndex, false).getModelIndex();
		}
		return modelIndexes;
	}

	private interface ValueGetter {
		String get(int cilIndex);
	}
}