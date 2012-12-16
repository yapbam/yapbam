package net.astesana.cloud.swing;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.astesana.ajlib.swing.framework.Application;
import net.astesana.cloud.dropbox.DropboxService;
import net.astesana.cloud.dropbox.swing.DropboxFileChooser;

public class Test extends Application {

	@Override
	protected Container buildMainPanel() {
		JPanel panel = new JPanel();
		final JButton btn = new JButton("Open");
		panel.add(btn);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileChooser fileChooser = new DropboxFileChooser(new DropboxService(new File("Data/cache/test")));
				fileChooser.showOpenDialog(btn, "test");
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
