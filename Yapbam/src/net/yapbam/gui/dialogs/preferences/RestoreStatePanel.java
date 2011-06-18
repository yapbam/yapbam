package net.yapbam.gui.dialogs.preferences;

import net.yapbam.gui.PreferencePanel;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JSeparator;

public class RestoreStatePanel extends PreferencePanel {
	public RestoreStatePanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		
		JCheckBox chckbxConserverLordreDes = new JCheckBox("Conserver l'ordre des onglets");
		chckbxConserverLordreDes.setEnabled(false);
		chckbxConserverLordreDes.setSelected(true);
		chckbxConserverLordreDes.setToolTipText("Cochez cette case pour que l'ordre des onglets soit conserv\u00E9 d'une ex\u00E9cution \u00E0 l'autre");
		GridBagConstraints gbc_chckbxConserverLordreDes = new GridBagConstraints();
		gbc_chckbxConserverLordreDes.anchor = GridBagConstraints.WEST;
		gbc_chckbxConserverLordreDes.insets = new Insets(5, 0, 5, 0);
		gbc_chckbxConserverLordreDes.gridx = 0;
		gbc_chckbxConserverLordreDes.gridy = 0;
		add(chckbxConserverLordreDes, gbc_chckbxConserverLordreDes);
		
		JCheckBox chckbxConserverLesMasquages = new JCheckBox("Conserver les masquages de colonnes");
		chckbxConserverLesMasquages.setEnabled(false);
		chckbxConserverLesMasquages.setSelected(true);
		chckbxConserverLesMasquages.setToolTipText("Cochez cette case pour que les colonnes masqu\u00E9es le restent d'une ex\u00E9cution \u00E0 l'autre");
		GridBagConstraints gbc_chckbxConserverLesMasquages = new GridBagConstraints();
		gbc_chckbxConserverLesMasquages.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxConserverLesMasquages.anchor = GridBagConstraints.WEST;
		gbc_chckbxConserverLesMasquages.gridx = 0;
		gbc_chckbxConserverLesMasquages.gridy = 1;
		add(chckbxConserverLesMasquages, gbc_chckbxConserverLesMasquages);
		
		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.weightx = 1.0;
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 2;
		add(separator, gbc_separator);
		
		JCheckBox chckbxConserverLeFiltre = new JCheckBox("Conserver le filtre");
		chckbxConserverLeFiltre.setToolTipText("Cochez cette case pour conserver le filtre d'une ex\u00E9cution \u00E0 l'autre");
		chckbxConserverLeFiltre.setEnabled(false);
		GridBagConstraints gbc_chckbxConserverLeFiltre = new GridBagConstraints();
		gbc_chckbxConserverLeFiltre.weighty = 1.0;
		gbc_chckbxConserverLeFiltre.anchor = GridBagConstraints.NORTHWEST;
		gbc_chckbxConserverLeFiltre.gridx = 0;
		gbc_chckbxConserverLeFiltre.gridy = 3;
		add(chckbxConserverLeFiltre, gbc_chckbxConserverLeFiltre);
	}
	private static final long serialVersionUID = 1L;

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Etat au lancement"; //LOCAL
	}

	@Override
	public String getToolTip() {
		// TODO Auto-generated method stub
		return "Cet onglet permet de régler les informations à conserver entre deux exécution de Yapbam";
	}

	@Override
	public boolean updatePreferences() {
		// TODO Auto-generated method stub
		return false;
	}
}
