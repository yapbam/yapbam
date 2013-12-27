package net.yapbam.gui.statistics;

import org.jfree.chart.resources.JFreeChartResources;
import org.jfree.resources.JCommonResources;

/** A Maven shade helper class.
 * <br>JFreechart reference two classes not statically. This causes this required classes
 * to be removed from minimized jar by Maven Shade plugin.
 * <br>This class simply restores a static reference to solve the problem.  
 */
final class MavenShadeHelper {
	static {
	    @SuppressWarnings ("unused") Class<?>[] classes = new Class<?>[] {
            JFreeChartResources.class, JCommonResources.class
	    };
	}
}
