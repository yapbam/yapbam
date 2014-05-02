package net.yapbam.gui.util;

import java.io.Serializable;
import java.util.Comparator;

public class DoubleArrayComparator implements Comparator<double[]>, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public int compare(double[] o1, double[] o2) {
		//FIXME What a strange order !!! Bug if one has no elements !!!
		return (int) Math.signum(o1[0]-o2[0]);
	}
}