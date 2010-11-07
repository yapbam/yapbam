package net.yapbam.util;

/** Utility to securely test whether two instances are equals ... even if one of them is null.
 * @author Fathzer
 * <BR>License : GPL v3
 */
public final class NullUtils {
	private NullUtils() {}
	
	/** Tests whetwer two objects are equals or not.
	 * <br>The arguments may be null.
	 * @param o1 the first object
	 * @param o2 the second object
	 * @return true if both objects are null or if the first object is equals to the second (with the equal method of first object).
	 */
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
