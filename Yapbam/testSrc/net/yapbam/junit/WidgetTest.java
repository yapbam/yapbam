package net.yapbam.junit;

import java.awt.BorderLayout;
import java.util.Date;
import java.util.Locale;

import javax.swing.JDialog;

import org.junit.Test;

import com.fathzer.soft.ajlib.swing.widget.date.DateWidget;

import static org.junit.Assert.*;

import net.yapbam.gui.widget.CurrencyWidget;

public class WidgetTest {
	private PropertyChangeSpy listener = new PropertyChangeSpy();
	
/*	@Test
	public void test() {
		DateWidgetPanel datePanel = new DateWidgetPanel();
		datePanel.addPropertyChangeListener(DateWidgetPanel.DATE_PROPERTY, listener);
		AmountWidget widget = new AmountWidget();
		widget.addPropertyChangeListener(AmountWidget.VALUE_PROPERTY, listener);
		
		JDialog frame = new JDialog ();
		frame.setModal(true);
		frame.setLayout(new BorderLayout());
		widget.setEmptyAllowed(true);
		widget.setMinValue(0.0);
		widget.setValue(100.0);
		datePanel.setDate(new Date());
		frame.add(widget, BorderLayout.NORTH);
		frame.add(datePanel, BorderLayout.SOUTH);
		frame.pack();
		frame.setVisible(true);
//		assertNull(widget.getValue());
		listener.lastEvent=null;
//		assertNotNull(listener.lastEvent);
//		assertNull(listener.lastEvent.getOldValue());
//		assertEquals(listener.lastEvent.getNewValue(), 0);
	}*/
	
	@Test
	public void testAmountWidget() {
		testAmountWidget(new Locale("FR","fr"), "5,25", 5.25);
		testAmountWidget(new Locale("FR","be"), "5,25", 5.25);
		testAmountWidget(new Locale("PT","br"), "5,25", 5.25);
		testAmountWidget(new Locale("EN","us"), "5.25", 5.25);
	}

	private void testAmountWidget(Locale locale, String str, double value) {
		CurrencyWidget widget = new CurrencyWidget(locale);
		widget.setText(str);
		assertTrue (widget.getValue()==value);
//		System.out.println (locale+" is ok");
	}
}
