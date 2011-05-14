package net.yapbam.gui.widget;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.Icon;

import terai.xrea.jp.DnDTabbedPane;

/** A JTabbedPane compatible with drag & drop.
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public class TabbedPane extends DnDTabbedPane {
	private ArrayList<Integer> positions;
	
	public TabbedPane() {
		super();
		positions = new ArrayList<Integer>();
	}

	@Override
	protected void convertTab(int prev, int next) {
		System.out.println ("_____________________");
		System.out.println ("Old positions : "+intArrayToString(getPositions()));
		System.out.println (prev+"/"+this.getTabCount()+" -> "+next+"/"+this.getTabCount());
		System.out.println ("New positions : "+intArrayToString(getPositions()));
		super.convertTab(prev, next);
	}

	@Override
	public void insertTab(String title, Icon icon, Component component, String tip, int index) {
		positions.add(index, new Integer(getTabCount()));
		super.insertTab(title, icon, component, tip, index);
	}

	public int[] getPositions() {
		int[] result = new int[positions.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = positions.get(i);
		}
		return result;
	}
	
	private static String intArrayToString(int[] array) {
		StringBuilder builder = new StringBuilder("[");
		for (int i = 0; i < array.length; i++) {
			if (i!=0) builder.append(", ");
			builder.append(array[i]);
		}
		builder.append("]");
		return builder.toString();
	}
}
