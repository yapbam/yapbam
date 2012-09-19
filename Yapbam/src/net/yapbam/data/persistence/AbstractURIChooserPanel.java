package net.yapbam.data.persistence;

import java.awt.Component;
import java.net.URI;

public interface AbstractURIChooserPanel {
	public static final String SELECTED_URI_PROPERTY = "selectedUri";
	public void refresh();
	public URI getSelectedURI();
	public Component getComponent();
}
