package net.yapbam.gui;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public abstract class IconManager {
	public static final Icon OPEN = create("images/open.png"); //$NON-NLS-1$
	public static final Icon SAVE = create("images/save.png"); //$NON-NLS-1$
	public static final Icon SAVE_AS = create("images/saveAs.png"); //$NON-NLS-1$
	public static final Icon NEW = create("images/new.png"); //$NON-NLS-1$
	public static final Icon DELETE = create("images/delete.png"); //$NON-NLS-1$
	public static final Icon EDIT = create("images/edit.png"); //$NON-NLS-1$
	public static final Icon DUPLICATE = create("images/duplicate.png"); //$NON-NLS-1$
	public static final Icon NEW_FILE = NEW;
	public static final Icon NEW_ACCOUNT = NEW;
	public static final Icon NEW_TRANSACTION = NEW;
	public static final Icon NEW_BULK_TRANSACTION = create("images/bulk.png");; //$NON-NLS-1$
	public static final Icon NEW_MODE = NEW;
	public static final Icon EDIT_MODE = EDIT;
	public static final Icon DELETE_MODE = DELETE;
	public static final Icon DUPLICATE_MODE = DUPLICATE;
	public static final Icon NEW_CATEGORY = NEW;
	public static final Icon DELETE_TRANSACTION = DELETE;
	public static final Icon EDIT_TRANSACTION = EDIT;
	public static final Icon DUPLICATE_TRANSACTION = DUPLICATE;
	public static final Icon SPREAD = create("images/spread.png"); //$NON-NLS-1$
	public static final Icon SPREADABLE = create("images/spreadable.png"); //$NON-NLS-1$
	public static final Icon CHECK_TRANSACTION = create("images/check.png"); //$NON-NLS-1$
	public static final Icon UNCHECK_TRANSACTION = create("images/uncheck.png"); //$NON-NLS-1$
	public static final Icon HELP = create("images/help.png"); //$NON-NLS-1$
	public static final Icon PRINT = create("images/print.png"); //$NON-NLS-1$
	public static final Icon FIRST = create("images/first.png"); //$NON-NLS-1$
	public static final Icon PREVIOUS = create("images/previous.png"); //$NON-NLS-1$
	public static final Icon NEXT = create("images/next.png"); //$NON-NLS-1$
	public static final Icon LAST = create("images/last.png"); //$NON-NLS-1$
	public static final Icon UP = create("images/up.png"); //$NON-NLS-1$
	public static final Icon DOWN = create("images/down.png"); //$NON-NLS-1$
	public static final Icon TOP = create("images/top.png"); //$NON-NLS-1$
	public static final Icon BOTTOM = create("images/bottom.png"); //$NON-NLS-1$
	public static final Icon IMPORT = create("images/import.png"); //$NON-NLS-1$
	public static final Icon EXPORT = create("images/export.png"); //$NON-NLS-1$
	public static final Icon ALERT = create("images/alert.png"); //$NON-NLS-1$
	public static final Icon LOCK = create("images/locked.png"); //$NON-NLS-1$
	public static final Icon DEPLOY = SPREADABLE;
	public static final Icon UNDEPLOY = create("images/undeploy.png"); //$NON-NLS-1$
		
	/** Returns an ImageIcon, or null if the path was invalid. */
	private static ImageIcon create(String path) {
	    URL imgURL = IconManager.class.getResource(path);
	    if (imgURL != null) {
	        return new ImageIcon(imgURL);
	    } else {
	        System.err.println("Couldn't find file: " + path); //TODO Add to log
	        return null;
	    }
	}
}