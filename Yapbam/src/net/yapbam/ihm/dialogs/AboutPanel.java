package net.yapbam.ihm.dialogs;

import javax.swing.JComponent;

import net.yapbam.ihm.LocalizationData;

import java.text.MessageFormat;

import java.awt.Dimension;
import javax.swing.JTabbedPane;
import net.yapbam.ihm.widget.HTMLPane;
import net.yapbam.update.VersionManager;

public class AboutPanel extends AbstractInfoPanel {
	private static final Dimension PREFERED_HTML_PANE_SIZE = new Dimension(480,240);  //  @jve:decl-index=0:
	private static final long serialVersionUID = 1L;
	private JTabbedPane jTabbedPane = null;
	private HTMLPane relnotesPane = null;
	private HTMLPane licensePane = null;
	/**
	 * This is the default constructor
	 */
	public AboutPanel() {
		super(null);
		this.setHeaderMessage(MessageFormat.format(LocalizationData.get("AboutDialog.Content"), "Jean-Marc Astesana (Fathzer)", VersionManager.getVersion()));
	}

	/**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	protected JComponent getJTabbedPane() {
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

	@Override
	protected JComponent getCenterComponent() {
		return getJTabbedPane();
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
