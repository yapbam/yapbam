package net.yapbam.data;

import static org.junit.Assert.*;

import java.util.Date;

import net.yapbam.data.BalanceHistory;
import net.yapbam.data.BalanceHistoryElement;

import org.junit.Test;

@SuppressWarnings("deprecation")
public class BalanceHistoryTest {
	private static final Date dBeforeUnix = new Date(-1000,1,1); // A date before the Unix start of time (1/1/1970)

	@Test
	public void testBalanceHistory() {		
		Date date1 = new Date(109,5,1);
		BalanceHistory history = new BalanceHistory(0);
		assertEquals(0, history.getBalance(date1), 0.01);
		
		history.add(10, null);
		assertEquals(10, history.getMinBalance(), 0.01);
		assertEquals(10, history.getMaxBalance(), 0.01);
		assertEquals(10, history.getBalance(date1), 0.01);
		assertEquals(1, history.size());
		
		Date date2 = new Date(109,5,15);
		history.add(-20, date2);
		assertEquals(-10, history.getMinBalance(), 0.01);
		assertEquals(10, history.getMaxBalance(), 0.01);
		assertEquals(10, history.getBalance(date1), 0.01);
		assertEquals(-10, history.getBalance(date2), 0.01);
		assertEquals(2, history.size());

		Date date3 = new Date(109,5,30);
		history.add(30, date3);
		assertEquals(-10, history.getMinBalance(), 0.01);
		assertEquals(20, history.getMaxBalance(), 0.01);
		assertEquals(10, history.getBalance(date1), 0.01);
		assertEquals(-10, history.getBalance(date2), 0.01);
		assertEquals(20, history.getBalance(date3), 0.01);
		assertEquals(3, history.size());

		history.add(5, date2);
		assertEquals(-5, history.getMinBalance(), 0.01);
		assertEquals(25, history.getMaxBalance(), 0.01);
		assertEquals(10, history.getBalance(date1), 0.01);
		assertEquals(-5, history.getBalance(date2), 0.01);
		assertEquals(25, history.getBalance(date3), 0.01);
		assertEquals(3, history.size());
		
		history.add(15, date2);
		assertEquals(2, history.size());
		
		history.add(5,dBeforeUnix);
		assertEquals(3, history.size());
		
		Date date = new Date(108,1,1);
		assertEquals(15, history.getMaxBalance(date), 0.01);
	}
	
	@Test
	public void testElement() {
		BalanceHistoryElement element = new BalanceHistoryElement(0, new Date(110,0,1), new Date(111,0,1));
		assertTrue(element.getRelativePosition(new Date(109,0,1))<0);
		assertEquals(0,element.getRelativePosition(new Date(110,5,1)));
		assertEquals(0,element.getRelativePosition(new Date(110,0,1)));
		assertTrue(element.getRelativePosition(new Date(111,0,1))>0);
		assertTrue(element.getRelativePosition(new Date(112,0,1))>0);
		
		// Test if a date before unix start of times is before BalanceHistoryElement start of times or not
		element = new BalanceHistoryElement(0, null, new Date(111,0,1));
		assertEquals(0,element.getRelativePosition(dBeforeUnix));
	}
}
