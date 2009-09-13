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
						System.err.println("Attempted to read a bad URL: " + url);//TODO
					} catch (URISyntaxException e2) {
						e2.printStackTrace();
					}
				}
			}
		});
		this.setViewportView(textPane);
	}
	
	private void setContent (String text) {
		textPane.setContentType("text/html");
		textPane.setText(text);
	}
	
	private void setContent (URL url) {
		if (url != null) {
			try {
				textPane.setPage(url);
			} catch (IOException e) {
				System.err.println("Attempted to read a bad URL: " + url);//TODO
			}
		}
	}
	
	public HTMLPane (URL url) {
		this();
		setContent(url);
	}
	public HTMLPane (String text) {
		this();
		setContent(text);
	}
}