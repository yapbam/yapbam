package net.yapbam.gui.persistence;

import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.net.URI;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;

import net.astesana.ajlib.swing.Utils;
import net.astesana.ajlib.swing.dialog.urichooser.AbstractURIChooserPanel;
import net.astesana.ajlib.swing.dialog.urichooser.MultipleURIChooserDialog;
import net.astesana.ajlib.swing.worker.WorkInProgressFrame;
import net.astesana.ajlib.swing.worker.Worker;
import net.astesana.ajlib.utilities.StringUtils;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.persistence.file.FilePersistencePlugin;
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
	
	private HashMap<String, PersistencePlugin> pluginsMap;
	private List<String> pluginSchemes;

	private PersistenceManager() {
		// Load the default persistence plugins
		this.pluginsMap = new HashMap<String, PersistencePlugin>();
		this.pluginSchemes = new ArrayList<String>();
		
		add(new FilePersistencePlugin());
//		add(new DropboxPersistencePlugin());
		
		// Load plugins under development
		String testedPlugin = System.getProperty("testedPersistencePlugin.className"); //$NON-NLS-1$
		if (testedPlugin!=null) {
			String[] testedPlugins = StringUtils.split(testedPlugin, ',');
			for (String className : testedPlugins) {
				if (className.length()!=0) {
					try {
						@SuppressWarnings("unchecked")
						Class<? extends PersistencePlugin> pClass = (Class<? extends PersistencePlugin>) Class.forName(className);
						add(pClass.newInstance());
					} catch (Exception e) {
						ErrorManager.INSTANCE.display(null, e, MessageFormat.format(LocalizationData.get("persitencePlugin.load.error"), className));
					}
				}
			}
		}
	}
	
	public static WorkInProgressFrame buildWaitDialog(Window owner, Worker<?,?> worker) {
		WorkInProgressFrame waitFrame = new WorkInProgressFrame(owner, LocalizationData.get("Generic.wait.title"), ModalityType.APPLICATION_MODAL, worker); //$NON-NLS-1$
		waitFrame.setSize(400, waitFrame.getSize().height);
		Utils.centerWindow(waitFrame, owner);
		return waitFrame;
	}
	
	private void add(PersistencePlugin plugin) {
		// TODO Check there's no duplicated schemes in persistence plugins. 
		for (String scheme : plugin.getSchemes()) {
			pluginsMap.put(scheme, plugin);
		}
		pluginSchemes.addAll(plugin.getSchemes());
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
		MultipleURIChooserDialog dialog = getURIChooserDialog(owner);
		dialog.setLocale(LocalizationData.getLocale());
		dialog.setSaveDialog(save);
		dialog.setSelectedURI(data.getURI());
		String title = save?LocalizationData.get("MainMenu.Save"):LocalizationData.get("MainMenu.Open"); //$NON-NLS-1$ //$NON-NLS-2$
		dialog.setTitle(title);
		return dialog.showDialog();
	}
	
	private MultipleURIChooserDialog getURIChooserDialog(Window owner) {
		AbstractURIChooserPanel[] panels = new AbstractURIChooserPanel[PersistenceManager.MANAGER.getPluginsNumber()];
		for (int i = 0; i < panels.length; i++) {
			panels[i] = PersistenceManager.MANAGER.getPlugin(i).buildChooser();
		}
		return new MultipleURIChooserDialog(owner, "", panels); //$NON-NLS-1$
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
		if (verify(frame, data)) {
			if (path==null) {
				path = getURI(frame, data, false);
			}
			if (path != null) {
				try {
					new DataReader(frame, data, path).read();
				} catch (ExecutionException exception) {
					boolean notProcessed = true;
					if (errProcessor!=null) {
						notProcessed = !errProcessor.processError(exception.getCause());
					}
					if (notProcessed) {
						ErrorManager.INSTANCE.display(frame, exception.getCause(), MessageFormat.format(LocalizationData
								.get("MainMenu.Open.Error.DialogContent"), getPlugin(path).getDisplayableName(path))); //$NON-NLS-1$
					}
				}
			}
		}
	}

	public int getPluginsNumber() {
		return this.pluginSchemes.size();
	}

	public PersistencePlugin getPlugin(int index) {
		return this.pluginsMap.get(this.pluginSchemes.get(index));
	}
	
	public PersistencePlugin getPlugin(URI uri) {
		return this.pluginsMap.get(uri.getScheme());
	}

	public String getDisplayable(URI uri) {
		return getPlugin(uri).getDisplayableName(uri);
	}
}
