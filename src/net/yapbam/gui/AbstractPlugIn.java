package net.yapbam.gui;

import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeSupport;
import java.util.List;

import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import com.fathzer.soft.ajlib.utilities.NullUtils;

import net.yapbam.data.Account;
import net.yapbam.data.Filter;
import net.yapbam.gui.dialogs.preferences.PreferencePanel;

/** This abstract class represents a Yapbam plugin.
 * Such a plugin can define (or not) some GUI elements that will be added to the Yapbam interface :<UL>
 * <LI>A panel to add to the main TabbedPanel</LI> 
 * <LI>Some specific menus</LI>
 * <LI>Some specific menu items to insert in standard menus</LI>
 * <LI>A preference panel to add to the preferences dialog</LI>
 * </UL>
 * This implementation is a totally empty plugin which does ... nothing.
 * You may override its methods in order to implement a fully functional plugin.<BR>
 * <B>A plugin has to had a public constructor with two arguments :</B><OL>
 * <LI>FilteredData : The root object of Yapbam's data</LI>
 * <LI>Object an object representing the state of the plugin (returned by getRestartData()) or null if no previous state
 * was saved.</LI></OL>
 * The net.yapbam.ihm.transactiontable.TransactionsPlugIn is a good example of what could be done by a plugin.
 * @see #AbstractPlugIn()
 */
public abstract class AbstractPlugIn implements AccountSelector {
	//TODO Define how to check for updates and how to download plugins
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
	/** The import/export part of the file menu */
	public static final int IMPORT_EXPORT_PART = 8;
	/** The web sites direct acces part of the file menu */
	public static final int WEB_SITES_PART = 9;
	
	/** The filter menu id */
	public static final int FILTER_MENU = 2;
	/** The transactions menu id */
	public static final int TRANSACTIONS_MENU = 1;
	
	/** Panel icon property name. */
	public static final String PANEL_ICON_PROPERTY_NAME = "panelIcon"; //$NON-NLS-1$
	/** Panel tooltip property name. */
	public static final String PANEL_TOOLTIP_PROPERTY_NAME = "panelTooltip"; //$NON-NLS-1$
	/** Panel title property name. */
	public static final String PANEL_TITLE_PROPERTY_NAME = "panelTitle"; //$NON-NLS-1$
	/** Panel title property name. */
	public static final String PRINTING_SUPPORTED_PROPERTY_NAME = "printing"; //$NON-NLS-1$
	/** Panel allow menu. */
	public static final String FILTER_SUPPORTED_PROPERTY_NAME = "filter"; //$NON-NLS-1$
	
	private PropertyChangeSupport propertyChangeSupport;
	private Icon panelIcon;
	private String panelToolTip;
	private String panelTitle;
	private boolean printSupport;
	private YapbamInstance context;
	
	/** Constructor.
	 * <br>Be aware that a <b>Yapbam plugin has to had a public constructor with two arguments</b> :<OL>
	 * <LI>FilteredData : The root object of Yapbam's data</LI>
	 * <LI>Object : an object representing the state of the plugin (returned by getRestartData()) or null if no previous state was save.</LI>
	 */
	protected AbstractPlugIn() {
		super();
		propertyChangeSupport = new PropertyChangeSupport(this);
		this.panelIcon = null;
		this.panelTitle = null;
		this.panelToolTip = null;
		this.printSupport = false;
	}
	
	void setContext(YapbamInstance context) {
		this.context = context;
	}
	
	public YapbamInstance getContext() {
		return this.context;
	}
	
	/** Gets the plugin specific menus.
	 *  These menus are inserted in the menu bar just before the "?" menu.
	 *  <br>A plugin may (probably) hide its specific menus when its panel is not shown. This could be done with the
	 *  setDisplayed method.
	 * @return The menus or null if the plugin has no specific menus.
	 * @see #setDisplayed(boolean)
	 */
	public JMenu[] getPlugInMenu() {
        return null;
	}
	
	/** Gets the plugin specific menu items for a part of a standard menu.
	 * @param part an integer that specific the part of the menu.
	 * The possible values are the ones of this class constants.
	 * @return Menus items or null if nothing has to be added to that part of the menu.
	 * A null item means a separator.
	 */
	public JMenuItem[] getMenuItem(int part) {
		return null;
	}

