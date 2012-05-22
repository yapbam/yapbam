package net.yapbam.gui;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.astesana.ajlib.swing.Utils;
import net.astesana.ajlib.swing.worker.WorkInProgressFrame;
import net.astesana.ajlib.swing.worker.Worker;
import net.astesana.ajlib.utilities.NullUtils;
import net.yapbam.data.*;
import net.yapbam.data.event.*;
import net.yapbam.data.xml.Serializer;
import net.yapbam.data.xml.Serializer.SerializationData;
import net.yapbam.gui.actions.CheckNewReleaseAction;
import net.yapbam.gui.dialogs.DefaultHTMLInfoDialog;
import net.yapbam.gui.dialogs.GetPasswordDialog;
import net.yapbam.gui.preferences.StartStateOptions;
import net.yapbam.gui.welcome.WelcomeDialog;
import net.yapbam.gui.widget.TabbedPane;
import net.yapbam.update.ReleaseInfo;
import net.yapbam.update.VersionManager;

public class MainFrame extends JFrame implements DataListener {
	//TODO implements undo support (see package undo in JustSomeTests project)
	//TODO implements copy/paste support ?
	private static final long serialVersionUID = 1L;
	private static final String LAST_URI = "data.uri"; //$NON-NLS-1$
	private static final String FRAME_SIZE_WIDTH = "frame.size.width"; //$NON-NLS-1$
	private static final String FRAME_SIZE_HEIGHT = "frame.size.height"; //$NON-NLS-1$
	private static final String FRAME_LOCATION_Y = "frame.location.y"; //$NON-NLS-1$
	private static final String FRAME_LOCATION_X = "frame.location.x"; //$NON-NLS-1$
	private static final String LAST_VERSION_USED = "lastVersionUsed"; //$NON-NLS-1$
	private static final String LAST_FILTER_USED = "filter"; //$NON-NLS-1$

	private GlobalData data;
	private FilteredData filteredData;

	MainMenuBar mainMenu;
	private TabbedPane mainPane;
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
		// Warning the new event queue may ABSOLUTLY be NOT installed by the event dispatch thread under java 1.6 or the program will never exit
		if (isJava6()) installEventQueue();
		// Set the look and feel
		setLookAndFeel();
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Install the exceptions logger on the AWT event queue.
				// Warning the new event queue may ABSOLUTLY be installed by the event dispatch thread under java 1.7 or the program will never exit
				if (!isJava6()) installEventQueue();
				MainFrame frame = new MainFrame(null, null);

				// Initialize the data ... and start the end of the initialization process :
				// As the initialization uses a background task, the end of the process is called at the end of this background task
				// If we had let this extra initialization tasks here, they would have start during the data initialization.
				frame.initData(args.length==0?null:args[0]);

				// Check for an update
				CheckNewReleaseAction.doAutoCheck(frame);
				
				// As the check for update is a (possibly) long background task, we do not wait its completion before showing the dialogs
				// So, we do not need to use a BackgroundTaskContext there
				if (Preferences.INSTANCE.isWelcomeAllowed()) new WelcomeDialog(frame).setVisible(true);
				
