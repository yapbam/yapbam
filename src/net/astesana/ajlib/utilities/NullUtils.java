package net.astesana.ajlib.utilities;

/** Utility to securely test whether two instances are equals ... even if one of them is null.
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public final class NullUtils {
	private NullUtils() {}
	
	/** Tests whether two objects are equals or not.
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

	/** Compares two objects.
	 * <br>The arguments may be null.
	 * @param o1 the first object
	 * @param o2 the second object
	 * @param nullIsLowest true if null if lower than any other value, false if it is greater.
	 * @return o1.compareTo(o2) assuming that null is the lowest possible instance.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static int compareTo(Comparable o1, Comparable o2, boolean nullIsLowest) {
		if (o1!=null) {
			return o2==null?(nullIsLowest?1:-1):o1.compareTo(o2);
		} else {
			return o2==null?0:(nullIsLowest?-1:1);
		}
	}
}
