package net.yapbam.file;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.astesana.ajlib.swing.dialog.urichooser.AbstractURIChooserPanel;
import net.astesana.ajlib.swing.dialog.urichooser.URIChooser;
import net.astesana.ajlib.swing.framework.Application;
import net.yapbam.gui.persistence.PersistenceManager;

final class URIChooserTest extends Application {
	@Override
	protected Container buildMainPanel() {
		JPanel result = new JPanel();
		JButton button = new JButton("Open");
		result.add(button);
		AbstractURIChooserPanel[] panels = new AbstractURIChooserPanel[PersistenceManager.MANAGER.getPluginsNumber()];
		for (int i = 0; i < panels.length; i++) {
			panels[i] = PersistenceManager.MANAGER.getPlugin(i).buildChooser();
		}
		final URIChooser uriChooser = new URIChooser(panels);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println (uriChooser.showOpenDialog(getJFrame()));
			}
		});
		JButton saveButton = new JButton("Save");
		result.add(saveButton);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println (uriChooser.showSaveDialog(getJFrame()));
			}
		});
		return result;
	}
	
	public static void main(String[] args) {
		new URIChooserTest().launch();
	}
}