				if (!Preferences.INSTANCE.isFirstRun()) {
					String importantNews = buildNews();
					if (importantNews.length()>0) {
						new DefaultHTMLInfoDialog(frame, LocalizationData.get("ImportantNews.title"), LocalizationData.get("ImportantNews.intro"), importantNews).setVisible(true); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
			}
		});
	}
	
	private static boolean isJava6() {
		return "1.6".equals(System.getProperty("java.specification.version"));
	}
	
	private static void installEventQueue() {
		EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
		queue.push(new EventQueue() {
			@Override
			protected void dispatchEvent(AWTEvent event) {
				try {
					super.dispatchEvent(event);
				} catch (Throwable t) {
					ErrorManager.INSTANCE.log (null,t);
					// The following portion of code closes the main window if it is not visible.
					// This solves the case where a exception is thrown during the Mainframe.setVisible(true)
					// If we do nothing, the window is there, but invisible, so, the user has no way to close
					// the window for exiting from yapbam.
					Window[] windows = Window.getWindows();
					for (int i = 0; i < windows.length; i++) {
						if (windows[i] instanceof MainFrame) {
							if (!windows[i].isVisible()) {
								windows[i].dispatchEvent(new WindowEvent(windows[i],WindowEvent.WINDOW_CLOSING));
							}
						}
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
	 * @param filteredData The current filtered data if the application is restarted, null if the application is simply started
	 * @param restartData The plugins restartData if the application is restarted, null if it is simply started 
	 */
	private MainFrame(FilteredData filteredData, Object[] restartData) {
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
					// Be aware, you can think that it's a good idea to write the following line before the if/then/else block.
					// But it's not, it would lead to the state not remember which file was last saved in the following scenario:
					// Create a new file, then add a transaction, click the close window -> The save/ignore/cancel dialog appears
					// (The SaveManager.MANAGER.verify(frame) displays it). If the state save occurs before that, it can't save
					// the file you will choose after clicking the save option.
					frame.saveState();
					// You could wonder why we don't save preferences when closing the preferences dialog
					// It's because there's other dialogs that changes the preferences (for example when closing the welcome dialog: show/hide at startup) 
					try {
						Preferences.INSTANCE.save();
					} catch (IOException e) {
						JOptionPane.showMessageDialog(MainFrame.this, MessageFormat.format(LocalizationData.get("MainFrame.SavePreferencesError"), getStateSaver().getFile().getAbsolutePath()), //$NON-NLS-1$
								LocalizationData.get("ErrorManager.title"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
					}
					super.windowClosing(event);
					frame.dispose();
					
					if ((updater!=null) && updater.exists() && updater.isFile()) {
						// If an update is available
						// The update will be done by an external program, as changing a jar on the fly
						// may lead to serious problems.					
						ArrayList<String> command = new ArrayList<String>();
						command.add(System.getProperty("java.home")+"/bin/java"); //$NON-NLS-1$ //$NON-NLS-2$
						command.add("-jar"); //$NON-NLS-1$
						command.add(updater.getAbsolutePath());
						ProcessBuilder builder = new ProcessBuilder(command);
						try {
							// I've tried to remove these lines that prevent Yapbam from quitting before the end of the update
							// Unfortunately, under ubuntu 10.10 ... it led to the update crash :-(
							// The most strange is that it ran perfectly under eclipse under ubuntu 10.10 ... what a strange behaviour !!!
							// see also http://www.javaworld.com/jw-12-2000/jw-1229-traps.html
							// The good new is it seems to be ok under ubuntu 11.04
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
			// Create the data structures if they are not provided as argument
			this.data = new GlobalData();
			this.filteredData = new FilteredData(this.data);
		} else {
			this.data = filteredData.getGlobalData();
			this.filteredData = filteredData;
		}
	    
		PlugInContainer[] pluginContainers = Preferences.getPlugins();
		if (restartData == null) restartData = new Object[pluginContainers.length];
		this.plugins = new AbstractPlugIn[pluginContainers.length];
		Context context = new Context(this);
		for (int i = 0; i < plugins.length; i++) {
			if (pluginContainers[i].isActivated()) {
				this.plugins[i] = (AbstractPlugIn) pluginContainers[i].build(this.filteredData, restartData[0]);
				this.plugins[i].setContext(context);
			}
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
		this.data.addListener(this);

		mainMenu = new MainMenuBar(this);
		setJMenuBar(mainMenu);
	    
		// Restore initial state (last opened file, window position, ...)
		restoreMainFramePosition();
		getStateSaver().restoreState(mainPane, this.getClass().getCanonicalName());
		for (int i = 0; i < plugins.length; i++) {
			if (plugins[i] != null) plugins[i].restoreState();
		}

		updateSelectedPlugin();
		updateWindowTitle();

		// Display the window.
		setVisible(true);
	}
	
	public boolean readData(URI uri) throws ExecutionException {
		String password = null;
		try {
			SerializationData info = Serializer.getSerializationData(uri);
			// Retrieving the file password
			if (info.isPasswordRequired()) {
				GetPasswordDialog dialog = new GetPasswordDialog(this,
						LocalizationData.get("FilePasswordDialog.title"), LocalizationData.get("FilePasswordDialog.openFile.question"), //$NON-NLS-1$ //$NON-NLS-2$
						UIManager.getIcon("OptionPane.questionIcon"), null); //$NON-NLS-1$
				dialog.setPasswordFieldToolTipText(LocalizationData.get("FilePasswordDialog.openFile.tooltip")); //$NON-NLS-1$
				dialog.setVisible(true);
				password = dialog.getPassword();
				while (true) {
					if (password==null) uri = null; // The user cancels the read
					if ((password==null) || Serializer.isPasswordOk(uri, password)) break; // If the user cancels or entered the right password ... go next step
					dialog = new GetPasswordDialog(this,
							LocalizationData.get("FilePasswordDialog.title"), LocalizationData.get("FilePasswordDialog.openFile.badPassword.question"), //$NON-NLS-1$ //$NON-NLS-2$
							UIManager.getIcon("OptionPane.warningIcon"), null); //$NON-NLS-1$
					dialog.setPasswordFieldToolTipText(LocalizationData.get("FilePasswordDialog.openFile.tooltip")); //$NON-NLS-1$
					dialog.setVisible(true);
					password = dialog.getPassword();
				}
			}
		} catch (IOException e) {
			new ExecutionException(e);
		}
		final BackgroundReader worker = new BackgroundReader(uri, password);
		WorkInProgressFrame waitFrame = new WorkInProgressFrame(this, "Reading ...", ModalityType.APPLICATION_MODAL, worker);
		Utils.centerWindow(waitFrame, this);
		waitFrame.setVisible(true);
		boolean cancelled = worker.isCancelled();
		if (!cancelled) {
				GlobalData redData;
				try {
					redData = worker.get();
					boolean enabled = data.isEventsEnabled();
					data.setEventsEnabled(false);
					data.copy(redData);
					data.setChanged(false);
					data.setEventsEnabled(enabled);
				} catch (InterruptedException e) {
					throw new ExecutionException(e);
				}
		}
		return !cancelled;
	}
	
	/** A worker (see AJLib library) that reads a GlobalData URI in background. 
	 */
	public static class BackgroundReader extends Worker<GlobalData, Void> implements ProgressReport {
		private URI uri;
		private String password;
	
		/** Constructor.
		 * @param uri The source URI (null to do nothing)
		 * @param password The password to access to the source (null if no password is needed)
		 */
		public BackgroundReader (URI uri, String password) {
			this.uri = uri;
			this.password = password;
		}
		
		@Override
		protected GlobalData doInBackground() throws Exception {
			return uri==null ? null : Serializer.read(uri, password, this);
		}
	
		@Override
		public void setMax(int length) {
			super.setPhase("Reading file", length); //LOCAL
		}
	}
	
	private Container createContentPane() {
		mainPane = new TabbedPane();
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
							tabIndex = mainPane.getIndexOf(tabIndex);
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
			updateWindowTitle();
		}
	}

	private void updateWindowTitle() {
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
		setLookAndFeel();
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MainFrame(filteredData, restartData);
			}
		});
	}
	
	public static void setLookAndFeel() {
		try {
			String lookAndFeelName = Preferences.INSTANCE.getLookAndFeel();
			LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
			String lookAndFeelClass = UIManager.getSystemLookAndFeelClassName();
			for (LookAndFeelInfo lookAndFeelInfo : installedLookAndFeels) {
				// Prior the 0.9.8, the class name were used instead of the generic name.
				// It caused problem when changing java version (ie: Nimbus in java 1.6 was implemented by a class in com.sun.etc and in javax.swing in java 1.7)
				if (lookAndFeelInfo.getName().equals(lookAndFeelName)) {
					lookAndFeelClass = lookAndFeelInfo.getClassName();
					break;
				}
			}
			UIManager.setLookAndFeel(lookAndFeelClass);
			UIManager.getLookAndFeelDefaults().setDefaultLocale(LocalizationData.getLocale());
		} catch (Exception e) {}
	}

	private void updateSelectedPlugin() {
		if (lastSelected>=0) paneledPlugins.get(lastSelected).setDisplayed(false);
		lastSelected = mainPane.getId(mainPane.getSelectedIndex());
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
		getStateSaver().saveState(mainPane, this.getClass().getCanonicalName());
		getStateSaver().save(LAST_VERSION_USED, VersionManager.getVersion());
		if (Preferences.INSTANCE.getStartStateOptions().isRememberFilter()) {
			if (!getData().somethingHasChanged() && getFilteredData().getFilter().isActive()) {
				getStateSaver().save(LAST_FILTER_USED, getFilteredData().getFilter(), getData().getPassword());
			} else {
				getStateSaver().remove(LAST_FILTER_USED);
			}
		}
		try {
			getStateSaver().toDisk();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, MessageFormat.format(LocalizationData.get("MainFrame.SaveStateError"), getStateSaver().getFile().getAbsolutePath()), //$NON-NLS-1$
					LocalizationData.get("ErrorManager.title"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
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
	
	private void initData(String path) {
		URI uri = null;
		if (path!=null) { // If a path was provided
			uri = new File(path).toURI();
		} else {
			// Restore the data according to the Preferences
			StartStateOptions startOptions = Preferences.INSTANCE.getStartStateOptions();
			if (startOptions.isRememberFile()) {
				if (getStateSaver().contains(LAST_URI)) {
					try {
						uri = new URI((String) getStateSaver().get(LAST_URI));
					} catch (URISyntaxException e1) {
						// The saved uri is invalidFormat
						//TODO inform the user !!!
					}
				}
			}
		}
		boolean restore = (path == null);
		if (uri!=null) {
			try {
				readData(uri);
				if (restore && (uri!=null) && Preferences.INSTANCE.getStartStateOptions().isRememberFilter()) {
					try {
						Filter filter = getStateSaver().restoreFilter(LAST_FILTER_USED, getData());
						if (filter!=null) getFilteredData().setFilter(filter);
					} catch (Exception e) {
						ErrorManager.INSTANCE.log(MainFrame.this, e);
						ErrorManager.INSTANCE.display(MainFrame.this, e, LocalizationData.get("MainFrame.ReadLastFilterError")); //$NON-NLS-1$					
					}
				}
			} catch (ExecutionException exception) {
				Throwable e = exception.getCause();
				if (restore && (e instanceof FileNotFoundException)) {
					ErrorManager.INSTANCE.display(MainFrame.this, null, MessageFormat.format(LocalizationData.get("MainFrame.LastNotFound"),uri)); //$NON-NLS-1$
				} else if (e instanceof IOException) {
					if (restore) {
						ErrorManager.INSTANCE.display(MainFrame.this, e, MessageFormat.format(LocalizationData.get("MainFrame.ReadLastError"),uri)); //$NON-NLS-1$
					} else {
						ErrorManager.INSTANCE.display(MainFrame.this, e, LocalizationData.get("MainFrame.ReadError")); //$NON-NLS-1$ //If path is not null
					}
				} else {
					ErrorManager.INSTANCE.log(MainFrame.this, e);
				}
			}
		}
	}
}
