package net.yapbam.data.persistence;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import net.astesana.ajlib.swing.dialog.FileChooser;

public class FilePersistencePlugin extends PersistencePlugin {
	private FileChooser fileChooser;

	@Override
	public String getName() {
		return "Computer";
	}

	@Override
	public String getScheme() {
		return "file";
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public void load() {
		// TODO Auto-generated method stub

	}

	@Override
	public AbstractURIChooserPanel getChooser() {
		@SuppressWarnings("serial")
		AbstractURIChooserPanel dummy = new AbstractURIChooserPanel() {};
		dummy.setLayout(new BorderLayout());
		dummy.add(getFileChooser(), BorderLayout.CENTER);
		return dummy;
	}
	
	private JFileChooser getFileChooser() {
		if (fileChooser == null) {
			fileChooser = new FileChooser();
			fileChooser.setControlButtonsAreShown(false);
			fileChooser.addPropertyChangeListener(FileChooser.SELECTED_FILE_CHANGED_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					System.out.println("File selected: " + fileChooser.getSelectedFile());
				}
			});
			
			fileChooser.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
						System.out.println("File selected: " + fileChooser.getSelectedFile());
					} else if (e.getActionCommand().equals(JFileChooser.CANCEL_SELECTION)) {
						System.out.println("Cancel was called");
					} else {
						System.out.println("Something else: " + e.getActionCommand());
					}
				}
			});
		}
		return fileChooser;
	}


	@Override
	public String getTooltip() {
		return "Select this tab to save/read data to/from a local storage";
	}

	@Override
	public Icon getIcon() {
		return new ImageIcon(getClass().getResource("computer.png"));
	}
}
