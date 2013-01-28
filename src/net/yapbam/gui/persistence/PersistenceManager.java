package net.yapbam.gui.persistence;

import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;

import com.fathzer.soft.jclop.Service;
import com.fathzer.soft.jclop.swing.URIChooser;
import com.fathzer.soft.jclop.swing.URIChooserDialog;

import net.astesana.ajlib.swing.worker.WorkInProgressFrame;
import net.astesana.ajlib.swing.worker.Worker;
import net.astesana.ajlib.utilities.StringUtils;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.BasicHTMLDialog;
import net.yapbam.gui.dialogs.BasicHTMLDialog.Type;
import net.yapbam.gui.persistence.file.FilePersistenceAdapter;
import net.yapbam.gui.persistence.reading.DataReader;
import net.yapbam.gui.persistence.writing.DataWriter;

public class PersistenceManager {
	public static PersistenceManager MANAGER = new PersistenceManager();
	public interface ErrorProcessor {
		/** Process an error.
		 * @param e The error to be processed
		 * @return true if the error was processed by the manager.
		 */
		public abstract boolean processError(Throwable e);
	}
	
	private HashMap<String, PersistenceAdapter> pluginsMap;
	private List<String> pluginSchemes;

	private PersistenceManager() {
		// Load the default persistence plugins
		this.pluginsMap = new HashMap<String, PersistenceAdapter>();
		this.pluginSchemes = new ArrayList<String>();
		
		add(new FilePersistenceAdapter());
//		add(new DropboxPersistencePlugin());
		
		// Load plugins under development
		String testedPlugin = System.getProperty("testedPersistencePlugin.className"); //$NON-NLS-1$
		if (testedPlugin!=null) {
			String[] testedPlugins = StringUtils.split(testedPlugin, ',');
			for (String className : testedPlugins) {
				if (className.length()!=0) {
					try {
						@SuppressWarnings("unchecked")
						Class<? extends PersistenceAdapter> pClass = (Class<? extends PersistenceAdapter>) Class.forName(className);
						add(pClass.newInstance());
					} catch (Exception e) {
						ErrorManager.INSTANCE.display(null, e, MessageFormat.format(LocalizationData.get("persitencePlugin.load.error"), className)); //$NON-NLS-1$
					}
				}
			}
		}
	}
	
	public static WorkInProgressFrame buildWaitDialog(Window owner, Worker<?,?> worker) {
		WorkInProgressFrame waitFrame = new WorkInProgressFrame(owner, LocalizationData.get("Generic.wait.title"), ModalityType.APPLICATION_MODAL, worker); //$NON-NLS-1$
		waitFrame.setSize(400, waitFrame.getSize().height);
		waitFrame.setLocationRelativeTo(owner);
		return waitFrame;
	}
	
	private void add(PersistenceAdapter plugin) {
		// TODO Check there's no duplicated schemes in persistence plugins. 
		pluginsMap.put(plugin.getScheme(), plugin);
		pluginSchemes.add(plugin.getScheme());
	}

