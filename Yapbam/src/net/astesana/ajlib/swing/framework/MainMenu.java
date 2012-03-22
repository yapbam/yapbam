package net.astesana.ajlib.swing.framework;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
class MainMenu extends JMenuBar {
	public MainMenu (Application application) {
		JMenu menu = new JMenu(Application.getString("MainMenu.file")); //$NON-NLS-1$
		menu.setMnemonic(Application.getString("MainMenu.file.mnemonic").charAt(0)); //$NON-NLS-1$
		this.add(menu);
		JMenuItem menuItem = new JMenuItem(new QuitAction(application));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(Application.getString("MainMenu.quit.shortcut").charAt(0), ActionEvent.CTRL_MASK)); //$NON-NLS-1$
		menuItem.setMnemonic(Application.getString("MainMenu.quit.mnemonic").charAt(0)); //$NON-NLS-1$
		menu.add(menuItem);
	}
	
	private static class QuitAction extends AbstractAction {
		private Application app;
		
		QuitAction(Application app) {
			super(Application.getString("MainMenu.quit"), null); //$NON-NLS-1$
			putValue(SHORT_DESCRIPTION, Application.getString("MainMenu.quit.tooltip")); //$NON-NLS-1$
			this.app = app;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			app.quit();
		}
	}
}
