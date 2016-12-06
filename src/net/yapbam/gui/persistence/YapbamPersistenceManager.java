package net.yapbam.gui.persistence;

import java.io.IOException;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.utilities.StringUtils;

import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.persistence.classpath.ClasspathPersistenceAdapter;
import net.yapbam.gui.persistence.dropbox.YapbamDropboxPersistenceAdapter;
import net.yapbam.gui.persistence.file.FilePersistenceAdapter;

public class YapbamPersistenceManager extends PersistenceManager {
	public static PersistenceManager MANAGER = new YapbamPersistenceManager();
	
	private YapbamPersistenceManager() {
		super();
		add(new FilePersistenceAdapter());
		try {
			add(new YapbamDropboxPersistenceAdapter());
			add(new ClasspathPersistenceAdapter());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		// Load adapters under development
		String testedAdapter = System.getProperty("testedPersistenceAdapter.className"); //$NON-NLS-1$
		if (testedAdapter!=null) {
			String[] testedAdapters = StringUtils.split(testedAdapter, ',');
			for (String className : testedAdapters) {
				if (className.length()!=0) {
					try {
						@SuppressWarnings("unchecked")
						Class<? extends PersistenceAdapter> pClass = (Class<? extends PersistenceAdapter>) Class.forName(className);
						add(pClass.newInstance());
					} catch (Exception e) {
						ErrorManager.INSTANCE.display(null, e, Formatter.format(LocalizationData.get("persitencePlugin.load.error"), className)); //$NON-NLS-1$
					}
				}
			}
		}
	}
}
