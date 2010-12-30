package net.yapbam.dialogs;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.ImageIcon;

import net.yapbam.gui.widget.RoundButton;

@SuppressWarnings("serial")
public class WelcomePluginPanel extends JPanel {
	/**
	 * Creates the panel.
	 */
	public WelcomePluginPanel() {
    GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel tutorial = new JLabel("Tutorial");
		GridBagConstraints gbc_tutorial = new GridBagConstraints();
		gbc_tutorial.insets = new Insets(0, 0, 5, 0);
		gbc_tutorial.gridx = 6;
		gbc_tutorial.gridy = 1;
		add(tutorial, gbc_tutorial);
		
		JLabel sample = new JLabel("Ouvrir fichier exemple");
		sample.setToolTipText("<html>Cliquez ici pour ouvrir un fichier de donn\u00E9es de d\u00E9monstration.<br>Ceci vous permettra de voir Yapbam en action sur un jeu de donn\u00E9es suffisamment \u00E9tendu pour vous permettre de parcourir les possibilit\u00E9s de Yapbam.</html>");
		GridBagConstraints gbc_sample = new GridBagConstraints();
		gbc_sample.insets = new Insets(0, 0, 5, 5);
		gbc_sample.gridx = 2;
		gbc_sample.gridy = 2;
		add(sample, gbc_sample);
		
		JButton button = new JButton("New button");
		button.setIcon(new ImageIcon(WelcomePluginPanel.class.getResource("/net/yapbam/gui/images/alert.png")));
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.insets = new Insets(0, 0, 5, 5);
		gbc_button.gridx = 2;
		gbc_button.gridy = 3;
		add(button, gbc_button);
		
		RoundButton label = new RoundButton("New label");
		label.setTransparency(0.5f);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 4;
		gbc_label.gridy = 3;
		add(label, gbc_label);
		
		JLabel faq = new JLabel("Question courantes");
		faq.setToolTipText("<html>Ouvre la page web regroupant les questions courantes sur Yapbam.</html>");
		GridBagConstraints gbc_faq = new GridBagConstraints();
		gbc_faq.insets = new Insets(0, 0, 0, 5);
		gbc_faq.gridx = 4;
		gbc_faq.gridy = 5;
		add(faq, gbc_faq);

	}

}
