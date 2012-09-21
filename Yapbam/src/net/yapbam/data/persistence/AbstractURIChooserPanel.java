package net.yapbam.data.persistence;

import java.net.URI;

public interface AbstractURIChooserPanel {
	public static final String SELECTED_URI_PROPERTY = "selectedUri";
	public void refresh();
	public URI getSelectedURI();
	public void setDialogType(boolean save);
}
