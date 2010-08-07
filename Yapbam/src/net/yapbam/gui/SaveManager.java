package net.yapbam.gui;

import java.io.File;
import java.net.URI;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import net.yapbam.gui.util.SafeJFileChooser;

class SaveManager {
	static SaveManager MANAGER = new SaveManager();
	private SaveManager() {}

	/** This method gives a last chance to save unsaved data.
	 * @param data The data currently edited
	 * @return true if the process can continue (everything is saved or the user wants to discard the changes).
	 */
	boolean verify(MainFrame frame) {
		if (frame.getData().somethingHasChanged()) { // Some modifications has not been saved
			String[] options =new String[]{LocalizationData.get("NotSavedDialog.save"),LocalizationData.get("NotSavedDialog.ignore"),LocalizationData.get("GenericButton.cancel")};
			int n = JOptionPane.showOptionDialog(frame,
				    LocalizationData.get("NotSavedDialog.message"),
				    LocalizationData.get("NotSavedDialog.title"),
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
		URI file = frame.getData().getPath();
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
		URI path = frame.getData().getPath();
		String parent = path==null?null:new File(path).getParent();
		JFileChooser chooser = new SafeJFileChooser(parent);
		chooser.setLocale(new Locale(LocalizationData.getLocale().getLanguage()));
		chooser.updateUI();
		File result = chooser.showSaveDialog(frame)==JFileChooser.APPROVE_OPTION?chooser.getSelectedFile():null;
		return result.toURI();
	}

	private boolean saveTo(MainFrame frame, URI uri) {
		try {
			frame.getData().save(uri);
			return true;
		} catch (Throwable e) {
			ErrorManager.INSTANCE.display(frame, e);
			return false;
		}
	}
}
