package net.yapbam.gui;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

import com.fathzer.soft.ajlib.utilities.StringUtils;

public class TranslationsTest {

	@Test
	public void test() throws IOException {
		final String[] path = {
			"src/Resources",
			"src/net/yapbam/gui/tools/messages"
		};
		
		Map<String, List<String>> missing = null;
		for(String location : path) {
			missing = getMissing(location);
			assertTrue("Missing resources: " + missing, missing.isEmpty());
		}
		
		for(String location : path) {
			missing = getEpoorlyFormatted(location);
			assertTrue("Wrong resources: " + missing, missing.isEmpty());
		}
	}

	private Map<String, List<String>> getMissing(String path) throws IOException {
		final File f = new File(path);
		
		final Set<String> known = new HashSet<String>();
		final Enumeration<String> knownEnum = getKeys(new File(path+".properties"));
		while (knownEnum.hasMoreElements()) {
			known.add(knownEnum.nextElement());
		}

		Map<String, List<String>> result = new HashMap<String, List<String>>();
		for (String translation : getFiles(f)) {
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

	private Map<String, List<String>> getEpoorlyFormatted(String path) throws IOException {
		final File f = new File(path);

		Map<String, List<String>> result = new HashMap<String, List<String>>();
		for (String translation : getFiles(f)) {
			MessageFormat messageFormat = new MessageFormat(StringUtils.EMPTY);
			for(String pattern : getValues(new File(f.getParent(),translation))) {
				try {
					messageFormat.applyPattern(pattern);
				} catch(IllegalArgumentException ex) {
					add(result, translation, pattern);
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
	
	private String[] getValues(File translationFile) throws FileNotFoundException, IOException {
		final Properties content = new Properties();
		final InputStream stream = new FileInputStream(translationFile);
		try {
			content.load(stream);
		} finally {
			stream.close();
		}
		final Collection<Object> values = content.values();
		return values.toArray(new String[values.size()]);
	}
	
	private String[] getFiles(final File path) {
		return path.getParentFile().list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith(path.getName()+"_") && name.endsWith(".properties");
			}
		});
	}
	
}
