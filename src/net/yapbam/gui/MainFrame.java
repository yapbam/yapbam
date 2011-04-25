package net.yapbam.gui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.AccessControlException;
import java.text.MessageFormat;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.yapbam.data.*;
import net.yapbam.data.event.*;
import net.yapbam.data.xml.Serializer;
import net.yapbam.data.xml.Serializer.SerializationData;
import net.yapbam.gui.actions.CheckNewReleaseAction;
import net.yapbam.gui.dialogs.DefaultHTMLInfoDialog;
import net.yapbam.gui.dialogs.GetPasswordDialog;
import net.yapbam.gui.welcome.WelcomeDialog;
import net.yapbam.update.ReleaseInfo;
import net.yapbam.update.VersionManager;
import net.yapbam.util.NullUtils;

public class MainFrame extends JFrame implements DataListener {
	private static final String LAST_VERSION_USED = "lastVersionUsed"; //$NON-NLS-1$
	//TODO implements undo support (see package undo in JustSomeTests project)
	//TODO implements copy/paste support ?
	private static final long serialVersionUID = 1L;
	private static final String LAST_URI = "data.uri"; //$NON-NLS-1$
	private static final String FRAME_SIZE_WIDTH = "frame.size.width"; //$NON-NLS-1$
	private static final String FRAME_SIZE_HEIGHT = "frame.size.height"; //$NON-NLS-1$
	private static final String FRAME_LOCATION_Y = "frame.location.y"; //$NON-NLS-1$
	private static final String FRAME_LOCATION_X = "frame.location.x"; //$NON-NLS-1$

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
	 * <br>The jar file will be executed by a new instance of the same JVM than the current Yapbam instance.
	 */
	public static File updater = null;

