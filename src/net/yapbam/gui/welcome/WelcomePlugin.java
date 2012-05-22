package net.yapbam.gui.welcome;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.LocalizationData;

public class WelcomePlugin extends AbstractPlugIn {

	public WelcomePlugin(FilteredData data, Object restoreData) {
	}

	@Override
	public JMenuItem[] getMenuItem(int part) {
		if (part==AbstractPlugIn.WEB_SITES_PART) {
			return new JMenuItem[] {null, new JMenuItem(new WelcomeAction())};
		} else {
			return super.getMenuItem(part);
		}
	}
	
	@SuppressWarnings("serial")
	private final class WelcomeAction extends AbstractAction {
		WelcomeAction () {
			super(LocalizationData.get("Welcome.menuTitle")); //$NON-NLS-1$
	    putValue(SHORT_DESCRIPTION, LocalizationData.get("Welcome.MenuToolTip")); //$NON-NLS-1$
		}

    @Override
		public void actionPerformed(ActionEvent e) {
			new WelcomeDialog(getContext().getMainFrame()).setVisible(true);
		}
	}
}
