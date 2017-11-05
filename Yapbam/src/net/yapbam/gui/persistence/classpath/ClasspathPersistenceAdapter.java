package net.yapbam.gui.persistence.classpath;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.persistence.PersistenceAdapter;
import net.yapbam.util.Portable;

import com.fathzer.soft.jclop.Account;
import com.fathzer.soft.jclop.Cancellable;
import com.fathzer.soft.jclop.Entry;
import com.fathzer.soft.jclop.JClopException;
import com.fathzer.soft.jclop.Service;
import com.fathzer.soft.jclop.SynchronizationState;
import com.fathzer.soft.jclop.swing.URIChooser;

public class ClasspathPersistenceAdapter extends PersistenceAdapter {
	public static final String SCHEME = "classpath"; //$NON-NLS-1$

	public ClasspathPersistenceAdapter() throws IOException {
		super(new Service(new File(Portable.getDataDirectory(), "cache"), true) { //$NON-NLS-1$
			
			@Override
			public boolean upload(InputStream in, long length, URI uri,
					Cancellable task, Locale locale) throws JClopException, IOException {
				return true;
			}
			
			@Override
			public String getScheme() {
				return SCHEME;
			}
			
			@Override
			public String getRemoteRevision(URI uri) throws JClopException {
				return "1"; //$NON-NLS-1$
			}
			
			@Override
			public Collection<Entry> getRemoteEntries(Account account, Cancellable task) throws JClopException {
				return Collections.emptyList();
			}
			
			@Override
			public String getConnectionDataURIFragment(Serializable connectionData) {
				return null;
			}
			
			@Override
			public Serializable getConnectionData(String uriFragment) {
				return null;
			}
			
			@Override
			public boolean download(URI uri, OutputStream out, Cancellable task,
					Locale locale) throws JClopException, IOException {
				return true;
			}

			@Override
			public SynchronizationState synchronize(URI uri, Cancellable task, Locale locale) throws JClopException, IOException {
				return SynchronizationState.SYNCHRONIZED;
			}

			@Override
			public Entry getEntry(URI uri) {
				if (!uri.getScheme().equals(getScheme())) {
					throw new IllegalArgumentException();
				}
				Account account = new Account(this, "1", "common", BigInteger.ZERO); //$NON-NLS-1$ //$NON-NLS-2$
				return new Entry(account, uri.toString().substring(SCHEME.length()+1));
			}

			@Override
			public String getDisplayable(URI uri) {
				return LocalizationData.get("Generic.example")+getEntry(uri).getDisplayName(); //$NON-NLS-1$
			}
		});
	}

	@Override
	public URIChooser buildChooser() {
		return null;
	}
}

