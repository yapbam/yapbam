package net.yapbam.ihm.dialogs;

import javax.swing.JComponent;

import java.awt.Dimension;

import net.yapbam.ihm.widget.HTMLPane;

public class DefaultHTMLInfoPanel extends AbstractInfoPanel {
	private static final long serialVersionUID = 1L;
	private static final Dimension PREFERED_HTML_PANE_SIZE = new Dimension(480,240);  //  @jve:decl-index=0:

	private HTMLPane htmlPane = null;

	/**
	 * This is the default constructor
	 */
	public DefaultHTMLInfoPanel(String header, String detailInformation) {
		super(detailInformation);
		this.setHeaderMessage(header);
	}

	/**
	 * This method initializes relnotesPane	
	 * 	
	 * @return net.yapbam.ihm.widget.relnotesPane	
	 */
	private HTMLPane getHTMLPane() {
		if (htmlPane == null) {
			htmlPane = new HTMLPane((String) data);
			htmlPane.setPreferredSize(PREFERED_HTML_PANE_SIZE);
		}
		return htmlPane;
	}

	@Override
	protected JComponent getCenterComponent() {
		return getHTMLPane();
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
