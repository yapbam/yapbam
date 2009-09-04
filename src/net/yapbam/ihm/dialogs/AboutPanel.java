package net.yapbam.ihm.dialogs;

import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.UIManager;

import java.awt.BorderLayout;
import javax.swing.JLabel;

import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.VersionManager;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.MessageFormat;

import java.awt.Dimension;
import javax.swing.JTabbedPane;
import net.yapbam.ihm.widget.HTMLPane;

public class AboutPanel extends JPanel {

	private static final Dimension PREFERED_HTML_PANE_SIZE = new Dimension(480,240);  //  @jve:decl-index=0:
	private static final long serialVersionUID = 1L;
	private JPanel northPanel = null;
	private JLabel iconLabel = null;
	private JLabel textLabel = null;
	private JTabbedPane jTabbedPane = null;
	private HTMLPane relnotesPane = null;
	private HTMLPane licensePane = null;
	/**
	 * This is the default constructor
	 */
	public AboutPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(309, 281);
		this.setLayout(new BorderLayout());
		this.add(getNorthPanel(), BorderLayout.NORTH);
		this.add(getJTabbedPane(), BorderLayout.EAST);
	}

	/**
	 * This method initializes northPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getNorthPanel() {
		if (northPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints1.gridy = 0;
			textLabel = new JLabel();
			textLabel.setText(MessageFormat.format(LocalizationData.get("AboutDialog.Content"), "Jean-Marc Astesana (Fathzer)", VersionManager.getVersion()));
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints.gridy = 0;
			northPanel = new JPanel();
			northPanel.setLayout(new GridBagLayout());
			northPanel.add(getIconLabel(), gridBagConstraints);
			northPanel.add(textLabel, gridBagConstraints1);
		}
		return northPanel;
	}

	/**
	 * This method initializes iconLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getIconLabel() {
		if (iconLabel == null) {
			iconLabel = new JLabel();
			iconLabel.setText("");
			iconLabel.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
		}
		return iconLabel;
	}

	/**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.addTab(LocalizationData.get("AboutDialog.License.TabName"), null, getLicensePane(), null);
			jTabbedPane.addTab(LocalizationData.get("AboutDialog.RelNotes.TabName"), null, getRelnotesPane(), null);
		}
		return jTabbedPane;
	}

	/**
	 * This method initializes relnotesPane	
	 * 	
	 * @return net.yapbam.ihm.widget.relnotesPane	
	 */
	private HTMLPane getRelnotesPane() {
		if (relnotesPane == null) {
			relnotesPane = new HTMLPane(LocalizationData.getURL("Release notes.html"));
			relnotesPane.setPreferredSize(PREFERED_HTML_PANE_SIZE);
		}
		return relnotesPane;
	}

	/**
	 * This method initializes licensePane	
	 * 	
	 * @return net.yapbam.ihm.widget.HTMLPane	
	 */
	private HTMLPane getLicensePane() {
		if (licensePane == null) {
			licensePane = new HTMLPane(LocalizationData.getURL("license.html"));
			licensePane.setPreferredSize(PREFERED_HTML_PANE_SIZE);
		}
		return licensePane;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
