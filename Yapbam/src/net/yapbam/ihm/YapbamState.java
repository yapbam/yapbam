package net.yapbam.ihm;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

public class YapbamState {
	private static final String COLUMN_WIDTH = "net.yapbam.transactionTable.column.width."; //$NON-NLS-1$
	private static final String COLUMN_INDEX = "net.yapbam.transactionTable.column.index."; //$NON-NLS-1$
//	private static final String SELECTED_ROW = "net.yapbam.transactionTable.selectedRow"; //$NON-NLS-1$
	private static final String SCROLL_POSITION = "net.yapbam.transactionTable.scrollPosition"; //$NON-NLS-1$
	
	private static final String FILE_PATH = "file.path"; //$NON-NLS-1$
	
	private static final String FRAME_SIZE_WIDTH = "frame.size.width"; //$NON-NLS-1$
	private static final String FRAME_SIZE_HEIGHT = "frame.size.height"; //$NON-NLS-1$
	private static final String FRAME_LOCATION_Y = "frame.location.y"; //$NON-NLS-1$
	private static final String FRAME_LOCATION_X = "frame.location.x"; //$NON-NLS-1$

	private static final String STATE_FILENAME = ".yapbam"; //$NON-NLS-1$

	public static final YapbamState INSTANCE = new YapbamState();
	
	private Properties properties;

	private YapbamState() {
		this.properties = new Properties();
		try {
			properties.load(new FileInputStream(STATE_FILENAME));
		} catch (Throwable e) {
			// On the first run, the file doesn't exist
			// If there's another error, maybe it would be better to do something else //TODO
		}
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
		frame.setSize(width,height); //FIXME if window is maximized, demaximized it results in a 0x0 window
		int extendedState = Frame.NORMAL;
		if (height<0) extendedState = extendedState | Frame.MAXIMIZED_VERT;
		if (width<0) extendedState = extendedState | Frame.MAXIMIZED_HORIZ;
		frame.setExtendedState(extendedState);
	}
	
	void restoreGlobalData(MainFrame frame) {
		if (properties.containsKey(FILE_PATH)) {
			File file = new File((String) properties.get(FILE_PATH));
			try {
				frame.getData().read(file);
			} catch (IOException e) {
				ErrorManager.INSTANCE.display(frame, e, MessageFormat.format(LocalizationData.get("MainFrame.ReadLastError"),file)); //$NON-NLS-1$
			}
		}
	}

	void restoreTransactionTableColumns(MainFrame frame) {
		JTable table = frame.getTransactionTable();
		TableColumnModel model = table.getColumnModel();
		for (int i = 0; i < model.getColumnCount(); i++) {
			String valueString = (String) properties.get(COLUMN_WIDTH+i);
			if (valueString!=null) {
				int width = Integer.parseInt(valueString);
				if (width>0) model.getColumn(i).setPreferredWidth(width);
			}
		}
		// Restore column order
		for (int i = model.getColumnCount()-1; i>=0 ; i--) {
			String valueString = (String) properties.get(COLUMN_INDEX+i);
			if (valueString!=null) {
				int modelIndex = Integer.parseInt(valueString);
				if (modelIndex>=0) table.moveColumn(table.convertColumnIndexToView(modelIndex), i);
			}
		}
		// Now the selected row (not a very good idea).
//		String valueString = (String) properties.get(SELECTED_ROW);
//		if (valueString!=null) {
//			int index = Integer.parseInt(valueString);
//			if (index < table.getRowCount()) table.getSelectionModel().setSelectionInterval(index, index);
//		}
		// And the scroll position
		Rectangle visibleRect = getRectangle(SCROLL_POSITION);
		table.scrollRectToVisible(visibleRect);
	}
	
	private static String toString(Rectangle rect) {
		return rect.x+","+rect.y+","+rect.width+","+rect.height;
	}
	
	private Rectangle getRectangle(String property) {
		String value = properties.getProperty(property);
		if (value==null) return null;
		StringTokenizer tokens = new StringTokenizer(value, ",");
		return new Rectangle(Integer.parseInt(tokens.nextToken()),Integer.parseInt(tokens.nextToken()),
				Integer.parseInt(tokens.nextToken()),Integer.parseInt(tokens.nextToken()));
	}

	private static void saveTransactionTableState(JTable table, Properties properties) {
		TableColumnModel model = table.getColumnModel();
		for (int i = 0; i < model.getColumnCount(); i++) {
			properties.put(COLUMN_WIDTH+table.convertColumnIndexToModel(i), Integer.toString(model.getColumn(i).getWidth()));
		}
		// Save the column order (if two or more columns were inverted)
		for (int i = 0; i < model.getColumnCount(); i++) {
			properties.put(COLUMN_INDEX+i, Integer.toString(table.convertColumnIndexToModel(i)));
		}
//		properties.put(SELECTED_ROW, Integer.toString(table.getSelectedRow()));
		properties.put(SCROLL_POSITION,toString(table.getVisibleRect()));
	}
	
	static void save(MainFrame frame) {
		Properties properties = INSTANCE.properties;
		if (frame.getData().getPath()!=null) {
			properties.put(FILE_PATH, frame.getData().getPath().toString());
		}
		Point location = frame.getLocation();
		properties.put(FRAME_LOCATION_X, Integer.toString(location.x));
		properties.put(FRAME_LOCATION_Y, Integer.toString(location.y));
		Dimension size = frame.getSize();
		int h = ((frame.getExtendedState() & Frame.MAXIMIZED_VERT) == 0) ? size.height : -1;
		properties.put(FRAME_SIZE_HEIGHT, Integer.toString(h));
		int w = ((frame.getExtendedState() & Frame.MAXIMIZED_HORIZ) == 0) ? size.width : -1;
		properties.put(FRAME_SIZE_WIDTH, Integer.toString(w));
		saveTransactionTableState(frame.getTransactionTable(), properties);
		try {
			properties.store(new FileOutputStream(STATE_FILENAME), "Yapbam statup state"); //$NON-NLS-1$
		} catch (IOException e) {
			//TODO What could we do ?
		}
	}
}
