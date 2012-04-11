package net.yapbam.localization;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Tester {
	/**
	 * @param args exemple : src/Resources.properties src/Resources_fr.properties src/Resources_pt.properties src/Resources_de.properties
	 */
	public static void main(String[] args) {
		if (args.length==0) {
			System.out.println ("usage : java "+Tester.class.getName()+" refFile [translatedFile ...]");
			System.exit(0);
		}
		try {
			PropertyFile pFile = new PropertyFile(args[0]);
			check(args[0], pFile);
			for (int i = 1; i < args.length; i++) {
				check(args[i], pFile, new PropertyFile(args[i]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void check(String name, PropertyFile pFile) {
		System.out.println ("checking "+name);
		for (String key : pFile) {
			int[] lines = pFile.getLines(key);
			if (lines.length>1) {
				System.out.println ("  "+key+" is duplicated in lines "+toString(lines));
			}
		}
	}
	
	private static String toString(int[] lines) {
		StringBuilder buf = new StringBuilder();
		for (int i : lines) {
			if (buf.length()>0) buf.append(", ");
			buf.append(i);
		}
		return buf.toString();
	}

	public static void check(String name, PropertyFile refFile, PropertyFile pFile) {
		check(name, pFile);
		for (String key : pFile) {
			if (refFile.getLines(key).length==0) {
				System.out.println ("  "+key+" is not in reference");
			}
		}
		for (String key : refFile) {
			if (pFile.getLines(key).length==0) {
				System.out.println ("  "+key+" is missing");
			}
		}
	}

	private static class PropertyFile extends ArrayList<String> {
		private HashMap<String, ArrayList<Integer>> keyToLines;
		
		PropertyFile(String path) throws IOException {
			super();
			keyToLines = new HashMap<String,ArrayList<Integer>>();
			int lineCounter = 1;
			BufferedReader reader = new BufferedReader(new FileReader(path));
			try {
				for (String line=reader.readLine(); line!=null; line=reader.readLine()) {
					line = line.trim();
					if ((line.length()>0) && (!line.startsWith("#"))) {
						String key = new StringTokenizer(line,"=").nextToken();
						if (key.length()!=0) {
							add(key);
							ArrayList<Integer> lines = keyToLines.get(key);
							if (lines==null) {
								lines = new ArrayList<Integer>();
								keyToLines.put(key, lines);
							}
							lines.add(lineCounter);
						}
					}
					lineCounter++;
				}
			} finally {
				reader.close();
			}
		}
		
		public int[] getLines (String key) {
			ArrayList<Integer> list = keyToLines.get(key);
			if (list==null) return new int[0];
			int[] result = new int[list.size()];
			for (int i = 0; i < result.length; i++) {
				result[i] = list.get(i);
			}
			return result;
		}
	}

}
