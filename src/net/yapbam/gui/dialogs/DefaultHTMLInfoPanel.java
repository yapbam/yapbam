package net.yapbam.gui.dialogs;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.UIManager;

import net.astesana.ajlib.swing.widget.HTMLPane;

import java.awt.Dimension;

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

	@Override
	public Icon getIcon() {
		return UIManager.getIcon("OptionPane.informationIcon"); //$NON-NLS-1$
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
