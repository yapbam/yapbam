package net.yapbam.gui.dialogs;

import javax.swing.JComponent;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.HTMLPane;

import java.text.MessageFormat;

import java.awt.Dimension;
import javax.swing.JTabbedPane;
import net.yapbam.update.VersionManager;

public class AboutPanel extends AbstractInfoPanel {
	private static final Dimension PREFERED_HTML_PANE_SIZE = new Dimension(480,240);  //  @jve:decl-index=0:
	private static final long serialVersionUID = 1L;
	private JTabbedPane jTabbedPane = null;
	private HTMLPane relnotesPane = null;
	private HTMLPane licensePane = null;
	private HTMLPane aboutPane = null;
	/**
	 * This is the default constructor
	 */
	public AboutPanel() {
		super(null);
		this.setHeaderMessage(MessageFormat.format(LocalizationData.get("AboutDialog.Content"), "Jean-Marc Astesana (Fathzer)", VersionManager.getVersion())); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	protected JComponent getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.addTab(LocalizationData.get("AboutDialog.License.TabName"), null, getLicensePane(), null); //$NON-NLS-1$
			jTabbedPane.addTab(LocalizationData.get("AboutDialog.RelNotes.TabName"), null, getRelnotesPane(), null); //$NON-NLS-1$
			jTabbedPane.addTab(LocalizationData.get("AboutDialog.Contributors.TabName"), null, getAboutPane(), null); //$NON-NLS-1$
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
			relnotesPane = new HTMLPane(LocalizationData.getURL("Release notes.html")); //$NON-NLS-1$
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
			licensePane = new HTMLPane(LocalizationData.getURL("license.html")); //$NON-NLS-1$
			licensePane.setPreferredSize(PREFERED_HTML_PANE_SIZE);
		}
		return licensePane;
	}

	@Override
	protected JComponent getCenterComponent() {
		return getJTabbedPane();
	}

	/**
	 * This method initializes aboutPane	
	 * 	
	 * @return net.yapbam.gui.widget.HTMLPane	
	 */
	private HTMLPane getAboutPane() {
		if (aboutPane == null) {
			aboutPane = new HTMLPane(LocalizationData.getURL("contributors.html")); //$NON-NLS-1$
		}
		return aboutPane;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
