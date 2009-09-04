package net.yapbam.ihm.widget;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

@SuppressWarnings("serial")
public class HTMLPane extends JScrollPane {
	public HTMLPane (URL url) {
		super();
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JTextPane jTextPane = new JTextPane();
		jTextPane.setEditable(false);
		jTextPane.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType()==HyperlinkEvent.EventType.ACTIVATED) {
					URL url = e.getURL();
					try {
						Desktop.getDesktop().browse(url.toURI()); 
					} catch (IOException e1) {
						System.err.println("Attempted to read a bad URL: " + url);//TODO
					} catch (URISyntaxException e2) {
						e2.printStackTrace();
					}
				}
			}
		});
		if (url != null) {
			try {
				jTextPane.setPage(url);
			} catch (IOException e) {
				System.err.println("Attempted to read a bad URL: " + url);//TODO
			}
		}
		this.setViewportView(jTextPane);
	}
}