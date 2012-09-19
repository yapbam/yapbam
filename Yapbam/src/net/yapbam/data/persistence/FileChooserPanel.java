package net.yapbam.data.persistence;
import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URI;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import net.astesana.ajlib.swing.dialog.FileChooser;

@SuppressWarnings("serial")
public class FileChooserPanel extends JPanel implements AbstractURIChooserPanel {
	private FileChooser fileChooser;

	public FileChooserPanel() {
		setLayout(new BorderLayout(0, 0));
		add(getFileChooser(), BorderLayout.CENTER);
		getFileChooser().addPropertyChangeListener(FileChooser.SELECTED_FILE_CHANGED_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				File oldFile = (File) evt.getOldValue();
				File newFile = (File) evt.getNewValue();
				URI oldURI = oldFile==null?null:oldFile.toURI();
				URI newURI = newFile==null?null:newFile.toURI();
				firePropertyChange(SELECTED_URI_PROPERTY, oldURI, newURI);
			}
		});
	}
	
	private JFileChooser getFileChooser() {
		if (fileChooser == null) {
			fileChooser = new FileChooser();
			fileChooser.setControlButtonsAreShown(false);
		}
		return fileChooser;
	}

	@Override
	public URI getSelectedURI() {
		File selectedFile = getFileChooser().getSelectedFile();
		return selectedFile==null?null:selectedFile.toURI();
	}

	@Override
	public void refresh() {}

	@Override
	public Component getComponent() {
		return this;
	}
}
