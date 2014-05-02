package net.yapbam.gui.util;

import java.io.Serializable;
import java.util.Comparator;

public class DoubleArrayComparator implements Comparator<double[]>, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public int compare(double[] o1, double[] o2) {
		int len = Math.min(o1.length,o2.length);
		for (int i = 0; i < len; i++) {
			int result = (int) Math.signum(o1[0]-o2[0]);
			if (result!=0) {
				return result;
			}
		}
		return o1.length-o2.length;
	}
}