package net.yapbam.gui;

import java.awt.event.*;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import net.yapbam.data.Account;
import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.data.event.AccountAddedEvent;
import net.yapbam.data.event.AccountPropertyChangedEvent;
import net.yapbam.data.event.AccountRemovedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.NeedToBeSavedChangedEvent;
import net.yapbam.gui.actions.*;
import net.yapbam.gui.dialogs.AboutDialog;
import net.yapbam.gui.dialogs.AccountDialog;
import net.yapbam.gui.dialogs.export.ExportDialog;
import net.yapbam.gui.dialogs.export.Exporter;
import net.yapbam.gui.dialogs.export.ImportDialog;
import net.yapbam.gui.dialogs.export.ImportError;
import net.yapbam.gui.dialogs.export.ImportErrorDialog;
import net.yapbam.gui.dialogs.export.Importer;
import net.yapbam.gui.transactiontable.GeneratePeriodicalTransactionsAction;

public class MainMenuBar extends JMenuBar implements ActionListener, DataListener {
	private static final long serialVersionUID = 1L;

	private MainFrame frame;

	private JMenuItem menuItemNew;
    private JMenuItem menuItemOpen;
	private JMenuItem menuItemImport;
    private JMenuItem menuItemSave;
    private JMenuItem menuItemSaveAs;
	private JMenuItem menuItemExport;
	private JMenuItem menuItemPrint;
    private JMenuItem menuItemQuit;

	private Action editPreferences;
    
	private JMenuItem menuItemAbout;
	
	private JMenu filterMenu;
	private JMenu transactionMenu;



