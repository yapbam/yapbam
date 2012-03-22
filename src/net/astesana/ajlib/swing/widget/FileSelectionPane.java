package net.astesana.ajlib.swing.widget;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import net.astesana.ajlib.swing.framework.Application;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

/** A panel that allows the selection of a file. */
public class FileSelectionPane extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public static final String SELECTED_FILE_PROPERTY = "selectedFile"; //$NON-NLS-1$
	private JTextField textField;
	private File selectedFile;

	private TitledBorder border;
	private JLabel lblFile;

	/**
	 * Create the panel.
	 */
	public FileSelectionPane() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		border = new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null); //$NON-NLS-1$
		setBorder(border);
		
		lblFile = new JLabel(Application.getString("FileSelectionPanel.file")); //$NON-NLS-1$
		GridBagConstraints gbc_lblFile = new GridBagConstraints();
		gbc_lblFile.anchor = GridBagConstraints.WEST;
		gbc_lblFile.insets = new Insets(0, 0, 0, 5);
		gbc_lblFile.gridx = 0;
		gbc_lblFile.gridy = 0;
		add(lblFile, gbc_lblFile);
		
		textField = new JTextField();
		textField.setEditable(false);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.weightx = 1.0;
		gbc_textField.insets = new Insets(0, 0, 0, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		add(textField, gbc_textField);
		textField.setColumns(10);
		
		final JButton btnChange = new JButton(Application.getString("FileSelectionPanel.change")); //$NON-NLS-1$
		btnChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(".")); //$NON-NLS-1$
				File file = chooser.showOpenDialog(btnChange) == JFileChooser.APPROVE_OPTION ? chooser.getSelectedFile() : null;
				if (file != null) {
					setSelectedFile(file);
				}
			}
		});
		GridBagConstraints gbc_btnChange = new GridBagConstraints();
		gbc_btnChange.gridx = 2;
		gbc_btnChange.gridy = 0;
		add(btnChange, gbc_btnChange);
	}

	public void setSelectedFile(File file) {
		if (!file.equals(selectedFile)) {
			File oldValue = this.selectedFile;
			this.selectedFile = file;
			textField.setText(file.getAbsolutePath());
			this.firePropertyChange(SELECTED_FILE_PROPERTY, oldValue, file);
		}
	}
	
	public File getSelectedFile() {
		return this.selectedFile;
	}
	
	public void setTitle(String title) {
		border.setTitle(title);
	}
	
	public void setLabel(String label) {
		lblFile.setText(label);
	}
}
