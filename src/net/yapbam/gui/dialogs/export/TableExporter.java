package net.yapbam.gui.dialogs.export;

import java.io.IOException;

import org.apache.commons.lang3.ArrayUtils;

import com.fathzer.soft.ajlib.swing.table.JTable;

import net.yapbam.export.ExportWriter;
import net.yapbam.export.Exporter;
import net.yapbam.gui.util.FriendlyTable;
import net.yapbam.gui.util.XTableColumnModel;

public class TableExporter<T extends ExporterParameters> extends Exporter<T ,FriendlyTable> {
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
					return format(getValueAt(table, modelRowIndex, modelIndexes[colIndex]));
				}
			};
			AdditionalValueGetter avg = new AdditionalValueGetter() {
				@Override
				public String[] getAdditional(int colIndex) {
					return getAdditionalValueInfo(modelRowIndex, modelIndexes[colIndex]).getInfos();
				}
			};
			writeLine(table, format, vg, avg);
		}
		format.addFooter();
	}
	
	protected Object getValueAt(JTable table, int modelRowIndex, int modelColIndex) {
		return table.getModel().getValueAt(modelRowIndex, modelColIndex);
	}
	
	protected AdditionalValueInfo getAdditionalValueInfo(int modelRowIndex, int modelColIndex) {
		return AdditionalValueInfo.NO_INFO;
	}

	private void writeLine(final FriendlyTable table, ExportWriter format, ValueGetter vg, AdditionalValueGetter avg) throws IOException {
		format.addLineStart();
		for (int colIndex = 0; colIndex < table.getColumnCount(false); colIndex++) {
			if (table.isColumnVisible(colIndex)) {
				format.addValue(ArrayUtils.addFirst(avg != null ? avg.getAdditional(colIndex) : null, vg.get(colIndex)));
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
	
	private interface AdditionalValueGetter {
		String[] getAdditional(int colIndex);
	}
	
	protected enum AdditionalValueInfo {
		
		NO_INFO(), 
		RECEIPT("receipt"), 
		DEBT("debit");
		
		private final String[] infos;
		
		AdditionalValueInfo(String ...infos) {
			this.infos = infos;
		}

		public String[] getInfos() {
			return infos;
		}
		
	}
}
