package net.yapbam.file;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.astesana.ajlib.swing.dialog.urichooser.AbstractURIChooserPanel;
import net.astesana.ajlib.swing.dialog.urichooser.MultipleURIChooserDialog;
import net.astesana.ajlib.swing.framework.Application;
import net.yapbam.gui.persistence.PersistenceManager;

final class URIChooserTest extends Application {
	@Override
	protected Container buildMainPanel() {
		AbstractURIChooserPanel[] panels = new AbstractURIChooserPanel[PersistenceManager.MANAGER.getPluginsNumber()];
		for (int i = 0; i < panels.length; i++) {
			panels[i] = PersistenceManager.MANAGER.getPlugin(i).buildChooser();
		}
		final MultipleURIChooserDialog uriChooser = new MultipleURIChooserDialog(getJFrame(),"Select an URI",panels);
		JPanel result = new JPanel();
		JButton button = new JButton("Open");
		result.add(button);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				uriChooser.setSaveDialog(false);
				System.out.println (uriChooser.showDialog());
			}
		});
		JButton saveButton = new JButton("Save");
		result.add(saveButton);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				uriChooser.setSaveDialog(true);
				uriChooser.setSelectedURI(new File("c:/users/Jean-Marc/toto").toURI());
				System.out.println (uriChooser.showDialog());
			}
		});
		return result;
	}
	
	public static void main(String[] args) {
		new URIChooserTest().launch();
	}
}