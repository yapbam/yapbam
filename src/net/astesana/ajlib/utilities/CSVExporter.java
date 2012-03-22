package net.astesana.ajlib.utilities;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.swing.table.TableModel;

import net.astesana.ajlib.swing.table.TitledRowsTableModel;

public class CSVExporter {
	private char separator;
	private boolean quoteCells;
	
	public CSVExporter(char separator, boolean quoteCells) {
		this.separator = separator;
		this.quoteCells = quoteCells;
	}

	public void export(BufferedWriter writer, TableModel model, boolean withTitles) throws IOException {
		boolean withRowTitle = model instanceof TitledRowsTableModel;
		if (withTitles) {
			boolean lineIsEmpty = true;
			if (withRowTitle) {
				lineIsEmpty = false;
				writeCell(writer, "");
			}
			for (int i = 0; i < model.getColumnCount(); i++) {
				if (!lineIsEmpty) writer.append(separator);
				writeCell(writer, model.getColumnName(i));
				lineIsEmpty = false;
			}
			writer.newLine();
		}
		for (int rowIndex = 0; rowIndex < model.getRowCount(); rowIndex++) {
			boolean lineIsEmpty = true;
			if (withRowTitle) {
				lineIsEmpty = false;
				writeCell(writer, ((TitledRowsTableModel)model).getRowName(rowIndex));
			}
			for (int columnIndex = 0; columnIndex < model.getColumnCount(); columnIndex++) {
				if (!lineIsEmpty) writer.append(separator);
				Object value = model.getValueAt(rowIndex, columnIndex);
				writeCell(writer, value==null?"":value.toString()); //$NON-NLS-1$
				lineIsEmpty = false;
			}
			writer.newLine();
		}
		writer.newLine();
	}

	private void writeCell(BufferedWriter writer, String cell) throws IOException {
		//FIXME Have a look at the cvs spec => quoting pay also be used if a cell contains a separator
		//FIXME If a cell contains a quote, this quote may be replaced by two quotes.
		if (quoteCells) writer.append('"');
		writer.append(cell);
		if (quoteCells) writer.append('"');		
	}
	
	public char getSeparator() {
		return this.separator;
	}
	
	public void setSeparator(char separator) {
		this.separator = separator;
	}

}
