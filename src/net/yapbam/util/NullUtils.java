package net.yapbam.util;

public final class NullUtils {
	private NullUtils() {}
	
	public static boolean areEquals(Object o1, Object o2) {
		if (o1==null) {
			return (o2==null);
		} else if (o2 == null) {
			return false;
		} else {
			return o1.equals(o2);
		}
	}
}
