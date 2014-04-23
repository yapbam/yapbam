package net.yapbam.gui.util;

import java.util.Comparator;

public class DoubleArrayComparator implements Comparator<double[]> {
	@Override
	public int compare(double[] o1, double[] o2) {
		return (int) Math.signum(o1[0]-o2[0]);
	}
}