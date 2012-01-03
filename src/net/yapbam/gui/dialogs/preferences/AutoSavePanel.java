package net.yapbam.gui.dialogs.preferences;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.PreferencePanel;
import net.yapbam.gui.widget.IntegerWidget;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JCheckBox;
import java.awt.Insets;
import java.math.BigInteger;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JTextField;
import javax.swing.JButton;
import net.yapbam.gui.dialogs.preferences.backup.FTPPanel;

public class AutoSavePanel extends PreferencePanel {
	private static final long serialVersionUID = 1L;
	
	private JCheckBox chckbxBackup;
	private IntegerWidget maxDiskField;
	private JRadioButton rdbtnOnDisk;
	private JTextField diskFolder;
	private final javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
	private FTPPanel ftpPanel;

	/**
	 * Create the panel.
	 */
	public AutoSavePanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0};
		gridBagLayout.columnWeights = new double[]{1.0};
		setLayout(gridBagLayout);
		
		JLabel intro = new JLabel("<html>toto<br>titi</html>");
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
		
		JPanel panelDisk = new JPanel();
		panelDisk.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelDisk = new GridBagConstraints();
		gbc_panelDisk.insets = new Insets(5, 5, 5, 0);
		gbc_panelDisk.fill = GridBagConstraints.BOTH;
		gbc_panelDisk.gridx = 0;
		gbc_panelDisk.gridy = 2;
		add(panelDisk, gbc_panelDisk);
		GridBagLayout gbl_panelDisk = new GridBagLayout();
		gbl_panelDisk.columnWeights = new double[]{1.0, 0.0};
		panelDisk.setLayout(gbl_panelDisk);
		
		GridBagConstraints gbc_rdbtnOnDisk = new GridBagConstraints();
		gbc_rdbtnOnDisk.gridwidth = 0;
		gbc_rdbtnOnDisk.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnOnDisk.weightx = 1.0;
		gbc_rdbtnOnDisk.anchor = GridBagConstraints.WEST;
		gbc_rdbtnOnDisk.gridx = 0;
		gbc_rdbtnOnDisk.gridy = 0;
		panelDisk.add(getRdbtnOnDisk(), gbc_rdbtnOnDisk);
		
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
		
		JLabel lblTailleMaximumRserve = new JLabel("Taille maximum réservée aux sauvegardes :");
		GridBagConstraints gbc_lblTailleMaximumRserve = new GridBagConstraints();
		gbc_lblTailleMaximumRserve.insets = new Insets(0, 5, 0, 0);
		gbc_lblTailleMaximumRserve.anchor = GridBagConstraints.WEST;
		gbc_lblTailleMaximumRserve.gridx = 0;
		gbc_lblTailleMaximumRserve.gridy = 0;
		panelSize.add(lblTailleMaximumRserve, gbc_lblTailleMaximumRserve);
		
		GridBagConstraints gbc_maxDiskField = new GridBagConstraints();
		gbc_maxDiskField.insets = new Insets(0, 5, 0, 5);
		gbc_maxDiskField.anchor = GridBagConstraints.WEST;
		gbc_maxDiskField.gridx = 1;
		gbc_maxDiskField.gridy = 0;
		panelSize.add(getMaxDiskField(), gbc_maxDiskField);
		
		JLabel lblNewLabel = new JLabel("Mo");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.weightx = 1.0;
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 0;
		panelSize.add(lblNewLabel, gbc_lblNewLabel);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.weighty = 1.0;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 5;
		add(panel, gbc_panel);

		JRadioButton defaultDisk = new JRadioButton("Emplacement par d\u00E9faut ()");
		buttonGroup.add(defaultDisk);
		GridBagConstraints gbc_defaultDisk = new GridBagConstraints();
		gbc_defaultDisk.insets = new Insets(0, 10, 5, 0);
		gbc_defaultDisk.anchor = GridBagConstraints.WEST;
		gbc_defaultDisk.gridx = 0;
		gbc_defaultDisk.gridy = 1;
		panelDisk.add(defaultDisk, gbc_defaultDisk);
		
		JRadioButton customDisk = new JRadioButton("Emplacement personnalis\u00E9");
		buttonGroup.add(customDisk);
		GridBagConstraints gbc_customDisk = new GridBagConstraints();
		gbc_customDisk.insets = new Insets(0, 10, 5, 0);
		gbc_customDisk.anchor = GridBagConstraints.WEST;
		gbc_customDisk.gridx = 0;
		gbc_customDisk.gridy = 2;
		panelDisk.add(customDisk, gbc_customDisk);
		
		diskFolder = new JTextField();
		diskFolder.setEditable(false);
		GridBagConstraints gbc_diskFolder = new GridBagConstraints();
		gbc_diskFolder.insets = new Insets(0, 15, 0, 5);
		gbc_diskFolder.ipadx = 10;
		gbc_diskFolder.anchor = GridBagConstraints.WEST;
		gbc_diskFolder.fill = GridBagConstraints.HORIZONTAL;
		gbc_diskFolder.gridx = 0;
		gbc_diskFolder.gridy = 3;
		panelDisk.add(diskFolder, gbc_diskFolder);
		diskFolder.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("Choisir");
		btnNewButton_1.setToolTipText("Cliquez sur ce bouton pour choisir l'emplacement o\u00F9 sauvegarder vos donn\u00E9es");
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.gridx = 1;
		gbc_btnNewButton_1.gridy = 3;
		panelDisk.add(btnNewButton_1, gbc_btnNewButton_1);
	}

	public IntegerWidget getMaxDiskField() {
		if (maxDiskField==null) {
			maxDiskField = new IntegerWidget(BigInteger.ONE, null);
			maxDiskField.setLocale(LocalizationData.getLocale());
			maxDiskField.setToolTipText("<html>Entrez ici la l'espace maximum alloué au sauvegardes.<br>Une fois la limite atteinte, les sauvegardes les plus anciennes seront effacées.<br><br>Laissez ce champ vide pour ne pas limiter l'espace dédié au sauvegardes</html>");
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
					getRdbtnOnDisk().setEnabled(enabled);
					getFtpPanel().setEnabled(enabled);
					getMaxDiskField().setEnabled(enabled);
					getMaxDiskField().setEditable(enabled);
				}
			});
			chckbxBackup.setToolTipText("Cochez cette case pour activer les sauvegardes");
		}
		return chckbxBackup;
	}
	
	public JRadioButton getRdbtnOnDisk() {
		if (rdbtnOnDisk==null) {
			rdbtnOnDisk = new JRadioButton("Sur disque");
			rdbtnOnDisk.setToolTipText("Sélectionnez cette option pour effectuer vos sauvegardes sur un disque de votre ordinateur ou une clef USB");
			rdbtnOnDisk.setSelected(true);
		}
		return rdbtnOnDisk;
	}

	private FTPPanel getFtpPanel() {
		if (ftpPanel == null) {
			ftpPanel = new FTPPanel();
		}
		return ftpPanel;
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
