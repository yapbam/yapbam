package net.yapbam.gui.welcome;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.dialogs.AbstractDialog;

public class WelcomePlugin extends AbstractPlugIn {

	public WelcomePlugin(FilteredData data, Object restoreData) {//LOCAL
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
	private static final class WelcomeAction extends AbstractAction {
		WelcomeAction () {
			super("Ecran de bienvenue");
	    putValue(SHORT_DESCRIPTION, "toolTip");
		}

    @Override
		public void actionPerformed(ActionEvent e) {
			new WelcomeDialog(AbstractDialog.getOwnerWindow((Component)e.getSource())).setVisible(true);
		}
	}
}
