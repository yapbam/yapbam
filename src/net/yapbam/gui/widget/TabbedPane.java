package net.yapbam.gui.widget;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.Icon;

import terai.xrea.jp.DnDTabbedPane;

/** A JTabbedPane compatible with drag & drop, and with the ability to keep
 * track of the tab moves.
 * <br>Each time a tab is added, an number (the current number of tabs in the pane) is gave to it.
 * <br>The getPositions() method returns these ids in the same order than the tabs.
 * <br><b>Please note</b> that, when you remove a tab from the pane, all indexes of tabs that were
 * added after the removed tab are decreased.
 * <br>panel.add(p1); // p1 has id 0
 * <br>panel.add(p2); // p2 has id 1
 * <br>panel.remove(0); // p1 is removed, now, p2 has id 0
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 * @see #getPositions()
 */
@SuppressWarnings("serial")
public class TabbedPane extends DnDTabbedPane {
	private ArrayList<Integer> positions;
	private boolean movingTab;
	
	/** Constructor.
	 */
	public TabbedPane() {
		super();
		positions = new ArrayList<Integer>();
		this.movingTab = false;
	}

	@Override
	protected void convertTab(int prev, int next) {
//		System.out.println ("_____________________");
//		System.out.println ("Old positions : "+intArrayToString(getPositions()));
		System.out.println (prev+"/"+this.getTabCount()+" -> "+next+"/"+this.getTabCount());
		if (next>prev) {
			positions.add(next-1, positions.remove(prev));
		} else if (next!=prev){
			positions.add(next, positions.remove(prev));
		}
		this.movingTab = true;
		super.convertTab(prev, next);
		this.movingTab = false;
		System.out.println ("New positions : "+intArrayToString(getPositions()));
	}

	@Override
	public void insertTab(String title, Icon icon, Component component, String tip, int index) {
		if (!movingTab) {
			System.out.println ("insert tab is called");
			positions.add(index, new Integer(getTabCount()));
		}
		super.insertTab(title, icon, component, tip, index);
	}

	@Override
	public void removeTabAt(int index) {
		if (!movingTab) {
			System.out.println ("remove tab is called "+index);
			int removed = positions.remove(index);
			for (int i = 0; i < positions.size(); i++) {
				if (positions.get(i)>removed) {
					positions.set(i, positions.get(i)-1);
				}
			}
		}
		super.removeTabAt(index);
	}

	/** Gets the tabs id in the same order as the tabs into the pane.
	 * @return an int array.
	 */
	public int[] getPositions() {
		int[] result = new int[positions.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = positions.get(i);
		}
		return result;
	}
	
	public void setOrder(int[] indexes) {
		
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
