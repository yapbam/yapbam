package net.yapbam.gui.util;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import net.yapbam.gui.LocalizationData;

/** A file chooser with a confirm dialog when the selected file already exists
 * SaveAs mode.
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
		if ((getDialogType() == SAVE_DIALOG) && (file != null) && file.exists()) {
			int answer = showSaveDisplayQuestion(file);
			if (answer == JOptionPane.NO_OPTION) {
				// User doesn't want to overwrite the file
				return;
			}
		}
		super.approveSelection();
	}

	private int showSaveDisplayQuestion(File file) {
		String message = LocalizationData.get("saveDialog.FileExist.message"); //$NON-NLS-1$
		return JOptionPane.showOptionDialog(this, message, LocalizationData.get("saveDialog.FileExist.title"), //$NON-NLS-1$
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
	}

}
