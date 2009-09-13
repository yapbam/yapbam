package net.yapbam.ihm;

import java.awt.event.*;
import java.io.File;
import java.text.MessageFormat;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.yapbam.data.Account;
import net.yapbam.data.AccountFilteredData;
import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.data.event.AccountAddedEvent;
import net.yapbam.data.event.AccountRemovedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.NeedToBeSavedChangedEvent;
import net.yapbam.data.event.PeriodicalTransactionAddedEvent;
import net.yapbam.data.event.PeriodicalTransactionRemovedEvent;
import net.yapbam.ihm.actions.*;
import net.yapbam.ihm.dialogs.AboutDialog;
import net.yapbam.ihm.dialogs.AccountDialog;

public class MainMenuBar extends JMenuBar implements ActionListener, DataListener, ChangeListener {
	private static final long serialVersionUID = 1L;

	private MainFrame frame;

	private JMenuItem menuItemNew;
    private JMenuItem menuItemOpen;
    private JMenuItem menuItemSave;
    private JMenuItem menuItemSaveAs;
    private JMenuItem menuItemQuit;

	private JMenu transactionMenu;
	Action newTransactionAction;
	Action editTransactionAction;
	Action duplicateTransactionAction;
	Action deleteTransactionAction;
	private Action generatePeriodical;
	private Action editPreferences;
    
	private JMenuItem menuItemAbout;
	
	private JMenu filterMenu;
	private JMenu accountMenu;


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
        this.menuItemOpen.setAccelerator(KeyStroke.getKeyStroke(LocalizationData.getChar("MainMenu.Open.Accelerator"), ActionEvent.CTRL_MASK)); //$NON-NLS-1$
        this.menuItemOpen.setToolTipText(LocalizationData.get("MainMenu.Open.ToolTip")); //$NON-NLS-1$
        this.menuItemOpen.addActionListener(this);
        menu.add(this.menuItemOpen);

        menu.addSeparator();

        this.menuItemSave = new JMenuItem(LocalizationData.get("MainMenu.Save"), IconManager.SAVE); //$NON-NLS-1$
        this.menuItemSave.setAccelerator(KeyStroke.getKeyStroke(LocalizationData.getChar("MainMenu.Save.Accelerator"), ActionEvent.CTRL_MASK)); //$NON-NLS-1$
        this.menuItemSave.setMnemonic(LocalizationData.getChar("MainMenu.Save.Mnemonic")); //$NON-NLS-1$
        this.menuItemSave.setToolTipText(LocalizationData.get("MainMenu.Save.ToolTip")); //$NON-NLS-1$
        this.menuItemSave.addActionListener(this);
        this.menuItemSave.setEnabled(false);
        menu.add(this.menuItemSave);
        this.menuItemSaveAs = new JMenuItem(LocalizationData.get("MainMenu.SaveAs"), IconManager.SAVE_AS); //$NON-NLS-1$
        this.menuItemSaveAs.setMnemonic(LocalizationData.getChar("MainMenu.SaveAs.Mnemonic")); //$NON-NLS-1$
        this.menuItemSaveAs.setToolTipText(LocalizationData.get("MainMenu.SaveAs.ToolTip")); //$NON-NLS-1$
        this.menuItemSaveAs.addActionListener(this);
        this.menuItemSaveAs.setEnabled(false);
        menu.add(this.menuItemSaveAs);

        menu.addSeparator();
        editPreferences = new EditPreferenceAction(frame);
        menu.add(editPreferences);

        menu.addSeparator();
        this.menuItemQuit = new JMenuItem(LocalizationData.get("MainMenu.Quit")); //$NON-NLS-1$
        this.menuItemQuit.setAccelerator(KeyStroke.getKeyStroke(LocalizationData.getChar("MainMenu.Quit.Accelerator"), ActionEvent.CTRL_MASK)); //$NON-NLS-1$
        this.menuItemQuit.setMnemonic(LocalizationData.getChar("MainMenu.Quit.Mnemonic")); //$NON-NLS-1$
        this.menuItemQuit.setToolTipText(LocalizationData.get("MainMenu.Quit.ToolTip")); //$NON-NLS-1$
        this.menuItemQuit.addActionListener(this);
        menu.add(this.menuItemQuit);

        accountMenu = new JMenu(LocalizationData.get("MainMenu.Accounts")); //$NON-NLS-1$
        accountMenu.setMnemonic(LocalizationData.getChar("MainMenu.Accounts.Mnemonic")); //$NON-NLS-1$
        accountMenu.setToolTipText(LocalizationData.get("MainMenu.Accounts.ToolTip")); //$NON-NLS-1$
        updateAccountMenu();
        this.add(accountMenu);

