package net.yapbam.ihm.transactiontable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.yapbam.ihm.LocalizationData;

public class DateRenderer extends ObjectRenderer {
	private static final long serialVersionUID = 1L;
	private static final DateFormat FORMATER = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, LocalizationData.getLocale());
	
	public DateRenderer () {
		super();
		this.setHorizontalAlignment(CENTER);
	}

	@Override
    public void setValue(Object value) {
		setText(FORMATER.format(value));
    }
}
