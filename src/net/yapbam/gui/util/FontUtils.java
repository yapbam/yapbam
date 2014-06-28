package net.yapbam.gui.util;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class FontUtils {
	private FontUtils() {
		super();
	}
	
	public static List<Font> getAvailableFonts(Locale locale) {
		ArrayList<Font> result = new ArrayList<Font>();
		Font[] allfonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
		for (Font font : allfonts) {
		    if (font.canDisplayUpTo(font.getFontName(locale)) == -1) {
		    	result.add(font);
		    }
		}
		return result;
	}
}