	/** Gets the main panel of the plugin.
	 *  <br>This panel will be added to the main tabbed pane.
	 * @return the panel or null if the plugin has no panel (for instance for a import/export plugin that's just define menu items)
	 */
	public JPanel getPanel() {
		return null;
	}
	
	/** Gets the main panel icon of the plugin.
	 *  This icon is displayed near the panel title in the main tabbed pane.
	 *  @return An icon or null if no icon is set (which is the default).
	 */
	public final Icon getPanelIcon() {
		return panelIcon;
	}
	
	/** Sets the main panel icon of the plugin.
	 * @param panelIcon the new panel icon. Note that if the plugin has no panel, this Icon is never displayed
	 * @see #getPanelIcon()
	 */
	public final void setPanelIcon(Icon panelIcon) {
		if (this.panelIcon!=panelIcon) {
			Icon old = this.panelIcon;
			this.panelIcon = panelIcon;
			this.getPropertyChangeSupport().firePropertyChange(PANEL_ICON_PROPERTY_NAME, old, panelIcon);
		}
	}

	/** Gets the tab title of this plugin.
	 * @return the title or null if there's no panel
	 * @see #getPanel()
	 */
	public final String getPanelTitle() {
		return this.panelTitle;
	}
	
	/** Sets the tab title of the plugin.
	 * @param title the new panel title. Note that if the plugin has no panel, this title is never displayed
	 * @see #getPanelTitle()
	 */
	public final void setPanelTitle(String title) {
		if (!NullUtils.areEquals(this.panelTitle,title)) {
			String old = this.panelTitle;
			this.panelTitle = title;
			this.getPropertyChangeSupport().firePropertyChange(PANEL_TITLE_PROPERTY_NAME, old, title);
		}
	}

	/** Gets the tab tooltip of this plugin.
	 * @return the tooltip or null if there's no panel
	 * @see #getPanel()
	 */
	public final String getPanelToolTip() {
		return panelToolTip;
	}

	/** Sets the tab tooltip of the plugin.
	 * @param tooltip the new panel tooltip. Note that if the plugin has no panel, this tooltip is never displayed
	 * @see #getPanelToolTip()
	 */
	public final void setPanelToolTip(String tooltip) {
		if (!NullUtils.areEquals(this.panelToolTip,tooltip)) {
			String old = this.panelToolTip;
			this.panelToolTip = tooltip;
			this.getPropertyChangeSupport().firePropertyChange(PANEL_TOOLTIP_PROPERTY_NAME, old, tooltip);
		}
	}

	/** This method is called when the plugin panel gain or loose the focus.
	 * <br>The plugin may perform some actions like showing/hidding its specifics menus when such an event occurs.
	 * @param displayed true if the panel obtains the focus, false if it looses it
	 */
	public void setDisplayed(boolean displayed) {
		// Override this method to do plugin specific actions
	}
	
	/** This method is called when the plugin panel gain the focus.
	 * <br>It ask the plugin if a general (not a plugin specific) menu is useful for this plugin.
	 * <br>Note : It's not possible to hide file and help menus
	 * @param menuId The menu id (see this class constants)
	 * @return true (by default) if the menu is useful, false otherwise (the menu will then be hidden).
	 */
	public boolean allowMenu(int menuId) {
		return true;
	}

	/** Saves the state of this panel.
	 * This method is called when needed (usually just before Yapbam quits) in order to let the plugin save its state
	 * (for instance, the column sizes of a JTable in the plugin panel).
	 * <br>The plugin may use YabamState class to perform these backups.
	 * @see YapbamState
	 */
	public void saveState() {
		// Override this method to save plugin state
	}

	/** Restores the state of this panel.
	 * This method is called when needed (usually at Yapbam startup) in order to let the plugin restore its previous state
	 * (for instance, the column sizes of a JTable in the plugin panel).
	 * <br>The plugin may use YabamState class to perform its restore.
	 * @see YapbamState
	 */
	public void restoreState() {
		// Override this method to restore plugin state
	}

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
	
	/** Returns the preference panel of this plugin.
	 * <br>Note : If the preferences deals with look and feel, they should not be managed by
	 * the returned panel, but by the one returned by getLFPreferencePanel
	 * @return the preferencePanel, null if no preference panel is defined.
	 * @see PreferencePanel
	 * @see #getLFPreferencePanel()
	 */
	public PreferencePanel getPreferencePanel() {
		return null;
	}

