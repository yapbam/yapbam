package net.yapbam.gui;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public abstract class IconManager {
	public static final Icon SAVE = create("images/save.png");
	public static final Icon SAVE_AS = create("images/saveAs.png");
	public static final Icon NEW = create("images/new.png");
	public static final Icon DELETE = create("images/delete.png");
	public static final Icon EDIT = create("images/edit.png");
	public static final Icon DUPLICATE = create("images/duplicate.png");
	public static final Icon NEW_FILE = NEW;
	public static final Icon NEW_ACCOUNT = NEW;
	public static final Icon NEW_TRANSACTION = NEW;
	public static final Icon NEW_MODE = NEW;
	public static final Icon EDIT_MODE = EDIT;
	public static final Icon DELETE_MODE = DELETE;
	public static final Icon DUPLICATE_MODE = DUPLICATE;
	public static final Icon NEW_CATEGORY = NEW;
	public static final Icon DELETE_TRANSACTION = DELETE;
	public static final Icon EDIT_TRANSACTION = EDIT;
	public static final Icon DUPLICATE_TRANSACTION = DUPLICATE;
	public static final Icon SPREAD = create("images/spread.png");
	public static final Icon SPREADABLE = create("images/spreadable.png");
	public static final Icon CHECK_TRANSACTION = create("images/check.png");
		
	/** Returns an ImageIcon, or null if the path was invalid. */
	private static ImageIcon create(String path) {
	    URL imgURL = MainMenuBar.class.getResource(path);
	    if (imgURL != null) {
	        return new ImageIcon(imgURL);
	    } else {
	        System.err.println("Couldn't find file: " + path); //TODO Add to log
	        return null;
	    }
	}
}