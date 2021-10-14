package net.yapbam.gui.dialogs.export;

import java.io.IOException;

import com.fathzer.soft.ajlib.swing.table.JTable;

import net.yapbam.export.Exporter;
import net.yapbam.export.ExportWriter;
import net.yapbam.gui.util.FriendlyTable;
import net.yapbam.gui.util.XTableColumnModel;

public class TableExporter extends Exporter<ExporterParameters ,FriendlyTable> {
	public TableExporter() {
		super(new ExporterParameters());
	}

	@Override
	public void export(final FriendlyTable table, ExportWriter format) throws IOException {
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
					return format(getValueAt(table, modelRowIndex, modelIndexes[colIndex]));
				}
			};
			writeLine(table, format, vg);
		}
		format.addFooter();
	}
	
	protected Object getValueAt(JTable table, int modelRowIndex, int modelColIndex) {
		return table.getModel().getValueAt(modelRowIndex, modelColIndex);
	}

	private void writeLine(final FriendlyTable table, ExportWriter format, ValueGetter vg) throws IOException {
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
		String get(int colIndex);
	}
}