        //Build transactions menu item
        transactionMenu = new JMenu(LocalizationData.get("MainMenu.Transactions")); //$NON-NLS-1$
        transactionMenu.setMnemonic(LocalizationData.getChar("MainMenu.Transactions.Mnemonic")); //$NON-NLS-1$
        transactionMenu.setToolTipText(LocalizationData.get("MainMenu.Transactions.ToolTip")); //$NON-NLS-1$
        
        this.newTransactionAction = new NewTransactionAction(frame);
        JMenuItem item = new JMenuItem(newTransactionAction);
        item.setAccelerator(KeyStroke.getKeyStroke(LocalizationData.getChar("MainMenu.Transactions.New.Accelerator"), ActionEvent.CTRL_MASK)); //$NON-NLS-1$
        transactionMenu.add(item);
        this.duplicateTransactionAction = new DuplicateTransactionAction(frame);
        item = new JMenuItem(this.duplicateTransactionAction);
        transactionMenu.add(item);
        this.editTransactionAction = new EditTransactionAction(frame);
        item = new JMenuItem(this.editTransactionAction);
        transactionMenu.add(item);
        this.deleteTransactionAction = new DeleteTransactionAction(frame);
        item = new JMenuItem(deleteTransactionAction); //$NON-NLS-1$
        item.setAccelerator(KeyStroke.getKeyStroke(LocalizationData.getChar("MainMenu.Transactions.Delete.Accelerator"), ActionEvent.CTRL_MASK)); //$NON-NLS-1$
        transactionMenu.add(item);
        transactionMenu.addSeparator();
        generatePeriodical = new GeneratePeriodicalTransactionsAction(frame);
        transactionMenu.add(new JMenuItem(generatePeriodical));
        this.add(transactionMenu);
        
        //Build the filter menu
        filterMenu = new JMenu(LocalizationData.get("MainMenuBar.Filter")); //$NON-NLS-1$
        filterMenu.setToolTipText(LocalizationData.get("MainMenuBar.Filter.Tooltip")); //$NON-NLS-1$
        filterMenu.setMnemonic(LocalizationData.getChar("MainMenuBar.Filter.Mnemonic")); //$NON-NLS-1$
        updateFilterMenu();
        this.add(filterMenu);

        //Build Help menu.
        menu = new JMenu(LocalizationData.get("MainMenu.QuestionMark")); //$NON-NLS-1$
        menu.setMnemonic(LocalizationData.getChar("MainMenu.QuestionMark.Mnemonic")); //$NON-NLS-1$
        menu.setToolTipText(LocalizationData.get("MainMenu.QuestionMark.ToolTip")); //$NON-NLS-1$
        this.add(menu);

