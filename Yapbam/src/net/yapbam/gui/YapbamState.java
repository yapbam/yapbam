package net.yapbam.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import net.yapbam.util.DateUtils;
import net.yapbam.util.Portable;

public class YapbamState {
	private static final String FILE_PATH = "file.path"; //$NON-NLS-1$
	private static final String LAST_URI = "data.uri"; //$NON-NLS-1$
	
	private static final String COLUMN_WIDTH = "column.width."; //$NON-NLS-1$
	private static final String COLUMN_INDEX = "column.index."; //$NON-NLS-1$
//	private static final String SELECTED_ROW = "selectedRow"; //$NON-NLS-1$
//	private static final String SCROLL_POSITION = "scrollPosition"; //$NON-NLS-1$

	private static final String FRAME_SIZE_WIDTH = "frame.size.width"; //$NON-NLS-1$
	private static final String FRAME_SIZE_HEIGHT = "frame.size.height"; //$NON-NLS-1$
	private static final String FRAME_LOCATION_Y = "frame.location.y"; //$NON-NLS-1$
	private static final String FRAME_LOCATION_X = "frame.location.x"; //$NON-NLS-1$

	public static final YapbamState INSTANCE = new YapbamState();
	
	private Properties properties;

	private YapbamState() {
		this.properties = new Properties();
		try {
			properties.load(new FileInputStream(getFile()));
		} catch (Throwable e) {
			// On the first run, the file doesn't exist
			// If there's another error, maybe it would be better to do something else //TODO
		}
	}

	private static File getFile() {
		return new File (Portable.getLaunchDirectory(), ".yapbam");
	}
	
