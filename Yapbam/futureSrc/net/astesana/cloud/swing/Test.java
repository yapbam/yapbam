package net.astesana.cloud.swing;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.session.WebAuthSession;

import net.astesana.ajlib.swing.Utils;
import net.astesana.ajlib.swing.dialog.urichooser.AbstractURIChooserPanel;
import net.astesana.ajlib.swing.dialog.urichooser.FileChooserPanel;
import net.astesana.ajlib.swing.dialog.urichooser.MultipleURIChooserPanel;
import net.astesana.ajlib.swing.dialog.urichooser.URIChooserDialog;
import net.astesana.ajlib.swing.framework.Application;
import net.astesana.cloud.dropbox.DropboxService;
import net.astesana.cloud.dropbox.swing.DropboxFileChooser;
import net.yapbam.gui.persistence.dropbox.Dropbox;

public class Test extends Application {

	@Override
	protected Container buildMainPanel() {
		JPanel panel = new JPanel();
//		final FileChooser fileChooser = new DropboxFileChooser(new DropboxService(new File("Data/cache/test")));
		final JButton btn = new JButton("Open");
		panel.add(btn);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AbstractURIChooserPanel dbChooser = new DropboxFileChooser(new DropboxService(new File("Data/cache/test"), Dropbox.getAPI()));
				AbstractURIChooserPanel fileChooser = new FileChooserPanel();
				URIChooserDialog dialog = new URIChooserDialog(Utils.getOwnerWindow(btn), "Open", new AbstractURIChooserPanel[]{fileChooser,dbChooser});
				dialog.setSaveDialogType(false);
				dialog.setVisible(true);
				System.out.println (dialog.getResult());
//				fileChooser.showOpenDialog(btn, "Open");
			}
		});
		final JButton btnSave = new JButton("Save");
		panel.add(btnSave);
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AbstractURIChooserPanel dbChooser = new DropboxFileChooser(new DropboxService(new File("Data/cache/test"), Dropbox.getAPI()));
				AbstractURIChooserPanel fileChooser = new FileChooserPanel();
				URIChooserDialog dialog = new URIChooserDialog(Utils.getOwnerWindow(btn), "Save", new AbstractURIChooserPanel[]{fileChooser,dbChooser});
				dialog.setSaveDialogType(true);
				dialog.setVisible(true);
				System.out.println (dialog.getResult());
//				FileChooser fileChooser = new DropboxFileChooser(new DropboxService(new File("Data/cache/test")));
//				fileChooser.showSaveDialog(btnSave, "Save");
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
