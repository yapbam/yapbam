package net.yapbam.gui.dialogs;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.astesana.ajlib.swing.framework.Application;
import net.astesana.dropbox.FileId;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.dropbox.YapbamDropboxFileChooser;

public class DropBoxFileChooserTest extends Application{

	@Override
	protected Container buildMainPanel() {
		JPanel panel = new JPanel();
		final JButton button = new JButton("Test");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileId id = new YapbamDropboxFileChooser().showSaveDialog(button);
				if (id!=null) {
					System.out.println (id.toURI());
				}
			}
		});
		panel.add(button);
		return panel;
	}
	
	public static void main(String[] args) {
		new DropBoxFileChooserTest().launch();
	}

	/* (non-Javadoc)
	 * @see net.astesana.ajlib.swing.framework.Application#onClose(java.awt.event.WindowEvent)
	 */
	@Override
	protected void onClose(WindowEvent event) {
		try {
			Preferences.INSTANCE.save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onClose(event);
	}
}
