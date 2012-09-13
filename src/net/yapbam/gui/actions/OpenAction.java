package net.yapbam.gui.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URI;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import net.astesana.ajlib.swing.dialog.FileChooser;
import net.yapbam.data.GlobalData;
import net.yapbam.data.persistence.SaveManager;
import net.yapbam.gui.DataReader;
import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.MainFrame;

@SuppressWarnings("serial")
public class OpenAction extends AbstractAction {
	private MainFrame frame;

	public OpenAction(MainFrame frame) {
		super(LocalizationData.get("MainMenu.Open"), IconManager.OPEN); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Open.ToolTip")); //$NON-NLS-1$
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		GlobalData data = frame.getData();
		if (SaveManager.MANAGER.verify(frame, data)) {
			URI path = data.getURI();
			String parent = path == null ? null : new File(path).getParent();
			JFileChooser chooser = new FileChooser(parent);
			chooser.setLocale(new Locale(LocalizationData.getLocale().getLanguage()));
			final File file = chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION ? chooser.getSelectedFile() : null;
			if (file != null) {
				try {
					DataReader.INSTANCE.readData(frame, data, file.toURI());
				} catch (ExecutionException exception) {
					ErrorManager.INSTANCE.display(frame, exception.getCause(), MessageFormat.format(LocalizationData
							.get("MainMenu.Open.Error.DialogContent"), file)); //$NON-NLS-1$
				}
			}
		}
		}
}
