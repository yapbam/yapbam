package net.yapbam.ihm.administration;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import net.yapbam.ihm.LocalizationData;

import java.awt.GridBagConstraints;

public class AdministrationPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTabbedPane jTabbedPane = null;
	private PeriodicTransactionListPanel periodicalTransactionPanel = null;
	/**
	 * This is the default constructor
	 */
	public AdministrationPanel() {
		super();
		this.setToolTipText("Ajout, suppression, modification des comptes, des catégories, etc ...");
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.gridx = 0;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(getJTabbedPane(), gridBagConstraints);
	}

	/**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.addTab(LocalizationData.get("PeriodicManagementDialog.title"), getPeriodicalTransactionPanel());
		}
		return jTabbedPane;
	}

	/**
	 * This method initializes periodicalTransactionPanel	
	 * 	
	 * @return net.yapbam.ihm.administration.PeriodicTransactionListPanel	
	 */
	private PeriodicTransactionListPanel getPeriodicalTransactionPanel() {
		if (periodicalTransactionPanel == null) {
			periodicalTransactionPanel = new PeriodicTransactionListPanel();
		}
		return periodicalTransactionPanel;
	}

}
