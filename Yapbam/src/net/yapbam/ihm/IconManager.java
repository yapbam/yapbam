package net.yapbam.ihm;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public abstract class IconManager {
	public static final Icon SAVE = create("images/save.png");
	public static final Icon SAVE_AS = create("images/saveAs.png");
	private static final Icon NEW = create("images/new.png");
	public static final Icon NEW_FILE = NEW;
	public static final Icon NEW_ACCOUNT = NEW;
	public static final Icon NEW_TRANSACTION = NEW;
	public static final Icon NEW_MODE = NEW;
	public static final Icon NEW_CATEGORY = NEW;
	private static final Icon DELETE = create("images/delete.png");
	public static final Icon DELETE_TRANSACTION = DELETE;
	public static final Icon EDIT_TRANSACTION = create("images/edit.png");
	public static final Icon DUPLICATE_TRANSACTION = create("images/duplicate.png");
	public static final Icon SPREAD = create("images/spread.png");
	public static final Icon SPREADABLE = create("images/spreadable.png");
	
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