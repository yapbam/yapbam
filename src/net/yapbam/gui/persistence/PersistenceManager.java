package net.yapbam.gui.persistence;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.worker.WorkInProgressFrame;
import com.fathzer.soft.ajlib.swing.worker.WorkInProgressPanel;
import com.fathzer.soft.ajlib.swing.worker.Worker;
import com.fathzer.soft.jclop.Service;
import com.fathzer.soft.jclop.swing.URIChooser;
import com.fathzer.soft.jclop.swing.URIChooserDialog;

import net.yapbam.data.xml.UnsupportedFileVersionException;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.BasicHTMLDialog;
import net.yapbam.gui.dialogs.BasicHTMLDialog.Type;
import net.yapbam.gui.persistence.reading.DataReader;
import net.yapbam.gui.persistence.writing.DataWriter;

public abstract class PersistenceManager {
	public interface ErrorProcessor {
		/** Process an error.
		 * @param e The error to be processed
		 * @return true if the error was processed by the manager.
		 */
		public abstract boolean processError(Throwable e);
	}
	
	private Map<String, PersistenceAdapter> adaptersMap;
	private List<String> schemes;

	public PersistenceManager() {
		// Load the default persistence adapters
		this.adaptersMap = new HashMap<String, PersistenceAdapter>();
		this.schemes = new ArrayList<String>();
	}
	
	/** Adds an adapter.
	 * @param adapter The adapter to add.
	 * @throws IllegalArgumentException if the scheme of the adapter's service is already know by this adapter
	 * @see PersistenceAdapter#getService()
	 */
	public void add(PersistenceAdapter adapter) {
		String scheme = adapter.getService().getScheme();
		if (adaptersMap.containsKey(scheme)) {
			throw new IllegalArgumentException(Formatter.format("Can't have two adapters for {0} scheme",scheme)); //$NON-NLS-1$
		}
		adaptersMap.put(scheme, adapter);
		schemes.add(scheme);
	}
	
	public WorkInProgressFrame buildWaitDialog(Window owner, Worker<?,?> worker) {
		WorkInProgressFrame waitFrame = new WorkInProgressFrame(owner, LocalizationData.get("Generic.wait.title"), ModalityType.APPLICATION_MODAL, worker); //$NON-NLS-1$
		WorkInProgressPanel panel = waitFrame.getWorkInProgressPanel();
		Dimension oldPref = panel.getPreferredSize();
		panel.setPreferredSize(new java.awt.Dimension(Math.max(400,oldPref.width), oldPref.height));
		waitFrame.pack();
		waitFrame.setLocationRelativeTo(owner);
		return waitFrame;
	}

