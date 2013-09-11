package net.yapbam.gui.dialogs.preferences.backup;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.border.TitledBorder;
import javax.swing.JRadioButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JButton;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;
import net.yapbam.gui.LocalizationData;
import net.yapbam.util.Portable;

import java.text.MessageFormat;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JLabel;

public class DiskPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JRadioButton diskRdnButton;
	private JTextField folderField;
	private JButton selectButton;
	private JLabel lblLocation;
	private JButton btnDefault;

	/**
	 * Create the panel.
	 */
	public DiskPanel() {
		initialize();
	}
	
	private void initialize() {
		setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbc_diskRdnButton = new GridBagConstraints();
		gbc_diskRdnButton.weightx = 1.0;
		gbc_diskRdnButton.gridwidth = 0;
		gbc_diskRdnButton.anchor = GridBagConstraints.WEST;
		gbc_diskRdnButton.insets = new Insets(0, 0, 5, 5);
		gbc_diskRdnButton.gridx = 0;
		gbc_diskRdnButton.gridy = 0;
		add(getDiskRdnButton(), gbc_diskRdnButton);
		GridBagConstraints gbc_lblLocation = new GridBagConstraints();
		gbc_lblLocation.insets = new Insets(0, 10, 0, 5);
		gbc_lblLocation.anchor = GridBagConstraints.EAST;
		gbc_lblLocation.gridx = 0;
		gbc_lblLocation.gridy = 1;
		add(getLblLocation(), gbc_lblLocation);
		GridBagConstraints gbc_folderField = new GridBagConstraints();
		gbc_folderField.anchor = GridBagConstraints.WEST;
		gbc_folderField.weightx = 1.0;
		gbc_folderField.insets = new Insets(0, 0, 0, 5);
		gbc_folderField.fill = GridBagConstraints.HORIZONTAL;
		gbc_folderField.gridx = 1;
		gbc_folderField.gridy = 1;
		add(getFolderField(), gbc_folderField);
		GridBagConstraints gbc_selectButton = new GridBagConstraints();
		gbc_selectButton.insets = new Insets(0, 0, 0, 5);
		gbc_selectButton.anchor = GridBagConstraints.WEST;
		gbc_selectButton.gridx = 2;
		gbc_selectButton.gridy = 1;
		add(getSelectButton(), gbc_selectButton);
		GridBagConstraints gbc_btnDefault = new GridBagConstraints();
		gbc_btnDefault.gridx = 3;
		gbc_btnDefault.gridy = 1;
		add(getBtnDefault(), gbc_btnDefault);
	}

	public JRadioButton getDiskRdnButton() {
		if (diskRdnButton == null) {
			diskRdnButton = new JRadioButton(LocalizationData.get("Backup.preference.disk.disk")); //$NON-NLS-1$
			diskRdnButton.setToolTipText(LocalizationData.get("Backup.preference.disk.disk.tooltip")); //$NON-NLS-1$
			diskRdnButton.setSelected(true);
		}
		return diskRdnButton;
	}
	
	private JTextField getFolderField() {
		if (folderField == null) {
			folderField = new JTextField(10);
			folderField.setEditable(false);
			folderField.setText(Portable.getBackupDirectory().getPath());
		}
		return folderField;
	}
	
	private JButton getSelectButton() {
		if (selectButton == null) {
			selectButton = new JButton(LocalizationData.get("Backup.preference.disk.select")); //$NON-NLS-1$
			selectButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = new JFileChooser();
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					if (fc.showSaveDialog(AbstractDialog.getOwnerWindow(selectButton))==JFileChooser.APPROVE_OPTION) {
						getFolderField().setText(fc.getSelectedFile().getPath());
						getDiskRdnButton().setSelected(true);
					}
				}
			});
			selectButton.setToolTipText(LocalizationData.get("Backup.preference.disk.select.tooltip")); //$NON-NLS-1$
		}
		return selectButton;
	}
	
	private JLabel getLblLocation() {
		if (lblLocation == null) {
			lblLocation = new JLabel(LocalizationData.get("Backup.preference.disk.location")); //$NON-NLS-1$
		}
		return lblLocation;
	}
	
	private JButton getBtnDefault() {
		if (btnDefault == null) {
			btnDefault = new JButton(LocalizationData.get("Backup.preference.disk.default")); //$NON-NLS-1$
			btnDefault.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getFolderField().setText(Portable.getBackupDirectory().getPath());
					getDiskRdnButton().setSelected(true);
				}
			});
			btnDefault.setToolTipText(MessageFormat.format(LocalizationData.get("Backup.preference.disk.default.tooltip"),Portable.getBackupDirectory().getPath())); //$NON-NLS-1$
		}
		return btnDefault;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		getDiskRdnButton().setEnabled(enabled);
		getFolderField().setEnabled(enabled);
		getSelectButton().setEnabled(enabled);
		getLblLocation().setEnabled(enabled);
		getBtnDefault().setEnabled(enabled);
	}
	
	/** Gets the currently selected file.
	 * @return a file or null if the disk selection button is not activated.
	 */
	public File getFile() {
		if (getDiskRdnButton().isSelected()) {
			return new File(getFolderField().getText());
		} else {
			return null;
		}
	}
	
	public void setFile (File file) {
		if (file==null) {
			getDiskRdnButton().setSelected(false);
			file = Portable.getBackupDirectory();
		}
		getFolderField().setText(file.getPath());
	}
}
