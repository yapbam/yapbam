package net.yapbam.gui.widget;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JComponent;
import javax.swing.JTextField;

import net.yapbam.gui.LocalizationData;

/** This class allows the user to just enter a day, or a day and a month, instead of a complete date (day, month, year)
 * It auto completes the typed date with the current month and year.
 */
public class DateWidget extends JTextField {
	private static final long serialVersionUID = 1L;
	public static final String DATE_PROPERTY = "date";
	
	private DateFormat formatter;
	private Date date;

	public DateWidget() {
		super();
		formatter = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, LocalizationData.getLocale());
		this.setDate(new Date());
		this.setInputVerifier(new DefaultInputVerifier() {
			protected boolean check(JComponent input, boolean change) {
				if (change && (date!=null)) setText(formatter.format(date));
				return date!=null;
			}
		});
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				updateDate();
			}
		});
	}
	
	private void updateDate() {
		String text = this.getText().trim();
		if (text.length()==0) {
			internalSetDate(new GregorianCalendar().getTime());
		} else {
			Date changed = null;
			try {
				changed = formatter.parse(text);
			} catch (ParseException e) {
				try {
					int day = Integer.parseInt(text);
					GregorianCalendar today = new GregorianCalendar();
					if ((day>0) && (day<=today.getActualMaximum(GregorianCalendar.DAY_OF_MONTH))) {
						changed = new GregorianCalendar(today.get(GregorianCalendar.YEAR),today.get(GregorianCalendar.MONTH),day).getTime();
					}
				} catch (NumberFormatException e1) {
					try {
						text = text+"/"+new GregorianCalendar().get(GregorianCalendar.YEAR);
						changed = formatter.parse(text+"/"+new GregorianCalendar().get(GregorianCalendar.YEAR));
					} catch (ParseException e2) {
					}
				}
			}
			internalSetDate(changed);
		}
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		if (internalSetDate(date)) this.setText(date==null?"":formatter.format(date));
	}

	private boolean internalSetDate(Date date) {
		// To nothing if the this date is equals to current widget date
		if (date==null) { // Be aware of null values
			if (this.date==null) return false;
		} else if (date.equals(this.date)) return false;
		Date old = this.date;
		this.date = date;
		firePropertyChange(DATE_PROPERTY, old, date);
		return true;
	}
}
