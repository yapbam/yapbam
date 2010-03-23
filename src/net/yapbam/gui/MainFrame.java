package net.yapbam.gui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.yapbam.data.*;
import net.yapbam.data.event.*;
import net.yapbam.gui.actions.CheckNewReleaseAction;

public class MainFrame extends JFrame implements DataListener {
	//TODO implements undo support (see package undo in JustSomeTests project)
	//TODO implements copy/paste support ?
	//TODO CheckBook support
	
	private static final long serialVersionUID = 1L;
    
    private GlobalData data;
	private FilteredData filteredData;

	private MainMenuBar mainMenu;
	private JTabbedPane mainPane;
	private AbstractPlugIn[] plugins;
	private ArrayList<AbstractPlugIn> paneledPlugins;
	private int lastSelected = -1;
	private boolean isRestarting = false;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(Preferences.INSTANCE.getLookAndFeel());
		} catch (Exception e) {}
	    //Schedule a job for the event-dispatching thread:
	    //creating and showing this application's GUI.
	    javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	            MainFrame frame = new MainFrame(null, null);
	        	CheckNewReleaseAction.doAutoCheck(frame);
	        }
	    });
	}
	
	/** Create the GUI and show it.  For thread safety, this method should be invoked from the
	 * event-dispatching thread.
	 */
	private MainFrame(FilteredData filteredData, Object[] restartData) {
	    //Create and set up the window.
		super();
		this.setMinimumSize(new Dimension(800,400));
		
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
	
	    if (filteredData==null) {
	    	this.data = new GlobalData();
	    	this.filteredData = new FilteredData(this.data);
	    } else {
	    	this.data = filteredData.getGlobalData();
	    	this.filteredData = filteredData;
	    }
	    if (filteredData==null) YapbamState.INSTANCE.restoreGlobalData(this);
	    
	    Class<AbstractPlugIn>[] pluginClasses = Preferences.getPlugins();
	    if (restartData==null) restartData = new Object[pluginClasses.length];
	    this.plugins=new AbstractPlugIn[pluginClasses.length];
	    for (int i = 0; i < pluginClasses.length; i++) {
			try {
				this.plugins[i] = (AbstractPlugIn) pluginClasses[i].getConstructor(FilteredData.class, Object.class).newInstance(this.filteredData, restartData[0]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    this.paneledPlugins=new ArrayList<AbstractPlugIn>();

	    setContentPane(this.createContentPane());
	    mainMenu = new MainMenuBar(this);
		setJMenuBar(mainMenu);
		mainPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateSelectedPlugin();
			}
		});
		
		newDataOccured();
	    
	    this.data.addListener(this);
	    this.data.addListener(mainMenu);
	    
	    // Restore initial state (last opened file and window position)
	    YapbamState.INSTANCE.restoreMainFramePosition(this);
	    for (int i = 0; i < plugins.length; i++) {
			plugins[i].restoreState();
		}
	
	    updateSelectedPlugin();
	    
	    //Display the window.
	    setVisible(true);
	}

	private Container createContentPane() {
        mainPane = new JTabbedPane(JTabbedPane.TOP);
        for (int i = 0; i < plugins.length; i++) {
            JPanel pane = plugins[i].getPanel();
    		if (pane!=null) {
    			paneledPlugins.add(plugins[i]);
    			mainPane.addTab(plugins[i].getPanelTitle(), null, plugins[i].getPanel(), plugins[i].getPanelToolTip());
    			if (plugins[i].getPanelIcon()!=null) {
    				mainPane.setIconAt(mainPane.getTabCount()-1, plugins[i].getPanelIcon());
    			}
    		}
    		// Listening for panel title, tooltip and icon changes
    		plugins[i].getPropertyChangeSupport().addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					int tabIndex = paneledPlugins.indexOf(evt.getSource());
					if (tabIndex > 0) {
						if (evt.getPropertyName().equals(AbstractPlugIn.PANEL_ICON_PROPERTY_NAME)) {
							mainPane.setIconAt(tabIndex, (Icon) evt.getNewValue());
						} else if (evt.getPropertyName().equals(AbstractPlugIn.PANEL_TITLE_PROPERTY_NAME)) {
							mainPane.setTitleAt(tabIndex, (String) evt.getNewValue());
						} else if (evt.getPropertyName().equals(AbstractPlugIn.PANEL_TOOLTIP_PROPERTY_NAME)) {
							mainPane.setToolTipTextAt(tabIndex, (String) evt.getNewValue());
						}
					}
				}
			});
		}
        return mainPane;
    }

	public GlobalData getData() {
		return data;
	}
	
	public FilteredData getFilteredData() {
		return this.filteredData;
	}
	
	public int getPlugInsNumber() {
		return plugins.length;
	}

	public AbstractPlugIn getPlugIn(int index) {
		return plugins[index];
	}
	
	/** Gets the plugin currently displayed in the tabbed pane.
	 * @return the currently displayed plugin
	 */
	public AbstractPlugIn getCurrentPlugIn() {
		return lastSelected<0?null:this.paneledPlugins.get(lastSelected);
	}

	public void processEvent(DataEvent event) {
		if ((event instanceof FileChangedEvent) || (event instanceof EverythingChangedEvent) || (event instanceof NeedToBeSavedChangedEvent)) {
			newDataOccured();
		}
	}

	private void newDataOccured() {
		String title = LocalizationData.get("ApplicationName"); //$NON-NLS-1$
		File file = data.getPath();
		if (file!=null) title = title + " - " + file; //$NON-NLS-1$
		if (data.somethingHasChanged()) title = title+" *";
		this.setTitle(title);
	}
	
	boolean isTransactionTableVisible() {
		return this.mainPane.getSelectedIndex()==0;
	}

	public void restart() {
		//FIXME MemoryLeak : We would need to remove the obsoletes listeners (from the closing window) ... but don't know how to do that efficiently
		this.isRestarting = true;
		final Object[] restartData = new Object[this.plugins.length];
		for (int i = 0; i < restartData.length; i++) {
			restartData[i] = this.plugins[i].getRestartData();
		}
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		try {
			UIManager.setLookAndFeel(Preferences.INSTANCE.getLookAndFeel());
		} catch (Exception e) {}
	    //Schedule a job for the event-dispatching thread:
	    //creating and showing this application's GUI.
	    javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	            new MainFrame(filteredData, restartData);
	        }
	    });
	}

	private void updateSelectedPlugin() {
		if (lastSelected>=0) paneledPlugins.get(lastSelected).setDisplayed(false);
		lastSelected = mainPane.getSelectedIndex();
		if (lastSelected<paneledPlugins.size()) paneledPlugins.get(lastSelected).setDisplayed(true);
		mainMenu.updateMenu(paneledPlugins.get(lastSelected));
	}
}
