package net.yapbam.gui.dialogs.preferences;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.PreferencePanel;
import net.yapbam.gui.widget.IntegerWidget;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JCheckBox;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;

public class AutoSavePanel extends PreferencePanel {
	private static final long serialVersionUID = 1L;
	private IntegerWidget maxDiskField;

	/**
	 * Create the panel.
	 */
	public AutoSavePanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
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
		
		JCheckBox chckbxActiverLesSauvegardes = new JCheckBox("Activer les sauvegardes automatiques");
		chckbxActiverLesSauvegardes.setToolTipText("Cochez cette case pour activer les sauvegardes");
		GridBagConstraints gbc_chckbxActiverLesSauvegardes = new GridBagConstraints();
		gbc_chckbxActiverLesSauvegardes.anchor = GridBagConstraints.WEST;
		gbc_chckbxActiverLesSauvegardes.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxActiverLesSauvegardes.gridx = 0;
		gbc_chckbxActiverLesSauvegardes.gridy = 1;
		add(chckbxActiverLesSauvegardes, gbc_chckbxActiverLesSauvegardes);
		
		JPanel panelDisk = new JPanel();
		panelDisk.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelDisk = new GridBagConstraints();
		gbc_panelDisk.insets = new Insets(5, 5, 5, 0);
		gbc_panelDisk.fill = GridBagConstraints.BOTH;
		gbc_panelDisk.gridx = 0;
		gbc_panelDisk.gridy = 2;
		add(panelDisk, gbc_panelDisk);
		GridBagLayout gbl_panelDisk = new GridBagLayout();
		gbl_panelDisk.columnWidths = new int[]{0, 0};
		gbl_panelDisk.rowHeights = new int[]{0, 0};
		gbl_panelDisk.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panelDisk.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panelDisk.setLayout(gbl_panelDisk);
		
		JRadioButton rdbtnSurDisque = new JRadioButton("Sur disque");
		rdbtnSurDisque.setToolTipText("Sélectionnez cette option pour effectuer vos sauvegardes sur un disque de votre ordinateur ou une clef USB");
		GridBagConstraints gbc_rdbtnSurDisque = new GridBagConstraints();
		gbc_rdbtnSurDisque.gridx = 0;
		gbc_rdbtnSurDisque.gridy = 0;
		panelDisk.add(rdbtnSurDisque, gbc_rdbtnSurDisque);
		
		JPanel panelFTP = new JPanel();
		panelFTP.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelFTP = new GridBagConstraints();
		gbc_panelFTP.insets = new Insets(5, 5, 5, 0);
		gbc_panelFTP.fill = GridBagConstraints.BOTH;
		gbc_panelFTP.gridx = 0;
		gbc_panelFTP.gridy = 3;
		add(panelFTP, gbc_panelFTP);
		GridBagLayout gbl_panelFTP = new GridBagLayout();
		panelFTP.setLayout(gbl_panelFTP);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("Sur un serveur FTP");
		rdbtnNewRadioButton.setToolTipText("Sélectionnez cette option pour effectuer la sauvegarde sur un serveur FTP");
		GridBagConstraints gbc_rdbtnNewRadioButton = new GridBagConstraints();
		gbc_rdbtnNewRadioButton.weightx = 1.0;
		gbc_rdbtnNewRadioButton.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNewRadioButton.gridx = 0;
		gbc_rdbtnNewRadioButton.gridy = 0;
		panelFTP.add(rdbtnNewRadioButton, gbc_rdbtnNewRadioButton);
		
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
		
		maxDiskField = new IntegerWidget();
		maxDiskField.setLocale(LocalizationData.getLocale());
		maxDiskField.setToolTipText("Entrez ici la taille maximum allouée au sauvegardes. Une fois cette taille atteinte, les sauvegardes les plus anciennes seront effacées");
		GridBagConstraints gbc_maxDiskField = new GridBagConstraints();
		gbc_maxDiskField.insets = new Insets(0, 5, 0, 5);
		gbc_maxDiskField.anchor = GridBagConstraints.WEST;
		gbc_maxDiskField.gridx = 1;
		gbc_maxDiskField.gridy = 0;
		panelSize.add(maxDiskField, gbc_maxDiskField);
		maxDiskField.setColumns(5);
		
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
