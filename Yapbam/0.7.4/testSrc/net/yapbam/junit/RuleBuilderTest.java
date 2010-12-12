package net.yapbam.junit;

import static org.junit.Assert.*;

import net.yapbam.gui.graphics.balancehistory.RuleBuilder;

import org.junit.Test;

public class RuleBuilderTest {

	@Test
	public void testGetNormalizedStep() {
		assertEquals(1, RuleBuilder.getNormalizedStep(1), 0.01);
		assertEquals(2.5, RuleBuilder.getNormalizedStep(2.5), 0.01);
		assertEquals(75, RuleBuilder.getNormalizedStep(52), 1);
		assertEquals(40, RuleBuilder.getNormalizedStep(40), 1);
		assertEquals(40, RuleBuilder.getNormalizedStep(26), 1);
		assertEquals(25, RuleBuilder.getNormalizedStep(24), 1);
		assertEquals(5000, RuleBuilder.getNormalizedStep(4200), 1);
		assertEquals(100, RuleBuilder.getNormalizedStep(76), 1);
		assertEquals(0.4, RuleBuilder.getNormalizedStep(0.32), 0.001);
		assertEquals(0.075, RuleBuilder.getNormalizedStep(0.054), 0.001);
		assertEquals(0.00025, RuleBuilder.getNormalizedStep(0.000219), 0.0000000001);
	}

}
