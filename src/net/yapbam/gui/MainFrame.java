package net.yapbam.gui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;

import javax.swing.*;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.FontUtils;
import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.utilities.NullUtils;
import com.fathzer.soft.jclop.Service;

import net.yapbam.data.*;
import net.yapbam.data.event.*;
import net.yapbam.data.xml.UnsupportedFileVersionException;
import net.yapbam.data.xml.UnsupportedFormatException;
import net.yapbam.gui.actions.CheckNewReleaseAction;
import net.yapbam.gui.dialogs.BasicHTMLDialog;
import net.yapbam.gui.info.MessagesBuilder;
import net.yapbam.gui.persistence.PersistenceManager;
import net.yapbam.gui.persistence.UnsupportedSchemeException;
import net.yapbam.gui.persistence.YapbamDataWrapper;
import net.yapbam.gui.persistence.YapbamPersistenceManager;
import net.yapbam.gui.welcome.WelcomeDialog;
import net.yapbam.update.ReleaseInfo;
import net.yapbam.util.ApplicationContext;
import net.yapbam.util.HtmlUtils;

public class MainFrame extends JFrame implements YapbamInstance {
	public static final String APPLICATION_NAME = "Yet Another Personal Account Manager";

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
	private AbstractPlugIn[] plugins;
	private boolean isRestarting = false;
	
	/** The updater jar file. If this attribute is not null, the jar it contains will be executed when
	 * the application quits.
	 * <br>The jar file will be executed by a new instance of the same JVM than the current Yapbam instance.
	 */
	public static File updater = null;
	
	public JFrame getJFrame() {
		return this;
	}

