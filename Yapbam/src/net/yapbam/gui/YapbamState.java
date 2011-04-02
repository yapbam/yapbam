package net.yapbam.gui;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import net.yapbam.gui.util.XTableColumnModel;
import net.yapbam.util.DateUtils;
import net.yapbam.util.Portable;

public class YapbamState {
	private static final String COLUMN_WIDTH = "column.width."; //$NON-NLS-1$
	private static final String COLUMN_INDEX = "column.index."; //$NON-NLS-1$
	private static final String COLUMN_HIDDEN = "column.hidden."; //$NON-NLS-1$
//	private static final String SELECTED_ROW = "selectedRow"; //$NON-NLS-1$
//	private static final String SCROLL_POSITION = "scrollPosition"; //$NON-NLS-1$

	public static final YapbamState INSTANCE = new YapbamState();
	
	private Properties properties;

	protected YapbamState() {
		this.properties = new Properties();
		try {
			properties.load(new FileInputStream(getFile()));
		} catch (Throwable e) {
			// On the first run, the file doesn't exist
			// If there's another error, maybe it would be better to do something else //TODO
		}
	}

	protected File getFile() {
		return new File (Portable.getDataDirectory(), ".yapbam");
	}
	
	public void restoreState(JTable table, String prefix) {
		TableColumnModel model = table.getColumnModel();
		// Restore the width
		for (int i = 0; i < model.getColumnCount(); i++) {
			String valueString = (String) properties.get(prefix+COLUMN_WIDTH+i);
			if (valueString!=null) {
				int width = Integer.parseInt(valueString);
				if (width>0) model.getColumn(i).setPreferredWidth(width);
			}
		}
		// Restore column order
		for (int i = model.getColumnCount()-1; i>=0 ; i--) {
			String valueString = (String) properties.get(prefix+COLUMN_INDEX+i);
			if (valueString!=null) {
				int modelIndex = Integer.parseInt(valueString);
				if (modelIndex>=0) table.moveColumn(table.convertColumnIndexToView(modelIndex), i);
			}
		}
		// Restore the show/hide column
		if (model instanceof XTableColumnModel) {
			XTableColumnModel xModel = (XTableColumnModel)model;
			for (int i = 0; i < xModel.getColumnCount(false); i++) {
				if (Boolean.valueOf(properties.getProperty(prefix+COLUMN_HIDDEN+i, "false"))) {
					TableColumn column = xModel.getColumnByModelIndex(i);
					xModel.setColumnVisible(column, false);
				}
			}
		}
		// Now the selected row (not a very good idea).
//		String valueString = (String) properties.get(prefix+SELECTED_ROW);
//		if (valueString!=null) {
//			int index = Integer.parseInt(valueString);
//			if (index < table.getRowCount()) table.getSelectionModel().setSelectionInterval(index, index);
//		}
		// And the scroll position (not sure it's a good idea)
//		Rectangle visibleRect = YapbamState.getRectangle(prefix+SCROLL_POSITION);
//		if (visibleRect!=null) table.scrollRectToVisible(visibleRect);
	}

	public void saveState(JTable table, String prefix) {
		TableColumnModel model = table.getColumnModel();
		if (model instanceof XTableColumnModel) {
			XTableColumnModel xModel = (XTableColumnModel)model;
			for (int viewIndex = 0; viewIndex < xModel.getColumnCount(false); viewIndex++) {
				TableColumn column = xModel.getColumn(viewIndex, false);
				int modelIndex = column.getModelIndex();
				// Save the column width
				properties.put(prefix+COLUMN_WIDTH+modelIndex, Integer.toString(column.getWidth()));
				// Save the column order (if two or more columns were inverted)
				properties.put(prefix+COLUMN_INDEX+viewIndex, Integer.toString(modelIndex));
				String key = prefix+COLUMN_HIDDEN+modelIndex;
				properties.put(key, Boolean.valueOf(!xModel.isColumnVisible(column)).toString());
			}
		} else {
			for (int viewIndex = 0; viewIndex < model.getColumnCount(); viewIndex++) {
				int modelIndex = table.convertColumnIndexToModel(viewIndex);
				// Save the column width
				properties.put(prefix+COLUMN_WIDTH+modelIndex, Integer.toString(model.getColumn(viewIndex).getWidth()));
				// Save the column order (if two or more columns were inverted)
				properties.put(prefix+COLUMN_INDEX+viewIndex, Integer.toString(modelIndex));
			}
		}
//		properties.put(prefix+SELECTED_ROW, Integer.toString(table.getSelectedRow()));
//		YapbamState.put(prefix+SCROLL_POSITION, table.getVisibleRect());
	}

	public boolean contains(String key) {
		return properties.containsKey(key);
	}
	
	public void remove(String key) {
		properties.remove(key);
	}

	public String get(String key) {
		return properties.getProperty(key);
	}

	public String get(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	public void put(String key, String value) {
		properties.put(key, value);
	}

	public Date getDate(String key) {
		String dummy = properties.getProperty(key);
		if (dummy==null) return new Date(0);
		return DateUtils.integerToDate(Integer.parseInt(dummy));
	}
	
	public void put (String key, Date date) {
		properties.put(key, Integer.toString(DateUtils.dateToInteger(date)));
	}
	
	public void put(String key, Rectangle value) {
		properties.put(key, value.x+","+value.y+","+value.width+","+value.height);
	}
	
	public Rectangle getRectangle(String key) {
		String value = properties.getProperty(key);
		if (value==null) return null;
		StringTokenizer tokens = new StringTokenizer(value, ",");
		return new Rectangle(Integer.parseInt(tokens.nextToken()),Integer.parseInt(tokens.nextToken()),
				Integer.parseInt(tokens.nextToken()),Integer.parseInt(tokens.nextToken()));
	}

	public void toDisk() throws FileNotFoundException, IOException {
		properties.store(new FileOutputStream(getFile()), "Yapbam startup state"); //$NON-NLS-1$
	}
}
