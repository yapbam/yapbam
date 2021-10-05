package net.yapbam.gui.dialogs.export;

import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import com.fathzer.soft.ajlib.utilities.CSVWriter;

import net.yapbam.gui.util.FriendlyTable;
import net.yapbam.gui.util.XTableColumnModel;

public class DefaultTableExporter implements Exporter<FriendlyTable> {
	// TODO dateFormat and currency format should be passed as parameters instead of being guessed by this class
	private DateFormat dateFormater;
	private NumberFormat currencyFormat;

	public DefaultTableExporter(Locale locale) {
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

	@Override
	public void export(final FriendlyTable table, IExportableFormat format) throws IOException {
		final int[] modelIndexes = buildModelIndex(table);
		format.addHeader();
		ValueGetter vg = new ValueGetter() {
			@Override
			public String get(int colIndex) {
				return table.getModel().getColumnName(modelIndexes[colIndex]);
			}
		}; 

		writeLine(table, format, vg);
		for (int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
			final int modelRowIndex = table.convertRowIndexToModel(rowIndex);
			vg = new ValueGetter() {
				@Override
				public String get(int colIndex) {
					return format(table.getModel().getValueAt(modelRowIndex, modelIndexes[colIndex]));
				}
			};
			writeLine(table, format, vg);
		}
		format.addFooter();
	}

	private void writeLine(final FriendlyTable table, IExportableFormat format, ValueGetter vg) throws IOException {
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