	void restoreMainFramePosition(MainFrame frame) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = Integer.parseInt((String) properties.getProperty(FRAME_LOCATION_X,"0"));
		int y = Integer.parseInt((String) properties.getProperty(FRAME_LOCATION_Y,"0"));
		int width = Integer.parseInt((String) properties.getProperty(FRAME_SIZE_WIDTH,""+(screenSize.width/2)));
		int height = Integer.parseInt((String) properties.getProperty(FRAME_SIZE_HEIGHT,""+(screenSize.height/2)));
		frame.setExtendedState(Frame.MAXIMIZED_BOTH); //TODO Save the maximized state
		//TODO Beware of a screen size change (especially of a reduction) ?
  /*
		if ((width==0) || (width+x>screenSize.width)) {
			x=0;
			width = screenSize.width/2;
		}
		if ((height==0) || (height+y>screenSize.height)) {
			y=0;
			height = screenSize.height/2;
		}*/
        frame.setLocation(x,y);
		frame.setSize(width,height);
		int extendedState = Frame.NORMAL;
		if (height<0) extendedState = extendedState | Frame.MAXIMIZED_VERT;
		if (width<0) extendedState = extendedState | Frame.MAXIMIZED_HORIZ;
		frame.setExtendedState(extendedState);
	}
	
	void restoreGlobalData(MainFrame frame) {
		URI uri = null;
		if (properties.containsKey(FILE_PATH)) {
			// Old format (before the data can be saved to an URI)
			uri = new File((String) properties.get(FILE_PATH)).toURI();
			properties.remove(FILE_PATH);
		} else if (properties.containsKey(LAST_URI)) {
			try {
				uri = new URI((String) properties.get(LAST_URI));
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (uri!=null) {
			try {
				frame.readData(uri);
			} catch (IOException e) {
				ErrorManager.INSTANCE.display(frame, e, MessageFormat.format(LocalizationData.get("MainFrame.ReadLastError"),uri)); //$NON-NLS-1$
			}
		}
	}

	static void save(MainFrame frame) {
		Properties properties = INSTANCE.properties;
		if (frame.getData().getURI()!=null) {
			properties.put(LAST_URI, frame.getData().getURI().toString());
		} else {
			properties.remove(LAST_URI);
		}
		Point location = frame.getLocation();
		properties.put(FRAME_LOCATION_X, Integer.toString(location.x));
		properties.put(FRAME_LOCATION_Y, Integer.toString(location.y));
		Dimension size = frame.getSize();
		int h = ((frame.getExtendedState() & Frame.MAXIMIZED_VERT) == 0) ? size.height : -1;
		properties.put(FRAME_SIZE_HEIGHT, Integer.toString(h));
		int w = ((frame.getExtendedState() & Frame.MAXIMIZED_HORIZ) == 0) ? size.width : -1;
		properties.put(FRAME_SIZE_WIDTH, Integer.toString(w));
		for (int i = 0; i < frame.getPlugInsNumber(); i++) {
			if (frame.getPlugIn(i)!=null) frame.getPlugIn(i).saveState();
		}
		try {
			properties.store(new FileOutputStream(getFile()), "Yapbam startup state"); //$NON-NLS-1$
		} catch (IOException e) {
			e.printStackTrace();
			//TODO What could we do ?
		}
	}
	
	public static void restoreState(JTable table, String prefix) {
		//TODO Restore which columns are hidden
		TableColumnModel model = table.getColumnModel();
		for (int i = 0; i < model.getColumnCount(); i++) {
			String valueString = (String) INSTANCE.properties.get(prefix+COLUMN_WIDTH+i);
			if (valueString!=null) {
				int width = Integer.parseInt(valueString);
				if (width>0) model.getColumn(i).setPreferredWidth(width);
			}
		}
		// Restore column order
		for (int i = model.getColumnCount()-1; i>=0 ; i--) {
			String valueString = (String) INSTANCE.properties.get(prefix+COLUMN_INDEX+i);
			if (valueString!=null) {
				int modelIndex = Integer.parseInt(valueString);
				if (modelIndex>=0) table.moveColumn(table.convertColumnIndexToView(modelIndex), i);
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

	public static void saveState(JTable table, String prefix) {
		//TODO Save which columns are hidden
		TableColumnModel model = table.getColumnModel();
		for (int i = 0; i < model.getColumnCount(); i++) {
			INSTANCE.properties.put(prefix+COLUMN_WIDTH+table.convertColumnIndexToModel(i), Integer.toString(model.getColumn(i).getWidth()));
		}
		// Save the column order (if two or more columns were inverted)
		for (int i = 0; i < model.getColumnCount(); i++) {
			INSTANCE.properties.put(prefix+COLUMN_INDEX+i, Integer.toString(table.convertColumnIndexToModel(i)));
		}
//		properties.put(prefix+SELECTED_ROW, Integer.toString(table.getSelectedRow()));
//		YapbamState.put(prefix+SCROLL_POSITION, table.getVisibleRect());
	}

	public static String get(String key) {
		return INSTANCE.properties.getProperty(key);
	}

	public static void put(String key, String value) {
		INSTANCE.properties.put(key, value);
	}

	public static Date getDate(String key) {
		String dummy = INSTANCE.properties.getProperty(key);
		if (dummy==null) return new Date(0);
		return DateUtils.integerToDate(Integer.parseInt(dummy));
	}
	
	public static void put (String key, Date date) {
		INSTANCE.properties.put(key, Integer.toString(DateUtils.dateToInteger(date)));
	}
	
	public static void put(String key, Rectangle value) {
		INSTANCE.properties.put(key, value.x+","+value.y+","+value.width+","+value.height);
	}
	
	public static Rectangle getRectangle(String key) {
		String value = INSTANCE.properties.getProperty(key);
		if (value==null) return null;
		StringTokenizer tokens = new StringTokenizer(value, ",");
		return new Rectangle(Integer.parseInt(tokens.nextToken()),Integer.parseInt(tokens.nextToken()),
				Integer.parseInt(tokens.nextToken()),Integer.parseInt(tokens.nextToken()));
	}
}
