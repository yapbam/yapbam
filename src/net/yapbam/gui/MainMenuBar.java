package net.yapbam.gui;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.dialog.FileChooser;

import net.yapbam.data.Account;
import net.yapbam.data.Filter;
import net.yapbam.data.GlobalData;
import net.yapbam.data.comparator.AccountComparator;
import net.yapbam.data.event.AccountAddedEvent;
import net.yapbam.data.event.AccountPropertyChangedEvent;
import net.yapbam.data.event.AccountRemovedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.FilterPropertyChangedEvent;
import net.yapbam.data.event.FiltersAddedEvent;
import net.yapbam.data.event.FiltersRemovedEvent;
import net.yapbam.export.ExportFormatType;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.actions.CheckNewReleaseAction;
import net.yapbam.gui.actions.CompoundTransactionSelector;
import net.yapbam.gui.actions.ConvertToPeriodicalTransactionAction;
import net.yapbam.gui.actions.CustomFilterAction;
import net.yapbam.gui.actions.DeleteTransactionAction;
import net.yapbam.gui.actions.DuplicateTransactionAction;
import net.yapbam.gui.actions.EditPreferenceAction;
import net.yapbam.gui.actions.EditTransactionAction;
import net.yapbam.gui.actions.NewAccountAction;
import net.yapbam.gui.actions.NewTransactionAction;
import net.yapbam.gui.actions.OpenAction;
import net.yapbam.gui.actions.SaveAction;
import net.yapbam.gui.actions.SaveAsAction;
import net.yapbam.gui.dialogs.AboutDialog;
import net.yapbam.gui.dialogs.GetPasswordDialog;
import net.yapbam.gui.dialogs.export.ExportDialog;
import net.yapbam.gui.dialogs.export.DataExporter;
import net.yapbam.gui.dialogs.export.ExportComponent;
import net.yapbam.gui.dialogs.export.ImportDialog;
import net.yapbam.gui.dialogs.export.ImportError;
import net.yapbam.gui.dialogs.export.ImportErrorDialog;
import net.yapbam.gui.dialogs.export.Importer;
import net.yapbam.gui.filter.UserDefinedFilterAction;
import net.yapbam.gui.persistence.YapbamDataWrapper;
import net.yapbam.gui.persistence.YapbamPersistenceManager;
import net.yapbam.gui.transactiontable.GeneratePeriodicalTransactionsAction;
import net.yapbam.gui.util.MenuScroller;
import net.yapbam.util.Portable;

public class MainMenuBar extends JMenuBar implements ActionListener {
	private static final long serialVersionUID = 1L;

	private MainFrame frame;

	private JMenuItem menuItemNew;
	private JMenuItem menuItemImport;
	private JMenuItem menuItemSave;
	private JMenuItem menuItemSaveAs;
	private JMenuItem menuItemProtect;
	private JMenuItem menuItemExport;
	private JMenuItem menuItemPrint;
	private JMenuItem menuItemQuit;

	private JMenuItem menuItemAbout;
	
	private JMenu filterMenu;
	private JMenu transactionMenu;