	public static void main(final String[] args) {
		if (!isJava6()) {
			// Workaround of a bug in swing with Java 1.7
			// Should absolutely be the first thing called in the program !!!
			System.setProperty("java.util.Arrays.useLegacyMergeSort", "true"); //$NON-NLS-1$ //$NON-NLS-2$
			if (!isJava7()) {
				// If java 8 or more, activate basic authentication proxy
				// see https://www.oracle.com/technetwork/java/javase/8u111-relnotes-3124969.html
				System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
			}
		}

		// Remove obsolete files from previous installations
		FolderCleaner.clean();
		// Set the look and feel
		setLookAndFeel();
		// Install the exceptions logger on the AWT event queue.
		if (isJava6()) {
			// Warning the new event queue may ABSOLUTLY be NOT installed by the event dispatch thread under java 1.6 or the program will never exit
			installEventQueue();
		} else {
			// Warning the new event queue may ABSOLUTLY be installed by the event dispatch thread under java 1.7 or the program will never exit
			// Warning - 2, if the new event queue is installed by the Runnable that launches start, it sometimes cause a NullPointerException
			// at java.awt.EventQueue.getCurrentEventImpl(EventQueue.java:796), for instance when setting the selectedItem of a JComboBox in the start method
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						installEventQueue();
					}
				});
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e.getCause());
			}
		}
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame frame = new MainFrame(null, null);

				// Initialize the data ... and start the end of the initialization process :
				// As the initialization uses a background task, the end of the process is called at the end of this background task
				// If we had let this extra initialization tasks here, they would have start during the data initialization.
				frame.initData(args.length==0?null:args[0]);

				// Check for an update
				CheckNewReleaseAction.doAutoCheck(frame.getJFrame());
				
				// As the check for update is a (possibly) long background task, we do not wait its completion before showing the dialogs
				// So, we do not need to use a BackgroundTaskContext there
				if (Preferences.INSTANCE.isWelcomeAllowed()) {
					new WelcomeDialog(frame.getJFrame(), frame.getData()).setVisible(true);
				}
				
				if (!Preferences.INSTANCE.isFirstRun()) {
					String importantNews = buildNews();
					if (importantNews.length()>0) {
						BasicHTMLDialog dialog = new BasicHTMLDialog(frame.getJFrame(), LocalizationData.get("ImportantNews.title"), LocalizationData.get("ImportantNews.intro"), BasicHTMLDialog.Type.INFO); //$NON-NLS-1$ //$NON-NLS-2$
						dialog.setContent(importantNews);
						dialog.setVisible(true);
					}
				}
			}
		});
	}
	
	private static boolean isJava6() {
		return "1.6".equals(System.getProperty("java.specification.version")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private static boolean isJava7() {
		return "1.7".equals(System.getProperty("java.specification.version")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private static void installEventQueue() {
		EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
		queue.push(new EventQueue() {
			@Override
			protected void dispatchEvent(AWTEvent event) {
				try {
					super.dispatchEvent(event);
				} catch (Throwable t) {
					t.printStackTrace();
					ErrorManager.INSTANCE.log (null,t);
					// The following portion of code closes the main window if it is not visible.
					// This solves the case where a exception is thrown during the Mainframe.setVisible(true)
					// If we do nothing, the window is there, but invisible, so, the user has no way to close
					// the window for exiting from yapbam.
					Window[] windows = Window.getWindows();
					for (int i = 0; i < windows.length; i++) {
						if ((windows[i] instanceof MainFrame) && !windows[i].isVisible()) {
							windows[i].dispatchEvent(new WindowEvent(windows[i],WindowEvent.WINDOW_CLOSING));
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
			String message = Formatter.format(HtmlUtils.removeHtmlTags(LocalizationData.get("ImportantNews.0.8.2")), //$NON-NLS-1$
					LocalizationData.get("CheckModePanel.title"), LocalizationData.get("MainFrame.Transactions"), //$NON-NLS-1$ //$NON-NLS-2$
					LocalizationData.get("StatementView.title"), LocalizationData.get("StatementView.notChecked")); //$NON-NLS-1$ //$NON-NLS-2$
			buf.append(message);
		}
		// The lines below are a sample for next time we want to add a "important" release information
		if (NullUtils.compareTo(lastVersion, new ReleaseInfo("0.14.5 (24/03/2013)"), true)<=0) { //$NON-NLS-1$
			if (buf.length()>0) {
				buf.append("<br><br><hr><br>"); //$NON-NLS-1$
			}
			String message = Formatter.format(HtmlUtils.removeHtmlTags(LocalizationData.get("ImportantNews.0.14.5")), //$NON-NLS-1$
					LocalizationData.get("CheckModePanel.title"), LocalizationData.get("MainFrame.Transactions"), //$NON-NLS-1$ //$NON-NLS-2$
					LocalizationData.get("StatementView.title"), LocalizationData.get("StatementView.notChecked")); //$NON-NLS-1$ //$NON-NLS-2$
			buf.append(message);
		}
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
		
//		System.out.println (System.getProperty("java.version")); //SEEYOU
		this.getJFrame().setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("yapbam_16.png"))); //$NON-NLS-1$
		getJFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getJFrame().addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				if (MainFrame.this.isRestarting) {
					MainFrame.this.saveState();
					super.windowClosing(event);
					event.getWindow().dispose();
				} else if (YapbamPersistenceManager.MANAGER.verify(getJFrame(), new YapbamDataWrapper(getData()))) {
					// Be aware, you can think that it's a good idea to write the following line before the if/then/else block.
					// But it's not, it would lead to the state not remember which file was last saved in the following scenario:
					// Create a new file, then add a transaction, click the close window -> The save/ignore/cancel dialog appears
					// (The SaveManager.MANAGER.verify(frame) displays it). If the state save occurs before that, it can't save
					// the file you will choose after clicking the save option.
					MainFrame.this.saveState();
					// You could wonder why we don't save preferences when closing the preferences dialog
					// It's because there's other dialogs that changes the preferences (for example when closing the welcome dialog: show/hide at startup) 
					try {
						Preferences.INSTANCE.save();
					} catch (IOException e) {
						JOptionPane.showMessageDialog(event.getWindow(), Formatter.format(LocalizationData.get("MainFrame.SavePreferencesError"), getStateSaver().getFile().getAbsolutePath()), //$NON-NLS-1$
								LocalizationData.get("ErrorManager.title"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
					}
					super.windowClosing(event);
					event.getWindow().dispose();
					
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
							ErrorManager.INSTANCE.log(event.getWindow(), e);
						}
					}
				}
			}
		});

		if (filteredData == null) {
			// Create the data structures if they are not provided as argument
			this.data = new GlobalData() {
				private final boolean checkEventOnEDT = Boolean.getBoolean("CheckGlobalDataEventAreOnEDT"); //$NON-NLS-1$
				@Override
				protected void fireEvent(DataEvent event) {
					if (isEventsEnabled() && checkEventOnEDT && !SwingUtilities.isEventDispatchThread() && (getNumberOfListeners()>0)) {
						RuntimeException e = new RuntimeException("WARNING: a GlobalData event is thrown in a thread different from the event dispatch thread !"); //$NON-NLS-1$
						e.fillInStackTrace();
						e.printStackTrace();
					}
					super.fireEvent(event);
				}
			};
			this.filteredData = new FilteredData(this.data);
		} else {
			this.data = filteredData.getGlobalData();
			this.filteredData = filteredData;
		}
	    
		PlugInContainer[] pluginContainers = Preferences.getPlugins();
		if (restartData == null) {
			restartData = new Object[pluginContainers.length];
		}
		this.plugins = new AbstractPlugIn[pluginContainers.length];
		for (int i = 0; i < plugins.length; i++) {
			if (pluginContainers[i].isActivated()) {
				this.plugins[i] = (AbstractPlugIn) pluginContainers[i].build(this.filteredData, restartData[i]);
				if (this.plugins[i]!=null) {
					this.plugins[i].setContext(this);
				}
			}
			if (pluginContainers[i].getInstanciationException()!=null) {
				// An error occurs during plugin instantiation
				ErrorManager.INSTANCE.log(null, pluginContainers[i].getInstanciationException());
			}
		}
		MainPanel mainPanel = new MainPanel(this.plugins);
		GlobalPanel gPanel = new GlobalPanel(mainPanel);
		getJFrame().setContentPane(gPanel);
		mainPanel.addPropertyChangeListener(MainPanel.SELECTED_PLUGIN_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				mainMenu.updateMenu(getCurrentPlugIn());
			}
		});
		this.data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if ((event instanceof URIChangedEvent) || (event instanceof EverythingChangedEvent) || (event instanceof NeedToBeSavedChangedEvent)) {
					updateWindowTitle();
				}
			}
		});

		mainMenu = new MainMenuBar(this);
		getJFrame().setJMenuBar(mainMenu);
		float ratio = Preferences.INSTANCE.getFontSizeRatio();
		this.getJFrame().setMinimumSize(new Dimension((int)(800*ratio),(int)(400*ratio)));
	    
		// Restore initial state (last opened file, window position, ...)
		restoreMainFramePosition();
		getStateSaver().restoreState(gPanel.getMainPanel(), this.getClass().getCanonicalName());
		for (int i = 0; i < plugins.length; i++) {
			if (plugins[i] != null) {
				plugins[i].restoreState();
			}
		}
		//If the order of the tabs leaves the orginal first tab in the first position,
		//No event is sent to refresh the displayed menu. So, we "manually" invoke the menu refresh. 
		mainMenu.updateMenu(gPanel.getMainPanel().getSelectedPlugIn());

		updateWindowTitle();

		// Display the window.
		getJFrame().setVisible(true);
		
		MessagesBuilder.build(((GlobalPanel)this.getContentPane()).getInfoPanel());
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
		return ((GlobalPanel)getContentPane()).getMainPanel().getSelectedPlugIn();
	}

	private void updateWindowTitle() {
		StringBuilder title = new StringBuilder(APPLICATION_NAME);
		URI uri = data.getURI();
		if (uri!=null) {
			title.append(" - ").append(YapbamPersistenceManager.MANAGER.getAdapter(uri).getService().getDisplayable(uri)); //$NON-NLS-1$
		}
		if (data.somethingHasChanged()) {
			title.append(" *"); //$NON-NLS-1$
		}
		this.getJFrame().setTitle(title.toString());
	}
	
	public void restart() {
		//FIXME MemoryLeak : We would need to remove the obsoletes listeners (from the closing window) ... but don't know how to do that efficiently
		//I think simply disposing the window is not enough (it has listeners on GlobalData). Should be investigated
		this.isRestarting = true;
		final Object[] restartData = new Object[this.plugins.length];
		for (int i = 0; i < restartData.length; i++) {
			restartData[i] = this.plugins[i].getRestartData();
		}
		this.getJFrame().dispatchEvent(new WindowEvent(this.getJFrame(), WindowEvent.WINDOW_CLOSING));
		setLookAndFeel();
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MainFrame(filteredData, restartData);
			}
		});
	}
	
	public static void setLookAndFeel() {
		String lookAndFeelName = Preferences.INSTANCE.getLookAndFeel();
		try {
			// Note : Prior the 0.9.8, the class name was used instead of the generic name.
			// It caused problem when changing java version (ie: Nimbus in java 1.6 was implemented by a class in com.sun.etc and in javax.swing in java 1.7)
			String lookAndFeelClass = Utils.getLFClassFromName(lookAndFeelName);
			if (lookAndFeelClass!=null) {
				UIManager.setLookAndFeel(lookAndFeelClass);
			}
			UIManager.getLookAndFeelDefaults().setDefaultLocale(LocalizationData.getLocale());
		} catch (Exception e) {
		}
		try {
			int iconSize = 16; // This is the default icon size
			Font defaultFont = Preferences.INSTANCE.getDefaultFont();
			if (defaultFont!=null) {
				Font requiredFont = defaultFont.deriveFont(Preferences.INSTANCE.getFontSizeRatio()*defaultFont.getSize());
				FontUtils.setDefaultFont(requiredFont);
				iconSize = 16*requiredFont.getSize()/12;
			}
			IconManager.reset(iconSize);
		} catch (Throwable e) {
			ErrorManager.INSTANCE.log(null, e);
		}
	}

	private void saveState() {
		if (getData().getURI()!=null) {
			getStateSaver().put(LAST_URI, getData().getURI().toString());
		} else {
			getStateSaver().remove(LAST_URI);
		}
		Point location = getJFrame().getLocation();
		getStateSaver().put(FRAME_LOCATION_X, Integer.toString(location.x));
		getStateSaver().put(FRAME_LOCATION_Y, Integer.toString(location.y));
		Dimension size = getJFrame().getSize();
		int h = ((getJFrame().getExtendedState() & Frame.MAXIMIZED_VERT) == 0) ? size.height : -1;
		getStateSaver().put(FRAME_SIZE_HEIGHT, Integer.toString(h));
		int w = ((getJFrame().getExtendedState() & Frame.MAXIMIZED_HORIZ) == 0) ? size.width : -1;
		getStateSaver().put(FRAME_SIZE_WIDTH, Integer.toString(w));
		for (int i = 0; i < getPlugInsNumber(); i++) {
			if (getPlugIn(i)!=null) {
				getPlugIn(i).saveState();
			}
		}
		getStateSaver().saveState(((GlobalPanel)getContentPane()).getMainPanel(), this.getClass().getCanonicalName());
		getStateSaver().save(LAST_VERSION_USED, ApplicationContext.getVersion());
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
			JOptionPane.showMessageDialog(this.getJFrame(), Formatter.format(LocalizationData.get("MainFrame.SaveStateError"), getStateSaver().getFile().getAbsolutePath()), //$NON-NLS-1$
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
		Utils.setSafeBounds(getJFrame(), new Rectangle(new Point(x,y), new Dimension(width, height)));
	}
	
	private void initData(String path) {
		URI lastUri = Preferences.INSTANCE.getStartStateOptions().isRememberFile() && getStateSaver().contains(LAST_URI) ?
			URI.create((String) getStateSaver().get(LAST_URI)) : null;
		URI uri = null;
		if (path!=null) {
			// If a path was provided
			uri = new File(path).toURI();
		} else {
			// Restore the data according to the Preferences
			uri = lastUri;
		}
		final boolean restore = path == null;
		if (uri!=null) {
			final URI finalURI = uri; // Just to be able to use it in the ErrorManager.
			YapbamPersistenceManager.MANAGER.read(getJFrame(), new YapbamDataWrapper(getData()), uri, new PersistenceManager.ErrorProcessor() {
				@Override
				public boolean processError(Throwable e) {
					if (e instanceof UnsupportedSchemeException) {
						// The scheme is no more supported, simply ignore the error
						return true;
					}
					Service service = YapbamPersistenceManager.MANAGER.getAdapter(finalURI).getService();
					String displayedURI = service.getDisplayable(finalURI);
					File file = service.getLocalFile(finalURI);
					if (e instanceof FileNotFoundException) {
						if (file.exists()) {
							// The file exist, but it is read protected
							if ("file".equals(finalURI.getScheme())) { //$NON-NLS-1$
								ErrorManager.INSTANCE.display(getJFrame(), null, Formatter.format(LocalizationData.get("MainFrame.LastNotReadable"),displayedURI)); //$NON-NLS-1$
							} else {
								ErrorManager.INSTANCE.display(getJFrame(), null,  Formatter.format(LocalizationData.get("openDialog.cacheNotReadable"),file)); //$NON-NLS-1$
							}
						} else {
							ErrorManager.INSTANCE.display(getJFrame(), null, Formatter.format(LocalizationData.get("MainFrame.LastNotFound"),displayedURI)); //$NON-NLS-1$
						}
					} else if (e instanceof IOException) {
						if ((e instanceof UnsupportedFileVersionException) || (e instanceof UnsupportedFormatException)) {
							return false; 
						}
						if (restore) {
							ErrorManager.INSTANCE.display(getJFrame(), e, Formatter.format(LocalizationData.get("MainFrame.ReadLastError"),displayedURI)); //$NON-NLS-1$
						} else {
							ErrorManager.INSTANCE.display(getJFrame(), e, LocalizationData.get("MainFrame.ReadError")); //$NON-NLS-1$ //If path is not null
						}
					} else {
						return false; // Let the standard error processor do its job
					}
					return true;
				}
			});
			if (uri.equals(lastUri) && Preferences.INSTANCE.getStartStateOptions().isRememberFilter()) {
				try {
					Filter filter = getStateSaver().restoreFilter(LAST_FILTER_USED, getData());
					if (filter!=null) {
						getFilteredData().getFilter().copy(filter);
					}
				} catch (Exception e) {
					ErrorManager.INSTANCE.log(getJFrame(), e);
					JOptionPane.showMessageDialog(getJFrame(), LocalizationData.get("MainFrame.ReadLastFilterError"), LocalizationData.get("ErrorManager.title"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$	
				}
			}
		}
	}
	
	@Override
	public TransactionSelector getCurrentTransactionSelector() {
		return this.mainMenu.getTransactionSelector();
	}

	@Override
	public Account getSelectedAccount() {
		return this.getCurrentPlugIn().getSelectedAccount();
	}

	@Override
	public Window getApplicationWindow() {
		return getJFrame();
	}
}
