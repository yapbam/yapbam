package net.yapbam.junit;

import static org.junit.Assert.*;

import java.util.Date;

import net.yapbam.data.BalanceHistory;

import org.junit.Test;

public class BalanceHistoryTest {

	@SuppressWarnings("deprecation")
	@Test
	public void testGetNormalizedStep() {		
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
	}
}
