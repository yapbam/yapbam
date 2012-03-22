package net.astesana.ajlib.swing.widget;

import java.awt.Desktop;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

@SuppressWarnings("serial")
public class HTMLPane extends JScrollPane {
	private JTextPane textPane;

	public HTMLPane () {
		super();
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType()==HyperlinkEvent.EventType.ACTIVATED) {
					URL url = e.getURL();
					try {
						Desktop.getDesktop().browse(url.toURI()); 
					} catch (IOException e1) {
						System.err.println("Attempted to read a bad URL: " + url);//FIXME These exceptions must be thrown
					} catch (URISyntaxException e2) {
						e2.printStackTrace();
					}
				}
			}
		});
		this.setViewportView(textPane);
	}
	
	public void setContent (String text) {
		textPane.setContentType("text/html"); //$NON-NLS-1$
		// We should not use textPane.setText because it scrolls the textPane to the end of the text
		try {
			textPane.read(new StringReader(text), "text/html"); //$NON-NLS-1$
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setContent (URL url) throws IOException {
		if (url != null) {
			textPane.setPage(url);
		}
	}
	
	public HTMLPane (URL url) throws IOException {
		this();
		setContent(url);
	}
	public HTMLPane (String text) {
		this();
		setContent(text);
	}
}