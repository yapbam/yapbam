package net.yapbam.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.MainFrame;
import net.yapbam.gui.persistence.YapbamDataWrapper;
import net.yapbam.gui.persistence.YapbamPersistenceManager;

@SuppressWarnings("serial")
public class SaveAsAction extends AbstractAction {
	private MainFrame frame;

	public SaveAsAction(MainFrame data) {
		super(LocalizationData.get("MainMenu.SaveAs"), IconManager.get(Name.SAVE_AS)); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.SaveAs.ToolTip")); //$NON-NLS-1$
		this.frame = data;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		YapbamPersistenceManager.MANAGER.saveAs(frame, new YapbamDataWrapper(frame.getData()));
	}
}
