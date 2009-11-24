package net.yapbam.gui.widget;

import java.util.Calendar;
import java.util.Date;

import net.yapbam.util.NullUtils;

import org.jfree.ui.DateChooserPanel;

public class CalendarDateChooser extends DateChooserPanel {
	public static final String DATE_PROPERTY = "DATE_PROPERTY";
	private static final long serialVersionUID = 1L;

	public CalendarDateChooser() {
	}

	public CalendarDateChooser(Calendar calendar, boolean controlPanel) {
		super(calendar, controlPanel);
	}

	@Override
	public void setDate(Date theDate) {
		Date old = this.getDate();
		if (!NullUtils.areEquals(theDate, old)) {
			super.setDate(theDate);
			this.firePropertyChange(DATE_PROPERTY, old, theDate);
		}
	}
}
