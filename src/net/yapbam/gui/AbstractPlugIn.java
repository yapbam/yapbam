package net.yapbam.gui;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import net.yapbam.gui.transactiontable.TransactionsPlugIn;

/** This abstract class represents a Yapbam plugin.
 * Such a plugin can define (or not) some GUI elements that will be added to the Yapbam interface :<UL>
 * <LI>A panel to add to the main TabbedPanel</LI> 
 * <LI>Some specific menus</LI>
 * <LI>Some specific menu items to insert in standard menus</LI></UL>
 * This implementation is a totally empty plugin which does ... nothing.
 * You may override its methods in order to implement a fully functional plugin.
 * A plugin has to had a public constructor with two arguments :<OL>
 * <LI>FilteredData : The root object of Yapbam's data</LI>
 * <LI>Object an object representing the state of the plugin (returned by getRestartData()) or null if no previous state
 * was saved.</LI></OL>
 * The net.yapbam.ihm.transactiontable.TransactionsPlugIn is a good example of what could be done by a plugin.
 * @see #AbstractPlugIn()
 * @see TransactionsPlugIn
 */
public abstract class AbstractPlugIn { //TODO Define interactions with Preferences dialog
	/** The open, save ... part of the file menu */
	public static final int FILE_MANIPULATION_PART = 1;
	/** The preference part of the file menu */
	public static final int PREFERENCES_PART = 2;
	/** The first part of the accounts menu. Menu items inserted there will be placed before the account list */
	public static final int ACCOUNTS_PART = 3;
	/** The first part of the transactions menu. Menu items inserted there will be placed after the "new transaction" item */
	public static final int TRANSACTIONS_PART = 4;
	/** The second part of the transactions menu. Menu items inserted there will be placed after the "new periodic transaction" item */
	public static final int PERIODIC_TRANSACTIONS_PART = 5;
	/** The first part of the "?" menu. Menu items inserted there will be placed after the "About" item */
	public static final int ABOUT_PART = 6;
	/** The second part of the "?" menu. Menu items inserted there will be placed after the "check for updates" item */
	public static final int UPDATES_PART = 7;
	
	/** The account menu id */
	public static final int ACCOUNT_MENU = 1;
	/** The filter menu id */
	public static final int FILTER_MENU = 3;
	/** The transactions menu id */
	public static final int TRANSACTIONS_MENU = 2;
	
	/** Constructor.
	 * <br>Be aware that a <b>Yapbam plugin has to had a public constructor with two arguments</b> :<OL>
	 * <LI>FilteredData : The root object of Yapbam's data</LI>
	 * <LI>Object : an object representing the state of the plugin (returned by getRestartData()) or null if no previous state was save.</LI>
	 */
	protected AbstractPlugIn() {
		super();
	}
	
	/** Get the plugin specific menus.
	 *  These menus are inserted in the menu bar just before the "?" menu.
	 *  <br>A plugin may (probably) hide its specific menus when its panel is not shown. This could be done with the
	 *  setDisplayed method.
	 * @return The menus or null if the plugin has no specific menus.
	 * @see #setDisplayed(boolean)
	 */
	public JMenu[] getPlugInMenu() {
        return null;
	}
	
	/** Get the plugin specific menu items for a part of a standard menu.
	 * @param part an integer that specific the part of the menu.
	 * The possible values are the ones of this class constants.
	 * @return Menus items or null if nothing has to be added to that part of the menu.
	 * A null item means a separator.
	 */
	public JMenuItem[] getMenuItem(int part) {
		return null;
	}

	/** Get the main panel of the plugin.
	 *  <br>This panel will be added to the main tabbed pane.
	 * @return the panel or null if the plugin has no panel (for instance for a import/export plugin that's just define menu items)
	 */
	public JPanel getPanel() {
		return null;
	}

	/** Get the tab title of this plugin.
	 * @return the title or null if there's no panel
	 * @see #getPanel()
	 */
	public String getPanelTitle() {
		return null;
	}
	
	/** Get the tab tooltip of this plugin.
	 * @return the tooltip or null if there's no panel
	 * @see #getPanel()
	 */
	public String getPanelToolIp() {
		return null;
	}

	/** This method is called when the plugin panel gain or loose the focus.
	 * <br>The plugin may perform some actions like showing/hidding its specifics menus when such an event occurs.
	 * @param displayed true if the panel obtains the focus, false if it looses it
	 */
	public void setDisplayed(boolean displayed) {}
	
	/** This method is called when the plugin panel gain the focus.
	 * <br>It ask the plugin if a general (not a plugin specific) menu is useful for this plugin.
	 * <br>Note : It's not possible to hide file and help menus
	 * @param menuId The menu id (see this class constants)
	 * @return true (by default) if the menu is useful, false otherwise (the menu will then be hidden).
	 */
	public boolean allowMenu(int menuId) {
		return true;
	}

	/** Save the state of this panel.
	 * This method is called when needed (usually just before Yapbam quits) in order to let the plugin save its state
	 * (for instance, the column sizes of a JTable in the plugin panel).
	 * <br>The plugin may use YabamState class to perform these backups.
	 * @see YapbamState
	 */
	public void saveState() {}

	/** Restore the state of this panel.
	 * This method is called when needed (usually at Yapbam startup) in order to let the plugin restore its previous state
	 * (for instance, the column sizes of a JTable in the plugin panel).
	 * <br>The plugin may use YabamState class to perform its restore.
	 * @see YapbamState
	 */
	public void restoreState() {}

	/** On some events, the GUI has to be restarted (for instance if the look and feel is changed).
	 * Then, the plugin has to be unloaded and then reloaded.
	 * <br>Before the unload this method is called. Its result is, then, sent to the constructor during the restart.
	 * <br>The goal is to retrieve exactly the state of the plugin after the restart. Of course, it occurs not
	 * very often, so, if the complete backup is to hard ... it could be acceptable to perform some shortcuts. 
	 * @return An object representing the plugin state.
	 */
	public Object getRestartData() {
		return null;
	}
}
