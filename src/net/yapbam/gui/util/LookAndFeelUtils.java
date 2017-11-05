package net.yapbam.gui.util;

public abstract class LookAndFeelUtils {

	private LookAndFeelUtils() {
		super();
	}

	public static boolean isValid(String lafName) {
		return !"Mac OS X".equals(lafName); //$NON-NLS-1$
	}
}