	/** Returns the look and feel preference panel of this plugin.
	 * <br>It will be included in the LF tab of the preference dialog
	 * @return the l&f preferencePanel, null if no l&f preference panel is defined.
	 * @see PreferencePanel
	 * @see #getPreferencePanel()
	 */
	public PreferencePanel getLFPreferencePanel() {
		return null;
	}
	
	/** Returns whether this plugin supports printing.
	 * Only plugins with a non null panel can support printing (other are supposed to have nothing to print).
	 * That returned value is used to enable/disable the "print" menu item in the "File" menu.
	 * <br>By default, plugins don't support printing. To change this, plugins should use setPrintingSupported method. 
	 * @return true if the panel supports printing.
	 * @see #print()
	 * @see #setPrintingSupported(boolean)
	 */
	public final boolean isPrintingSupported() {
		return printSupport;
	}
	
	/** Sets the support printing property.
	 * <br>Note that the printing support may depend of the state of the plugin. For instance, a plugin may have sub-panels,
	 * some could be printable, and others not. The plugin should then use this method to set the print support depending on which
	 * sub-panel is displayed.
	 * @param supported true if printing is supported.
	 * @see #isPrintingSupported()
	 */
	public final void setPrintingSupported(boolean supported) {
		if (supported!=this.printSupport) {
			this.printSupport = supported;
			this.propertyChangeSupport.firePropertyChange(PRINTING_SUPPORTED_PROPERTY_NAME, !supported, supported);
		}
	}
	
	/** Prints the content of this plugin.
	 * This method is called in response to the selection of the "print" menu item when this plugin
	 * is displayed. Note that this method is never called when isPrintingSupported returns false.
	 * By default, this method display the standard print dialog and prints the Printable instance returned by getPrintable.
	 * The printer setting are saved from one Yapbam execution to another. 
	 * @see #isPrintingSupported()
	 * @see #getPrintable()
	 */
	public void print() throws PrinterException {
		// TODO probably should be tested with no printer (maybe it throws an
		// exception).
		// Have a look at the javadoc of PrinterJob.getPrinterJob()
		PrinterJob job = PrinterJob.getPrinterJob();
		String prefix = this.getClass().getCanonicalName();
		PrintRequestAttributeSet attributes = YapbamState.INSTANCE.restorePrinterSettings(prefix);
		boolean doPrint = job.printDialog(attributes);
		// Ouch there's a known bug in job.printDialog, it's impossible to set the top margin with A4 format !
		// Sun bug id 6691639 ... no workaround after 2 years.
		if (doPrint) {
			YapbamState.INSTANCE.savePrinterSettings(prefix, attributes);
			job.setPrintable(getPrintable());
			try {
				job.print(attributes);
			} catch (PrinterException e) {
				/* The job did not successfully complete */
				//TODO
			}
		}
	}

	/** Gets the printer view of the plugin.
	 * This method is called by the print method. The default implementation returns null.
	 * @return a Printable
	 * @see #print()
	 */
	protected Printable getPrintable() {
		return null;
	}

	/** Plugins support events sending (see constants that define the supported events by default earlier in this page)
	 * @return a PropertyChangeSupport that can be listened to by other plugins (or by YapBam itself). Plugins may use
	 * this instance to fire their properties change.
	 */
	public final PropertyChangeSupport getPropertyChangeSupport() {
		return this.propertyChangeSupport;
	}

	/** Gets the transaction selector for this plugin.
	 * <br>By default, this method returns null, which means that the plugin is not able to select a transaction.
	 * Plugins that allows transaction selection may override this method.
	 * @return the transaction selector for this plugin.
	 * @see TransactionSelector
	 */
	public TransactionSelector getTransactionSelector() {
		return null;
	}
	
	@Override
	public Account getSelectedAccount() {
		if (allowMenu(FILTER_MENU)) {
			Filter filter = getContext().getCurrentTransactionSelector().getFilteredData().getFilter();
			List<Account> filterAccounts = filter.getValidAccounts();
			if ((filterAccounts!=null) && (filterAccounts.size()==1)) {
				// If the filter defines only one account, select this account in the created dialog
				return filterAccounts.get(0);
			}
		}
		return null;
	}
}
