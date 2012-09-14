package net.yapbam.data.persistence;

import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.astesana.ajlib.swing.Utils;
import net.astesana.dropbox.FileId;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings("serial")
public class URIChooser extends JTabbedPane {
	public enum Type {
    OPEN, SAVE 
	}
	
	public enum Source {
		FILE, DROPBOX
	}
	
	/**
	 * Create the panel.
	 */
	public URIChooser() {
		setTabPlacement(JTabbedPane.LEFT);
		for (int i = 0; i < PersistenceManager.MANAGER.getPluginsNumber(); i++) {
			PersistencePlugin plugin = PersistenceManager.MANAGER.getPlugin(i);
			addTab(plugin.getName(), plugin.getIcon(), plugin.getChooser(), plugin.getTooltip());
		}
		addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						refresh();
					}
				});
			}
		});
	}

	public void setDialogType(Type type) {
//TODO		getFileChooser().setDialogType(type.equals(Type.OPEN)?JFileChooser.OPEN_DIALOG:JFileChooser.SAVE_DIALOG);
	}
	
	public FileId showOpenDialog(Component parent) {
		return showDialog(parent, false);
	}
	
	public FileId showSaveDialog(Component parent) {
		return showDialog(parent, true);
	}
	
	public FileId showDialog(Component parent, boolean save) {
//TODO		this.getFilePanel().setVisible(save);
		Window owner = Utils.getOwnerWindow(parent);
		final URIChooserDialog dialog = new URIChooserDialog(owner, "URI Chooser", this);
		dialog.addWindowListener(new WindowAdapter() {
			/* (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowOpened(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowOpened(WindowEvent e) {
				refresh();
			}
		});
//		this.setCancelAction(new Runnable() {
//			@Override
//			public void run() {
//				dialog.cancel();
//			}
//		});
//		this.setConfirmAction(new Runnable() {
//			@Override
//			public void run() {
//				dialog.confirm();
//			}
//		});
		dialog.setVisible(true);
//		this.setCancelAction(null);
//		this.setConfirmAction(null);
		return dialog.getResult();
	}

  private void refresh() {
		((AbstractURIChooserPanel)getSelectedComponent()).refresh();
	}

	public static void main(String[] args) {
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
		    new URIChooser().showOpenDialog(null);
			}
		});
	}
}
