package net.yapbam.ihm.administration;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import net.yapbam.data.GlobalData;
import net.yapbam.ihm.LocalizationData;

import java.awt.GridBagConstraints;

public class AdministrationPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private GlobalData data;
	
	/**
	 * This is the constructor
	 */
	public AdministrationPanel(GlobalData data) {
		super();
		this.data = data;
		this.setToolTipText("Ajout, suppression, modification des comptes, des catégories, etc ..."); //LOCAL
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
		JTabbedPane jTabbedPane = new JTabbedPane();
//		AccountListPanel accountPanel = new AccountListPanel(data);
//		jTabbedPane.addTab("Gestion des comptes", null, accountPanel, accountPanel.getPanelToolTip());
//		CategoryListPanel categoryPanel = new CategoryListPanel(data);
//		jTabbedPane.addTab("Gestion des catégories", null, categoryPanel, categoryPanel.getToolTipText());
		PeriodicalTransactionListPanel periodicTransactionPanel = new PeriodicalTransactionListPanel(data);
		jTabbedPane.addTab(LocalizationData.get("PeriodicManagementDialog.title"), null, periodicTransactionPanel, periodicTransactionPanel.getToolTipText());
		this.add(jTabbedPane, gridBagConstraints);
	}
}
