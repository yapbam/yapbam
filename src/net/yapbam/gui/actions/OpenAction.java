package net.yapbam.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.MainFrame;
import net.yapbam.gui.persistence.PersistenceManager;

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
		PersistenceManager.MANAGER.read(frame, frame.getData(), null, null);
	}
}