        this.menuItemAbout = new JMenuItem(LocalizationData.get("MainMenu.About")); //$NON-NLS-1$
        this.menuItemAbout.setMnemonic(LocalizationData.getChar("MainMenu.About.Mnemonic")); //$NON-NLS-1$
        this.menuItemAbout.setToolTipText(LocalizationData.get("MainMenu.About.ToolTip")); //$NON-NLS-1$
        this.menuItemAbout.addActionListener(this);
        menu.add(this.menuItemAbout);
        menu.addSeparator();
        menu.add(new CheckNewReleaseAction(this.frame));
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
		} else if (source.equals(this.menuItemNew)) {
			if (SaveManager.MANAGER.verify(this.frame)) {
				this.frame.getData().clear();
			}
		} else if (source.equals(this.menuItemOpen)) {
			if (SaveManager.MANAGER.verify(this.frame)) {
				File path = frame.getData().getPath();
				String parent = path==null?null:path.getParent();
				JFileChooser chooser = new JFileChooser(parent);
				File file = chooser.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION?chooser.getSelectedFile():null;
				if (file!=null) {
					try {
						frame.getData().read(file);
					} catch (Exception exception) {
						ErrorManager.INSTANCE.display(frame, exception, MessageFormat.format(LocalizationData.get("MainMenu.Open.Error.DialogContent"),file)); //$NON-NLS-1$
					}
				}
			}
		} else if (source.equals(this.menuItemSave)) {
			SaveManager.MANAGER.save(this.frame);
		} else if (source.equals(this.menuItemSaveAs)) {
			SaveManager.MANAGER.saveAs(this.frame);
		} else if (source.equals(this.menuItemAbout)) {
			new AboutDialog(MainMenuBar.this.frame).setVisible(true);
		}
	}

	public void processEvent(DataEvent event) {
		GlobalData data = (GlobalData) event.getSource();
		if ((event instanceof NeedToBeSavedChangedEvent) || (event instanceof EverythingChangedEvent)) {
			this.refreshState(data);
			if (event instanceof EverythingChangedEvent) {
				this.updateFilterMenu();
				this.updateAccountMenu();
			}
		} else if ((event instanceof AccountAddedEvent) || (event instanceof AccountRemovedEvent)) {
			this.updateFilterMenu();
			this.updateAccountMenu();
		} else if ((event instanceof PeriodicalTransactionAddedEvent) || (event instanceof PeriodicalTransactionRemovedEvent)) {
			this.generatePeriodical.setEnabled(data.getPeriodicalTransactionsNumber()>0);
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// called when the main frame tabbed pane switch from one pane to another
		this.transactionMenu.setVisible(this.frame.isTransactionTableVisible());
		this.filterMenu.setVisible(this.frame.isTransactionTableVisible());
	}
	
	private void updateAccountMenu() {
		this.accountMenu.removeAll();
		JMenuItem menuItemNewAccount = new JMenuItem(LocalizationData.get("MainMenu.Accounts.New"), IconManager.NEW_ACCOUNT); //$NON-NLS-1$
	    menuItemNewAccount.setMnemonic(LocalizationData.getChar("MainMenu.Accounts.New.Mnemonic")); //$NON-NLS-1$
	    menuItemNewAccount.setToolTipText(LocalizationData.get("MainMenu.Accounts.New.ToolTip")); //$NON-NLS-1$
	    menuItemNewAccount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AccountDialog.open(frame.getData(), frame, null);
			}
		});
	    this.accountMenu.add(menuItemNewAccount);
	    this.accountMenu.addSeparator();
	    GlobalData data = this.frame.getData();
	    if (data!=null) {
			ActionListener listener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JMenuItem item = (JMenuItem) e.getSource();
					Account account = frame.getData().getAccount(item.getText());
					frame.getFilteredData().setAccounts(new Account[]{account});
					frame.getAccountFilter().setAccounts(new Account[]{account});
				}
			};
	        AccountFilteredData filter = frame.getAccountFilter();
			ButtonGroup group = new ButtonGroup();
			boolean hasAccountFilter = filter.hasFilterAccount();
	        for (int i=0;i<data.getAccountsNumber();i++) {
	        	Account account = data.getAccount(i);
	        	JRadioButtonMenuItem item = new JRadioButtonMenuItem(account.getName());
	        	if (hasAccountFilter) item.setSelected(filter.isOk(account));
	        	accountMenu.add(item);
	        	group.add(item);
	        	item.addActionListener(listener);
	        }
        	JRadioButtonMenuItem item = new JRadioButtonMenuItem(LocalizationData.get("MainMenuBar.NoFilter")); //$NON-NLS-1$
        	item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					frame.getFilteredData().clearAccounts();
					frame.getAccountFilter().clear();
				}
			});
        	item.setSelected(!hasAccountFilter);
        	accountMenu.add(item);
        	group.add(item);
	    }
	}

	private void updateFilterMenu() {
		filterMenu.removeAll();
		GlobalData data = this.frame.getData();
		if (data!=null) {
        	buildBooleanFilterChoiceMenu(new String[]{LocalizationData.get("MainMenuBar.checked"), //$NON-NLS-1$
        			LocalizationData.get("MainMenuBar.notChecked")}, new int[]{FilteredData.CHECKED, FilteredData.NOT_CHECKED}); //$NON-NLS-1$
        	filterMenu.addSeparator();
        	buildBooleanFilterChoiceMenu(new String[]{LocalizationData.get("MainMenuBar.Expenses"), LocalizationData.get("MainMenuBar.Receipts")}, //$NON-NLS-1$ //$NON-NLS-2$
        			new int[]{FilteredData.EXPENSE, FilteredData.RECEIPT});
			
        	//TODO filterMenu.addSeparator();
	        //JMenuItem perso = new JCheckBoxMenuItem(LocalizationData.get("MainMenuBar.customizedFilter")); //$NON-NLS-1$
			//filterMenu.add(perso);
		}
	}
	
	private void buildBooleanFilterChoiceMenu(String[] texts, int[] properties) {
        FilteredData filter = frame.getFilteredData();
        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(texts[0]);
        menuItem.setSelected(filter.isOk(properties[0]) && !filter.isOk(properties[0]));
		filterMenu.add(menuItem);
		group.add(menuItem);
		menuItem.addActionListener(new FilterActionItem(properties[0]));
		menuItem = new JRadioButtonMenuItem(texts[1]);
        menuItem.setSelected(!filter.isOk(properties[0]) && filter.isOk(properties[1]));
        filterMenu.add(menuItem);
		group.add(menuItem);
		menuItem.addActionListener(new FilterActionItem(properties[1]));
		menuItem = new JRadioButtonMenuItem(LocalizationData.get("MainMenuBar.NoFilter")); //$NON-NLS-1$
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
			frame.getFilteredData().setFilter(property);
		}
		
	}
}
