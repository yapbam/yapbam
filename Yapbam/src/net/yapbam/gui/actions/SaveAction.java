package net.yapbam.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.MainFrame;
import net.yapbam.gui.persistence.PersistenceManager;

@SuppressWarnings("serial")
public class SaveAction extends AbstractAction {
	private MainFrame frame;

	public SaveAction(MainFrame frame) {
		super(LocalizationData.get("MainMenu.Save"), IconManager.SAVE); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Save.ToolTip")); //$NON-NLS-1$
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		PersistenceManager.MANAGER.save(frame, frame.getData());
	}
}
