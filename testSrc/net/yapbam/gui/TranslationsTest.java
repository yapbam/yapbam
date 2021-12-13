package net.yapbam.gui;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

public class TranslationsTest {

	@Test
	public void test() throws IOException {
		Map<String, List<String>> missing = getMissing("src/Resources");
		assertTrue("Missing resources: "+missing, missing.isEmpty());
		
		missing = getMissing("src/net/yapbam/gui/tools/messages");
		assertTrue("Missing resources: "+missing, missing.isEmpty());	}

	private Map<String, List<String>> getMissing(String path) throws IOException {
		final File f = new File(path);
		final String[] translations = f.getParentFile().list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith(f.getName()+"_") && name.endsWith(".properties");
			}
		});
		
		final Set<String> known = new HashSet<String>();
		final Enumeration<String> knownEnum = getKeys(new File(path+".properties"));
		while (knownEnum.hasMoreElements()) {
			known.add(knownEnum.nextElement());
		}

		Map<String, List<String>> result = new HashMap<String, List<String>>();
		for (String translation : translations) {
			final int index = f.getName().length();
			final String lng = translation.substring(index+1, index+3);
			final Enumeration<String> keys = getKeys(new File(f.getParent(),translation));
			while (keys.hasMoreElements()) {
				final String key = keys.nextElement();
				if (!known.contains(key)) {
					add(result, key, lng);
				}
			}
		}
		return result;
	}

	private void add(Map<String, List<String>> result, String key, String lng) {
		List<String> languages = result.get(key);
		if (languages==null) {
			languages = new ArrayList<String>();
			result.put(key, languages);
		}
		languages.add(lng);
	}

	private Enumeration<String> getKeys(File translationFile) throws FileNotFoundException, IOException {
		final Properties content = new Properties();
		final InputStream stream=new FileInputStream(translationFile);
		try {
			content.load(stream);
		} finally {
			stream.close();
		}
		final Enumeration<Object> keys = content.keys(); 
		return new Enumeration<String>() {
			@Override
			public boolean hasMoreElements() {
				return keys.hasMoreElements();
			}

			@Override
			public String nextElement() {
				return (String) keys.nextElement();
			}
		};
	}
}
