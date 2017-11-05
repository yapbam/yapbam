package net.yapbam.gui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;

import com.fathzer.soft.ajlib.swing.Utils;

public abstract class IconManager {
	public enum Name {
		OPEN, CLOSE, SAVE, SAVE_AS, NEW, DELETE, EDIT, DUPLICATE, NEW_FILE,
		NEW_ACCOUNT, EDIT_ACCOUNT, DELETE_ACCOUNT,
		NEW_TRANSACTION, DELETE_TRANSACTION, EDIT_TRANSACTION, DUPLICATE_TRANSACTION, NEW_BULK_TRANSACTION,
		CHECK_TRANSACTION, UNCHECK_TRANSACTION,
		NEW_MODE, EDIT_MODE, DELETE_MODE, DUPLICATE_MODE,
		NEW_CATEGORY, DELETE_CATEGORY,
		SPREAD, SPREADABLE, SPREAD_UP,
		FIRST, PREVIOUS, NEXT, LAST, UP, DOWN, TOP, BOTTOM, DEPLOY, UNDEPLOY,
		IMPORT, EXPORT, ARCHIVE, LOCK, ALERT, NEW_MESSAGE, MESSAGE, HELP, PRINT, SETTINGS, LINK
	}
	
	static {
		reset (16);
	}
	
	private static Map<Name, Icon> map;
	private static int currentSize = -1;
	
	public static Icon get(Name name) {
		return map.get(name);
	}
	
	public static void reset(int size) {
		if (size<=0) {
			throw new IllegalArgumentException();
		}
		if (size == currentSize) {
			return;
		}
		if (map==null) {
			map = new HashMap<IconManager.Name, Icon>();
		}
		map.clear();
		map.put(Name.OPEN, create("images/open.png", size)); //$NON-NLS-1$
		map.put(Name.CLOSE, create("images/close.png", size)); //$NON-NLS-1$
		map.put(Name.SAVE, create("images/save.png", size)); //$NON-NLS-1$
		map.put(Name.SAVE_AS, create("images/saveAs.png", size)); //$NON-NLS-1$
		map.put(Name.NEW, create("images/new.png", size)); //$NON-NLS-1$
		map.put(Name.DELETE, create("images/delete.png", size)); //$NON-NLS-1$
		map.put(Name.EDIT, create("images/edit.png", size)); //$NON-NLS-1$
		map.put(Name.DUPLICATE, create("images/duplicate.png", size)); //$NON-NLS-1$
		map.put(Name.NEW_FILE, map.get(Name.NEW));
		map.put(Name.NEW_ACCOUNT, map.get(Name.NEW));
		map.put(Name.EDIT_ACCOUNT, map.get(Name.EDIT));
		map.put(Name.DELETE_ACCOUNT, map.get(Name.DELETE));
		map.put(Name.NEW_TRANSACTION, map.get(Name.NEW));
		map.put(Name.DELETE_TRANSACTION, map.get(Name.DELETE));
		map.put(Name.EDIT_TRANSACTION, map.get(Name.EDIT));
		map.put(Name.DUPLICATE_TRANSACTION, map.get(Name.DUPLICATE));
		map.put(Name.NEW_BULK_TRANSACTION, create("images/bulk.png", size)); //$NON-NLS-1$
		map.put(Name.NEW_MODE, map.get(Name.NEW));
		map.put(Name.DELETE_MODE, map.get(Name.DELETE));
		map.put(Name.EDIT_MODE, map.get(Name.EDIT));
		map.put(Name.DUPLICATE_MODE, map.get(Name.DUPLICATE));
		map.put(Name.NEW_CATEGORY, map.get(Name.NEW));
		map.put(Name.DELETE_CATEGORY, map.get(Name.DELETE));
		map.put(Name.CHECK_TRANSACTION, create("images/check.png", size)); //$NON-NLS-1$
		map.put(Name.UNCHECK_TRANSACTION, create("images/uncheck.png", size)); //$NON-NLS-1$
		map.put(Name.SPREAD, create("images/spread.png", size)); //$NON-NLS-1$
		map.put(Name.SPREAD_UP, create("images/spreadUp.png", size)); //$NON-NLS-1$
		map.put(Name.SPREADABLE, create("images/spreadable.png", size)); //$NON-NLS-1$
		map.put(Name.FIRST, create("/com/fathzer/soft/ajlib/swing/widget/first.png", size)); //$NON-NLS-1$
		map.put(Name.PREVIOUS, create("/com/fathzer/soft/ajlib/swing/widget/previous.png", size)); //$NON-NLS-1$
		map.put(Name.NEXT, create("/com/fathzer/soft/ajlib/swing/widget/next.png", size)); //$NON-NLS-1$
		map.put(Name.LAST, create("/com/fathzer/soft/ajlib/swing/widget/last.png", size)); //$NON-NLS-1$
		map.put(Name.UP, create("images/up.png", size)); //$NON-NLS-1$
		map.put(Name.DOWN, create("images/down.png", size)); //$NON-NLS-1$
		map.put(Name.TOP, create("images/top.png", size)); //$NON-NLS-1$
		map.put(Name.BOTTOM, create("images/bottom.png", size)); //$NON-NLS-1$
		map.put(Name.IMPORT, create("images/import.png", size)); //$NON-NLS-1$
		map.put(Name.EXPORT, create("images/export.png", size)); //$NON-NLS-1$
		map.put(Name.ARCHIVE, create("images/archive.png", size)); //$NON-NLS-1$
		map.put(Name.ALERT, create("images/alert.png", size)); //$NON-NLS-1$
		map.put(Name.NEW_MESSAGE, create("images/newMessage.png", size)); //$NON-NLS-1$
		map.put(Name.MESSAGE, create("images/message.png", size)); //$NON-NLS-1$
		map.put(Name.LOCK, create("images/locked.png", size)); //$NON-NLS-1$
		map.put(Name.HELP, create("images/help.png", size)); //$NON-NLS-1$
		map.put(Name.PRINT, create("images/print.png", size)); //$NON-NLS-1$
		map.put(Name.DEPLOY, get(Name.SPREADABLE)); //$NON-NLS-1$
		map.put(Name.UNDEPLOY, create("images/undeploy.png", size)); //$NON-NLS-1$
		map.put(Name.SETTINGS, create("images/settings.png", size)); //$NON-NLS-1$)
		map.put(Name.LINK, create("images/link.png", size)); //$NON-NLS-1$)
		currentSize = size;
//		System.out.println ("icon size reset to "+size);
	}
	
	private static Icon create(String path, int size) {
		return Utils.createIcon(IconManager.class.getResource(path), size);
	}
}