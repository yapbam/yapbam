package net.yapbam.gui;

import java.awt.Rectangle;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import net.yapbam.data.Filter;
import net.yapbam.data.xml.Serializer;
import net.yapbam.gui.util.XTableColumnModel;
import net.yapbam.gui.widget.TabbedPane;
import net.yapbam.util.ArrayUtils;
import net.yapbam.util.DateUtils;
import net.yapbam.util.Portable;

public class YapbamState {
	private static final String COLUMN_WIDTH = "column.width."; //$NON-NLS-1$
	private static final String COLUMN_INDEX = "column.index."; //$NON-NLS-1$
	private static final String COLUMN_HIDDEN = "column.hidden."; //$NON-NLS-1$
	private static final String PRINTING_ATTRIBUTES = ".printing.attributes"; //$NON-NLS-1$
	private static final String TAB_ORDER = ".tab.order"; //$NON-NLS-1$

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
	
	public void saveState(TabbedPane tabbedPane, String prefix) {
		put(prefix+TAB_ORDER, ArrayUtils.toString(tabbedPane.getIds()));
	}
	
	public void restoreState(TabbedPane tabbedPane, String prefix) {
		String property = get(prefix+TAB_ORDER);
		if (property!=null) {
			tabbedPane.setOrder(ArrayUtils.parseIntArray(property));
		}
		// TabbedPane.setOrder changes the selected tab.
		// I think it's better to restore with the first tab displayed
		if (tabbedPane.getTabCount()>0) tabbedPane.setSelectedIndex(0);
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

	public PrintRequestAttributeSet restorePrinterSettings(String prefix) {
		PrintRequestAttributeSet result = (PrintRequestAttributeSet) restore(prefix+PRINTING_ATTRIBUTES);
		return result!=null?result:new HashPrintRequestAttributeSet();
	}

	public void savePrinterSettings(String prefix, PrintRequestAttributeSet attributes) {
		save (prefix+PRINTING_ATTRIBUTES, (Serializable) attributes);
	}

	public void save(String key, Serializable serializable) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new GZIPOutputStream(baos));
			oos.writeObject(serializable);
			oos.close();
			properties.put(key, Base64.encode(baos.toByteArray()));
		} catch (IOException e) {
			// Should not happen ... because the serialization is made into memory
			throw new RuntimeException(e);
		}
	}
	
	public Serializable restore(String prefix) {
		String key = prefix;
		if (contains(key)) {
			try {
				byte[] decoded = Base64.decode(get(key));
				ByteArrayInputStream bais = new ByteArrayInputStream(decoded);
				ObjectInputStream ois = new ObjectInputStream(new GZIPInputStream(bais));
				Serializable result = (Serializable) ois.readObject();
				ois.close();
				return result;
			} catch (Exception e) {
				// If something goes wrong, return null
				return null;
			}
		} else {
			return null;
		}
	}

	public void save(String key, Filter filter, String password) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			Serializer serializer = new Serializer(password, stream);
			try {
				serializer.serialize(filter);
			} catch (SAXException e) {
				throw new RuntimeException(e);
			}
			serializer.closeDocument();
			stream.flush();
			properties.put(key, new String(/*Base64.encode(*/stream.toByteArray())); //TODO
		} catch (IOException e) {
				throw new RuntimeException(e);
		}
	}
}