    MainMenuBar (MainFrame frame) {
        super();

        this.frame = frame;
        
        //Build the file menu.
        JMenu menu = new JMenu(LocalizationData.get("MainMenu.File")); //$NON-NLS-1$
        menu.setMnemonic(LocalizationData.getChar("MainMenu.File.Mnemonic")); //$NON-NLS-1$
        menu.setToolTipText(LocalizationData.get("MainMenu.File.ToolTip")); //$NON-NLS-1$
        this.add(menu);

        this.menuItemNew = new JMenuItem(LocalizationData.get("MainMenu.NewFile"), LocalizationData.getChar("MainMenu.NewFile.Mnemonic")); //$NON-NLS-1$ //$NON-NLS-2$
        this.menuItemNew.setIcon(IconManager.NEW_FILE);
        this.menuItemNew.setToolTipText(LocalizationData.get("MainMenu.NewFile.ToolTip")); //$NON-NLS-1$
        this.menuItemNew.addActionListener(this);
        menu.add(this.menuItemNew);
		this.menuItemOpen = new JMenuItem(LocalizationData.get("MainMenu.Open"), LocalizationData.getChar("MainMenu.Open.Mnemonic")); //$NON-NLS-1$ //$NON-NLS-2$
		this.menuItemOpen.setIcon(IconManager.OPEN);
        this.menuItemOpen.setAccelerator(KeyStroke.getKeyStroke(LocalizationData.getChar("MainMenu.Open.Accelerator"), ActionEvent.CTRL_MASK)); //$NON-NLS-1$
        this.menuItemOpen.setToolTipText(LocalizationData.get("MainMenu.Open.ToolTip")); //$NON-NLS-1$
        this.menuItemOpen.addActionListener(this);
        menu.add(this.menuItemOpen);
        this.menuItemSave = new JMenuItem(LocalizationData.get("MainMenu.Save"), IconManager.SAVE); //$NON-NLS-1$
        this.menuItemSave.setAccelerator(KeyStroke.getKeyStroke(LocalizationData.getChar("MainMenu.Save.Accelerator"), ActionEvent.CTRL_MASK)); //$NON-NLS-1$
        this.menuItemSave.setMnemonic(LocalizationData.getChar("MainMenu.Save.Mnemonic")); //$NON-NLS-1$
        this.menuItemSave.setToolTipText(LocalizationData.get("MainMenu.Save.ToolTip")); //$NON-NLS-1$
        this.menuItemSave.addActionListener(this);
        this.menuItemSave.setEnabled(frame.getData().somethingHasChanged());
        menu.add(this.menuItemSave);
        this.menuItemSaveAs = new JMenuItem(LocalizationData.get("MainMenu.SaveAs"), IconManager.SAVE_AS); //$NON-NLS-1$
        this.menuItemSaveAs.setMnemonic(LocalizationData.getChar("MainMenu.SaveAs.Mnemonic")); //$NON-NLS-1$
        this.menuItemSaveAs.setToolTipText(LocalizationData.get("MainMenu.SaveAs.ToolTip")); //$NON-NLS-1$
        this.menuItemSaveAs.addActionListener(this);
        this.menuItemSaveAs.setEnabled(!frame.getData().isEmpty());
        menu.add(this.menuItemSaveAs);
        insertPluginMenuItems(menu, AbstractPlugIn.FILE_MANIPULATION_PART);
        menu.addSeparator();
        
        this.menuItemImport = new JMenuItem(LocalizationData.get("MainMenu.Import"), IconManager.EXPORT); //$NON-NLS-1$
        this.menuItemImport.setMnemonic(LocalizationData.getChar("MainMenu.Import.Mnemonic")); //$NON-NLS-1$
        this.menuItemImport.setToolTipText(LocalizationData.get("MainMenu.Import.ToolTip")); //$NON-NLS-1$
        this.menuItemImport.addActionListener(this);
        menu.add(this.menuItemImport);
        this.menuItemExport = new JMenuItem(LocalizationData.get("MainMenu.Export"), IconManager.EXPORT); //$NON-NLS-1$
        this.menuItemExport.setMnemonic(LocalizationData.getChar("MainMenu.Export.Mnemonic")); //$NON-NLS-1$
        this.menuItemExport.setToolTipText(LocalizationData.get("MainMenu.Export.ToolTip")); //$NON-NLS-1$
        this.menuItemExport.addActionListener(this);
        this.menuItemExport.setEnabled(!frame.getData().isEmpty());
        menu.add(this.menuItemExport);
        insertPluginMenuItems(menu, AbstractPlugIn.IMPORT_EXPORT_PART);

        menu.addSeparator();
        this.menuItemPrint = new JMenuItem(LocalizationData.get("MainMenuBar.Print"), IconManager.PRINT); //$NON-NLS-1$
        this.menuItemPrint.setAccelerator(KeyStroke.getKeyStroke(LocalizationData.getChar("MainMenuBar.Print.Accelerator"), ActionEvent.CTRL_MASK)); //$NON-NLS-1$
        this.menuItemPrint.setToolTipText(LocalizationData.get("MainMenuBar.Print.ToolTip")); //$NON-NLS-1$
        this.menuItemPrint.addActionListener(this);
        this.menuItemPrint.setEnabled(false);
        menu.add(this.menuItemPrint);
        
        menu.addSeparator();
        editPreferences = new EditPreferenceAction(frame);
        menu.add(editPreferences);
        insertPluginMenuItems(menu, AbstractPlugIn.PREFERENCES_PART);

        menu.addSeparator();
        this.menuItemQuit = new JMenuItem(LocalizationData.get("MainMenu.Quit")); //$NON-NLS-1$
        this.menuItemQuit.setAccelerator(KeyStroke.getKeyStroke(LocalizationData.getChar("MainMenu.Quit.Accelerator"), ActionEvent.CTRL_MASK)); //$NON-NLS-1$
        this.menuItemQuit.setMnemonic(LocalizationData.getChar("MainMenu.Quit.Mnemonic")); //$NON-NLS-1$
        this.menuItemQuit.setToolTipText(LocalizationData.get("MainMenu.Quit.ToolTip")); //$NON-NLS-1$
        this.menuItemQuit.addActionListener(this);
        menu.add(this.menuItemQuit);

//TODO Remove
/*
        accountMenu = new JMenu(LocalizationData.get("MainMenu.Accounts")); //$NON-NLS-1$
        accountMenu.setMnemonic(LocalizationData.getChar("MainMenu.Accounts.Mnemonic")); //$NON-NLS-1$
        accountMenu.setToolTipText(LocalizationData.get("MainMenu.Accounts.ToolTip")); //$NON-NLS-1$
        updateAccountMenu();
        this.add(accountMenu);
*/
      
        transactionMenu = new JMenu(LocalizationData.get("MainMenu.Transactions")); //$NON-NLS-1$
        transactionMenu.setMnemonic(LocalizationData.getChar("MainMenu.Transactions.Mnemonic")); //$NON-NLS-1$
        transactionMenu.setToolTipText(LocalizationData.get("MainMenu.Transactions.ToolTip")); //$NON-NLS-1$
		JMenuItem menuItemNewAccount = new JMenuItem(LocalizationData.get("MainMenu.Accounts.New"), IconManager.NEW_ACCOUNT); //$NON-NLS-1$
	    menuItemNewAccount.setMnemonic(LocalizationData.getChar("MainMenu.Accounts.New.Mnemonic")); //$NON-NLS-1$
	    menuItemNewAccount.setToolTipText(LocalizationData.get("MainMenu.Accounts.New.ToolTip")); //$NON-NLS-1$
	    menuItemNewAccount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AccountDialog.open(MainMenuBar.this.frame.getData(), MainMenuBar.this.frame, null);
			}
		});
	    this.transactionMenu.add(menuItemNewAccount);
        insertPluginMenuItems(this.transactionMenu, AbstractPlugIn.ACCOUNTS_PART);
	    this.transactionMenu.addSeparator();
	    JMenuItem item = new JMenuItem(new NewTransactionAction(frame.getData()));
        item.setAccelerator(KeyStroke.getKeyStroke(LocalizationData.getChar("MainMenu.Transactions.New.Accelerator"), ActionEvent.CTRL_MASK)); //$NON-NLS-1$
        transactionMenu.add(item);
        insertPluginMenuItems(transactionMenu, AbstractPlugIn.TRANSACTIONS_PART);
        transactionMenu.addSeparator();
        transactionMenu.add(new JMenuItem(new GeneratePeriodicalTransactionsAction(frame.getData())));
        insertPluginMenuItems(transactionMenu, AbstractPlugIn.PERIODIC_TRANSACTIONS_PART);
        this.add(transactionMenu);
        
        //Build the filter menu
        filterMenu = new JMenu(LocalizationData.get("MainMenuBar.Filter")); //$NON-NLS-1$
        filterMenu.setToolTipText(LocalizationData.get("MainMenuBar.Filter.Tooltip")); //$NON-NLS-1$
        filterMenu.setMnemonic(LocalizationData.getChar("MainMenuBar.Filter.Mnemonic")); //$NON-NLS-1$
        updateFilterMenu();
        this.add(filterMenu);

        //Build plugins menus
        for (int i = 0; i < this.frame.getPlugInsNumber(); i++) {
            JMenu[] menus = this.frame.getPlugIn(i).getPlugInMenu(); //TODO What if a plugin just wants to add a menuItem to an existing Menu ?
    		if (menus!=null) {
    			for (int j = 0; j < menus.length; j++) {
					this.add(menus[j]);
				}
    		}
		}
        
        //Build Help menu.
        menu = new JMenu(LocalizationData.get("MainMenu.QuestionMark")); //$NON-NLS-1$
        menu.setMnemonic(LocalizationData.getChar("MainMenu.QuestionMark.Mnemonic")); //$NON-NLS-1$
        menu.setToolTipText(LocalizationData.get("MainMenu.QuestionMark.ToolTip")); //$NON-NLS-1$
        this.add(menu);

        menu.add(new CheckNewReleaseAction(this.frame));
        insertPluginMenuItems(menu, AbstractPlugIn.UPDATES_PART);
        menu.addSeparator();
		item = getURLMenuItem(LocalizationData.get("MainMenu.web.support"), "http://sourceforge.net/projects/yapbam/support");
		item.setToolTipText(LocalizationData.get("MainMenu.web.support.tooltip"));
		menu.add(item);
		item = getURLMenuItem(LocalizationData.get("MainMenu.web.yapbam"), "http://www.yapbam.net");
		item.setToolTipText(LocalizationData.get("MainMenu.web.yapbam.tooltip"));
		menu.add(item);
        insertPluginMenuItems(menu, AbstractPlugIn.WEB_SITES_PART);
        menu.addSeparator();
        this.menuItemAbout = new JMenuItem(LocalizationData.get("MainMenu.About")); //$NON-NLS-1$
        this.menuItemAbout.setMnemonic(LocalizationData.getChar("MainMenu.About.Mnemonic")); //$NON-NLS-1$
        this.menuItemAbout.setToolTipText(LocalizationData.get("MainMenu.About.ToolTip")); //$NON-NLS-1$
        this.menuItemAbout.addActionListener(this);
        menu.add(this.menuItemAbout);
        insertPluginMenuItems(menu, AbstractPlugIn.ABOUT_PART);
    }

	private JMenuItem getURLMenuItem(String title, final String url) {
		JMenuItem item;
		item = new JMenuItem(new AbstractAction(title) {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					HelpManager.show(MainMenuBar.this.frame, new URI(url));
				} catch (URISyntaxException e1) {
					e1.printStackTrace(); //TODO
				}
			}
		});
		return item;
	}

	private void insertPluginMenuItems(JMenu menu, int part) {
		for (int i = 0; i < this.frame.getPlugInsNumber(); i++) {
            JMenuItem[] items = this.frame.getPlugIn(i).getMenuItem(part);
    		if (items!=null) {
    			for (int j = 0; j < items.length; j++) {
    				if (items[i]==null) {
    					menu.addSeparator();
    				} else {
    					menu.add(items[j]);
    				}
				}
    		}
		}
	}

	private void refreshState(GlobalData data) {
    	boolean somethingToSave = !data.isEmpty();
        this.menuItemSave.setEnabled(data.somethingHasChanged());
        this.menuItemSaveAs.setEnabled(somethingToSave);
    }

    public void actionPerformed(ActionEvent e) {
//		output.setText("Menu selected"+e.getSource().toString());
		Object source = e.getSource();
		if (source.equals(this.menuItemQuit)) {
			this.frame.dispatchEvent(new WindowEvent(this.frame, WindowEvent.WINDOW_CLOSING));
		} else {
			GlobalData data = this.frame.getData();
			if (source.equals(this.menuItemNew)) {
				if (SaveManager.MANAGER.verify(this.frame)) {
					data.clear();
				}
			} else if (source.equals(this.menuItemOpen)) {
				if (SaveManager.MANAGER.verify(this.frame)) {
					URI path = data.getPath();
					String parent = path==null?null:new File(path).getParent();
					JFileChooser chooser = new JFileChooser(parent);
					chooser.setLocale(new Locale(LocalizationData.getLocale().getLanguage()));
					chooser.updateUI();
					File file = chooser.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION?chooser.getSelectedFile():null;
					if (file!=null) {
						try {
							data.read(file.toURI());
						} catch (Exception exception) {
							ErrorManager.INSTANCE.display(frame, exception, MessageFormat.format(LocalizationData.get("MainMenu.Open.Error.DialogContent"),file)); //$NON-NLS-1$
						}
					}
				}
			} else if (source.equals(this.menuItemSave)) {
				SaveManager.MANAGER.save(this.frame);
			} else if (source.equals(this.menuItemSaveAs)) {
				SaveManager.MANAGER.saveAs(this.frame);
			} else if (source.equals(this.menuItemImport)) {
				JFileChooser chooser = new JFileChooser();
				chooser.setLocale(LocalizationData.getLocale());
				chooser.updateUI();
				File file = chooser.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION?chooser.getSelectedFile():null;
				if (file!=null) {
					ImportDialog dialog = new ImportDialog(this.frame, data, file);
					dialog.setVisible(true);
					Importer importer = dialog.getImporter();
					if (importer!=null) {
						if (SaveManager.MANAGER.verify(frame)) {
							if (!dialog.getAddToCurrentData()) {
								data.clear();
							}
							try {
								ImportError[] errors = importer.importFile(data);
								if (errors.length!=0) {
									new ImportErrorDialog(frame, importer.getImportedColumns(), errors).setVisible(true);
								}
							} catch (IOException e1) {
								//TODO Be more precise ?
								ErrorManager.INSTANCE.display(frame, e1);
							}
						}
					}
				}
			} else if (source.equals(this.menuItemExport)) {
				ExportDialog exportDialog = new ExportDialog(this.frame, this.frame.getFilteredData());
				exportDialog.setVisible(true);
				Exporter exporter = exportDialog.getExporter();
				if (exporter!=null) {
					JFileChooser chooser = new JFileChooser();
					chooser.setLocale(LocalizationData.getLocale());
					chooser.updateUI();
					File file = chooser.showSaveDialog(frame)==JFileChooser.APPROVE_OPTION?chooser.getSelectedFile():null;
					if (file!=null) {
						try {
							exporter.exportFile(file, frame.getFilteredData());
							JOptionPane.showMessageDialog(frame, LocalizationData.get("ExportDialog.done"), LocalizationData.get("ExportDialog.title"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
						} catch (IOException e1) {
							ErrorManager.INSTANCE.display(frame, e1);
						}
					}					
				}
			} else if (source.equals(this.menuItemPrint)) {
				try {
					this.frame.getCurrentPlugIn().print();
				} catch (PrinterException e1) {
					String okButton = LocalizationData.get("GenericButton.ok"); //$NON-NLS-1$
					String message = MessageFormat.format(LocalizationData.get("MainMenuBar.Print.Error.Message"),e1.getMessage()); //$NON-NLS-1$
					JOptionPane.showOptionDialog(frame,
						    message, LocalizationData.get("MainMenuBar.Print.Error.Title"), //$NON-NLS-1$
						    JOptionPane.YES_OPTION, JOptionPane.ERROR_MESSAGE, null,
						    new String[]{okButton}, okButton);
				}
			} else if (source.equals(this.menuItemAbout)) {
				new AboutDialog(MainMenuBar.this.frame).setVisible(true);
			}
		}
	}

	public void processEvent(DataEvent event) {
		GlobalData data = (GlobalData) event.getSource();
		this.menuItemExport.setEnabled(!data.isEmpty());
		if ((event instanceof NeedToBeSavedChangedEvent) || (event instanceof EverythingChangedEvent)) {
			this.refreshState(data);
			if (event instanceof EverythingChangedEvent) {
				this.updateFilterMenu();
			}
		} else if ((event instanceof AccountAddedEvent) || (event instanceof AccountRemovedEvent) ||
				((event instanceof AccountPropertyChangedEvent) && (((AccountPropertyChangedEvent)event).getProperty().equals(AccountPropertyChangedEvent.NAME)))) {
			this.updateFilterMenu();
		}
	}
		
	public void updateFilterMenu() {
		filterMenu.removeAll();
		if (frame.getFilteredData()!=null) {
			JCheckBoxMenuItem complexFilterMenuItem = new JCheckBoxMenuItem(new CustomFilterAction(frame.getFilteredData(), this));
			complexFilterMenuItem.setToolTipText(LocalizationData.get("MainMenuBar.customizedFilter.toolTip")); //$NON-NLS-1$
			filterMenu.add(complexFilterMenuItem);
			complexFilterMenuItem.setSelected(isComplex(frame.getFilteredData()));
			JMenuItem eraseItem = new JMenuItem(LocalizationData.get("MainMenuBar.eraseFilters")); //$NON-NLS-1$
			eraseItem.setToolTipText(LocalizationData.get("MainMenuBar.eraseFilters.toolTip")); //$NON-NLS-1$
			eraseItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					frame.getFilteredData().clear();
					updateFilterMenu();
				}
			});
			filterMenu.add(eraseItem);
        	filterMenu.addSeparator();
	    	GlobalData data = this.frame.getData();
			ActionListener listener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JMenuItem item = (JMenuItem) e.getSource();
					Account account = frame.getData().getAccount(item.getText());
					frame.getFilteredData().setAccounts(new Account[]{account});
					updateFilterMenu();
				}
			};
	        FilteredData filter = frame.getFilteredData();
			boolean hasAccountFilter = filter.hasFilterAccount();
	        for (int i=0;i<data.getAccountsNumber();i++) {
	        	Account account = data.getAccount(i);
	        	JRadioButtonMenuItem item = new JRadioButtonMenuItem(account.getName());
	        	item.setToolTipText(MessageFormat.format(LocalizationData.get("MainMenuBar.AccountFilter.toolTip"), account.getName())); //$NON-NLS-1$
	        	if (hasAccountFilter) item.setSelected(filter.isOk(account));
	        	filterMenu.add(item);
	        	item.addActionListener(listener);
	        }
        	JRadioButtonMenuItem item = new JRadioButtonMenuItem(LocalizationData.get("MainMenuBar.NoFilter")); //$NON-NLS-1$
        	item.setToolTipText(LocalizationData.get("MainMenuBar.NoAccountFilter.toolTip")); //$NON-NLS-1$
        	item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					frame.getFilteredData().setAccounts(null);
					updateFilterMenu();
				}
			});
        	item.setSelected(!hasAccountFilter);
        	filterMenu.add(item);
        	filterMenu.addSeparator();
        	buildBooleanFilterChoiceMenu(new String[]{LocalizationData.get("MainMenuBar.checked"), //$NON-NLS-1$
        		LocalizationData.get("MainMenuBar.notChecked")}, new int[]{FilteredData.CHECKED, FilteredData.NOT_CHECKED}, //$NON-NLS-1$
        		new String[]{LocalizationData.get("MainMenuBar.checked.toolTip"),LocalizationData.get("MainMenuBar.notChecked.toolTip")}, //$NON-NLS-1$ //$NON-NLS-2$
        		LocalizationData.get("MainMenuBar.NoCheckedFilter.toolTip")); //$NON-NLS-1$
        	filterMenu.addSeparator();
        	buildExpenseReceiptFilterChoiceMenu();			
		}
	}
	
	/** Gets a filter complexity.
	 * @param filter The filter to test
	 * @return true if the filtered could not be obtained by the filter menu (other than customized one).
	 */
	private boolean isComplex(FilteredData filter) {
		double min = filter.getMinimumAmount();
		double max = filter.getMaximumAmount();
		boolean amountSimple = ((min==Double.NEGATIVE_INFINITY) || (min==0)) &&
		((max==Double.POSITIVE_INFINITY) || (max==0)) && (min!=max);
		return (isComplex(filter.getAccounts(), new int[]{1,filter.getGlobalData().getAccountsNumber()}) ||
				(filter.getCategories()!=null) || (filter.getModes()!=null) ||
				(filter.getDateFrom()!=null) || (filter.getDateTo()!=null) ||
				(filter.getValueDateFrom()!=null) || (filter.getValueDateTo()!=null) ||
				(filter.getDescriptionFilter()!=null) || (filter.getNumberFilter()!=null) ||
				(filter.getStatementFilter()!=null) || !amountSimple);
	}
	
	/**
	 * @param array
	 * @return
	 *  @see #isComplex(FilteredData)
	 */
	private boolean isComplex(Object[] array, int[] simpleLengths) {
		if (array==null) return false;
		for (int i = 0; i < simpleLengths.length; i++) {
			if (array.length==simpleLengths[i]) return false;
		}
		return true;
	}

	private void buildBooleanFilterChoiceMenu(String[] texts, int[] properties, String[] tooltips, String eraseTooltip) {
        FilteredData filter = frame.getFilteredData();
        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(texts[0]);
        menuItem.setToolTipText(tooltips[0]);
        menuItem.setSelected(filter.isOk(properties[0]) && !filter.isOk(properties[1]));
		filterMenu.add(menuItem);
		group.add(menuItem);
		menuItem.addActionListener(new FilterActionItem(properties[0]));
		menuItem = new JRadioButtonMenuItem(texts[1]);
        menuItem.setToolTipText(tooltips[1]);
        menuItem.setSelected(!filter.isOk(properties[0]) && filter.isOk(properties[1]));
        filterMenu.add(menuItem);
		group.add(menuItem);
		menuItem.addActionListener(new FilterActionItem(properties[1]));
		menuItem = new JRadioButtonMenuItem(LocalizationData.get("MainMenuBar.NoFilter")); //$NON-NLS-1$
        menuItem.setToolTipText(eraseTooltip);
        menuItem.setSelected(filter.isOk(properties[0]) && filter.isOk(properties[1]));
        filterMenu.add(menuItem);
		group.add(menuItem);
		menuItem.addActionListener(new FilterActionItem(properties[0] | properties[1]));
	}

	class FilterActionItem implements ActionListener {
		private int property;
		FilterActionItem (int property) {
			this.property = property;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.getFilteredData().setStatementFilter(property,null);
		}
	}

	private void buildExpenseReceiptFilterChoiceMenu() {
		FilteredData filter = frame.getFilteredData();
        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(LocalizationData.get("MainMenuBar.Expenses")); //$NON-NLS-1$
        menuItem.setToolTipText(LocalizationData.get("MainMenuBar.Expenses.toolTip")); //$NON-NLS-1$
        menuItem.setSelected(filter.getMaximumAmount()<=0);
		filterMenu.add(menuItem);
		group.add(menuItem);
		menuItem.addActionListener(new AmountActionItem(Double.NEGATIVE_INFINITY, 0));
		
		menuItem = new JRadioButtonMenuItem(LocalizationData.get("MainMenuBar.Receipts")); //$NON-NLS-1$
        menuItem.setToolTipText(LocalizationData.get("MainMenuBar.Receipts.toolTip")); //$NON-NLS-1$
        menuItem.setSelected(filter.getMinimumAmount()>=0);
        filterMenu.add(menuItem);
		group.add(menuItem);
		menuItem.addActionListener(new AmountActionItem(0, Double.POSITIVE_INFINITY));
		
		menuItem = new JRadioButtonMenuItem(LocalizationData.get("MainMenuBar.NoFilter")); //$NON-NLS-1$
		menuItem.setToolTipText(LocalizationData.get("MainMenuBar.NoAmountFilter.toolTip")); //$NON-NLS-1$
        menuItem.setSelected((filter.getMaximumAmount()>0) && (filter.getMinimumAmount()<0));
        filterMenu.add(menuItem);
		group.add(menuItem);
		menuItem.addActionListener(new AmountActionItem(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
	}

	final class AmountActionItem implements ActionListener {
		private double min;
		private double max;
		AmountActionItem (double min, double max) {
			this.min = min;
			this.max = max;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.getFilteredData().setAmountFilter(min, max);
		}
	}

	public void updateMenu(AbstractPlugIn plugIn) {
		transactionMenu.setVisible(plugIn.allowMenu(AbstractPlugIn.TRANSACTIONS_MENU));
		filterMenu.setVisible(plugIn.allowMenu(AbstractPlugIn.FILTER_MENU));
		menuItemPrint.setEnabled(plugIn.isPrintingSupported());
	}
}
