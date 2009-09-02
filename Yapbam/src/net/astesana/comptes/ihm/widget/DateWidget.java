package net.astesana.comptes.ihm.widget;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JComponent;
import javax.swing.JTextField;

import net.astesana.comptes.ihm.LocalizationData;

/** This class allows the user to just enter a day, or a day and a month, instead of a complete date (day, month, year)
 * It auto completes the typed date with the current month and year.
 * @author Jean-Marc
 */
public class DateWidget extends JTextField {
	private static final long serialVersionUID = 1L;
	
	private DateFormat formatter;
	private Date date;

	public DateWidget() {
		super();
		formatter = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, LocalizationData.getLocale());
		this.setDate(new Date());
		this.setInputVerifier(new DefaultInputVerifier() {
			protected boolean check(JComponent input, boolean change) {
				DateWidget widget = (DateWidget)input;
				updateDate();
				if (change && (date!=null)) widget.setText(widget.formatter.format(date));
				return date!=null;
			}
		});
	}
	
	private void updateDate() {
		String text = this.getText();
		date = null;
		try {
			date = formatter.parse(text);
		} catch (ParseException e) {
			try {
				int day = Integer.parseInt(text);
				GregorianCalendar today = new GregorianCalendar();
				if ((day>0) && (day<=today.getActualMaximum(GregorianCalendar.DAY_OF_MONTH))) {
					date = new GregorianCalendar(today.get(GregorianCalendar.YEAR),today.get(GregorianCalendar.MONTH),day).getTime();
				}
			} catch (NumberFormatException e1) {
				try {
					text = text+"/"+new GregorianCalendar().get(GregorianCalendar.YEAR);
					date = formatter.parse(text+"/"+new GregorianCalendar().get(GregorianCalendar.YEAR));
				} catch (ParseException e2) {
				}
			}
		}
	}

	public Date getDate() {
		updateDate();
		return this.date;
	}

	public void setDate(Date date) {
		this.setText(formatter.format(date));
	}
}
