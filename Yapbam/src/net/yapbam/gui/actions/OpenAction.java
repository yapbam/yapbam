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
import net.yapbam.data.persistence.PersistenceManager;
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
		PersistenceManager.MANAGER.open(frame, frame.getData());
	}
}
