package net.yapbam.gui;

import java.awt.Rectangle;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.utilities.FileUtils;
import com.fathzer.soft.ajlib.utilities.StringUtils;

import net.yapbam.data.Filter;
import net.yapbam.data.GlobalData;
import net.yapbam.data.ProgressReport;
import net.yapbam.data.xml.AbstractSerializer;
import net.yapbam.data.xml.FilterHandler;
import net.yapbam.data.xml.XMLSerializer;
import net.yapbam.gui.preferences.StartStateSettings;
import net.yapbam.gui.util.XTableColumnModel;
import net.yapbam.gui.widget.TabbedPane;
import net.yapbam.util.ArrayUtils;
import net.yapbam.util.DateUtils;
import net.yapbam.util.Portable;
import net.yapbam.util.PreferencesUtils;

public class YapbamState {
	private static final String COLUMN_WIDTH = "column.width."; //$NON-NLS-1$
	private static final String COLUMN_INDEX = "column.index."; //$NON-NLS-1$
	private static final String COLUMN_HIDDEN = "column.hidden."; //$NON-NLS-1$
	private static final String COLUMN_SORTER = "column.sorter"; //$NON-NLS-1$
	private static final String PRINTING_ATTRIBUTES = ".printing.attributes"; //$NON-NLS-1$
	private static final String TAB_ORDER = ".tab.order"; //$NON-NLS-1$

	public static final YapbamState INSTANCE = new YapbamState();
	
	private Properties properties;

	protected YapbamState() {
		this.properties = new Properties();
		try {
			if (Portable.isPortable()) {
				if (!Preferences.INSTANCE.isFirstRun()) {
					FileInputStream inStream = new FileInputStream(getFile());
					try {
						properties.load(inStream);
					} finally {
						inStream.close();
					}
				}
			} else {
				PreferencesUtils.fromPreferences(getPreferences(), properties);
			}
		} catch (Throwable e) {
			// On the first run, the file doesn't exist
			// If there's another error, maybe it would be better to do something else
			//TODO
		}
	}

	private java.util.prefs.Preferences getPreferences() {
		return java.util.prefs.Preferences.userRoot().node(this.getClass().getName());
	}

	protected File getFile() {
		return new File (Portable.getDataDirectory(), ".yapbam"); //$NON-NLS-1$
	}
	
