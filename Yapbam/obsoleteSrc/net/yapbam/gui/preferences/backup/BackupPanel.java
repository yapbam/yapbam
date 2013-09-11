package net.yapbam.gui.dialogs.preferences.backup;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;
import com.fathzer.soft.ajlib.swing.widget.IntegerWidget;
import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.HelpManager;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.PreferencePanel;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.dialogs.preferences.BackupOptions;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JCheckBox;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class BackupPanel extends PreferencePanel {
	private static final long serialVersionUID = 1L;
	
	private JCheckBox chckbxBackup;
	private JLabel helpChckbxBackup;
	private IntegerWidget maxDiskField;
	private FTPPanel ftpPanel;
	private DiskPanel diskPanel;
	private JLabel lblTailleMaximumReserve;
	private JLabel lblSizeUnit;
	private JCheckBox chckbxCompress;
	private JPanel panel;

	/**
	 * Create the panel.
	 */
	public BackupPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		
		GridBagConstraints gbc_chckbxBackup = new GridBagConstraints();
		gbc_chckbxBackup.anchor = GridBagConstraints.WEST;
		gbc_chckbxBackup.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxBackup.gridx = 0;
		gbc_chckbxBackup.gridy = 0;
		add(getChckbxBackup(), gbc_chckbxBackup);
		
		GridBagConstraints gbc_diskPanel = new GridBagConstraints();
		gbc_diskPanel.gridwidth = 0;
		gbc_diskPanel.insets = new Insets(0, 0, 5, 0);
		gbc_diskPanel.fill = GridBagConstraints.BOTH;
		gbc_diskPanel.gridx = 0;
		gbc_diskPanel.gridy = 1;
		add(getDiskPanel(), gbc_diskPanel);
		
		GridBagConstraints gbc_ftpPanel = new GridBagConstraints();
		gbc_ftpPanel.gridwidth = 0;
		gbc_ftpPanel.insets = new Insets(0, 0, 5, 0);
		gbc_ftpPanel.fill = GridBagConstraints.BOTH;
		gbc_ftpPanel.gridx = 0;
		gbc_ftpPanel.gridy = 2;
		add(getFtpPanel(), gbc_ftpPanel);
		
		JPanel panelSize = new JPanel();
		GridBagConstraints gbc_panelSize = new GridBagConstraints();
		gbc_panelSize.insets = new Insets(0, 0, 5, 0);
		gbc_panelSize.anchor = GridBagConstraints.NORTH;
		gbc_panelSize.gridwidth = 0;
		gbc_panelSize.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelSize.gridx = 0;
		gbc_panelSize.gridy = 3;
		add(panelSize, gbc_panelSize);
		GridBagLayout gbl_panelSize = new GridBagLayout();
		panelSize.setLayout(gbl_panelSize);
		
		lblTailleMaximumReserve = new JLabel(LocalizationData.get("Backup.preference.maxSize")); //$NON-NLS-1$
		GridBagConstraints gbc_lblTailleMaximumReserve = new GridBagConstraints();
		gbc_lblTailleMaximumReserve.insets = new Insets(0, 5, 0, 0);
		gbc_lblTailleMaximumReserve.anchor = GridBagConstraints.WEST;
		gbc_lblTailleMaximumReserve.gridx = 0;
		gbc_lblTailleMaximumReserve.gridy = 0;
		panelSize.add(lblTailleMaximumReserve, gbc_lblTailleMaximumReserve);
		
		GridBagConstraints gbc_maxDiskField = new GridBagConstraints();
		gbc_maxDiskField.insets = new Insets(0, 5, 0, 5);
		gbc_maxDiskField.anchor = GridBagConstraints.WEST;
		gbc_maxDiskField.gridx = 1;
		gbc_maxDiskField.gridy = 0;
		panelSize.add(getMaxDiskField(), gbc_maxDiskField);
		
		lblSizeUnit = new JLabel(LocalizationData.get("Backup.preference.maxSize.unit")); //$NON-NLS-1$
		GridBagConstraints gbc_lblSizeUnit = new GridBagConstraints();
		gbc_lblSizeUnit.anchor = GridBagConstraints.WEST;
		gbc_lblSizeUnit.weightx = 1.0;
		gbc_lblSizeUnit.gridx = 2;
		gbc_lblSizeUnit.gridy = 0;
		panelSize.add(lblSizeUnit, gbc_lblSizeUnit);
		
		ButtonGroup group = new ButtonGroup();
		group.add(getFtpPanel().getFtpRdnButton());
		group.add(getDiskPanel().getDiskRdnButton());
		GridBagConstraints gbc_helpChckbxBackup = new GridBagConstraints();
		gbc_helpChckbxBackup.anchor = GridBagConstraints.WEST;
		gbc_helpChckbxBackup.weightx = 1.0;
		gbc_helpChckbxBackup.insets = new Insets(0, 0, 5, 0);
		gbc_helpChckbxBackup.gridx = 1;
		gbc_helpChckbxBackup.gridy = 0;
		add(getHelpChckbxBackup(), gbc_helpChckbxBackup);
		GridBagConstraints gbc_chckbxCompress = new GridBagConstraints();
		gbc_chckbxCompress.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxCompress.anchor = GridBagConstraints.WEST;
		gbc_chckbxCompress.gridwidth = 0;
		gbc_chckbxCompress.gridx = 0;
		gbc_chckbxCompress.gridy = 4;
		add(getChckbxCompress(), gbc_chckbxCompress);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.weighty = 1.0;
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 5;
		add(getPanel(), gbc_panel);
		init();
	}

	private void init() {
		BackupOptions options = Preferences.INSTANCE.getBackupOptions();
		getChckbxBackup().setSelected(options.isEnabled());
		getChckbxCompress().setSelected(options.isCompressed());
		getMaxDiskField().setValue(options.getSpaceLimit());
		if ("file".equalsIgnoreCase(options.getUri().getScheme())) { //$NON-NLS-1$
			getDiskPanel().setFile(new File(options.getUri()));
		} else {
			getFtpPanel().setURI(options.getUri());
		}
	}

	public IntegerWidget getMaxDiskField() {
		if (maxDiskField==null) {
			maxDiskField = new IntegerWidget(BigInteger.ONE, null);
			maxDiskField.setLocale(LocalizationData.getLocale());
			maxDiskField.setToolTipText(LocalizationData.get("Backup.preference.maxSize.tooltip")); //$NON-NLS-1$
			maxDiskField.setColumns(5);
		}
		return maxDiskField;
	}

	public JCheckBox getChckbxBackup() {
		if (chckbxBackup==null) {
			chckbxBackup = new JCheckBox(LocalizationData.get("Backup.preference.activate")); //$NON-NLS-1$
			chckbxBackup.setSelected(true);
			chckbxBackup.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					boolean enabled = e.getStateChange()==ItemEvent.SELECTED;
					getDiskPanel().setEnabled(enabled);
					getFtpPanel().setEnabled(enabled);
					getMaxDiskField().setEnabled(enabled);
					getMaxDiskField().setEditable(enabled);
					getChckbxCompress().setEnabled(enabled);
					lblTailleMaximumReserve.setEnabled(enabled);
					lblSizeUnit.setEnabled(enabled);
				}
			});
			chckbxBackup.setToolTipText(LocalizationData.get("Backup.preference.activate.tooltip")); //$NON-NLS-1$
		}
		return chckbxBackup;
	}

	private JLabel getHelpChckbxBackup() {
		if (helpChckbxBackup == null) {
			helpChckbxBackup = new JLabel();
			helpChckbxBackup.setToolTipText(LocalizationData.get("Backup.preference.helpButton.toolTip")); //$NON-NLS-1$
			helpChckbxBackup.setIcon(IconManager.get(Name.HELP));
			helpChckbxBackup.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					HelpManager.show(e.getComponent(), HelpManager.BACKUP);
				}
			});
		}
		return helpChckbxBackup;
	}

	private FTPPanel getFtpPanel() {
		if (ftpPanel == null) {
			ftpPanel = new FTPPanel();
			ftpPanel.addPropertyChangeListener(OK_DISABLED_CAUSE_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					setOkDisabledCause(ftpPanel.getOkDisabledCause());
				}
			});
		}
		return ftpPanel;
	}

	private DiskPanel getDiskPanel() {
		if (diskPanel == null) {
			diskPanel = new DiskPanel();
		}
		return diskPanel;
	}

	private JCheckBox getChckbxCompress() {
		if (chckbxCompress == null) {
			chckbxCompress = new JCheckBox(LocalizationData.get("Backup.preference.compress")); //$NON-NLS-1$
			chckbxCompress.setToolTipText(LocalizationData.get("Backup.preference.compress.tooltip")); //$NON-NLS-1$
		}
		return chckbxCompress;
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
		}
		return panel;
	}

	@Override
	public String getTitle() {
		return LocalizationData.get("Backup.preference.title"); //$NON-NLS-1$
	}

	@Override
	public String getToolTip() {
		return LocalizationData.get("Backup.preference.tooltip"); //$NON-NLS-1$
	}

	@Override
	public boolean updatePreferences() {
		boolean enabled = getChckbxBackup().isSelected();
		URI uri = null;
		if (enabled) {
			File file = getDiskPanel().getFile();
			if (file!=null) {
				try {
					uri = file.getCanonicalFile().toURI();
				} catch (IOException e) {
					ErrorManager.INSTANCE.log(AbstractDialog.getOwnerWindow(this), e);
				} 
			} else {
				uri = getFtpPanel().getURI();
			}
		}
		Preferences.INSTANCE.setBackupOptions(new BackupOptions(enabled, uri, getChckbxCompress().isSelected(), getMaxDiskField().getValue()));
		return false;
	}
}
