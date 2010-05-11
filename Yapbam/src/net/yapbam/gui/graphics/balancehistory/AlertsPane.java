package net.yapbam.gui.graphics.balancehistory;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.CoolJComboBox;

import java.awt.Insets;
import java.text.MessageFormat;

class AlertsPane extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel jLabel1 = null;
	private CoolJComboBox alertsMenu = null;
	private Alert[] alerts;

	/**
	 * This is the default constructor
	 */
	public AlertsPane() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints2.gridx = 1;
		gridBagConstraints2.gridy = 0;
		gridBagConstraints2.ipady = 0;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.insets = new Insets(0, 0, 0, 0);
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.ipadx = 0;
		gridBagConstraints1.insets = new Insets(0, 0, 0, 5);
		gridBagConstraints1.gridy = 0;
		this.setLayout(new GridBagLayout());
		jLabel1 = new JLabel();
		jLabel1.setText(LocalizationData.get("BalanceHistory.alerts")); //$NON-NLS-1$
		this.add(jLabel1, gridBagConstraints1);
		this.setSize(this.getPreferredSize());
		this.add(getAlerts(), gridBagConstraints2);
	}

	/**
	 * This method initializes alerts	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	JComboBox getAlerts() {
		if (alertsMenu == null) {
			alertsMenu = new CoolJComboBox();
			alertsMenu.setToolTipText(LocalizationData.get("BalanceHistory.alerts.toolTip")); //$NON-NLS-1$
		}
		return alertsMenu;
	}

	public void setAlerts(Alert[] alerts) {
		this.alerts = alerts;
		this.setVisible(alerts.length>0);
		this.alertsMenu.setActionEnabled(false);
		this.alertsMenu.removeAllItems();
		for (int i = 0; i < alerts.length; i++) {
			MessageFormat format = new MessageFormat(LocalizationData.get("BalanceHistory.alerts.format"), LocalizationData.getLocale()); //$NON-NLS-1$
			String balance = LocalizationData.getCurrencyInstance().format(alerts[i].getBalance());
			String threshold = LocalizationData.getCurrencyInstance().format(alerts[i].getThreshold());
			String ope = alerts[i].getKind()==Alert.IS_LESS?" < ":" > "; //$NON-NLS-1$ //$NON-NLS-2$
			String message = format.format(new Object[]{alerts[i].getAccount().getName(), alerts[i].getDate(), (balance+ope+threshold)});
			this.alertsMenu.addItem(message);
		}
		this.alertsMenu.setActionEnabled(true);
	}

	public Alert getSelectedAlert() {
		return this.alerts[this.alertsMenu.getSelectedIndex()];
	}
	
}
