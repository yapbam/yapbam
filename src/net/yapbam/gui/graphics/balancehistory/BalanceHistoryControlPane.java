package net.yapbam.gui.graphics.balancehistory;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import javax.swing.JCheckBox;

import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import java.awt.BorderLayout;
import java.awt.Insets;

class BalanceHistoryControlPane extends JPanel {

	private static final long serialVersionUID = 1L;
	private JComboBox accounts = null;
	private JLabel jLabel = null;
	private JLabel report = null;
	private JButton today = null;
	private JCheckBox isGridVisible = null;
	private JPanel west = null;
	private JPanel center = null;
	private JPanel east = null;
	private JLabel jLabel1 = null;
	private JComboBox alerts = null;
	private JPanel south = null;

	/**
	 * This is the default constructor
	 */
	public BalanceHistoryControlPane() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		report = new JLabel();
		report.setText("JLabel");
		report.setToolTipText(LocalizationData.get("BalanceHistory.report.toolTip"));
		jLabel = new JLabel();
		jLabel.setText("Comptes :");
		this.setSize(300, 200);
		this.setLayout(new BorderLayout());
		this.add(getWest(), BorderLayout.WEST);
		this.add(getCenter(), BorderLayout.CENTER);
		this.add(getEast(), BorderLayout.EAST);
		this.add(getSouth(), BorderLayout.SOUTH);
	}

	/**
	 * This method initializes accounts	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getAccounts() {
		if (accounts == null) {
			accounts = new JComboBox();
			accounts.addItem("xxxx"); //TODO
			accounts.setToolTipText("Sélectionnez le compte dont vous souhaitez consulter l'historique dans ce menu");
		}
		return accounts;
	}

	/**
	 * This method initializes today	
	 * 	
	 * @return javax.swing.JButton	
	 */
	JButton getToday() {
		if (today == null) {
			today = new JButton();
			today.setIcon(new ImageIcon(getClass().getResource("/net/yapbam/gui/widget/stop.png")));
			today.setToolTipText("Cliquez ce bouton pour positionner la date à aujourd'hui"); //LOCAL
			today.setPreferredSize(new Dimension(20, 20));
		}
		return today;
	}

	/**
	 * This method initializes isGridVisible	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	JCheckBox getIsGridVisible() {
		if (isGridVisible == null) {
			isGridVisible = new JCheckBox();
			isGridVisible.setText(LocalizationData.get("BalanceHistory.showGrid"));
			isGridVisible.setToolTipText(LocalizationData.get("BalanceHistory.showGrid.toolTip"));
		}
		return isGridVisible;
	}

	void setReportText(String report) {
		this.report.setText(report);
	}

	/**
	 * This method initializes west	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getWest() {
		if (west == null) {
			jLabel1 = new JLabel();
			jLabel1.setText("Alertes :");
			jLabel1.setIcon(IconManager.ALERT);
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints1.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints.gridx = 1;
			west = new JPanel();
			west.setLayout(new GridBagLayout());
			west.add(jLabel, gridBagConstraints1);
			west.add(getAccounts(), gridBagConstraints);
		}
		return west;
	}

	/**
	 * This method initializes center	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCenter() {
		if (center == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints2.gridy = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 0;
			center = new JPanel();
			center.setLayout(new GridBagLayout());
			center.add(report, gridBagConstraints4);
			center.add(getToday(), gridBagConstraints2);
		}
		return center;
	}

	/**
	 * This method initializes east	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getEast() {
		if (east == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = -1;
			gridBagConstraints3.gridy = -1;
			east = new JPanel();
			east.setLayout(new GridBagLayout());
			east.add(getIsGridVisible(), gridBagConstraints3);
		}
		return east;
	}

	/**
	 * This method initializes alerts	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getAlerts() {
		if (alerts == null) {
			alerts = new JComboBox();
			alerts.setToolTipText("Séléctionnez l'alerte à visualiser dans ce menu");
			alerts.addItem("Alerte le 29/06/2010 sur le compte ING Direct"); //TODO
		}
		return alerts;
	}

	/**
	 * This method initializes south	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSouth() {
		if (south == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints6.gridx = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints5.gridy = 0;
			south = new JPanel();
			south.setLayout(new GridBagLayout());
			south.add(jLabel1, gridBagConstraints5);
			south.add(getAlerts(), gridBagConstraints6);
		}
		return south;
	}
}