	public static void main(final String[] args) {
		// Remove obsolete files from previous installations
		FolderCleaner.clean();
		// Install the exceptions logger on the AWT event queue. 
		EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
		queue.push(new EventQueue() {
			@Override
			protected void dispatchEvent(AWTEvent newEvent) {
				try {
					super.dispatchEvent(newEvent);
				} catch (Throwable t) {
					ErrorManager.INSTANCE.log (null,t);
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
				if (Preferences.INSTANCE.isWelcomeAllowed()) new WelcomeDialog(frame, frame.getData()).setVisible(true);
				if (!Preferences.INSTANCE.isFirstRun()) {
					String importantNews = buildNews();
					if (importantNews.length()>0) {
						new DefaultHTMLInfoDialog(frame, LocalizationData.get("ImportantNews.title"), LocalizationData.get("ImportantNews.intro"), importantNews).setVisible(true); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
			}
		});
	}
	
	private static String buildNews () {
		StringBuilder buf = new StringBuilder();
		ReleaseInfo lastVersion = (ReleaseInfo) YapbamState.INSTANCE.restore(LAST_VERSION_USED);
		if (NullUtils.compareTo(lastVersion, new ReleaseInfo("0.8.2 (10/04/2011)"), true)<=0) { //$NON-NLS-1$
			String message = MessageFormat.format(LocalizationData.get("ImportantNews.0.8.2"), //$NON-NLS-1$
					LocalizationData.get("CheckModePanel.title"), LocalizationData.get("MainFrame.Transactions"), //$NON-NLS-1$ //$NON-NLS-2$
					LocalizationData.get("StatementView.title"), LocalizationData.get("StatementView.notChecked")); //$NON-NLS-1$ //$NON-NLS-2$
			buf.append(message);
		}
		// The lines below are a sample for next time we want to add a "important" release information
//		if (lastVersion.compareTo(new ReleaseInfo("0.8.2 (10/04/2011)"))<=0) { //$NON-NLS-1$
//			if (buf.length()>0) buf.append("<br><br><hr><br>");
//			String message = MessageFormat.format(LocalizationData.get("ImportantNews.0.8.2"), //$NON-NLS-1$
//					LocalizationData.get("CheckModePanel.title"), LocalizationData.get("MainFrame.Transactions"), //$NON-NLS-1$ //$NON-NLS-2$
//					LocalizationData.get("StatementView.title"), LocalizationData.get("StatementView.notChecked")); //$NON-NLS-1$ //$NON-NLS-2$
//			buf.append(message);
//		}
		return buf.toString();
	}
	
	/** Create the GUI and show it.  For thread safety, this method should be invoked from the
	 * event-dispatching thread.
	 */
	private MainFrame(FilteredData filteredData, Object[] restartData, String path) {
	    //Create and set up the window.
		super();
		
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("yapbam_16.png"))); //$NON-NLS-1$
		this.setMinimumSize(new Dimension(800,400));

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				MainFrame frame = (MainFrame) event.getWindow();
				if (frame.isRestarting) {
					frame.saveState();
					super.windowClosing(event);
					frame.dispose();
				} else if (SaveManager.MANAGER.verify(frame)) {
					frame.saveState();
					Preferences.INSTANCE.save();
					super.windowClosing(event);
					frame.dispose();
					
					if ((updater!=null) && updater.exists() && updater.isFile()) {
						// If an update is available
						// The update will be done by a external program, as changing a jar on the fly
						// may lead to serious problems.					
						ArrayList<String> command = new ArrayList<String>();
						command.add(System.getProperty("java.home")+"/bin/java"); //$NON-NLS-1$ //$NON-NLS-2$
						command.add("-jar"); //$NON-NLS-1$
						command.add(updater.getAbsolutePath());
						System.out.println (command);
						ProcessBuilder builder = new ProcessBuilder(command);
						try {
							// I've tried to remove these lines that prevent Yapbam from quitting before the end of the update
							// Unfortunately, under ubuntu 10.10 ... it led to the update crash :-(
							// The most strange is that it ran perfectly under eclipse under ubuntu 10.10 ... what a strange behaviour !!!
							// see also http://www.javaworld.com/jw-12-2000/jw-1229-traps.html 
							Process process = builder.start();
							BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
							for (String line = err.readLine(); line!=null; line = err.readLine()) {
							}
						} catch (IOException e) {
							ErrorManager.INSTANCE.log(frame, e);
						}
					}
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
				restoreGlobalData();
			}
		}
	    
		PlugInContainer[] pluginContainers = Preferences.getPlugins();
		if (restartData == null) restartData = new Object[pluginContainers.length];
		this.plugins = new AbstractPlugIn[pluginContainers.length];
		for (int i = 0; i < plugins.length; i++) {
			if (pluginContainers[i].isActivated()) this.plugins[i] = (AbstractPlugIn) pluginContainers[i].build(this.filteredData, restartData[0]);
			if (pluginContainers[i].getInstanciationException()!=null) { // An error occurs during plugin instantiation
				ErrorManager.INSTANCE.display(null, pluginContainers[i].getInstanciationException(), "Une erreur est survenue durant l'instanciation du plugin "+"?"); //LOCAL //TODO
				ErrorManager.INSTANCE.log(this, pluginContainers[i].getInstanciationException());
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
		restoreMainFramePosition();
		for (int i = 0; i < plugins.length; i++) {
			if (plugins[i] != null) plugins[i].restoreState();
		}

		updateSelectedPlugin();

		// Display the window.
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
	
	private void saveState() {
		if (getData().getURI()!=null) {
			getStateSaver().put(LAST_URI, getData().getURI().toString());
		} else {
			getStateSaver().remove(LAST_URI);
		}
		Point location = getLocation();
		getStateSaver().put(FRAME_LOCATION_X, Integer.toString(location.x));
		getStateSaver().put(FRAME_LOCATION_Y, Integer.toString(location.y));
		Dimension size = getSize();
		int h = ((getExtendedState() & Frame.MAXIMIZED_VERT) == 0) ? size.height : -1;
		getStateSaver().put(FRAME_SIZE_HEIGHT, Integer.toString(h));
		int w = ((getExtendedState() & Frame.MAXIMIZED_HORIZ) == 0) ? size.width : -1;
		getStateSaver().put(FRAME_SIZE_WIDTH, Integer.toString(w));
		for (int i = 0; i < getPlugInsNumber(); i++) {
			if (getPlugIn(i)!=null) getPlugIn(i).saveState();
		}
		getStateSaver().save(LAST_VERSION_USED, VersionManager.getVersion());
		try {
			getStateSaver().toDisk();
		} catch (IOException e) {
			e.printStackTrace();
			//TODO What could we do ?
		}
	}

	private YapbamState getStateSaver() {
		return YapbamState.INSTANCE;
	}
	
	private void restoreMainFramePosition() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = Integer.parseInt((String) getStateSaver().get(FRAME_LOCATION_X,"0")); //$NON-NLS-1$
		int y = Integer.parseInt((String) getStateSaver().get(FRAME_LOCATION_Y,"0")); //$NON-NLS-1$
		int width = Integer.parseInt((String) getStateSaver().get(FRAME_SIZE_WIDTH,""+(screenSize.width/2))); //$NON-NLS-1$
		int height = Integer.parseInt((String) getStateSaver().get(FRAME_SIZE_HEIGHT,""+(screenSize.height/2))); //$NON-NLS-1$
		setExtendedState(Frame.MAXIMIZED_BOTH); //TODO Save the maximized state
		//TODO Beware of a screen size change (especially of a reduction) ?
  /*
		if ((width==0) || (width+x>screenSize.width)) {
			x=0;
			width = screenSize.width/2;
		}
		if ((height==0) || (height+y>screenSize.height)) {
			y=0;
			height = screenSize.height/2;
		}*/
        setLocation(x,y);
		setSize(width,height);
		int extendedState = Frame.NORMAL;
		if (height<0) extendedState = extendedState | Frame.MAXIMIZED_VERT;
		if (width<0) extendedState = extendedState | Frame.MAXIMIZED_HORIZ;
		setExtendedState(extendedState);
	}
	
	private void restoreGlobalData() {
		URI uri = null;
		if (getStateSaver().contains(LAST_URI)) {
			try {
				uri = new URI((String) getStateSaver().get(LAST_URI));
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (uri!=null) {
			try {
				readData(uri);
			} catch (IOException e) {
				ErrorManager.INSTANCE.display(this, e, MessageFormat.format(LocalizationData.get("MainFrame.ReadLastError"),uri)); //$NON-NLS-1$
			}
		}
	}
}