	/** This method gives a last chance to save unsaved data.
	 * @param owner The window where the data is displayed (dialogs displayed during the save will have this window as parent).
	 * @param data The data to save
	 * @return true if the process can continue (everything is saved or the user wants to discard the changes).
	 */
	public boolean verify(Window owner, DataWrapper<?> data) {
		if (data.isChanged() && !data.isEmpty()) {
			// Some modifications has not been saved
			String[] options =new String[]{LocalizationData.get("NotSavedDialog.save"),LocalizationData.get("NotSavedDialog.ignore"),LocalizationData.get("GenericButton.cancel")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			int n = JOptionPane.showOptionDialog(owner,
				    LocalizationData.get("NotSavedDialog.message"), //$NON-NLS-1$
				    LocalizationData.get("NotSavedDialog.title"), //$NON-NLS-1$
				    JOptionPane.YES_NO_CANCEL_OPTION,
				    JOptionPane.WARNING_MESSAGE,
				    null,     //do not use a custom Icon
				    options,  //the titles of buttons
				    options[2]); //default button title
			if (n==2) {
				return false;
			} else if (n==0) {
				return save(owner, data);
			}
		}
		return true;
	}
	
	/** Saves the data associated with a main frame. Ask for the file where to save if needed.
	 * @param owner The window where the data is displayed (dialogs displayed during the save will have this window as parent).
	 * @param data The data to save
	 * @return true if the data was saved
	 */
	public boolean save(Window owner, DataWrapper<?> data) {
		URI uri = data.getURI();
		if (uri==null) {
			uri = getURI(owner, uri, true);
		}
		if (uri==null) {
			return false;
		}
		return saveTo(owner, data, uri);
	}

	/** Saves the data associated with a main frame. Ask for the file where to save if needed.
	 * @param owner The window where the data is displayed (dialogs displayed during the save will have this window as parent).
	 * @param data The data to save
	 * @return true if the data was saved
	 */
	public boolean saveAs(Window owner, DataWrapper<?> data) {
		URI uri = getURI(owner, data.getURI(), true);
		if (uri==null) {
			return false;
		}
		return saveTo(owner, data, uri);
	}

	private URI getURI(Window owner, URI uri, boolean save) {
		URIChooserDialog dialog = getChooserDialog(owner);
		dialog.setSaveDialog(save);
		if ((uri!=null) && dialog.hasScheme(uri.getScheme())) {
			dialog.setSelectedURI(uri);
		}
		String title = save?LocalizationData.get("MainMenu.Save"):LocalizationData.get("MainMenu.Open"); //$NON-NLS-1$ //$NON-NLS-2$
		dialog.setTitle(title);
		dialog.pack();
		return dialog.showDialog();
	}

	/** Gets a dialog to open/save file using all the available adapters
	 * @param owner The dialog's owner
	 * @return a dialog
	 */
	public URIChooserDialog getChooserDialog(Window owner) {
		List<URIChooser> panels = new ArrayList<URIChooser>(getAdaptersNumber());
		for (int i = 0; i < getAdaptersNumber(); i++) {
			URIChooser chooser = getAdapter(i).buildChooser();
			if (chooser!=null) {
				panels.add(chooser);
			}
		}
		URIChooserDialog dialog = new URIChooserDialog(owner, "", panels.toArray(new URIChooser[panels.size()])); //$NON-NLS-1$
		dialog.setLocale(LocalizationData.getLocale());
		return dialog;
	}
	
	private boolean saveTo(Window owner, DataWrapper<?> data, URI uri) {
		return new DataWriter(this, owner, data, uri).save();
	}

	/** Reads the data contained at an URI.
	 * <br>This checks if data has unsaved content. If it has, it displays a message to ask the user if he wants to save
	 * its modifications or not.
	 * @param frame The parent frame for the dialogs
	 * @param data The data to be updated
	 * @param path the path to read or null to choose this path in a dialog.
	 * @param errProcessor An ErrorManager that will be used if the read fails or null to display the standard error message. 
	 */
	public boolean read(Window frame, DataWrapper<?> data, URI path, ErrorProcessor errProcessor) {
		if (verify(frame, data)) {
			if (path==null) {
				path = getURI(frame, data.getURI(), false);
			}
			if (path != null) {
				try {
					return new DataReader(this, frame, data, path).read();
				} catch (ExecutionException e) {
					Throwable exception = e.getCause();
					boolean notProcessed = true;
					if (errProcessor!=null) {
						notProcessed = !errProcessor.processError(exception);
					}
					if (notProcessed) {
						PersistenceAdapter adapter = getAdapter(path);
						Service service = adapter==null?null:adapter.getService();
						String displayedURI = service==null?path.toString():service.getDisplayable(path);
						if (exception instanceof FileNotFoundException) {
							File file = service.getLocalFile(path);
							if (file.exists()) {
								String message;
								// The file exist, but it is read protected
								if ("file".equals(path.getScheme())) { //$NON-NLS-1$
									message = Formatter.format(LocalizationData.get("openDialog.fileNotReadable"),displayedURI); //$NON-NLS-1$
								} else {
									message = Formatter.format(LocalizationData.get("openDialog.cacheNotReadable"),file); //$NON-NLS-1$
								}
								JOptionPane.showMessageDialog(frame, message, LocalizationData.get("ErrorManager.title"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
							} else {
								throw new RuntimeException(exception);
							}
						} else if (exception instanceof UnsupportedFileVersionException) {
							String message = Formatter.format(LocalizationData.get("MainMenu.Open.Error.DialogContent.needUpdate"), //$NON-NLS-1$
									adapter.getService().getDisplayable(path));
							JOptionPane.showMessageDialog(frame, message, LocalizationData.get("ErrorManager.title"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
						} else if (exception instanceof IOException) {
							ByteArrayOutputStream out = new ByteArrayOutputStream();
							exception.printStackTrace(new PrintStream(out));
							String trace = out.toString();
							trace = trace.replace("\t", "  "); //$NON-NLS-1$ //$NON-NLS-2$
							//Next line is html version ... bad idea as html is not easy to copy/paste in an email
							//trace = "<html>"+trace.replace("\n", "<br>").replace("\t", "&nbsp;&nbsp;")+"</html>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
							BasicHTMLDialog dialog = new BasicHTMLDialog(frame, LocalizationData.get("ErrorManager.title"), Formatter.format(LocalizationData //$NON-NLS-1$
									.get("MainMenu.Open.Error.DialogContent"), displayedURI), Type.ERROR); //$NON-NLS-1$
							dialog.setContent(trace);
							dialog.setVisible(true);
						} else if (exception instanceof UnsupportedSchemeException) {
							// The scheme is no more supported, simply ignore the error
							//TODO Seems a strange idea, probably it should be better to inform the user
						} else {
							throw new RuntimeException(exception);
						}
					}
				}
			}
		}
		return false;
	}

	/** Gets the number of available adapters.
	 * @return an int
	 */
	public int getAdaptersNumber() {
		return this.schemes.size();
	}

	/** Gets an available adapter.
	 * @param index the adpater's index
	 * @return a PersistenceAdapter
	 */
	public PersistenceAdapter getAdapter(int index) {
		return this.adaptersMap.get(this.schemes.get(index));
	}
	
	public PersistenceAdapter getAdapter(URI uri) {
		return this.adaptersMap.get(uri.getScheme());
	}
}
