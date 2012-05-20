package net.yapbam.gui;

import java.io.File;
import java.net.URI;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import net.astesana.ajlib.swing.dialog.FileChooser;
import net.astesana.ajlib.utilities.FileUtils;
import net.yapbam.data.ProgressReport;
import net.yapbam.util.Portable;

class SaveManager {
	static SaveManager MANAGER = new SaveManager();
	private SaveManager() {}

	/** This method gives a last chance to save unsaved data.
	 * @param frame The frame that is currently editing data
	 * @return true if the process can continue (everything is saved or the user wants to discard the changes).
	 */
	boolean verify(MainFrame frame) {
		if (frame.getData().somethingHasChanged()) { // Some modifications has not been saved
			String[] options =new String[]{LocalizationData.get("NotSavedDialog.save"),LocalizationData.get("NotSavedDialog.ignore"),LocalizationData.get("GenericButton.cancel")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			int n = JOptionPane.showOptionDialog(frame,
				    LocalizationData.get("NotSavedDialog.message"), //$NON-NLS-1$
				    LocalizationData.get("NotSavedDialog.title"), //$NON-NLS-1$
				    JOptionPane.YES_NO_CANCEL_OPTION,
				    JOptionPane.WARNING_MESSAGE,
				    null,     //do not use a custom Icon
				    options,  //the titles of buttons
				    options[2]); //default button title
			if (n==2) return false;
			if (n==0) {
				return save(frame);
			}
		}
		return true;
	}

	/** Save the data associated with a main frame. Ask for the file where to save if needed.
	 * @param frame
	 * @return true if the data was saved
	 */
	boolean save(MainFrame frame) {
		URI file = frame.getData().getURI();
		if (file==null) {
			file = getFile(frame);
		}
		if (file==null) return false;
		return saveTo(frame, file);
	}

	/** Save the data associated with a main frame. Ask for the file where to save if needed.
	 * @param frame
	 * @return true if the data was saved
	 */
	boolean saveAs(MainFrame frame) {
		URI file = getFile(frame);
		if (file==null) return false;
		return saveTo(frame, file);
	}

	private URI getFile(MainFrame frame) {
		URI path = frame.getData().getURI();
		String parent = path==null?null:new File(path).getParent();
		JFileChooser chooser = new FileChooser(parent);
		chooser.setLocale(new Locale(LocalizationData.getLocale().getLanguage()));
		File result = chooser.showSaveDialog(frame)==JFileChooser.APPROVE_OPTION?chooser.getSelectedFile():null;
		return result==null?null:result.toURI();
	}

	private boolean saveTo(MainFrame frame, URI uri) {
		try {
			if (uri.getScheme().equals("file") && FileUtils.isIncluded(new File(uri), Portable.getLaunchDirectory())) { //$NON-NLS-1$
				Object[] options = {LocalizationData.get("GenericButton.cancel"),LocalizationData.get("GenericButton.continue")}; //$NON-NLS-1$ //$NON-NLS-2$
				String message = LocalizationData.get("saveDialog.dangerousLocation.message"); //$NON-NLS-1$
				int choice = JOptionPane.showOptionDialog(frame, message,	LocalizationData.get("Generic.warning"),
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]); //$NON-NLS-1$
				if (choice==0) return false;
			}
			frame.getData().save(uri, new ProgressReport() { //TODO
				private int max;

				@Override
				public void setMax(int length) {
					this.max = length;
				}
				
				@Override
				public void reportProgress(int progress) {
					System.out.println (progress+"/"+max);
				}

				@Override
				public boolean isCancelled() {
					return false;
				}
			});
			return true;
		} catch (Throwable e) {
			ErrorManager.INSTANCE.display(frame, e);
			return false;
		}
	}
}
