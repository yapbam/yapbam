package net.astesana.cloud.swing;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.astesana.ajlib.swing.Utils;
import net.astesana.ajlib.swing.dialog.urichooser.AbstractURIChooserPanel;
import net.astesana.ajlib.swing.dialog.urichooser.FileChooserPanel;
import net.astesana.ajlib.swing.dialog.urichooser.MultipleURIChooserDialog;
import net.astesana.ajlib.swing.framework.Application;
import net.astesana.cloud.Account;
import net.astesana.cloud.Entry;
import net.astesana.cloud.dropbox.DropboxService;
import net.astesana.cloud.dropbox.swing.DropboxURIChooser;
import net.yapbam.gui.persistence.dropbox.Dropbox;

public class Test extends Application {
	private URI lastSelected = null;

	@Override
	protected Container buildMainPanel() {
		JPanel panel = new JPanel();
		final DropboxService service = new DropboxService(new File("Data/cache/test"), Dropbox.getAPI()) {
			@Override
			public Entry filterRemote(Account account, String path) {
				if (!path.endsWith(".zip")) return null;
				path = path.substring(0, path.length()-".zip".length());
				return super.filterRemote(account, path);
			}
			@Override
			public URI getURI(Account account, String displayName) {
				return super.getURI(account, displayName+".zip");
			}
		};
		final AbstractURIChooserPanel dbChooser = new DropboxURIChooser(service);
		final AbstractURIChooserPanel fileChooser = new FileChooserPanel();
		final JButton btn = new JButton("Open");
		panel.add(btn);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MultipleURIChooserDialog dialog = new MultipleURIChooserDialog(Utils.getOwnerWindow(btn), "Open", new AbstractURIChooserPanel[]{fileChooser,dbChooser});
				dialog.setSelectedURI(lastSelected);
				lastSelected = dialog.showDialog();
				System.out.println (lastSelected);
			}
		});
		
		final JButton btnSave = new JButton("Save");
		panel.add(btnSave);
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MultipleURIChooserDialog dialog = new MultipleURIChooserDialog(Utils.getOwnerWindow(btn), "Save", new AbstractURIChooserPanel[]{fileChooser,dbChooser});
				dialog.setSaveDialog(true);
				dialog.setSelectedURI(lastSelected);
				lastSelected = dialog.showDialog();
				System.out.println (lastSelected);
			}
		});
		
		final JButton btnOnly = new JButton("Open Dropbox");
		panel.add(btnOnly);
		btnOnly.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dbChooser.setSelectedURI(lastSelected);
				lastSelected = (new DropboxURIChooser(service)).showOpenDialog(Utils.getOwnerWindow(btn), "Open Dropbox");
				System.out.println (lastSelected);
			}
		});

		return panel;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Test().launch();
	}
}
