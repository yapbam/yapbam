package net.yapbam.gui.dialogs.export;

import java.io.IOException;

import com.fathzer.soft.ajlib.swing.table.JTable;

import net.yapbam.export.ExportWriter;
import net.yapbam.export.Exporter;
import net.yapbam.gui.util.FriendlyTable;
import net.yapbam.gui.util.XTableColumnModel;

public class TableExporter<T extends ExporterParameters<?>> extends Exporter<T ,FriendlyTable> {
	private interface ValueGetter {
		String get(int colIndex);
	}
	
	private interface StylesGetter {
		String[] getStyles(int colIndex);
	}
	
	public TableExporter(T params) {
		super(params);
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

		writeLine(table, format, vg, null);
		for (int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
			final int modelRowIndex = table.convertRowIndexToModel(rowIndex);
			vg = new ValueGetter() {
				@Override
				public String get(int colIndex) {
					return getParameters().format(getValueAt(table, modelRowIndex, modelIndexes[colIndex]));
				}
			};
			StylesGetter avg = new StylesGetter() {
				@Override
				public String[] getStyles(int colIndex) {
					return TableExporter.this.getStyles(modelRowIndex, modelIndexes[colIndex]);
				}
			};
			writeLine(table, format, vg, avg);
		}
		format.addFooter();
	}
	
	protected Object getValueAt(JTable table, int modelRowIndex, int modelColIndex) {
		return table.getModel().getValueAt(modelRowIndex, modelColIndex);
	}
	
	protected String[] getStyles(int modelRowIndex, int modelColIndex) {
		return null;
	}

	private void writeLine(final FriendlyTable table, ExportWriter format, ValueGetter vg, StylesGetter avg) throws IOException {
		format.addLineStart();
		for (int colIndex = 0; colIndex < table.getColumnCount(false); colIndex++) {
			if (table.isColumnVisible(colIndex)) {
				format.addValue(vg.get(colIndex), avg != null ? avg.getStyles(colIndex) : null);
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
}
