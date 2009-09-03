package net.yapbam.ihm.dialogs;

import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.UIManager;

import java.awt.BorderLayout;
import javax.swing.JLabel;

import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.VersionManager;

import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import java.awt.Dimension;

public class AboutPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel northPanel = null;
	private JLabel iconLabel = null;
	private JLabel textLabel = null;
	private JTextPane jTextPane = null;
	private JScrollPane jScrollPane = null;
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
		this.add(getJScrollPane(), BorderLayout.CENTER);
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
	 * This method initializes jTextPane	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
	private JTextPane getJTextPane() {
		if (jTextPane == null) {
			jTextPane = new JTextPane();
			jTextPane.setEditable(false);
			jTextPane.addHyperlinkListener(new HyperlinkListener() {
				@Override
				public void hyperlinkUpdate(HyperlinkEvent e) {
					if (e.getEventType()==HyperlinkEvent.EventType.ACTIVATED) {
						URL url = e.getURL();
						try {
							Desktop.getDesktop().browse(url.toURI()); 
						} catch (IOException e1) {
							System.err.println("Attempted to read a bad URL: " + url);
						} catch (URISyntaxException e2) {
							e2.printStackTrace();
						}
					}
				}
			});
			java.net.URL url = Object.class.getResource("/greetings.html");
			if (url != null) {
				try {
					jTextPane.setPage(url);
				} catch (IOException e) {
					System.err.println("Attempted to read a bad URL: " + url);
				}
			} else {
				System.err.println("Couldn't find file: greetings.html");
			}
		}
		return jTextPane;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jScrollPane.setPreferredSize(new Dimension(480, 161));
			jScrollPane.setViewportView(getJTextPane());
		}
		return jScrollPane;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
