package net.yapbam.gui.widget;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JTextField;

import net.yapbam.util.NullUtils;

/** This class allows the user to just enter a day, or a day and a month, instead of a complete date (day, month, year)
 * It auto completes the typed date with the current month and year.
 * This field allows you to define what an empty field means. By default, an empty field means a null date, but, using the
 * setEmptyDate method, you can change this behavior.
 * This field has an inputVerifier in order to prevent entering something that is not a date in the field. By default, an empty
 * field is allowed, you can change that calling setIsEmptyNullDateValid. Keep in mind that if you called the setEmptyDate method
 * with a non null argument, the empty field will always be valid.
 * The up/down arrow keys increments/decrements the date.
 */
public class DateWidget extends JTextField {
	private static final long serialVersionUID = 1L;
	public static final String DATE_PROPERTY = "date";
		
	private DateFormat formatter;
	private Date date;
	private boolean valid;
	private Date emptyValue;
	private boolean isEmptyNullDateValid;
	
	/** Constructor.
	 * Creates a new Date widget. The date is set to today, the empty date is set to null.
	 * @see #setEmptyDate(Date)
	 */
	public DateWidget() {
		this(null);
	}
	
	@Override
	public void setLocale(Locale l) {
		super.setLocale(l);
		this.formatter = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, l);
		if (date!=null) this.setText(formatter.format(date));
	}

	/** Constructor.
	 * Creates a new Date widget. The date is set to today.
	 * @param emptyDate The date to be set if the field becomes empty
	 * @see #setEmptyDate(Date)
	 */
	public DateWidget(Date emptyDate) {
		super();
		this.setColumns(6);
		this.isEmptyNullDateValid = true;
		this.emptyValue = emptyDate;
		formatter = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT);
		 // Set the field to today's date (we don't use setDate because new Date() returns a date with hours, minutes and seconds fields not always set to 0).
		this.setText(formatter.format(new Date()));
		this.setInputVerifier(new DefaultInputVerifier() {
			protected boolean check(JComponent input, boolean change) {
				if (change && (date!=null)) setText(formatter.format(date));
				return valid;
			}
		});
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int increment = 0;
				if (e.getKeyCode()==KeyEvent.VK_DOWN) {
					// Set the date to next day after the current date
					increment = -1;
				} else if (e.getKeyCode()==KeyEvent.VK_UP) {
					// Set the date to previous day before the current date
					increment = 1;						
				}
				if (increment!=0) {
					if (getDate()!=null) {
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(getDate());
						calendar.add(Calendar.DATE, increment);
						setDate(calendar.getTime());
					}
				} else {
					updateDate();
				}
			}
		});
	}
	
	/** Set the meaning of an empty field.
	 * @param date The date that a is equivalent to an empty field.
	 * By default, this date is null.
	 */
	public void setEmptyDate(Date date) {
		this.emptyValue = date;
		if (this.getText().trim().length()==0) updateDate();
	}
	
	/** Allow/Disallow this field to be empty (if it means a null date)
	 * Keep in mind that a now null date for an empty field is always valid.
	 * @param valid
	 * @see #setEmptyDate(Date)
	 */
	public void setIsEmptyNullDateIsValid(boolean valid) {
		this.isEmptyNullDateValid = valid;
		if (this.getText().trim().length()==0) updateDate();
	}

	private void updateDate() {
		String text = this.getText().trim();
		if (text.length()==0) {
			internalSetDate(emptyValue);
			this.valid = (emptyValue!=null) || isEmptyNullDateValid;
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
			this.valid = changed!=null;
			internalSetDate(changed);
		}
	}

	/** Get the widget current date.
	 * @return The date. If the value contained by the field is not valid, returns null.
	 */
	public Date getDate() {
		return this.date;
	}

	/** Set the widget date.
	 * The text field is updated.
	 * @param date The date to set.
	 */
	public void setDate(Date date) {
		if (internalSetDate(date)) {
			this.setText(date==null?"":formatter.format(date));
		}
	}

	@Override
	public void setText(String t) {
		super.setText(t);
		updateDate();
	}

	/** Set the date without changing the content of the TextField.
	 * This method does nothing if the date is equals to the current widget date.
	 * @param date
	 * @return true if the value was changed
	 */
	private boolean internalSetDate(Date date) {
		// Does nothing if the this date is equals to current widget date
		// Be aware of null values
		if (NullUtils.areEquals(date, this.date)) return false;
		Date old = this.date;
		this.date = date;
		firePropertyChange(DATE_PROPERTY, old, date);
		return true;
	}
}
