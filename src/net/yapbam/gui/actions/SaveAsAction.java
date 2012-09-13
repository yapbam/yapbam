package net.yapbam.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.yapbam.data.persistence.PersistenceManager;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.MainFrame;

@SuppressWarnings("serial")
public class SaveAsAction extends AbstractAction {
	private MainFrame frame;

	public SaveAsAction(MainFrame data) {
		super(LocalizationData.get("MainMenu.SaveAs"), IconManager.SAVE_AS); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.SaveAs.ToolTip")); //$NON-NLS-1$
		this.frame = data;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		PersistenceManager.MANAGER.saveAs(frame, frame.getData());
	}
}
