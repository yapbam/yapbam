package net.yapbam.gui.util;

import java.awt.Color;
import java.awt.Font;
import java.net.URISyntaxException;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import net.yapbam.gui.HelpManager;
import net.yapbam.util.HtmlUtils;

/** An JEditorPane that can be used as message in JOptionPane.showOptionDialog.
 * <br>It allows to insert html links in the message.
 */
public class MessageWithLink extends JEditorPane {
	private static final long serialVersionUID = 1L;

	public MessageWithLink(String htmlBody) {
	    super("text/html", "<html><body style=\"" + getStyle() + "\">" + HtmlUtils.removeHtmlTags(htmlBody) + "</body></html>");
	    addHyperlinkListener(new HyperlinkListener() {
	        @Override
	        public void hyperlinkUpdate(HyperlinkEvent e) {
	            if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
					try {
						HelpManager.show(null, e.getURL().toURI());
					} catch (URISyntaxException e1) {
						throw new IllegalArgumentException(e.getURL().toString()+" is not an valid URL");
					}
	            }
	        }
	    });
	    setEditable(false);
	    setBorder(null);
	}
	
	static StringBuilder getStyle() {
	    // for copying style
	    JLabel label = new JLabel();
	    Font font = label.getFont();
	    Color color = label.getBackground();

	    // create some css from the label's font
	    StringBuilder style = new StringBuilder("font-family:" + font.getFamily() + ";");
	    style.append("font-weight:" + (font.isBold() ? "bold" : "normal") + ";");
	    style.append("font-size:" + font.getSize() + "pt;");
	    style.append("background-color: rgb("+color.getRed()+","+color.getGreen()+","+color.getBlue()+");");
	    return style;
	}
}