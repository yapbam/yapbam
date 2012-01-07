package net.yapbam.relnotes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import net.yapbam.util.StringUtils;

public class ReleaseNotesFormatter { //LOCAL
	private boolean divOpened;
	private boolean needSeparator;
	private boolean firstOne;
	private List<String> known;
	private List<String> changes;
	private List<String> fixes;
	private List<String> currentList;
	
	private BufferedWriter writer;
	
	//TODO Localization
	private String relnotesTitle="Release notes";
	private String next="Next release";
	private String knownSingular="Known bug:";
	private String knownPlural="Known bugs:";
	private String changeSingular="Change:";
	private String changePlural="Changes:";
	private String fixSingular="Bug fix:";
	private String fixPlural="Bug fixes:";
	
	public ReleaseNotesFormatter() {
		this.divOpened = false; // Has the version opened a div element (the "not yet released" version didn't open a div element)
		this.needSeparator = false; // Is a separator needed between current version and next one
		this.firstOne = true; // Is it the first already released version ?
		this.changes = new ArrayList<String>(); // The current version's changes
		this.fixes = new ArrayList<String>(); // The current version's bug fixes
		this.known = new ArrayList<String>(); // The current version's known bugs
		this.currentList = null; // The array (changes or fix) we are currently reading
	}
	
	public synchronized void build(Reader in, Writer out) throws IOException {
		this.writer = new BufferedWriter(out);
		echo ("<h1>"+this.relnotesTitle+"</h1>");
		BufferedReader reader = new BufferedReader(in);
		for (String line=reader.readLine(); line!=null; line=reader.readLine()) {
			String[] fields = StringUtils.split(line, '\t');
			String code = fields[0].trim();
			line = fields.length>1?fields[1].trim():"";
			if (code.equals("version")) {
				openVersion(line);
			} else if (code.equals("improvement")) {
				this.currentList = this.changes;
			} else if (code.equals("fix")) {
				this.currentList = this.fixes;
			} else if (code.equals("known")) {
				this.currentList = this.known;
			} else if ((code.length()==0) && (line.length()!=0)) {
				this.currentList.add(line);
			}
		}
	}
	
	private void openVersion(String version) throws IOException {
		this.closeVersion();
		if (version.equals("next")) {
			// Version "not yet released"
			echo ("<h2>"+this.next+"</h2>");
			this.needSeparator = true; // There's always a separator between next version and others
		} else {
			// Other versions
			if (this.needSeparator) {
				echo ("<hr/>");
			}
			echo ("<div class=\"relnotes-version\"><h2>"+version+"</h2>");
			this.divOpened = true;
			this.needSeparator = this.firstOne;
			this.firstOne = false;
		}
	}
	
	private void closeVersion() throws IOException {
		this.output(this.known, "relnotes-knownBugs", this.knownSingular, this.knownPlural);
		this.output(this.changes, "relnotes-new", this.changeSingular, this.changePlural);
		this.output(this.fixes, "relnotes-bugFix", this.fixSingular, this.fixPlural);
		this.changes.clear();
		this.fixes.clear();
		this.known.clear();
		if (this.divOpened) {
			echo ("</div>");
			this.divOpened = false;
		}
	}
	
	private void output(List<String> array, String className, String singular, String plural) throws IOException {
		if (array.size()!=0) {
			String title = (array.size()==1) ? singular : plural;
			echo ("<div class=\""+className+"\"><h3>"+title+"</h3><ul>");
			for (String line : array) {
				echo ("<li>"+line+"</li>");
			}
			echo ("</ul></div>");
		}
	}

	private void echo(String string) throws IOException {
		this.writer.write(string);
		this.writer.newLine();
	}
}
