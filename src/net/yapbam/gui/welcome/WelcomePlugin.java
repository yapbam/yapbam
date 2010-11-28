package net.yapbam.gui.welcome;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.AbstractPlugIn;

public class WelcomePlugin extends AbstractPlugIn {

	public WelcomePlugin(FilteredData data, Object restoreData) {
		this.setPanelTitle("Bienvenue !");
		this.setPanelToolTip("Cet onglet regroupe les informations utiles à la découverte de Yapbam");
	}

	@Override
	public JMenuItem[] getMenuItem(int part) {
		if (part==AbstractPlugIn.WEB_SITES_PART) {
			return new JMenuItem[] {new JMenuItem(new WelcomeAction())};
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
			// TODO Auto-generated method stub
			System.out.println ("Yeah !!! show welcome screen");
		}
	}

	@Override
	public JPanel getPanel() {
		WelcomePanel panel = new WelcomePanel();
		panel.setVisible(false);
		return panel;
	}

	@Override
	public boolean allowMenu(int menuId) {
		if (menuId==AbstractPlugIn.FILTER_MENU) return false;
		return super.allowMenu(menuId);
	}
}
