package net.yapbam.junit;

import static org.junit.Assert.*;

import java.util.GregorianCalendar;

import net.yapbam.date.helpers.DateStepper;
import net.yapbam.date.helpers.DayDateStepper;
import net.yapbam.date.helpers.MonthDateStepper;

import org.junit.Test;

public class DateStepperTest {

	@Test
	public void test() {
		DateStepper stepper = new MonthDateStepper(1, 10, new GregorianCalendar(2012, 1, 15).getTime());
		assertEquals(new GregorianCalendar(2012, 1 , 10).getTime(), stepper.getNextStep(new GregorianCalendar(2012, 0 , 1).getTime()));
		assertNull(stepper.getNextStep(new GregorianCalendar(2012, 1 , 10).getTime()));

		stepper = new DayDateStepper(10, new GregorianCalendar(2012, 0, 15).getTime());
		assertEquals(new GregorianCalendar(2012, 0 , 11).getTime(), stepper.getNextStep(new GregorianCalendar(2012, 0 , 1).getTime()));
		assertNull(stepper.getNextStep(new GregorianCalendar(2012, 0 , 10).getTime()));
	}

}
