package net.yapbam.gui.dialogs;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.fathzer.soft.ajlib.swing.dialog.FileChooser;
import com.fathzer.soft.ajlib.swing.widget.TextWidget;

import net.yapbam.gui.HelpManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.util.HtmlUtils;

import javax.swing.JButton;
import javax.swing.JFileChooser;

class LinkEditPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JLabel textLabel;
	private TextWidget textField;
	private JLabel urlLabel;
	private TextWidget urlField;
	private JButton fileButton;
	private JButton testButton;

	/**
	 * Creates the panel.
	 * @param data 
	 */
	LinkEditPanel(String[] data) {
		initialize();
		getUrlField().addPropertyChangeListener(TextWidget.TEXT_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateTestButton();
			}
		});
		if (data!=null) {
			getTextField().setText(data[0]);
			if (data[1]!=null) {
				getUrlField().setText(data[1]);
			}
		}
	}
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbcTextLabel = new GridBagConstraints();
		gbcTextLabel.anchor = GridBagConstraints.WEST;
		gbcTextLabel.insets = new Insets(0, 0, 5, 5);
		gbcTextLabel.gridx = 0;
		gbcTextLabel.gridy = 0;
		add(getTextLabel(), gbcTextLabel);
		GridBagConstraints gbcTextField = new GridBagConstraints();
		gbcTextField.gridwidth = 0;
		gbcTextField.weightx = 1.0;
		gbcTextField.insets = new Insets(0, 0, 5, 5);
		gbcTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcTextField.gridx = 1;
		gbcTextField.gridy = 0;
		add(getTextField(), gbcTextField);
		GridBagConstraints gbcTestButton = new GridBagConstraints();
		gbcTestButton.insets = new Insets(0, 0, 5, 0);
		gbcTestButton.gridx = 3;
		gbcTestButton.gridy = 1;
		add(getTestButton(), gbcTestButton);
		GridBagConstraints gbcUrlLabel = new GridBagConstraints();
		gbcUrlLabel.anchor = GridBagConstraints.WEST;
		gbcUrlLabel.insets = new Insets(0, 0, 5, 5);
		gbcUrlLabel.gridx = 0;
		gbcUrlLabel.gridy = 1;
		add(getUrlLabel(), gbcUrlLabel);
		GridBagConstraints gbcUrlField = new GridBagConstraints();
		gbcUrlField.insets = new Insets(0, 0, 5, 5);
		gbcUrlField.weightx = 1.0;
		gbcUrlField.fill = GridBagConstraints.HORIZONTAL;
		gbcUrlField.gridx = 1;
		gbcUrlField.gridy = 1;
		add(getUrlField(), gbcUrlField);
		GridBagConstraints gbcFileButton = new GridBagConstraints();
		gbcFileButton.insets = new Insets(0, 0, 5, 5);
		gbcFileButton.gridx = 2;
		gbcFileButton.gridy = 1;
		add(getFileButton(), gbcFileButton);
	}

	private JLabel getTextLabel() {
		if (textLabel == null) {
			textLabel = new JLabel(LocalizationData.get("LinkEditor.text")); //$NON-NLS-1$
		}
		return textLabel;
	}
	TextWidget getTextField() {
		if (textField == null) {
			textField = new TextWidget();
			textField.setToolTipText(LocalizationData.get("LinkEditor.text.tooltip")); //$NON-NLS-1$
			textField.setColumns(10);
		}
		return textField;
	}
	private JLabel getUrlLabel() {
		if (urlLabel == null) {
			urlLabel = new JLabel(LocalizationData.get("LinkEditor.url")); //$NON-NLS-1$
		}
		return urlLabel;
	}
	TextWidget getUrlField() {
		if (urlField == null) {
			urlField = new TextWidget(30);
			urlField.setToolTipText(LocalizationData.get("LinkEditor.url.tooltip")); //$NON-NLS-1$
		}
		return urlField;
	}
	private JButton getFileButton() {
		if (fileButton == null) {
			fileButton = new JButton(LocalizationData.get("LinkEditor.file")); //$NON-NLS-1$
			fileButton.setToolTipText(LocalizationData.get("LinkEditor.file.tooltip")); //$NON-NLS-1$
			fileButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					FileChooser chooser = new FileChooser();
					chooser.setSelectedFile(getFileURL());
					if (chooser.showOpenDialog(getFileButton())==JFileChooser.APPROVE_OPTION) {
						File file = chooser.getSelectedFile();
						try {
							getUrlField().setText(file.toURI().toURL().toString());
						} catch (MalformedURLException e1) {
							throw new RuntimeException(e1);
						}
					}
				}
			});
		}
		return fileButton;
	}
	private File getFileURL() {
		if (!getUrlField().getText().isEmpty()) {
			try {
				URL url = new URL(getUrlField().getText());
				if ("file".equalsIgnoreCase(url.getProtocol())) { //$NON-NLS-1$
					return new File(url.getFile());
				}
			} catch (MalformedURLException e) {
				// URL is not valid, do nothing
			}
		}
		return null;
	}
	private JButton getTestButton() {
		if (testButton == null) {
			testButton = new JButton(LocalizationData.get("LinkEditor.test")); //$NON-NLS-1$
			updateTestButton();
			testButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						HelpManager.show(getTestButton(), new URI(getUrlField().getText()));
					} catch (URISyntaxException e1) {
						throw new RuntimeException(e1);
					}
				}
			});
		}
		return testButton;
	}
	boolean isURLOk() {
		return getUrlField().getText().isEmpty() || HtmlUtils.isValidURL(getUrlField().getText());
	}
	public void updateTestButton() {
		boolean ok = !getUrlField().getText().isEmpty() && isURLOk(); 
		getTestButton().setEnabled(ok);
		getTestButton().setToolTipText(ok?LocalizationData.get("LinkEditor.test.tooltip"): //$NON-NLS-1$
			LocalizationData.get("LinkEditor.incorrectURL")); //$NON-NLS-1$
	}
}
