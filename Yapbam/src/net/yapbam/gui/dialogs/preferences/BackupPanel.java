package net.yapbam.gui.dialogs.preferences;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.PreferencePanel;
import net.yapbam.gui.widget.IntegerWidget;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JCheckBox;
import java.awt.Insets;
import java.math.BigInteger;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import net.yapbam.gui.dialogs.preferences.backup.FTPPanel;
import net.yapbam.gui.dialogs.preferences.backup.DiskPanel;

public class BackupPanel extends PreferencePanel {
	private static final long serialVersionUID = 1L;
	
	private JCheckBox chckbxBackup;
	private IntegerWidget maxDiskField;
	private FTPPanel ftpPanel;
	private DiskPanel diskPanel;
	private JLabel lblTailleMaximumReserve;
	private JLabel lblSizeUnit;

	/**
	 * Create the panel.
	 */
	public BackupPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		
		JLabel intro = new JLabel("<html>Sauvegarder ses données est essentiel pour en assurer la sécurité.<br><br>Yapbam vous permet de faire une copie compressée de chaque fichier lu.<br>" +
				"Pour plus de sécurité, il est recommandé de ne pas conserver ses sauvegardes sur le même support que l'original. L'idéal étant de les faire sur un serveur à l'extérieur de votre domicile.<br>" +
				"La plupart des opérateurs Internet fournissent un accès à un serveur FTP (consultez le site de votre opérateur ou votre moteur de recherche favori pour en savoir plus).</html>");
		GridBagConstraints gbc_intro = new GridBagConstraints();
		gbc_intro.fill = GridBagConstraints.HORIZONTAL;
		gbc_intro.weightx = 1.0;
		gbc_intro.insets = new Insets(0, 0, 5, 0);
		gbc_intro.anchor = GridBagConstraints.NORTHWEST;
		gbc_intro.gridx = 0;
		gbc_intro.gridy = 0;
		add(intro, gbc_intro);
		
		GridBagConstraints gbc_chckbxBackup = new GridBagConstraints();
		gbc_chckbxBackup.anchor = GridBagConstraints.WEST;
		gbc_chckbxBackup.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxBackup.gridx = 0;
		gbc_chckbxBackup.gridy = 1;
		add(getChckbxBackup(), gbc_chckbxBackup);
		
		GridBagConstraints gbc_diskPanel = new GridBagConstraints();
		gbc_diskPanel.insets = new Insets(0, 0, 5, 0);
		gbc_diskPanel.fill = GridBagConstraints.BOTH;
		gbc_diskPanel.gridx = 0;
		gbc_diskPanel.gridy = 2;
		add(getDiskPanel(), gbc_diskPanel);
		
		GridBagConstraints gbc_ftpPanel = new GridBagConstraints();
		gbc_ftpPanel.insets = new Insets(0, 0, 5, 0);
		gbc_ftpPanel.fill = GridBagConstraints.BOTH;
		gbc_ftpPanel.gridx = 0;
		gbc_ftpPanel.gridy = 3;
		add(getFtpPanel(), gbc_ftpPanel);
		
		JPanel panelSize = new JPanel();
		GridBagConstraints gbc_panelSize = new GridBagConstraints();
		gbc_panelSize.insets = new Insets(0, 0, 5, 0);
		gbc_panelSize.fill = GridBagConstraints.BOTH;
		gbc_panelSize.gridx = 0;
		gbc_panelSize.gridy = 4;
		add(panelSize, gbc_panelSize);
		GridBagLayout gbl_panelSize = new GridBagLayout();
		panelSize.setLayout(gbl_panelSize);
		
		lblTailleMaximumReserve = new JLabel("Taille maximum réservée aux sauvegardes :");
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
		
		lblSizeUnit = new JLabel("Mo");
		GridBagConstraints gbc_lblSizeUnit = new GridBagConstraints();
		gbc_lblSizeUnit.anchor = GridBagConstraints.WEST;
		gbc_lblSizeUnit.weightx = 1.0;
		gbc_lblSizeUnit.gridx = 2;
		gbc_lblSizeUnit.gridy = 0;
		panelSize.add(lblSizeUnit, gbc_lblSizeUnit);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.weighty = 1.0;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 5;
		add(panel, gbc_panel);
		
		ButtonGroup group = new ButtonGroup();
		group.add(getFtpPanel().getFtpRdnButton());
		group.add(getDiskPanel().getDiskRdnButton());
	}

	public IntegerWidget getMaxDiskField() {
		if (maxDiskField==null) {
			maxDiskField = new IntegerWidget(BigInteger.ONE, null);
			maxDiskField.setLocale(LocalizationData.getLocale());
			maxDiskField.setToolTipText("<html>Entrez ici la l'espace maximum alloué au sauvegardes.<br>Une fois la limite atteinte, les sauvegardes les plus anciennes seront effacées.<br><br>Laissez ce champ vide pour ne pas limiter l'espace dédié au sauvegardes.</html>");
			maxDiskField.setColumns(5);
		}
		return maxDiskField;
	}

	public JCheckBox getChckbxBackup() {
		if (chckbxBackup==null) {
			chckbxBackup = new JCheckBox("Activer les sauvegardes automatiques");
			chckbxBackup.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					boolean enabled = e.getStateChange()==ItemEvent.SELECTED;
					getDiskPanel().setEnabled(enabled);
					getFtpPanel().setEnabled(enabled);
					getMaxDiskField().setEnabled(enabled);
					getMaxDiskField().setEditable(enabled);
					lblTailleMaximumReserve.setEnabled(enabled);
					lblSizeUnit.setEnabled(enabled);
				}
			});
			chckbxBackup.setToolTipText("Cochez cette case pour activer les sauvegardes");
		}
		return chckbxBackup;
	}

	private FTPPanel getFtpPanel() {
		if (ftpPanel == null) {
			ftpPanel = new FTPPanel();
		}
		return ftpPanel;
	}

	private DiskPanel getDiskPanel() {
		if (diskPanel == null) {
			diskPanel = new DiskPanel();
		}
		return diskPanel;
	}

	@Override
	public String getTitle() {
		return "Sauvegardes";
	}

	@Override
	public String getToolTip() {
		return "Cet onglet permet de paramétrer les sauvegardes automatiques";
	}

	@Override
	public boolean updatePreferences() {
		// TODO Auto-generated method stub
		return false;
	}
}
