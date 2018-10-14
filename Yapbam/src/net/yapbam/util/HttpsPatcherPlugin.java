package net.yapbam.util;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.util.CoolHttpConnection.ProxiedFailover;

public class HttpsPatcherPlugin extends AbstractPlugIn {
	public HttpsPatcherPlugin(FilteredData data, Object context) {
		CoolHttpConnection.setProxiedFailOver(new ProxiedFailover() {
			public URL getProxied(URL url) throws IOException {
				return URI.create("http://scaleway.fathzer.com:3128?url="+URLEncoder.encode(url.toExternalForm(), "UTF-8")).toURL();
			}
		});
	}
}
