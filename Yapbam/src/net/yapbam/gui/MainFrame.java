package net.yapbam.gui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.URI;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.yapbam.data.*;
import net.yapbam.data.event.*;
import net.yapbam.data.xml.Serializer;
import net.yapbam.data.xml.Serializer.SerializationData;
import net.yapbam.gui.actions.CheckNewReleaseAction;
import net.yapbam.gui.dialogs.GetPasswordDialog;
import net.yapbam.gui.welcome.WelcomeDialog;
import net.yapbam.util.FileUtils;
import net.yapbam.util.Portable;

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
	
	/** The updater jar file. If this attribute is not null, the jar it contains will be executed when
	 * the application quits.
	 * <br>The jar file will be executed by the same JVM than the current Yapbam instance.
	 * Be aware that the updater may absolutely output nothing to stderr and std out
	 * see http://www.javaworld.com/jw-12-2000/jw-1229-traps.html 
	 */
	public static File updater = null;

	public static void main(final String[] args) {
		// Remove obsolete files from previous installations
		FolderCleaner.clean();
		// Install the exceptions logger on the AWT event queue. 
		EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
		queue.push(new EventQueue() {
			protected void dispatchEvent(AWTEvent newEvent) {
				try {
					super.dispatchEvent(newEvent);
				} catch (Throwable t) {
					ErrorManager.INSTANCE.log (t);
				}
			}
		});
		// Set the look and feel
		try {
			UIManager.setLookAndFeel(Preferences.INSTANCE.getLookAndFeel());
		} catch (Exception e) {}
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame frame = new MainFrame(null, null, args.length > 0 ? args[0] : null);
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
		
		boolean restart = restartData!=null;
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("yapbam_16.png")));
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
					
					if ((updater!=null) && updater.exists() && updater.isFile()) {
						// If an update is available
						// The update will be done by a external program, as changing a jar on the fly
						// may lead to serious problems.
//						Enumeration<Object> keys = System.getProperties().keys();
//						while (keys.hasMoreElements()) {
//							String key = (String) keys.nextElement();
//							System.out.println (key+" = "+System.getProperty(key));
//						}
						
						ArrayList<String> command = new ArrayList<String>();
						command.add(System.getProperty("java.home")+"/bin/java");
						command.add("-jar");
						command.add(updater.getAbsolutePath());
						System.out.println (command);
						ProcessBuilder builder = new ProcessBuilder(command);
						try {
							Process process = builder.start();
							BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
							for (String line = err.readLine(); line!=null; line = err.readLine()) {
								System.err.println (line);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					super.windowClosing(event);
					frame.dispose();
				}
			}
		});
	
		if (filteredData == null) {
			this.data = new GlobalData();
			this.filteredData = new FilteredData(this.data);
		} else {
			this.data = filteredData.getGlobalData();
			this.filteredData = filteredData;
		}
		if (filteredData == null) {
			if (path != null) {
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
		if (restartData == null) restartData = new Object[pluginContainers.length];
		this.plugins = new AbstractPlugIn[pluginContainers.length];
		for (int i = 0; i < plugins.length; i++) {
			if (pluginContainers[i].isActivated()) this.plugins[i] = (AbstractPlugIn) pluginContainers[i].build(this.filteredData, restartData[0]);
			if (pluginContainers[i].getInstanciationException()!=null) { // An error occurs during plugin instanciation
				ErrorManager.INSTANCE.display(null, pluginContainers[i].getInstanciationException(), "Une erreur est survenue durant l'instanciation du plugin "+"?"); //LOCAL //TODO
				ErrorManager.INSTANCE.log(pluginContainers[i].getInstanciationException());
			}
		}
		this.paneledPlugins = new ArrayList<AbstractPlugIn>();
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
			if (plugins[i] != null) plugins[i].restoreState();
		}

		updateSelectedPlugin();

		// Display the window.
		setVisible(true);
		
		if (Preferences.INSTANCE.isWelcomeAllowed() && !restart) new WelcomeDialog(this).setVisible(true);
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
					if (password==null) break;
					this.data.read(uri, password);
					break;
				} catch (AccessControlException e) {
					dialog = new GetPasswordDialog(this,
							LocalizationData.get("FilePasswordDialog.title"), LocalizationData.get("FilePasswordDialog.openFile.badPassword.question"), //$NON-NLS-1$ //$NON-NLS-2$
							UIManager.getIcon("OptionPane.warningIcon"), null); //$NON-NLS-1$
					dialog.setPasswordFieldToolTipText(LocalizationData.get("FilePasswordDialog.openFile.tooltip")); //$NON-NLS-1$
					dialog.setVisible(true);
					password = dialog.getPassword();
				}
			}
		} else {
			this.data.read(uri, null);
		}
	}

	private Container createContentPane() {
		mainPane = new JTabbedPane(JTabbedPane.TOP);
		for (int i = 0; i < plugins.length; i++) {
			if (plugins[i] != null) {
				JPanel pane = plugins[i].getPanel();
				if (pane != null) {
					paneledPlugins.add(plugins[i]);
					mainPane.addTab(plugins[i].getPanelTitle(), null, pane, plugins[i].getPanelToolTip());
					if (plugins[i].getPanelIcon() != null) {
						mainPane.setIconAt(mainPane.getTabCount() - 1, plugins[i].getPanelIcon());
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
		if ((event instanceof URIChangedEvent) || (event instanceof EverythingChangedEvent) || (event instanceof NeedToBeSavedChangedEvent)) {
			newDataOccured();
		}
	}

	private void newDataOccured() {
		String title = LocalizationData.get("ApplicationName"); //$NON-NLS-1$
		URI file = data.getURI();
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
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
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