	/** This method gives a last chance to save unsaved data.
	 * @param owner The window where the data is displayed (dialogs displayed during the save will have this window as parent).
	 * @param data The data to save
	 * @return true if the process can continue (everything is saved or the user wants to discard the changes).
	 */
	public boolean verify(Window owner, GlobalData data) {
		if (data.somethingHasChanged()) { // Some modifications has not been saved
			String[] options =new String[]{LocalizationData.get("NotSavedDialog.save"),LocalizationData.get("NotSavedDialog.ignore"),LocalizationData.get("GenericButton.cancel")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			int n = JOptionPane.showOptionDialog(owner,
				    LocalizationData.get("NotSavedDialog.message"), //$NON-NLS-1$
				    LocalizationData.get("NotSavedDialog.title"), //$NON-NLS-1$
				    JOptionPane.YES_NO_CANCEL_OPTION,
				    JOptionPane.WARNING_MESSAGE,
				    null,     //do not use a custom Icon
				    options,  //the titles of buttons
				    options[2]); //default button title
			if (n==2) return false;
			if (n==0) {
				return save(owner, data);
			}
		}
		return true;
	}

	/** Save the data associated with a main frame. Ask for the file where to save if needed.
	 * @param owner The window where the data is displayed (dialogs displayed during the save will have this window as parent).
	 * @param data The data to save
	 * @return true if the data was saved
	 */
	public boolean save(Window owner, GlobalData data) {
		URI uri = data.getURI();
		if (uri==null) {
			uri = getURI(owner, data, true);
		}
		if (uri==null) return false;
		return saveTo(owner, data, uri);
	}

	/** Save the data associated with a main frame. Ask for the file where to save if needed.
	 * @param owner The window where the data is displayed (dialogs displayed during the save will have this window as parent).
	 * @param data The data to save
	 * @return true if the data was saved
	 */
	public boolean saveAs(Window owner, GlobalData data) {
		URI uri = getURI(owner, data, true);
		if (uri==null) return false;
		return saveTo(owner, data, uri);
	}

	private URI getURI(Window owner, GlobalData data, boolean save) {
		URIChooserDialog dialog = getURIChooserDialog(owner);
		dialog.setLocale(LocalizationData.getLocale());
		dialog.setSaveDialog(save);
		dialog.setSelectedURI(data.getURI());
		String title = save?LocalizationData.get("MainMenu.Save"):LocalizationData.get("MainMenu.Open"); //$NON-NLS-1$ //$NON-NLS-2$
		dialog.setTitle(title);
		return dialog.showDialog();
	}
	
	private URIChooserDialog getURIChooserDialog(Window owner) {
		URIChooser[] panels = new URIChooser[PersistenceManager.MANAGER.getPluginsNumber()];
		for (int i = 0; i < panels.length; i++) {
			panels[i] = PersistenceManager.MANAGER.getPlugin(i).buildChooser();
		}
		return new URIChooserDialog(owner, "", panels); //$NON-NLS-1$
	}

	private boolean saveTo(Window owner, GlobalData data, URI uri) {
		return new DataWriter(owner, data, uri).save();
	}

	/** Reads the data contained at an URI.
	 * <br>This checks if data has unsaved content. If it has, it displays a message to ask the user if he wants to save
	 * its modifications or not.
	 * @param frame The parent frame for the dialogs
	 * @param data The data to be updated
	 * @param path the path to read or null to choose this path in a dialog.
	 * @param errProcessor An ErrorManager that will be used if the read fails or null to display the standard error message. 
	 */
	public void read(Window frame, GlobalData data, URI path, ErrorProcessor errProcessor) {
		//TODO Why not simply throw IOException and UnsupportedSchemeException ?
		if (verify(frame, data)) {
			if (path==null) {
				path = getURI(frame, data, false);
			}
			if (path != null) {
				try {
					new DataReader(frame, data, path).read();
				} catch (ExecutionException e) {
					Throwable exception = e.getCause();
					boolean notProcessed = true;
					if (errProcessor!=null) {
						notProcessed = !errProcessor.processError(exception);
					}
					if (notProcessed) {
						PersistenceAdapter plugin = PersistenceManager.MANAGER.getPlugin(path);
						Service service = plugin==null?null:plugin.getService();
						String displayedURI = service==null?path.toString():service.getDisplayable(path);
						if (exception instanceof FileNotFoundException) {
							File file = service.getLocalFile(path);
							if (file.exists()) {
								// The file exist, but it is read protected
								if (path.getScheme().equals("file")) {
									ErrorManager.INSTANCE.display(frame, null, MessageFormat.format(LocalizationData.get("openDialog.fileNotReadable"),displayedURI)); //$NON-NLS-1$
								} else {
									ErrorManager.INSTANCE.display(frame, null,  MessageFormat.format(LocalizationData.get("openDialog.cacheNotReadable"),file)); //$NON-NLS-1$
								}
							} else {
								//TODO
								throw new RuntimeException(exception);
							}
						} else if (exception instanceof IOException) {
							ByteArrayOutputStream out = new ByteArrayOutputStream();
							exception.printStackTrace(new PrintStream(out));
							String trace = out.toString();
							trace = trace.replace("\t", "  "); //$NON-NLS-1$ //$NON-NLS-2$
							//Next line is html version ... bad idea as html is not easy to copy/paste in an email
							//trace = "<html>"+trace.replace("\n", "<br>").replace("\t", "&nbsp;&nbsp;")+"</html>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
							BasicHTMLDialog dialog = new BasicHTMLDialog(frame, LocalizationData.get("ErrorManager.title"), MessageFormat.format(LocalizationData //$NON-NLS-1$
									.get("MainMenu.Open.Error.DialogContent"), displayedURI), Type.ERROR); //$NON-NLS-1$
							dialog.setContent(trace);
							dialog.setVisible(true);
						} else if (exception instanceof UnsupportedSchemeException) {
							// The scheme is no more supported, simply ignore the error
						} else {
							ErrorManager.INSTANCE.log(frame, exception);
						}
					}
				}
			}
		}
	}

	public int getPluginsNumber() {
		return this.pluginSchemes.size();
	}

	public PersistenceAdapter getPlugin(int index) {
		return this.pluginsMap.get(this.pluginSchemes.get(index));
	}
	
	public PersistenceAdapter getPlugin(URI uri) {
		return this.pluginsMap.get(uri.getScheme());
	}
}
