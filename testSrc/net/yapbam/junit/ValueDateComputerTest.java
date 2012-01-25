package net.yapbam.junit;

import static org.junit.Assert.*;

import java.util.Date;

import net.yapbam.date.helpers.DateStepper;
import net.yapbam.date.helpers.DayDateStepper;
import net.yapbam.date.helpers.DeferredValueDateComputer;
import net.yapbam.date.helpers.MonthDateStepper;

import org.junit.Test;

public class ValueDateComputerTest {

	@SuppressWarnings("deprecation")
	@Test
	public void test() {
		MonthDateStepper mdi = new MonthDateStepper(1, 30);
		assertEquals(new Date(109,1,28), mdi.getNextStep(new Date(109,0,31)));
		assertEquals(new Date(109,2,30), mdi.getNextStep(new Date(109,1,28)));
		
		DateStepper dvc = new DeferredValueDateComputer(15,29);
		assertEquals(new Date(109,3,29), dvc.getNextStep(new Date(109,3,5)));
		assertEquals(new Date(109,3,29), dvc.getNextStep(new Date(109,3,15)));
		assertEquals(new Date(109,3,29), dvc.getNextStep(new Date(109,2,18)));
		assertEquals(new Date(109,1,28), dvc.getNextStep(new Date(109,1,12)));
		dvc = new DeferredValueDateComputer(1,1);
		assertEquals(new Date(110,0,1), dvc.getNextStep(new Date(109,11,30)));
		dvc = new DayDateStepper(3, null);
		assertEquals(new Date(109,3,16), dvc.getNextStep(new Date(109,3,13)));
		assertEquals(new Date(109,2,2), dvc.getNextStep(new Date(109,1,27)));
		assertEquals(new Date(110,0,3), dvc.getNextStep(new Date(109,11,31)));
		dvc = new DeferredValueDateComputer(15,31);
		assertEquals(new Date(109,4,31), dvc.getNextStep(new Date(109,4,15)));
		assertEquals(new Date(109,5,30), dvc.getNextStep(new Date(109,4,16)));
		assertEquals(new Date(109,1,28), dvc.getNextStep(new Date(109,0,16)));
		dvc = new DeferredValueDateComputer(15,1);
		assertEquals(new Date(109,5,1), dvc.getNextStep(new Date(109,4,15)));
		assertEquals(new Date(109,5,1), dvc.getNextStep(new Date(109,4,1)));
		assertEquals(new Date(109,6,1), dvc.getNextStep(new Date(109,4,18)));
	}

}
