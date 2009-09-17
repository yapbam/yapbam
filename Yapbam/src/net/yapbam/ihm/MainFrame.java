package net.yapbam.ihm;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

import javax.swing.*;

import net.yapbam.data.*;
import net.yapbam.data.event.*;
import net.yapbam.ihm.administration.AdministrationPanel;
import net.yapbam.ihm.graphics.balancehistory.BalanceHistoryPane;
import net.yapbam.ihm.transactiontable.TransactionsPlugIn;

public class MainFrame extends JFrame implements DataListener {
	//TODO implements undo support (see package undo in JustSomeTests project)
	//TODO implements copy/paste support ?
	//TODO CheckBook support
	//TODO periodic transaction support
	
	private static final long serialVersionUID = 1L;
    
    private GlobalData data;
	private AccountFilteredData accountFilter;
	private FilteredData filteredData;

	private MainMenuBar mainMenu;
	private JTabbedPane mainPane;
	private TransactionsPlugIn transactionPane;
//	private BalanceReportField currentBalance;
//	private BalanceReportField finalBalance;
//	private BalanceReportField checkedBalance;
	private BalanceHistoryPane balanceHistoryPane;
	private boolean isRestarting = false;
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(Preferences.INSTANCE.getLookAndFeel());
		} catch (Exception e) {}
	    //Schedule a job for the event-dispatching thread:
	    //creating and showing this application's GUI.
	    javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	            new MainFrame(null, null, null);
	        }
	    });
	}

	/** Create the GUI and show it.  For thread safety, this method should be invoked from the
	 * event-dispatching thread.
	 */
	private MainFrame(GlobalData data, AccountFilteredData acFilter, FilteredData fData) {
	    //Create and set up the window.
		super();
		this.setMinimumSize(new Dimension(800,300));
		
	    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    this.addWindowListener(new WindowAdapter() {
	    	@Override
	    	public void windowClosing(WindowEvent event) {
	    		MainFrame frame = (MainFrame) event.getWindow();
	    		if (frame.isRestarting) {
	    			YapbamState.save(frame);
	    			super.windowClosing(event);
	    			frame.dispose();
	    		} else if (SaveManager.MANAGER.verify(frame)) {
	    			YapbamState.save(frame);
	    			Preferences.INSTANCE.save();
	    			super.windowClosing(event);
	    			frame.dispose();
	    		}
	    	}
		});
	
	    this.data = (data==null)?new GlobalData():data;
	    this.accountFilter = acFilter==null?new AccountFilteredData(this.data):acFilter;
	    this.filteredData = fData==null?new FilteredData(this.data):fData;
	    if (data==null) YapbamState.INSTANCE.restoreGlobalData(this);

	    setContentPane(this.createContentPane());
	    mainMenu = new MainMenuBar(this);
		setJMenuBar(mainMenu);
		mainPane.addChangeListener(mainMenu);
		
		newDataOccured();
	    
	    this.data.addListener(this);
	    this.data.addListener(mainMenu);
	    
	    // Restore initial state (last opened file and window position)
	    YapbamState.INSTANCE.restoreMainFramePosition(this);
		getTransactionPlugIn().restoreState(YapbamState.INSTANCE.getProperties());
	
	    //Display the window.
	    setVisible(true);
	}

	private Container createContentPane() {
        mainPane = new JTabbedPane(JTabbedPane.TOP);
		
        transactionPane = new TransactionsPlugIn(accountFilter, filteredData);

		mainPane.add(LocalizationData.get("MainFrame.Transactions"), transactionPane); //$NON-NLS-1$
		
		balanceHistoryPane = new BalanceHistoryPane(accountFilter.getBalanceHistory());
		accountFilter.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				balanceHistoryPane.setBalanceHistory(accountFilter.getBalanceHistory());
				transactionPane.updateBalances();
			}
		});
		mainPane.add(LocalizationData.get("MainFrame.BalanceHistory"), balanceHistoryPane); //$NON-NLS-1$
		mainPane.add("Administration",new AdministrationPanel(getData()));//LOCAL
        return mainPane;
    }

	public GlobalData getData() {
		return data;
	}
	
	public AccountFilteredData getAccountFilter() {
		return this.accountFilter;
	}
	
	public FilteredData getFilteredData() {
		return this.filteredData;
	}

	public TransactionsPlugIn getTransactionPlugIn() {
		return transactionPane;
	}

	public void processEvent(DataEvent event) {
		if ((event instanceof FileChangedEvent) || (event instanceof EverythingChangedEvent)) {
			newDataOccured();
		}
	}

	private void newDataOccured() {
		String title = LocalizationData.get("ApplicationName"); //$NON-NLS-1$
		File file = data.getPath();
		if (file!=null) title = title + " - " + file; //$NON-NLS-1$
		this.setTitle(title);
		this.transactionPane.updateBalances();
	}
	
	boolean isTransactionTableVisible() {
		return this.mainPane.getSelectedIndex()==0;
	}

	public void restart() {
//		this.data.clearListeners();
//		this.accountFilter.clearListeners();
//		this.filteredData.clearListeners();
		//FIXME We would need to remove the obsoletes listeners (from the closing window) ... but don't know how to do that efficiently
		this.isRestarting = true;
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		try {
			UIManager.setLookAndFeel(Preferences.INSTANCE.getLookAndFeel());
		} catch (Exception e) {}
	    //Schedule a job for the event-dispatching thread:
	    //creating and showing this application's GUI.
	    javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	            new MainFrame(data, accountFilter, filteredData);
	        }
	    });
	}
}
