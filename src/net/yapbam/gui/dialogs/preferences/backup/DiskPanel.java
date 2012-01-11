package net.yapbam.gui.dialogs.preferences.backup;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.border.TitledBorder;
import javax.swing.JRadioButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ButtonGroup;

import net.yapbam.gui.LocalizationData;
import net.yapbam.util.Portable;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.text.MessageFormat;

public class DiskPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JRadioButton diskRdnButton;
	private JRadioButton defaultRdnButton;
	private JRadioButton customRdnButton;
	private JTextField folderField;
	private JButton selectButton;
	private final ButtonGroup buttonGroup = new ButtonGroup();

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
		GridBagConstraints gbc_defaultRdnButton = new GridBagConstraints();
		gbc_defaultRdnButton.gridwidth = 0;
		gbc_defaultRdnButton.anchor = GridBagConstraints.WEST;
		gbc_defaultRdnButton.insets = new Insets(0, 10, 5, 5);
		gbc_defaultRdnButton.gridx = 0;
		gbc_defaultRdnButton.gridy = 1;
		add(getDefaultRdnButton(), gbc_defaultRdnButton);
		GridBagConstraints gbc_customRdnButton = new GridBagConstraints();
		gbc_customRdnButton.gridwidth = 0;
		gbc_customRdnButton.anchor = GridBagConstraints.WEST;
		gbc_customRdnButton.insets = new Insets(0, 10, 5, 5);
		gbc_customRdnButton.gridx = 0;
		gbc_customRdnButton.gridy = 2;
		add(getCustomRdnButton(), gbc_customRdnButton);
		GridBagConstraints gbc_folderField = new GridBagConstraints();
		gbc_folderField.anchor = GridBagConstraints.WEST;
		gbc_folderField.weightx = 1.0;
		gbc_folderField.insets = new Insets(0, 15, 0, 5);
		gbc_folderField.fill = GridBagConstraints.HORIZONTAL;
		gbc_folderField.gridx = 0;
		gbc_folderField.gridy = 3;
		add(getFolderField(), gbc_folderField);
		GridBagConstraints gbc_selectButton = new GridBagConstraints();
		gbc_selectButton.anchor = GridBagConstraints.WEST;
		gbc_selectButton.gridx = 1;
		gbc_selectButton.gridy = 3;
		add(getSelectButton(), gbc_selectButton);
	}

	public JRadioButton getDiskRdnButton() {
		if (diskRdnButton == null) {
			diskRdnButton = new JRadioButton(LocalizationData.get("Backup.preference.disk.disk")); //$NON-NLS-1$
			diskRdnButton.setToolTipText(LocalizationData.get("Backup.preference.disk.disk.tooltip")); //$NON-NLS-1$
			diskRdnButton.setSelected(true);
			diskRdnButton.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					boolean selected = e.getStateChange()==ItemEvent.SELECTED;
					getCustomRdnButton().setVisible(selected);
					getDefaultRdnButton().setVisible(selected);
					getFolderField().setVisible(selected);
					getSelectButton().setVisible(selected);
				}
			});
		}
		return diskRdnButton;
	}
	
	private JRadioButton getDefaultRdnButton() {
		if (defaultRdnButton == null) {
			String text = MessageFormat.format(LocalizationData.get("Backup.preference.disk.default"), Portable.getBackupDirectory()); //$NON-NLS-1$
			defaultRdnButton = new JRadioButton(text);
			defaultRdnButton.setToolTipText(LocalizationData.get("Backup.preference.disk.default.tooltip")); //$NON-NLS-1$
			defaultRdnButton.setSelected(true);
			buttonGroup.add(defaultRdnButton);
		}
		return defaultRdnButton;
	}
	
	private JRadioButton getCustomRdnButton() {
		if (customRdnButton == null) {
			customRdnButton = new JRadioButton(LocalizationData.get("Backup.preference.disk.custom")); //$NON-NLS-1$
			customRdnButton.setToolTipText(LocalizationData.get("Backup.preference.disk.custom.tooltip")); //$NON-NLS-1$
			buttonGroup.add(customRdnButton);
		}
		return customRdnButton;
	}
	
	private JTextField getFolderField() {
		if (folderField == null) {
			folderField = new JTextField();
			folderField.setEditable(false);
			folderField.setColumns(10);
		}
		return folderField;
	}
	
	private JButton getSelectButton() {
		if (selectButton == null) {
			selectButton = new JButton(LocalizationData.get("Backup.preference.disk.select")); //$NON-NLS-1$
			selectButton.setToolTipText(LocalizationData.get("Backup.preference.disk.select.tooltip")); //$NON-NLS-1$
		}
		return selectButton;
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		getDiskRdnButton().setEnabled(enabled);
		getCustomRdnButton().setEnabled(enabled);
		getDefaultRdnButton().setEnabled(enabled);
		getFolderField().setEnabled(enabled);
		getSelectButton().setEnabled(enabled);
	}
}
