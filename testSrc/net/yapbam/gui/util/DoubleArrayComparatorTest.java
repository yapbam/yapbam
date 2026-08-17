package net.yapbam.gui.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DoubleArrayComparatorTest {
	private final DoubleArrayComparator comparator = new DoubleArrayComparator();

	@Test
	public void looksAtSecondElementWhenFirstElementsAreEqual() {
		// Same amount (index 0), different sub-amount (index 1): must not be "equal"
		double[] o1 = {1.0, 2.0};
		double[] o2 = {1.0, 3.0};
		assertTrue("o1 should be lower than o2 since 2.0<3.0 on the second element",
				comparator.compare(o1, o2) < 0);
		assertTrue("comparator must be antisymmetric",
				comparator.compare(o2, o1) > 0);
	}

	@Test
	public void returnsZeroWhenAllCompareElementsAreEqual() {
		double[] o1 = {1.0, 2.0};
		double[] o2 = {1.0, 2.0};
		assertEquals(0, comparator.compare(o1, o2));
	}

	@Test
	public void shorterArrayIsLowerWhenPrefixIsEqual() {
		double[] o1 = {1.0};
		double[] o2 = {1.0, 2.0};
		assertTrue(comparator.compare(o1, o2) < 0);
		assertTrue(comparator.compare(o2, o1) > 0);
	}

	@Test
	public void isConsistentWithNaN() {
		// Math.signum(NaN) cast to int used to always return 0, breaking the Comparator contract.
		// Double.compare-based ordering treats NaN as consistently greater than any other value.
		double[] withNaN = {Double.NaN};
		double[] normal = {5.0};
		assertTrue(comparator.compare(withNaN, normal) > 0);
		assertTrue(comparator.compare(normal, withNaN) < 0);
		assertEquals(0, comparator.compare(withNaN, new double[] {Double.NaN}));
	}
}
