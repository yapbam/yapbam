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
import net.astesana.ajlib.swing.dialog.urichooser.URIChooserDialog;
import net.astesana.ajlib.swing.framework.Application;
import net.astesana.cloud.Account;
import net.astesana.cloud.Entry;
import net.astesana.cloud.dropbox.DropboxService;
import net.astesana.cloud.dropbox.swing.DropboxFileChooser;
import net.yapbam.gui.persistence.dropbox.Dropbox;

public class Test extends Application {

	@Override
	protected Container buildMainPanel() {
		JPanel panel = new JPanel();
		DropboxService service = new DropboxService(new File("Data/cache/test"), Dropbox.getAPI()) {
			@Override
			public Entry filterRemote(String path) {
				if (!path.endsWith(".zip")) return null;
				path = path.substring(0, path.length()-".zip".length());
				return super.filterRemote(path);
			}
			@Override
			public URI getURI(Account account, String displayName) {
				return super.getURI(account, displayName+".zip");
			}
		};
		final FileChooser dbChooser = new DropboxFileChooser(service);
		final JButton btn = new JButton("Open");
		panel.add(btn);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AbstractURIChooserPanel fileChooser = new FileChooserPanel();
				URIChooserDialog dialog = new URIChooserDialog(Utils.getOwnerWindow(btn), "Open", new AbstractURIChooserPanel[]{fileChooser,dbChooser});
				dialog.setSaveDialogType(false);
				dialog.setVisible(true);
				System.out.println (dialog.getResult());
			}
		});
		
		final JButton btnSave = new JButton("Save");
		panel.add(btnSave);
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AbstractURIChooserPanel fileChooser = new FileChooserPanel();
				URIChooserDialog dialog = new URIChooserDialog(Utils.getOwnerWindow(btn), "Save", new AbstractURIChooserPanel[]{fileChooser,dbChooser});
				dialog.setSaveDialogType(true);
				dialog.setVisible(true);
				System.out.println (dialog.getResult());
			}
		});
		
		final JButton btnOnly = new JButton("Open Dropbox");
		panel.add(btnOnly);
		btnOnly.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println (dbChooser.showOpenDialog(Utils.getOwnerWindow(btn), "Open Dropbox"));
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
