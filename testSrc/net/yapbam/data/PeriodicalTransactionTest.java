package net.yapbam.data;

import java.util.ArrayList;
import java.util.Date;

import net.yapbam.date.helpers.MonthDateStepper;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PeriodicalTransactionTest {
	private Date firstJanuary;
	private Date firstFebruary;
	private Date firstMarch;
	private Date firstApril;
	

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {
		firstJanuary = new Date(112, 0, 1);
		firstFebruary = new Date(112, 1, 1);
		firstMarch = new Date(112, 2, 1);
		firstApril = new Date(112, 3, 1);
	}

	@Test (expected= IllegalArgumentException.class)
	public void dateAfterEnd() {
		MonthDateStepper ds = new MonthDateStepper(1, 1, firstFebruary);
		@SuppressWarnings("unused")
		PeriodicalTransaction pt = new PeriodicalTransaction("test", null, 10.0, new Account("test",0.0), Mode.UNDEFINED, Category.UNDEFINED, new ArrayList<SubTransaction>(),
				firstMarch, true, ds);
	}

	@Test (expected= IllegalArgumentException.class)
	public void nextIsNullAndEnabled() {
		MonthDateStepper ds = new MonthDateStepper(1, 1, firstFebruary);
		@SuppressWarnings("unused")
		PeriodicalTransaction pt = new PeriodicalTransaction("test", null, 10.0, new Account("test",0.0), Mode.UNDEFINED, Category.UNDEFINED, new ArrayList<SubTransaction>(),
				null, true, ds);
	}

	@Test
	public void doTest() {
		MonthDateStepper ds = new MonthDateStepper(1, 1, firstMarch);
		
		// Test a cool standard periodical transaction
		PeriodicalTransaction pt = new PeriodicalTransaction("test", null, 10.0, new Account("test",0.0), Mode.UNDEFINED, Category.UNDEFINED, new ArrayList<SubTransaction>(),
				firstFebruary, true, ds);
		assertFalse(pt.hasPendingTransactions(firstJanuary));
		assertTrue(pt.hasPendingTransactions(firstFebruary));
		assertTrue(pt.hasPendingTransactions(firstMarch));
		assertTrue(pt.hasPendingTransactions(firstApril));
	}
}
