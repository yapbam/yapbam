package net.yapbam.update;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import net.yapbam.gui.LocalizationData;

public class ReleaseInfo implements Comparable<ReleaseInfo>, Serializable {
	private static final long serialVersionUID = 2256410858572541319L;

	/** An unknown release.
	 * <br>That release is before any other release regarding to compareTo method.
	 */
	public static final ReleaseInfo UNKNOWN = new ReleaseInfo("");
	
	private boolean unknown;
	private int majorRevision;
	private int minorRevision;
	private int buildId;
	private Date releaseDate;
	private String preReleaseComment;
	
	public ReleaseInfo(String rel) {
		try {
			StringTokenizer parts = new StringTokenizer(rel, " ");
			StringTokenizer tokens = new StringTokenizer(parts.nextToken(), ".");
			majorRevision = Integer.parseInt(tokens.nextToken());
			minorRevision = Integer.parseInt(tokens.nextToken());
			buildId = Integer.parseInt(tokens.nextToken());
			preReleaseComment = tokens.hasMoreElements()?tokens.nextToken():null;
			parseDate(parts);
		} catch (IllegalArgumentException e) {
			this.releaseDate = new Date(0);
			this.preReleaseComment="Invalid version format";
		} catch (NoSuchElementException e) {
			this.unknown = true;
		}
	}

	private void parseDate(StringTokenizer parts) {
		StringTokenizer tokens = new StringTokenizer(parts.nextToken(),"()/");
		try {
			int dayOfMonth = Integer.parseInt(tokens.nextToken());
			int month = Integer.parseInt(tokens.nextToken());
			int year = Integer.parseInt(tokens.nextToken());
			releaseDate = new GregorianCalendar(year, month-1, dayOfMonth).getTime();
		} catch (NumberFormatException e) {
			releaseDate = new Date(Long.MAX_VALUE);
		}
	}
	
	public int getMajorRevision() {
		return majorRevision;
	}
	
	public int getMinorRevision() {
		return minorRevision;
	}
	
	public int getBuildId() {
		return buildId;
	}
	
	public String getPreReleaseComment() {
		return this.preReleaseComment;
	}
	
	public Date getReleaseDate() {
		return releaseDate;
	}

	@Override
	public int compareTo(ReleaseInfo o) {
		if (this.unknown) {
			if (o.unknown) {
				return 0;
			} else {
				return -1;
			}
		} else if (o.unknown) {
			return 1;
		}
		int result = majorRevision - o.majorRevision;
		if (result == 0) {
			result = minorRevision - o.minorRevision; 
		}
		if (result == 0) {
			result = buildId - o.buildId;
		}
		if (result == 0) {
			result = getReleaseDate().compareTo(o.getReleaseDate());
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof ReleaseInfo)) {
			return false;
		}
		return this.compareTo((ReleaseInfo) obj)==0;
	}

	@Override
	public int hashCode() {
		if (unknown) {
			return 0;
		} else {
			return majorRevision*100 + minorRevision*100 + buildId;
		}
	}

	@Override
	public String toString() {
		if (this.unknown) {
			return "?";
		} else {
			return majorRevision+"."+minorRevision+"."+buildId+(preReleaseComment==null?"":"."+preReleaseComment)+" ("+
					SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM, LocalizationData.getLocale()).format(releaseDate)+")";
		}
	}
}
