package net.yapbam.junit;

import java.awt.BorderLayout;
import java.util.Date;

import javax.swing.JDialog;

import org.junit.Test;

import net.yapbam.gui.widget.AmountWidget;
import net.yapbam.gui.widget.DateWidgetPanel;

public class WidgetTest {
	private PropertyChangeSpy listener = new PropertyChangeSpy();
	
	@Test
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
	}
}
