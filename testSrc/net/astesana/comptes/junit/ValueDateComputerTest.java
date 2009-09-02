package net.astesana.comptes.junit;

import static org.junit.Assert.*;

import java.util.Date;

import net.astesana.comptes.date.helpers.DateStepper;
import net.astesana.comptes.date.helpers.DeferredValueDateComputer;
import net.astesana.comptes.date.helpers.MonthDateStepper;
import net.astesana.comptes.date.helpers.DayDateStepper;

import org.junit.Test;

public class ValueDateComputerTest {

	@SuppressWarnings("deprecation")
	@Test
	public void test() {
		MonthDateStepper mdi = new MonthDateStepper(1, 30);
		assertEquals(mdi.getNextStep(new Date(109,0,31)), new Date(109,1,28));
		assertEquals(mdi.getNextStep(new Date(109,1,28)), new Date(109,2,30));
		
		DateStepper dvc = new DeferredValueDateComputer(15,29);
		assertEquals(dvc.getNextStep(new Date(109,3,5)), new Date(109,3,29));
		assertEquals(dvc.getNextStep(new Date(109,3,15)), new Date(109,3,29));
		assertEquals(dvc.getNextStep(new Date(109,2,18)), new Date(109,3,29));
		assertEquals(dvc.getNextStep(new Date(109,1,12)), new Date(109,1,28));
		dvc = new DeferredValueDateComputer(1,1);
		assertEquals(dvc.getNextStep(new Date(109,11,30)), new Date(110,0,1));
		dvc = new DayDateStepper(3);
		assertEquals(dvc.getNextStep(new Date(109,3,13)), new Date(109,3,16));
		assertEquals(dvc.getNextStep(new Date(109,1,27)), new Date(109,2,2));
		assertEquals(dvc.getNextStep(new Date(109,11,31)), new Date(110,0,3));
		dvc = new DeferredValueDateComputer(15,31);
		assertEquals(dvc.getNextStep(new Date(109,4,15)), new Date(109,4,31));
		assertEquals(dvc.getNextStep(new Date(109,4,16)), new Date(109,5,30));
		assertEquals(dvc.getNextStep(new Date(109,0,16)), new Date(109,1,28));
		dvc = new DeferredValueDateComputer(15,1);
		assertEquals(dvc.getNextStep(new Date(109,4,15)), new Date(109,5,1));
		assertEquals(dvc.getNextStep(new Date(109,4,1)), new Date(109,5,1));
		assertEquals(dvc.getNextStep(new Date(109,4,18)), new Date(109,6,1));
	}

}