	public void restoreState(JTable table, String prefix) {
		TableColumnModel model = table.getColumnModel();
		StartStateSettings startOptions = Preferences.INSTANCE.getStartStateOptions();
		if (startOptions.isRememberColumnsWidth()) {
			// Restore the width
			for (int i = 0; i < model.getColumnCount(); i++) {
				String valueString = (String) properties.get(prefix+COLUMN_WIDTH+i);
				if (valueString!=null) {
					int width = Integer.parseInt(valueString);
					if (width>0) {
						model.getColumn(i).setPreferredWidth(width);
					}
				}
			}
		}
		if (startOptions.isRememberColumnsOrder()) {
			// Restore column order
			for (int i = model.getColumnCount()-1; i>=0 ; i--) {
				String valueString = (String) properties.get(prefix+COLUMN_INDEX+i);
				if (valueString!=null) {
					int modelIndex = Integer.parseInt(valueString);
					if ((modelIndex>=0) && (modelIndex<table.getColumnCount())) {
						table.moveColumn(table.convertColumnIndexToView(modelIndex), i);
					}
				}
			}
		}
		if (startOptions.isRememberHiddenColumns()) {
			// Restore the show/hide column
			if (model instanceof XTableColumnModel) {
				XTableColumnModel xModel = (XTableColumnModel)model;
				for (int i = 0; i < xModel.getColumnCount(false); i++) {
					if (Boolean.valueOf(properties.getProperty(prefix+COLUMN_HIDDEN+i, "false"))) { //$NON-NLS-1$
						TableColumn column = xModel.getColumnByModelIndex(i);
						xModel.setColumnVisible(column, false);
					}
				}
			}
		}
		if (startOptions.isRememberRowsSortKeys()) {
			RowSorter<? extends TableModel> sorter = table.getRowSorter();
			if (sorter!=null) {
				String sorters = properties.getProperty(prefix+COLUMN_SORTER);
				if (sorters!=null) {
					ArrayList<SortKey> keys = new ArrayList<RowSorter.SortKey>();
					String[] split = StringUtils.split(sorters, ',');
					for (int i = 0; i < split.length; i++) {
						SortKey sortKey = getSortKey(split[i]);
						if (sortKey.getColumn()<table.getColumnCount()) {
							// If the column is in the table, add it to sort keys
							// Some may wonder why we have to test that column is in the table.
							// You should remember that some columns may have been removed since the state was saved.
							keys.add(sortKey);
						}
					}
					sorter.setSortKeys(keys);
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
	
	private static SortKey getSortKey(String encodedForm) {
		String[] split = StringUtils.split(encodedForm, ':');
		int column = Integer.parseInt(split[0]);
		boolean ascending = Boolean.parseBoolean(split[1]);
		return new SortKey(column, ascending?SortOrder.ASCENDING:SortOrder.DESCENDING);
	}
	

	public void saveState(JTable table, String prefix) {
		// Save the sort order
		StringBuilder buf = new StringBuilder();
		RowSorter<? extends TableModel> sorter = table.getRowSorter();
		if (sorter!=null) {
			List<? extends SortKey> sortKeys = sorter.getSortKeys();
			for (SortKey sortKey : sortKeys) {
				if (sortKey.getSortOrder()!=SortOrder.UNSORTED) {
					if (buf.length()!=0) {
						buf.append(',');
					}
					buf.append(sortKey.getColumn()+":"+sortKey.getSortOrder().equals(SortOrder.ASCENDING)); //$NON-NLS-1$
				}
			}
		}
		if (buf.length()>0) {
			put(prefix+COLUMN_SORTER, buf.toString());
		} else {
			properties.remove(prefix+COLUMN_SORTER);
		}
		
		// Save columns width, order and visibility
		TableColumnModel model = table.getColumnModel();
		if (model instanceof XTableColumnModel) {
			XTableColumnModel xModel = (XTableColumnModel)model;
			for (int viewIndex = 0; viewIndex < xModel.getColumnCount(false); viewIndex++) {
				TableColumn column = xModel.getColumn(viewIndex, false);
				int modelIndex = column.getModelIndex();
				// Save the column width
				put(prefix+COLUMN_WIDTH+modelIndex, Integer.toString(column.getWidth()));
				// Save the column order (if two or more columns were inverted)
				put(prefix+COLUMN_INDEX+viewIndex, Integer.toString(modelIndex));
				String key = prefix+COLUMN_HIDDEN+modelIndex;
				put(key, Boolean.valueOf(!xModel.isColumnVisible(column)).toString());
			}
		} else {
			for (int viewIndex = 0; viewIndex < model.getColumnCount(); viewIndex++) {
				int modelIndex = table.convertColumnIndexToModel(viewIndex);
				// Save the column width
				put(prefix+COLUMN_WIDTH+modelIndex, Integer.toString(model.getColumn(viewIndex).getWidth()));
				// Save the column order (if two or more columns were inverted)
				put(prefix+COLUMN_INDEX+viewIndex, Integer.toString(modelIndex));
			}
		}
//		put(prefix+SELECTED_ROW, Integer.toString(table.getSelectedRow()));
//		put(prefix+SCROLL_POSITION, table.getVisibleRect());
	}
	
	public void saveState(TabbedPane tabbedPane, String prefix) {
		put(prefix+TAB_ORDER, ArrayUtils.toString(tabbedPane.getIds()));
	}
	
	public void restoreState(TabbedPane tabbedPane, String prefix) {
		if (Preferences.INSTANCE.getStartStateOptions().isRememberTabsOrder()) {
			String property = get(prefix+TAB_ORDER);
			if (property!=null) {
				tabbedPane.setOrder(ArrayUtils.parseIntArray(property));
			}
			// TabbedPane.setOrder changes the selected tab.
			// I think it's better to restore with the first tab displayed
			if (tabbedPane.getTabCount()>0) {
				tabbedPane.setSelectedIndex(0);
			}
		}
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

	/** Sets a state value.
	 * @param key The key to set
	 * @param value The value to link with the key
	 * @see PreferencesUtils#verifyPreferencesCompliance(String, String)
	 */
	public void put(String key, String value) {
		PreferencesUtils.verifyPreferencesCompliance(key, value);
		properties.put(key, value);
	}

	public Date getDate(String key) {
		String dummy = properties.getProperty(key);
		if (dummy==null) {
			return new Date(0);
		}
		return DateUtils.integerToDate(Integer.parseInt(dummy));
	}
	
	/** Gets an integer value.
	 * @param key The key to get
	 * @return an Integer or null if the key is unknown or not contains an integer.  
	 */
	public Integer getInteger(String key) {
		String dummy = properties.getProperty(key, null);
		if (dummy!=null) {
			try {
				return Integer.valueOf(dummy);
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return null;
	}

	public void put (String key, Date date) {
		put(key, Integer.toString(DateUtils.dateToInteger(date)));
	}
	
	public void put(String key, Rectangle value) {
		put(key, value.x+","+value.y+","+value.width+","+value.height); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	public Rectangle getRectangle(String key) {
		String value = properties.getProperty(key);
		if (value==null) {
			return null;
		}
		StringTokenizer tokens = new StringTokenizer(value, ","); //$NON-NLS-1$
		return new Rectangle(Integer.parseInt(tokens.nextToken()),Integer.parseInt(tokens.nextToken()),
				Integer.parseInt(tokens.nextToken()),Integer.parseInt(tokens.nextToken()));
	}

	public void toDisk() throws IOException {
		if (Portable.isPortable()) {
			FileOutputStream stream = FileUtils.getHiddenCompliantStream(getFile());
			try {
				properties.store(stream, "Yapbam startup state"); //$NON-NLS-1$
			} finally {
				stream.close();
			}
		} else {
			PreferencesUtils.toPreferences(getPreferences(), properties, true);
		}
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
			put(key, Base64.encodeBase64String(baos.toByteArray()));
		} catch (IOException e) {
			// Should not happen ... because the serialization is made into memory
			throw new RuntimeException(e);
		}
	}
	
	/** Restores an object.
	 * @param key The key used to save the object
	 * @return a serializable object or null if the key is unknown or if an exception occurs during deserialization.
	 * @see #save(String, Serializable)
	 */
	public Serializable restore(String key) {
		if (!contains(key)) {
			return null;
		}
		try {
			byte[] decoded = Base64.decodeBase64(get(key));
			ByteArrayInputStream bais = new ByteArrayInputStream(decoded);
			ObjectInputStream ois = new ObjectInputStream(new GZIPInputStream(bais));
			Serializable result = (Serializable) ois.readObject();
			ois.close();
			return result;
		} catch (Exception e) {
			// If something goes wrong, return null
			LoggerFactory.getLogger(getClass()).warn(Formatter.format("Exception while restoring {0}",key), e); //$NON-NLS-1$
			return null;
		}
	}
	
	private class FilterSerializer extends AbstractSerializer<Filter> {
		private GlobalData data;

		FilterSerializer(GlobalData data) {
			this.data = data;
		}
		
		@Override
		public void directWrite(Filter filter, OutputStream out, ProgressReport report) throws IOException {
			try {
				XMLSerializer xmlSerializer = new XMLSerializer(out);
				xmlSerializer.serialize(filter);
				xmlSerializer.closeDocument();
			} catch (SAXException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Filter directRead(String password, InputStream in, ProgressReport report) throws IOException {
			FilterHandler handler = new FilterHandler(data);
			try {
				SAXParserFactory.newInstance().newSAXParser().parse(in, handler);
			} catch (SAXException e) {
				throw new RuntimeException(e);
			} catch (ParserConfigurationException e) {
				throw new RuntimeException(e);
			}
			return handler.getFilter();
		}
		
	}

	public void save(String key, Filter filter, String password) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			FilterSerializer serializer = new FilterSerializer(null);
			serializer.write(filter, stream, password, null);
			stream.flush();
			String xmlContent = Base64.encodeBase64String(stream.toByteArray());
			put(key, xmlContent);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Filter restoreFilter(String key, GlobalData data) {
		String property = properties.getProperty(key);
		if (property!=null) {
			byte[] bytes;
			if (property.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) { //$NON-NLS-1$
				// Yapbam versions until 0.9.4 did not base64 encode the saved filter.
				bytes = property.getBytes();
			} else {
				bytes = Base64.decodeBase64(property);
			}
			final InputStream stream = new ByteArrayInputStream(bytes);
			try {
				return new FilterSerializer(data).read(data.getPassword(), stream, null);
			} catch (AccessControlException e) {
				// The password is not compatible with the saved filter. Simply ignore filter.
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}
}
