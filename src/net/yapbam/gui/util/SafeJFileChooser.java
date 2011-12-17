package net.yapbam.gui.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.util.FileUtils;

/** A file chooser with a confirm dialog when the selected file already exists
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public class SafeJFileChooser extends JFileChooser {
	private static final long serialVersionUID = 1L;

	public SafeJFileChooser(String title) {
		super(title);
	}

	@Override
	public void approveSelection() {
		File file = getSelectedFile();
System.out.println (file);
		if (file!=null) {
			try {
				FileUtils.getCanonical(file);
			} catch (FileNotFoundException e) {
				// The file is a broken link ?
				JOptionPane.showOptionDialog(this, "The fucking target file doesn't exists", "Shit !", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
				return;
			} catch (IOException e) {
				ErrorManager.INSTANCE.log(AbstractDialog.getOwnerWindow(this), e);
			}
			if ((getDialogType() == SAVE_DIALOG) && file.exists()) {
				int answer = showSaveDisplayQuestion(file);
				if (answer == JOptionPane.NO_OPTION) {
					// User doesn't want to overwrite the file
					return;
				}
			}
		}
		super.approveSelection();
	}

	private int showSaveDisplayQuestion(File file) {
		String message = LocalizationData.get("saveDialog.FileExist.message"); //$NON-NLS-1$
		return JOptionPane.showOptionDialog(this, message, LocalizationData.get("saveDialog.FileExist.title"), //$NON-NLS-1$
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
	}

	@Override
	public File getSelectedFile() {
		File selectedFile = super.getSelectedFile();
		if ((selectedFile!=null) && selectedFile.exists()) {
			try {
				selectedFile = FileUtils.getCanonical(selectedFile);
			} catch (IOException e) {
				ErrorManager.INSTANCE.log(AbstractDialog.getOwnerWindow(this), e);
				return null;
			}
		}
		return selectedFile;
	}

	@Override
	public void setLocale(Locale l) {
		super.setLocale(l);
		updateUI();
	}
}
