package net.yapbam.gui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.yapbam.data.*;
import net.yapbam.data.event.*;
import net.yapbam.data.xml.BadPasswordException;
import net.yapbam.data.xml.Serializer;
import net.yapbam.data.xml.Serializer.SerializationData;
import net.yapbam.gui.actions.CheckNewReleaseAction;
import net.yapbam.gui.dialogs.GetPasswordDialog;

public class MainFrame extends JFrame implements DataListener {
	//TODO implements undo support (see package undo in JustSomeTests project)
	//TODO implements copy/paste support ?
	
	private static final long serialVersionUID = 1L;
    
    private GlobalData data;
	private FilteredData filteredData;

	private MainMenuBar mainMenu;
	private JTabbedPane mainPane;
	private AbstractPlugIn[] plugins;
	private ArrayList<AbstractPlugIn> paneledPlugins;
	private int lastSelected = -1;
	private boolean isRestarting = false;

	public static void main(final String[] args) {
		try {
			UIManager.setLookAndFeel(Preferences.INSTANCE.getLookAndFeel());
		} catch (Exception e) {}
	    //Schedule a job for the event-dispatching thread:
	    //creating and showing this application's GUI.
	    javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	            MainFrame frame = new MainFrame(null, null, args.length>0?args[0]:null);
	        	CheckNewReleaseAction.doAutoCheck(frame);
	        }
	    });
	}
	
	/** Create the GUI and show it.  For thread safety, this method should be invoked from the
	 * event-dispatching thread.
	 */
	private MainFrame(FilteredData filteredData, Object[] restartData, String path) {
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
	    if (filteredData==null) {
	    	if (path!=null) {
				URI file = new File(path).toURI();
				try {
					this.readData(file);
				} catch (IOException e) {
					ErrorManager.INSTANCE.display(this, e, LocalizationData.get("MainFrame.ReadError")); //$NON-NLS-1$
				}
	    	} else {
	    		YapbamState.INSTANCE.restoreGlobalData(this);
	    	}
	    }
	    
	    PlugInContainer[] pluginContainers = Preferences.getPlugins();
	    if (restartData==null) restartData = new Object[pluginContainers.length];
	    this.plugins=new AbstractPlugIn[pluginContainers.length];
	    for (int i = 0; i < plugins.length; i++) {
			if (pluginContainers[i].isActivated()) this.plugins[i] = (AbstractPlugIn) pluginContainers[i].build(this.filteredData, restartData[0]);
		}
	    this.paneledPlugins=new ArrayList<AbstractPlugIn>();
	    setContentPane(this.createContentPane());
		mainPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateSelectedPlugin();
			}
		});
		newDataOccured();
	    
	    this.data.addListener(this);
	    
	    mainMenu = new MainMenuBar(this);
		setJMenuBar(mainMenu);
	    
	    // Restore initial state (last opened file and window position)
	    YapbamState.INSTANCE.restoreMainFramePosition(this);
	    for (int i = 0; i < plugins.length; i++) {
			if (plugins[i]!=null) plugins[i].restoreState();
		}
	
	    updateSelectedPlugin();
	    
	    //Display the window.
	    setVisible(true);
	}
	
	void readData(URI uri) throws IOException {
		SerializationData info = Serializer.getSerializationData(uri);
		if (info.isPasswordRequired()) {
			GetPasswordDialog dialog = new GetPasswordDialog(this,
					LocalizationData.get("FilePasswordDialog.title"), LocalizationData.get("FilePasswordDialog.openFile.question"), //$NON-NLS-1$ //$NON-NLS-2$
					UIManager.getIcon("OptionPane.questionIcon"), null); //$NON-NLS-1$
			dialog.setPasswordFieldToolTipText(LocalizationData.get("FilePasswordDialog.openFile.tooltip")); //$NON-NLS-1$
			dialog.setVisible(true);
			String password = dialog.getPassword();
			while (true) {
				try {
					this.data.read(uri, password);
					break;
				} catch (BadPasswordException e) {
					dialog = new GetPasswordDialog(this,
							LocalizationData.get("FilePasswordDialog.title"), LocalizationData.get("FilePasswordDialog.openFile.badPassword.question"), //$NON-NLS-1$ //$NON-NLS-2$
							UIManager.getIcon("OptionPane.warningIcon"), null); //$NON-NLS-1$
					dialog.setPasswordFieldToolTipText(LocalizationData.get("FilePasswordDialog.openFile.tooltip")); //$NON-NLS-1$
					dialog.setVisible(true);
					password = dialog.getPassword();
					if (password==null) break;
				}
			}
		} else {
			this.data.read(uri, null);
		}
	}

	private Container createContentPane() {
        mainPane = new JTabbedPane(JTabbedPane.TOP);
        for (int i = 0; i < plugins.length; i++) {
        	if (plugins[i]!=null) {
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
						if (tabIndex >= 0) {
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
		URI file = data.getPath();
		if (file!=null) title = title + " - " + file; //$NON-NLS-1$
		if (data.somethingHasChanged()) title = title+" *"; //$NON-NLS-1$
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
	            new MainFrame(filteredData, restartData, null);
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
