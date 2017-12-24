package net.yapbam.gui.dialogs;

import javax.swing.JComponent;
import javax.swing.UIManager;

import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.swing.JTabbedPane;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.widget.AbstractTitledPanel;
import com.fathzer.soft.ajlib.swing.widget.HTMLPane;

import net.yapbam.relnotes.ReleaseNotesFormatter;
import net.yapbam.util.ApplicationContext;

public class AboutPanel extends AbstractTitledPanel<Void> {
	private static final Dimension PREFERED_HTML_PANE_SIZE = new Dimension(640,480);  //  @jve:decl-index=0:
	private static final long serialVersionUID = 1L;
	private JTabbedPane jTabbedPane = null;
	private HTMLPane relnotesPane = null;
	private HTMLPane licensePane = null;
	private HTMLPane aboutPane = null;
	/**
	 * This is the default constructor
	 */
	public AboutPanel() {
		super(Formatter.format(LocalizationData.get("AboutDialog.Content"), "Jean-Marc Astesana (Fathzer)", ApplicationContext.getVersion(), "2018"), UIManager.getIcon("OptionPane.informationIcon"), null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	/**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	protected JComponent getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			try {
				jTabbedPane.addTab(LocalizationData.get("AboutDialog.License.TabName"), null, getLicensePane(), null); //$NON-NLS-1$
				jTabbedPane.addTab(LocalizationData.get("AboutDialog.RelNotes.TabName"), null, getRelnotesPane(), null); //$NON-NLS-1$
				jTabbedPane.addTab(LocalizationData.get("AboutDialog.Contributors.TabName"), null, getAboutPane(), null); //$NON-NLS-1$
			} catch (IOException e) {
				ErrorManager.INSTANCE.log(Utils.getOwnerWindow(this), e);
			}
		}
		return jTabbedPane;
	}

	/**
	 * This method initializes relnotesPane	
	 * 	
	 * @return net.yapbam.ihm.widget.relnotesPane	
	 */
	private HTMLPane getRelnotesPane() throws IOException {
		if (relnotesPane == null) {
			URL url = LocalizationData.getURL("relnotes.txt"); //$NON-NLS-1$
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8")); //$NON-NLS-1$
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				ReleaseNotesFormatter releaseNotesFormatter = new ReleaseNotesFormatter();
				releaseNotesFormatter.setIgnoreNext(true);
				releaseNotesFormatter.build(reader, new BufferedWriter(new OutputStreamWriter(stream)));
				String html = stream.toString();
				relnotesPane = new HTMLPane(html);
			} finally {
				reader.close();
			}
			relnotesPane.setPreferredSize(PREFERED_HTML_PANE_SIZE);
		}
		return relnotesPane;
	}

	/**
	 * This method initializes licensePane	
	 * 	
	 * @return net.yapbam.ihm.widget.HTMLPane	
	 */
	private HTMLPane getLicensePane() throws IOException {
		if (licensePane == null) {
			licensePane = new HTMLPane(LocalizationData.getURL("license.html")); //$NON-NLS-1$
			licensePane.setPreferredSize(PREFERED_HTML_PANE_SIZE);
		}
		return licensePane;
	}

	@Override
	public JComponent getCenterComponent() {
		return getJTabbedPane();
	}

	/**
	 * This method initializes aboutPane	
	 * 	
	 * @return net.yapbam.gui.widget.HTMLPane	
	 */
	private HTMLPane getAboutPane() throws IOException {
		if (aboutPane == null) {
			aboutPane = new HTMLPane(LocalizationData.getURL("contributors.html")); //$NON-NLS-1$
		}
		return aboutPane;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