	MainMenuBar (MainFrame frame) {
		super();
		this.frame = frame;
		int menuShortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
	
		//Build the file menu.
		JMenu menu = new JMenu(LocalizationData.get("MainMenu.File")); //$NON-NLS-1$
		menu.setMnemonic(LocalizationData.getChar("MainMenu.File.Mnemonic")); //$NON-NLS-1$
		menu.setToolTipText(LocalizationData.get("MainMenu.File.ToolTip")); //$NON-NLS-1$
		this.add(menu);
	
		this.menuItemNew = new JMenuItem(LocalizationData.get("MainMenu.NewFile"), LocalizationData.getChar("MainMenu.NewFile.Mnemonic")); //$NON-NLS-1$ //$NON-NLS-2$
		this.menuItemNew.setIcon(IconManager.get(Name.NEW_FILE));
		this.menuItemNew.setToolTipText(LocalizationData.get("MainMenu.NewFile.ToolTip")); //$NON-NLS-1$
		this.menuItemNew.addActionListener(this);
		menu.add(this.menuItemNew);
		JMenuItem menuItemOpen = new JMenuItem(new OpenAction(this.frame));
		menuItemOpen.setMnemonic(LocalizationData.getChar("MainMenu.Open.Mnemonic")); //$NON-NLS-1$
		menuItemOpen.setAccelerator(KeyStroke.getKeyStroke(LocalizationData.getChar("MainMenu.Open.Accelerator"), menuShortcutKeyMask)); //$NON-NLS-1$
		menu.add(menuItemOpen);
		this.menuItemSave = new JMenuItem(new SaveAction(this.frame));
		this.menuItemSave.setAccelerator(KeyStroke.getKeyStroke(LocalizationData.getChar("MainMenu.Save.Accelerator"), menuShortcutKeyMask)); //$NON-NLS-1$
		this.menuItemSave.setMnemonic(LocalizationData.getChar("MainMenu.Save.Mnemonic")); //$NON-NLS-1$
		this.menuItemSave.setEnabled(frame.getData().somethingHasChanged());
		menu.add(this.menuItemSave);
		this.menuItemSaveAs = new JMenuItem(new SaveAsAction(this.frame)); //$NON-NLS-1$
		this.menuItemSaveAs.setMnemonic(LocalizationData.getChar("MainMenu.SaveAs.Mnemonic")); //$NON-NLS-1$
		this.menuItemSaveAs.setEnabled(!frame.getData().isEmpty());
		menu.add(this.menuItemSaveAs);
		insertPluginMenuItems(menu, AbstractPlugIn.FILE_MANIPULATION_PART);
		menu.addSeparator();
		this.menuItemProtect = new JMenuItem(LocalizationData.get("MainMenu.Protect"), IconManager.get(Name.LOCK)); //$NON-NLS-1$
		this.menuItemProtect.setMnemonic(LocalizationData.getChar("MainMenu.Protect.Mnemonic")); //$NON-NLS-1$
		this.menuItemProtect.setToolTipText(LocalizationData.get("MainMenu.Protect.ToolTip")); //$NON-NLS-1$
		this.menuItemProtect.addActionListener(this);
		this.menuItemProtect.setEnabled(frame.getData().getURI() != null);
		menu.add(this.menuItemProtect);
		menu.addSeparator();

		this.menuItemImport = new JMenuItem(LocalizationData.get("MainMenu.Import"), IconManager.get(Name.IMPORT)); //$NON-NLS-1$
		this.menuItemImport.setMnemonic(LocalizationData.getChar("MainMenu.Import.Mnemonic")); //$NON-NLS-1$
		this.menuItemImport.setToolTipText(LocalizationData.get("MainMenu.Import.ToolTip")); //$NON-NLS-1$
		this.menuItemImport.addActionListener(this);
		menu.add(this.menuItemImport);
		this.menuItemExport = new JMenuItem(LocalizationData.get("MainMenu.Export"), IconManager.get(Name.EXPORT)); //$NON-NLS-1$
		this.menuItemExport.setMnemonic(LocalizationData.getChar("MainMenu.Export.Mnemonic")); //$NON-NLS-1$
		this.menuItemExport.setToolTipText(LocalizationData.get("MainMenu.Export.ToolTip")); //$NON-NLS-1$
		this.menuItemExport.addActionListener(this);
		this.menuItemExport.setEnabled(frame.getData().getAccountsNumber()>0);
		menu.add(this.menuItemExport);
		insertPluginMenuItems(menu, AbstractPlugIn.IMPORT_EXPORT_PART);

		menu.addSeparator();
		this.menuItemPrint = new JMenuItem(LocalizationData.get("MainMenuBar.Print"), IconManager.get(Name.PRINT)); //$NON-NLS-1$
		this.menuItemPrint.setMnemonic(LocalizationData.getChar("MainMenu.Print.Mnemonic")); //$NON-NLS-1$
		this.menuItemPrint.setAccelerator(KeyStroke.getKeyStroke(LocalizationData.getChar("MainMenuBar.Print.Accelerator"), menuShortcutKeyMask)); //$NON-NLS-1$
		this.menuItemPrint.setToolTipText(LocalizationData.get("MainMenuBar.Print.ToolTip")); //$NON-NLS-1$
		this.menuItemPrint.addActionListener(this);
		this.menuItemPrint.setEnabled(false);
		menu.add(this.menuItemPrint);

		menu.addSeparator();
		JMenuItem editPrefMenu = new JMenuItem(new EditPreferenceAction(frame));
		editPrefMenu.setIcon(IconManager.get(Name.SETTINGS));
		menu.add(editPrefMenu);
		insertPluginMenuItems(menu, AbstractPlugIn.PREFERENCES_PART);

		menu.addSeparator();
		this.menuItemQuit = new JMenuItem(LocalizationData.get("MainMenu.Quit")); //$NON-NLS-1$
		this.menuItemQuit.setAccelerator(KeyStroke.getKeyStroke(LocalizationData.getChar("MainMenu.Quit.Accelerator"), menuShortcutKeyMask)); //$NON-NLS-1$
		this.menuItemQuit.setMnemonic(LocalizationData.getChar("MainMenu.Quit.Mnemonic")); //$NON-NLS-1$
		this.menuItemQuit.setToolTipText(LocalizationData.get("MainMenu.Quit.ToolTip")); //$NON-NLS-1$
		this.menuItemQuit.addActionListener(this);
		menu.add(this.menuItemQuit);

		transactionMenu = new JMenu(LocalizationData.get("MainMenu.Transactions")); //$NON-NLS-1$
		transactionMenu.setMnemonic(LocalizationData.getChar("MainMenu.Transactions.Mnemonic")); //$NON-NLS-1$
		transactionMenu.setToolTipText(LocalizationData.get("MainMenu.Transactions.ToolTip")); //$NON-NLS-1$
		JMenuItem menuItemNewAccount = new JMenuItem(new NewAccountAction(frame.getData())); //$NON-NLS-1$
		menuItemNewAccount.setMnemonic(LocalizationData.getChar("MainMenu.Accounts.New.Mnemonic")); //$NON-NLS-1$
		this.transactionMenu.add(menuItemNewAccount);
		insertPluginMenuItems(this.transactionMenu, AbstractPlugIn.ACCOUNTS_PART);
		this.transactionMenu.addSeparator();
		AccountSelector as = new AccountSelector() {
			@Override
			public Account getSelectedAccount() {
				return MainMenuBar.this.frame.getSelectedAccount();
			}
		};
		JMenuItem item = new JMenuItem(new NewTransactionAction(frame.getFilteredData(), getTransactionSelector(), false, as));
		item.setAccelerator(KeyStroke.getKeyStroke(LocalizationData.getChar("MainMenu.Transactions.New.Accelerator"), menuShortcutKeyMask)); //$NON-NLS-1$
		transactionMenu.add(item);
		item = new JMenuItem(new NewTransactionAction(frame.getFilteredData(), getTransactionSelector(), true, as));
		item.setText(LocalizationData.get("MainMenu.Transactions.NewMultiple")); //$NON-NLS-1$
		item.setToolTipText(LocalizationData.get("MainMenu.Transactions.NewMultiple.ToolTip")); //$NON-NLS-1$
		item.setAccelerator(KeyStroke.getKeyStroke(LocalizationData.getChar("MainMenu.Transactions.New.Accelerator"), menuShortcutKeyMask+ActionEvent.SHIFT_MASK)); //$NON-NLS-1$
		transactionMenu.add(item);
		TransactionSelector selector = getTransactionSelector();
		item = new JMenuItem(new EditTransactionAction(selector)); 
		item.setAccelerator(KeyStroke.getKeyStroke(LocalizationData.getChar("MainMenu.Transactions.Edit.Accelerator"), menuShortcutKeyMask)); //$NON-NLS-1$
		transactionMenu.add(item);
		item = new JMenuItem(new DuplicateTransactionAction(selector));
		item.setAccelerator(KeyStroke.getKeyStroke(LocalizationData.getChar("MainMenu.Transactions.Duplicate.Accelerator"), menuShortcutKeyMask)); //$NON-NLS-1$
		transactionMenu.add(item);
		item = new JMenuItem(new DeleteTransactionAction(selector));
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, menuShortcutKeyMask));

		transactionMenu.add(item);

		insertPluginMenuItems(transactionMenu, AbstractPlugIn.TRANSACTIONS_PART);
		transactionMenu.addSeparator();
		transactionMenu.add(new JMenuItem(new GeneratePeriodicalTransactionsAction(frame.getData(), true)));
		transactionMenu.add(new JMenuItem(new ConvertToPeriodicalTransactionAction(selector)));
		insertPluginMenuItems(transactionMenu,AbstractPlugIn.PERIODIC_TRANSACTIONS_PART);
		this.add(transactionMenu);

		// Build the filter menu
		filterMenu = new JMenu(LocalizationData.get("MainMenuBar.Filter")); //$NON-NLS-1$
		filterMenu.setToolTipText(LocalizationData.get("MainMenuBar.Filter.Tooltip")); //$NON-NLS-1$
		filterMenu.setMnemonic(LocalizationData.getChar("MainMenuBar.Filter.Mnemonic")); //$NON-NLS-1$
		updateFilterMenu();
		this.add(filterMenu);

		PropertyChangeListener listener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				//FIXME : We should test that the plugin is currently displayed
				if (AbstractPlugIn.FILTER_SUPPORTED_PROPERTY_NAME.equals(evt.getPropertyName())) {
					filterMenu.setVisible((Boolean) evt.getNewValue());
				} else if (AbstractPlugIn.PRINTING_SUPPORTED_PROPERTY_NAME.equals(evt.getPropertyName())) {
					menuItemPrint.setEnabled((Boolean) evt.getNewValue());
				}
			}
		};
		// Build plugins menus
		for (int i = 0; i < this.frame.getPlugInsNumber(); i++) {
			if (this.frame.getPlugIn(i) != null) {
				JMenu[] menus = this.frame.getPlugIn(i).getPlugInMenu();
				if (menus != null) {
					for (int j = 0; j < menus.length; j++) {
						this.add(menus[j]);
					}
				}
				this.frame.getPlugIn(i).getPropertyChangeSupport().addPropertyChangeListener(listener);
			}
		}

		// Build Help menu.
		menu = new JMenu(LocalizationData.get("MainMenu.QuestionMark")); //$NON-NLS-1$
		menu.setMnemonic(LocalizationData.getChar("MainMenu.QuestionMark.Mnemonic")); //$NON-NLS-1$
		menu.setToolTipText(LocalizationData.get("MainMenu.QuestionMark.ToolTip")); //$NON-NLS-1$
		this.add(menu);

		if (!Portable.isWebStarted()) {
			menu.add(new CheckNewReleaseAction(this.frame.getJFrame()));
			insertPluginMenuItems(menu, AbstractPlugIn.UPDATES_PART);
			menu.addSeparator();
		}
		item = getURLMenuItem(LocalizationData.get("MainMenu.help"), new File(Portable.getApplicationDirectory(),"help.html").toURI().toString()); //$NON-NLS-1$ //$NON-NLS-2$
		item.setToolTipText(LocalizationData.get("MainMenu.help.tooltip")); //$NON-NLS-1$
		menu.add(item);
		item = getURLMenuItem(LocalizationData.get("MainMenu.web.support"), "http://sourceforge.net/projects/yapbam/support"); //$NON-NLS-1$ //$NON-NLS-2$
		item.setToolTipText(LocalizationData.get("MainMenu.web.support.tooltip")); //$NON-NLS-1$
		menu.add(item);
		item = getURLMenuItem(LocalizationData.get("MainMenu.web.yapbam"), "https://www.yapbam.net"); //$NON-NLS-1$ //$NON-NLS-2$
		item.setToolTipText(LocalizationData.get("MainMenu.web.yapbam.tooltip")); //$NON-NLS-1$
		menu.add(item);
		insertPluginMenuItems(menu, AbstractPlugIn.WEB_SITES_PART);
		menu.addSeparator();
		this.menuItemAbout = new JMenuItem(LocalizationData.get("MainMenu.About")); //$NON-NLS-1$
		this.menuItemAbout.setMnemonic(LocalizationData.getChar("MainMenu.About.Mnemonic")); //$NON-NLS-1$
		this.menuItemAbout.setToolTipText(LocalizationData.get("MainMenu.About.ToolTip")); //$NON-NLS-1$
		this.menuItemAbout.addActionListener(this);
		menu.add(this.menuItemAbout);
		insertPluginMenuItems(menu, AbstractPlugIn.ABOUT_PART);

		this.frame.getData().addListener(new GlobalDataListener());
		this.frame.getFilteredData().addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if (event instanceof EverythingChangedEvent) {
					// The filter has changed
					// If an account is added, the filter menu refresh is perform by the
					// global data listener
					// It's because this listener will not receive new account events if
					// not all accounts are valid for the filter.
					updateFilterMenu();
				}
			}
		});
	}

	private JMenuItem getURLMenuItem(String title, final String url) {
		try {
			final URI uri = new URI(url);
			return new JMenuItem(new AbstractAction(title) {
				private static final long serialVersionUID = 1L;
				@Override
				public void actionPerformed(ActionEvent e) {
					HelpManager.show(MainMenuBar.this, uri);
				}
			});
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private void insertPluginMenuItems(JMenu menu, int part) {
		for (int i = 0; i < this.frame.getPlugInsNumber(); i++) {
			if (this.frame.getPlugIn(i) != null) {
				JMenuItem[] items = this.frame.getPlugIn(i).getMenuItem(part);
				if (items != null) {
					for (int j = 0; j < items.length; j++) {
						if (items[j] == null) {
							menu.addSeparator();
						} else {
							menu.add(items[j]);
						}
					}
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// output.setText("Menu selected"+e.getSource().toString());
		Object source = event.getSource();
		GlobalData data = this.frame.getData();
		if (source.equals(this.menuItemQuit)) {
			this.frame.getJFrame().dispatchEvent(new WindowEvent(this.frame.getJFrame(), WindowEvent.WINDOW_CLOSING));
		} else if (source.equals(this.menuItemNew)) {
			if (YapbamPersistenceManager.MANAGER.verify(this.frame, new YapbamDataWrapper(this.frame.getData())) &&
					JOptionPane.showConfirmDialog(this.frame,
							LocalizationData.get("MainMenu.NewFile.ConfirmMessage"), //$NON-NLS-1$
							LocalizationData.get("MainMenu.NewFile"), JOptionPane.YES_NO_OPTION)==0) { //$NON-NLS-1$
						data.clear();
			}
		} else if (source.equals(this.menuItemProtect)) {
			String password = this.frame.getData().getPassword();
			GetPasswordDialog dialog = new GetPasswordDialog(frame.getJFrame(), LocalizationData.get("FilePasswordDialog.title"), LocalizationData.get("FilePasswordDialog.setPassword.question"), null, password); //$NON-NLS-1$ //$NON-NLS-2$
			dialog.setWarningMessage(LocalizationData.get("FilePasswordDialog.setPassword.warning")); //$NON-NLS-1$
			dialog.setPasswordFieldToolTipText(LocalizationData.get("FilePasswordDialog.setPassword.tooltip")); //$NON-NLS-1$
			dialog.setConfirmIsRequired(true);
			dialog.setVisible(true);
			String newPassword = dialog.getPassword();
			if (newPassword!=null) {
				this.frame.getData().setPassword(newPassword);
			}
		} else if (source.equals(this.menuItemImport)) {
			doImport();
		} else if (source.equals(this.menuItemExport)) {
			doExport();
		} else if (source.equals(this.menuItemPrint)) {
			try {
				this.frame.getCurrentPlugIn().print();
			} catch (PrinterException e1) {
				String okButton = LocalizationData.get("GenericButton.ok"); //$NON-NLS-1$
				String message = Formatter.format(LocalizationData.get("MainMenuBar.Print.Error.Message"),e1.getMessage()); //$NON-NLS-1$
				JOptionPane.showOptionDialog(frame.getJFrame(),
					    message, LocalizationData.get("MainMenuBar.Print.Error.Title"), //$NON-NLS-1$
					    JOptionPane.YES_OPTION, JOptionPane.ERROR_MESSAGE, null,
					    new String[]{okButton}, okButton);
			}
		} else if (source.equals(this.menuItemAbout)) {
			new AboutDialog(MainMenuBar.this.frame.getJFrame()).setVisible(true);
		}
	}

	private void doImport() {
		JFileChooser chooser = new FileChooser(null);
		chooser.setLocale(LocalizationData.getLocale());
		File lastFile = ImportDialog.getLastFile();
		if ((lastFile != null) && lastFile.exists() && lastFile.canRead()) {
			chooser.setSelectedFile(lastFile);
		}
		chooser.updateUI();
		File file = chooser.showOpenDialog(frame.getJFrame())==JFileChooser.APPROVE_OPTION?chooser.getSelectedFile():null;
		if (file!=null) {
			try {
				GlobalData data = this.frame.getData();
				ImportDialog dialog = new ImportDialog(this.frame.getJFrame(), data, file);
				dialog.setVisible(true);
				Importer importer = dialog.getResult();
				if (importer!=null && YapbamPersistenceManager.MANAGER.verify(this.frame, new YapbamDataWrapper(data))) {
					if (!dialog.getAddToCurrentData()) {
						data.clear();
					}
					ImportError[] errors = importer.importFile(null);
					if (errors.length!=0) {
						ImportErrorDialog importErrorDialog = new ImportErrorDialog(frame.getJFrame(), importer.getParameters().getImportedFileColumns(), errors);
						importErrorDialog.setVisible(true);
						if (importErrorDialog.getResult()!=null) {
							errors = new ImportError[0];
						}
					}
					if (errors.length==0) {
						importer.importFile(data);
					}
				}
			} catch (IOException e) {
				ImportDialog.doError(this.frame.getJFrame(), e);
			}
		}
	}

	private void doExport() {
		ExportDialog exportDialog = new ExportDialog(this.frame.getJFrame(), this.frame.getFilteredData());
		exportDialog.setVisible(true);
		DataExporter exporter = exportDialog.getResult();
		if (exporter!=null) {
			ExportFormatType format = exporter.getParameters().getExportFormat();
			ExportComponent.chooseFileAndExport(frame.getFilteredData(), format, exportDialog, exporter);
		}
	}

	private final class AccountFilterActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem item = (JMenuItem) e.getSource();
			Account account = frame.getData().getAccount(item.getText());
			if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 0) {
				frame.getFilteredData().getFilter().setValidAccounts(Collections.singletonList(account));
			} else {
				doRemove(account);
			}
		}

		private void doRemove(Account account) {
			GlobalData globalData = frame.getData();
			Filter filter = frame.getFilteredData().getFilter();
			List<Account> validAccounts = frame.getFilteredData().getFilter().getValidAccounts();
			if (validAccounts==null) {
				validAccounts = new ArrayList<Account>();
				for (int i = 0; i < globalData.getAccountsNumber(); i++) {
					validAccounts.add(globalData.getAccount(i));
				}
			}
			if (validAccounts.contains(account)) {
				validAccounts.remove(account);
				if (validAccounts.isEmpty()) {
					validAccounts = null;
				}
			} else {
				validAccounts.add(account);
				if (validAccounts.size()==globalData.getAccountsNumber()) {
					validAccounts = null;
				}
			}
			filter.setValidAccounts(validAccounts);
		}
	}
	
	class GlobalDataListener implements DataListener {
	    public void processEvent(DataEvent event) {
			GlobalData data = (GlobalData) event.getSource();
			boolean somethingToSave = !data.isEmpty();
			menuItemSave.setEnabled(data.somethingHasChanged() && somethingToSave);
			menuItemSaveAs.setEnabled(somethingToSave);
			menuItemProtect.setEnabled(somethingToSave || (data.getURI() != null));
			menuItemExport.setEnabled(data.getAccountsNumber()!=0);
			boolean accountEvent = event instanceof AccountAddedEvent || event instanceof AccountRemovedEvent ||
					(event instanceof AccountPropertyChangedEvent && ((AccountPropertyChangedEvent)event).getProperty().equals(AccountPropertyChangedEvent.NAME));
			boolean filterEvent = (event instanceof FiltersAddedEvent || event instanceof FiltersRemovedEvent || event instanceof FilterPropertyChangedEvent);
			if (accountEvent || filterEvent) {
				updateFilterMenu();
			}
		}
	}		

	private transient MenuScroller menuScroller;
	private void updateFilterMenu() {
		if (menuScroller!=null) {
			menuScroller.dispose();
		}
		filterMenu.removeAll();
		if (frame.getFilteredData()!=null) {
			JCheckBoxMenuItem complexFilterMenuItem = new JCheckBoxMenuItem(new CustomFilterAction(frame.getFilteredData()));
			complexFilterMenuItem.setToolTipText(LocalizationData.get("MainMenuBar.customizedFilter.toolTip")); //$NON-NLS-1$
			filterMenu.add(complexFilterMenuItem);
			complexFilterMenuItem.setSelected(isComplex(frame.getFilteredData().getFilter()));
			JMenuItem eraseItem = new JMenuItem(LocalizationData.get("MainMenuBar.eraseFilters")); //$NON-NLS-1$
			eraseItem.setToolTipText(LocalizationData.get("MainMenuBar.eraseFilters.toolTip")); //$NON-NLS-1$
			eraseItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					frame.getFilteredData().getFilter().clear();
				}
			});
			filterMenu.add(eraseItem);
			filterMenu.addSeparator();
			GlobalData data = this.frame.getData();
			buildUserDefinedFiltersMenu(data);
			ActionListener listener = new AccountFilterActionListener();
			List<Account> filterAccounts = frame.getFilteredData().getFilter().getValidAccounts();
			boolean hasAccountFilter = filterAccounts!=null;
			Account[] accounts = AccountComparator.getSortedAccounts(frame.getData(), getLocale());
			for (int i = 0; i < data.getAccountsNumber(); i++) {
				Account account = accounts[i];
				JRadioButtonMenuItem item = new JRadioButtonMenuItem(account.getName());
				item.setToolTipText(Formatter.format(LocalizationData.get("MainMenuBar.AccountFilter.toolTip"), account.getName())); //$NON-NLS-1$
				if (hasAccountFilter) {
					item.setSelected(filterAccounts.contains(account));
				}
				filterMenu.add(item);
				item.addActionListener(listener);
			}
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(LocalizationData.get("MainMenuBar.NoFilter")); //$NON-NLS-1$
			item.setToolTipText(LocalizationData.get("MainMenuBar.NoAccountFilter.toolTip")); //$NON-NLS-1$
			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					frame.getFilteredData().getFilter().setValidAccounts(null);
				}
			});
			item.setSelected(!hasAccountFilter);
			filterMenu.add(item);
			filterMenu.addSeparator();
			buildBooleanFilterChoiceMenu(FilterActionItem.CHECKED_STATUS,
					new String[] { LocalizationData.get("MainMenuBar.checked"), //$NON-NLS-1$
							LocalizationData.get("MainMenuBar.notChecked") }, new int[] { Filter.CHECKED, Filter.NOT_CHECKED }, //$NON-NLS-1$
					new String[] { LocalizationData.get("MainMenuBar.checked.toolTip"), LocalizationData.get("MainMenuBar.notChecked.toolTip") }, //$NON-NLS-1$ //$NON-NLS-2$
					LocalizationData.get("MainMenuBar.NoCheckedFilter.toolTip")); //$NON-NLS-1$
			filterMenu.addSeparator();
			buildBooleanFilterChoiceMenu(FilterActionItem.NATURE,
					new String[] { LocalizationData.get("MainMenuBar.Expenses"), //$NON-NLS-1$
							LocalizationData.get("MainMenuBar.Receipts") }, new int[] { Filter.EXPENSES, Filter.RECEIPTS }, //$NON-NLS-1$
					new String[] {
							LocalizationData.get("MainMenuBar.Expenses.toolTip"), LocalizationData.get("MainMenuBar.Receipts.toolTip") }, //$NON-NLS-1$ //$NON-NLS-2$
					LocalizationData.get("MainMenuBar.NoAmountFilter.toolTip")); //$NON-NLS-1$
			menuScroller = new MenuScroller(filterMenu, 16, 150, data.getFiltersNumber()>0?5:3, 9, false);
		}
	}
	
	private void buildUserDefinedFiltersMenu(final GlobalData data) {
		if (data.getFiltersNumber()>0) {
			JMenu menu = new JMenu(LocalizationData.get("MainMenuBar.predefinedFilters.menu.title")); //$NON-NLS-1$
			menu.setToolTipText(LocalizationData.get("MainMenuBar.predefinedFilters.menu.tooltip")); //$NON-NLS-1$
			List<String> filterNames = new ArrayList<String>(data.getFiltersNumber());
			for (int i=0;i<data.getFiltersNumber();i++) {
				filterNames.add(data.getFilter(i).getName());
			}
			Collections.sort(filterNames, String.CASE_INSENSITIVE_ORDER);
			for (String name:filterNames) {
				menu.add(new JMenuItem(new UserDefinedFilterAction(frame.getFilteredData(), name)));
			}
			filterMenu.add(menu);
			filterMenu.addSeparator();
		}
	}

	/** Gets a filter complexity.
	 * @param filter The filter to test
	 * @return true if the filtered could not be obtained by the filter menu (other than customized one).
	 */
	private boolean isComplex(Filter filter) {
		double min = filter.getMinAmount();
		double max = filter.getMaxAmount();
		boolean amountSimple = (min==0) && (max==Double.POSITIVE_INFINITY);
		List<Account> validAccounts = filter.getValidAccounts();
		boolean complexAccount = (validAccounts!=null) && (validAccounts.size()!=1);
		return complexAccount ||
				(filter.getValidCategories()!=null) || (filter.getValidModes()!=null) ||
				(filter.getDateFrom()!=null) || (filter.getDateTo()!=null) ||
				(filter.getValueDateFrom()!=null) || (filter.getValueDateTo()!=null) ||
				(filter.getDescriptionMatcher()!=null) || (filter.getCommentMatcher()!=null) ||
				(filter.getNumberMatcher()!=null) || (filter.getStatementMatcher()!=null) || !amountSimple;
	}
	
	private void buildBooleanFilterChoiceMenu(int kind, String[] texts, int[] properties, String[] tooltips, String eraseTooltip) {
		Filter filter = frame.getFilteredData().getFilter();
		ButtonGroup group = new ButtonGroup();
		JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(texts[0]);
		menuItem.setToolTipText(tooltips[0]);
		menuItem.setSelected(filter.isOk(properties[0]) && !filter.isOk(properties[1]));
		filterMenu.add(menuItem);
		group.add(menuItem);
		menuItem.addActionListener(new FilterActionItem(kind, properties[0]));
		menuItem = new JRadioButtonMenuItem(texts[1]);
		menuItem.setToolTipText(tooltips[1]);
		menuItem.setSelected(!filter.isOk(properties[0]) && filter.isOk(properties[1]));
		filterMenu.add(menuItem);
		group.add(menuItem);
		menuItem.addActionListener(new FilterActionItem(kind, properties[1]));
		menuItem = new JRadioButtonMenuItem(LocalizationData.get("MainMenuBar.NoFilter")); //$NON-NLS-1$
		menuItem.setToolTipText(eraseTooltip);
		menuItem.setSelected(filter.isOk(properties[0]) && filter.isOk(properties[1]));
		filterMenu.add(menuItem);
		group.add(menuItem);
		menuItem.addActionListener(new FilterActionItem(kind, properties[0] | properties[1]));
	}

	class FilterActionItem implements ActionListener {
		static final int CHECKED_STATUS = 1;
		static final int NATURE = 2;
		private int property;
		private int kind;
		FilterActionItem (int kind, int property) {
			this.kind = kind;
			this.property = property;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			Filter filter = frame.getFilteredData().getFilter();
			if (this.kind==CHECKED_STATUS) {
				filter.setStatementFilter(property, property==Filter.NOT_CHECKED?null:filter.getStatementMatcher());
			} else {
				filter.setAmountFilter(property, filter.getMinAmount(), filter.getMaxAmount());
			}
		}
	}

	public void updateMenu(AbstractPlugIn plugin) {
		transactionMenu.setVisible((plugin!=null) && plugin.allowMenu(AbstractPlugIn.TRANSACTIONS_MENU));
		filterMenu.setVisible((plugin!=null) && plugin.allowMenu(AbstractPlugIn.FILTER_MENU));
		menuItemPrint.setEnabled((plugin!=null) && plugin.isPrintingSupported());
		getTransactionSelector().setInternalSelector(plugin==null?null:plugin.getTransactionSelector());
	}
	
	private transient CompoundTransactionSelector selector; 
	public CompoundTransactionSelector getTransactionSelector() {
		if (selector==null) {
			selector = new CompoundTransactionSelector(frame.getFilteredData());
		}
		return selector;
	}
}
