package net.yapbam.gui.graphics.balancehistory;

/** This class is used to compute the rule step */
public abstract class RuleBuilder {
	private static final double[] GRID = new double[]{1,2,2.5,4,5,7.5,10};

	public static double getNormalizedStep(double step) {
		double log = Math.log10(step);
		int iLog = (log > 0) ? (int) log : (int) log - 1;
		double normalized = step / Math.pow(10, iLog);
		double result = -1;
		for (int i = 0; i < GRID.length; i++) {
			if (normalized <= GRID[i]) {
				result = GRID[i];
				break;
			}
		}
		result = result * Math.pow(10, iLog);
		return result;
	}
}
