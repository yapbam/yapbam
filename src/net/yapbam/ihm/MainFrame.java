package net.yapbam.ihm;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.yapbam.data.*;
import net.yapbam.data.event.*;
import net.yapbam.ihm.administration.AdministrationPlugIn;
import net.yapbam.ihm.graphics.balancehistory.BalanceHistoryPlugIn;
import net.yapbam.ihm.transactiontable.TransactionsPlugIn;

public class MainFrame extends JFrame implements DataListener {
	//TODO implements undo support (see package undo in JustSomeTests project)
	//TODO implements copy/paste support ?
	//TODO CheckBook support
	//TODO periodic transaction support
	
	private static final long serialVersionUID = 1L;
    
    private GlobalData data;
	private AccountFilteredData accountFilter;
	private FilteredData filteredData;//TODO

	private JTabbedPane mainPane;
	private AbstractPlugIn[] plugins;
	private ArrayList<AbstractPlugIn> paneledPlugins;
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
	    
	    this.plugins=new AbstractPlugIn[]{
	    		new TransactionsPlugIn(this.accountFilter, filteredData),
	    		new BalanceHistoryPlugIn(accountFilter),
	    		new AdministrationPlugIn(accountFilter)};
	    this.paneledPlugins=new ArrayList<AbstractPlugIn>();

	    setContentPane(this.createContentPane());
	    MainMenuBar mainMenu = new MainMenuBar(this);
		setJMenuBar(mainMenu);
		mainPane.addChangeListener(new ChangeListener() {
			private int lastSelected = 0;

			@Override
			public void stateChanged(ChangeEvent e) {
				paneledPlugins.get(lastSelected).setDisplayed(false);
				int selectedIndex = mainPane.getSelectedIndex();
				if (selectedIndex<paneledPlugins.size()) paneledPlugins.get(selectedIndex).setDisplayed(true);
			}
		});
		
		newDataOccured();
	    
	    this.data.addListener(this);
	    this.data.addListener(mainMenu);
	    
	    // Restore initial state (last opened file and window position)
	    YapbamState.INSTANCE.restoreMainFramePosition(this);
	    for (int i = 0; i < plugins.length; i++) {
			plugins[i].restoreState(YapbamState.INSTANCE.getProperties());
		}
	
	    //Display the window.
	    setVisible(true);
	}

	private Container createContentPane() {
        mainPane = new JTabbedPane(JTabbedPane.TOP);
		
        for (int i = 0; i < plugins.length; i++) {
            JPanel pane = plugins[i].getPanel();
    		if (pane!=null) {
    			paneledPlugins.add(plugins[i]);
    			mainPane.add(plugins[i].getPanelTitle(), pane);
    		}
		}
        return mainPane;
    }

	public GlobalData getData() {
		return data;
	}
	
	public AccountFilteredData getAccountFilter() {
		return this.accountFilter;
	}
	
	public FilteredData getFilteredData() {
		return this.filteredData; //TODO
	}

	public int getPlugInsNumber() {
		return plugins.length;
	}

	public AbstractPlugIn getPlugIn(int index) {
		return plugins[index];
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
