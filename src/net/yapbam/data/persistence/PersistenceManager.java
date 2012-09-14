package net.yapbam.data.persistence;

import java.awt.Window;
import java.io.File;
import java.net.URI;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.astesana.ajlib.swing.dialog.FileChooser;
import net.astesana.ajlib.swing.worker.DefaultWorkInProgressPanel;
import net.astesana.ajlib.swing.worker.WorkInProgressFrame;
import net.astesana.ajlib.swing.worker.WorkInProgressPanel;
import net.astesana.ajlib.swing.worker.Worker;
import net.astesana.ajlib.utilities.FileUtils;
import net.astesana.ajlib.utilities.StringUtils;
import net.yapbam.data.GlobalData;
import net.yapbam.data.ProgressReport;
import net.yapbam.data.xml.Serializer;
import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.util.Portable;

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
						ErrorManager.INSTANCE.display(null, e, "Unable to load the persistence plugin "+className);
					}
				}
			}
		}
	}
	
	private void add(PersistencePlugin plugin) {
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
		URI file = data.getURI();
		if (file==null) {
			file = getFile(owner, data);
		}
		if (file==null) return false;
		return saveTo(owner, data, file);
	}

	/** Save the data associated with a main frame. Ask for the file where to save if needed.
	 * @param owner The window where the data is displayed (dialogs displayed during the save will have this window as parent).
	 * @param data The data to save
	 * @return true if the data was saved
	 */
	public boolean saveAs(Window owner, GlobalData data) {
		URI file = getFile(owner, data);
		if (file==null) return false;
		return saveTo(owner, data, file);
	}

	private URI getFile(Window owner, GlobalData data) {
		URI path = data.getURI();
		String parent = path==null?null:new File(path).getParent();
		JFileChooser chooser = new FileChooser(parent);
		chooser.setLocale(new Locale(LocalizationData.getLocale().getLanguage()));
		File result = chooser.showSaveDialog(owner)==JFileChooser.APPROVE_OPTION?chooser.getSelectedFile():null;
		return result==null?null:result.toURI();
	}

	private boolean saveTo(Window owner, GlobalData data, URI uri) {
		if (uri.getScheme().equals("file") && FileUtils.isIncluded(new File(uri), Portable.getLaunchDirectory())) { //$NON-NLS-1$
			Object[] options = {LocalizationData.get("GenericButton.cancel"),LocalizationData.get("GenericButton.continue")}; //$NON-NLS-1$ //$NON-NLS-2$
			String message = LocalizationData.get("saveDialog.dangerousLocation.message"); //$NON-NLS-1$
			int choice = JOptionPane.showOptionDialog(owner, message,	LocalizationData.get("Generic.warning"), //$NON-NLS-1$
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]); //$NON-NLS-1$
			if (choice==0) return false;
		}
		final Worker<Void, Void> worker = new BackgroundSaver(data, uri);
		WorkInProgressFrame waitFrame = new SaveProgressFrame(owner, worker);
		waitFrame.setVisible(true);
		boolean cancelled = worker.isCancelled();
		if (cancelled) return false;
		try {
			worker.get();
			data.setURI(uri);
			data.setChanged(false);
		} catch (ExecutionException e) {
			ErrorManager.INSTANCE.display(owner, e.getCause());
			return false;
		} catch (InterruptedException e) {
		}
		return true;
	}
	
	private static class SaveProgressFrame extends WorkInProgressFrame {
		// The save task should not be interrupted: Especially if we save over ftp, the file could be partially overwritted, this could lead to data corruption !!!
		// This window gives no chance to the user to cancel the task
		private static final long serialVersionUID = 1L;

		public SaveProgressFrame(Window owner, Worker<?, ?> worker) {
			super(owner, LocalizationData.get("Generic.wait.title"), ModalityType.APPLICATION_MODAL, worker); //$NON-NLS-1$
			this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		}

		/* (non-Javadoc)
		 * @see net.astesana.ajlib.swing.worker.WorkInProgressFrame#buildProgressPanel()
		 */
		@Override
		protected WorkInProgressPanel buildProgressPanel() {
			DefaultWorkInProgressPanel panel = new DefaultWorkInProgressPanel();
			panel.getBtnCancel().setVisible(false);
			return panel;
		}
	}
	
	private static class BackgroundSaver extends Worker<Void, Void> implements ProgressReport {
		private GlobalData data;
		private URI uri;

		BackgroundSaver(GlobalData data, URI uri) {
			this.data = data;
			this.uri = uri;
			setPhase(MessageFormat.format(LocalizationData.get("Generic.wait.writingTo"), uri.getPath()), -1); //$NON-NLS-1$
		}

		@Override
		public void setMax(int length) {
			super.setPhase(getPhase(), length);
		}

		@Override
		protected Void doInBackground() throws Exception {
			Serializer.write(data, uri, this);
			return null;
		}
	}

	/** Reads the data contains at an URI.
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
				String parent = data.getURI() == null ? null : new File(data.getURI()).getParent();
				JFileChooser chooser = new FileChooser(parent);
				chooser.setLocale(new Locale(LocalizationData.getLocale().getLanguage()));
				path = chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION ? chooser.getSelectedFile().toURI() : null;
			}
			if (path != null) {
				try {
					DataReader.INSTANCE.readData(frame, data, path);
				} catch (ExecutionException exception) {
					boolean notProcessed = true;
					if (errProcessor!=null) {
						notProcessed = !errProcessor.processError(exception.getCause());
					}
					if (notProcessed) {
						ErrorManager.INSTANCE.display(frame, exception.getCause(), MessageFormat.format(LocalizationData
								.get("MainMenu.Open.Error.DialogContent"), new File(path))); //$NON-NLS-1$
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
}
