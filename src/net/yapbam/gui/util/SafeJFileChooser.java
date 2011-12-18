package net.yapbam.gui.util;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.util.FileUtils;

/** A file chooser with a confirm dialog when the selected file already exists and message dialogs when file doesn't exists in open mode or exist in save mode.<br>
 * This chooser is compatible with shortcuts (symbolic link). It returns the target file when a shortcut is selected.
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
		// Refuse:
		// The broken links
		// The non existing files in OPEN_DIALOG mode
		// Ask what to do if the file exists and we are in SAVE_DIALOG mode 
		File file = super.getSelectedFile();
		if (file!=null) {
			try {
				if (getDialogType() == OPEN_DIALOG) {
					if (!file.exists()) {
						JOptionPane.showMessageDialog(this, LocalizationData.get("openDialog.fileDoesntExist"), LocalizationData.get("Generic.warning"), JOptionPane.ERROR_MESSAGE);  //$NON-NLS-1$//$NON-NLS-2$
						return;
					} else {
						File canonical = FileUtils.getCanonical(file);
						if (!canonical.exists()) {
							JOptionPane.showMessageDialog(this, LocalizationData.get("openDialog.targetDoesntExist"), LocalizationData.get("Generic.warning"), JOptionPane.ERROR_MESSAGE);  //$NON-NLS-1$//$NON-NLS-2$
							return;
						}
					}
				} else if ((getDialogType() == SAVE_DIALOG) && file.exists()) {
					File canonical = FileUtils.getCanonical(file);
					if (canonical.exists()) {
						int answer = showSaveDisplayQuestion(canonical);
						if (answer == JOptionPane.NO_OPTION) {
							// User doesn't want to overwrite the file
							return;
						}
					}
				}
			} catch (IOException e) {
				ErrorManager.INSTANCE.log(AbstractDialog.getOwnerWindow(this), e);
				return;
			}
		}
		super.approveSelection();
	}

	private int showSaveDisplayQuestion(File file) {
		String message = LocalizationData.get("saveDialog.FileExist.message"); //$NON-NLS-1$
		return JOptionPane.showOptionDialog(this, message, LocalizationData.get("Generic.warning"), //$NON-NLS-1$
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
	}

	@Override
	public File getSelectedFile() {
		// Replace the selected file by its target if it is a link
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
