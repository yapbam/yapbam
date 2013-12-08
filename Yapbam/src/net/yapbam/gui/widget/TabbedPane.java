package net.yapbam.gui.widget;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

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
 * @see #getIds()
 */
@SuppressWarnings("serial")
public class TabbedPane extends DnDTabbedPane {
	private List<Integer> positions;
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
//		System.out.println (prev+"/"+this.getTabCount()+" -> "+next+"/"+this.getTabCount());
		// If the tab was moved outside of the tabs region => Do nothing
		if (next==-1) {
			return;
		}
		if (next>prev) {
			positions.add(next-1, positions.remove(prev));
		} else if (next!=prev){
			positions.add(next, positions.remove(prev));
		}
		this.movingTab = true;
		super.convertTab(prev, next);
		this.movingTab = false;
//		System.out.println ("New positions : "+ArrayUtils.toString(getIds()));
	}

	@Override
	public void insertTab(String title, Icon icon, Component component, String tip, int index) {
		if (!movingTab) {
			positions.add(index, Integer.valueOf(getTabCount()));
		}
		super.insertTab(title, icon, component, tip, index);
	}

	@Override
	public void removeTabAt(int index) {
		if (!movingTab) {
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
	public int[] getIds() {
		int[] result = new int[positions.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = positions.get(i);
		}
		return result;
	}
	
	/** Reorders the tabs.
	 * <br><b>WARNING:</b>This method may change the selected tab.
	 * @param ids an array of ids, in the order we want tabs to appear.
	 */
	public void setOrder(int[] ids) {
		for (int i = 0; i < ids.length; i++) {
			convertTab(getIndexOf(ids[i]), i);
		}
	}
	
	/** Gets the position of a tab identified by its id.
	 * @param id A tab id
	 * @return a positive integer if there is a tab with the id in this, -1 if not.
	 */
	public int getIndexOf(int id) {
		for (int i = 0; i < positions.size(); i++) {
			if (positions.get(i)==id) {
				return i;
			}
		}
		return -1;
	}
	
	/** Gets the id of a tab.
	 * @param index the view index of the tab
	 * @return the id of the tab
	 */
	public int getId(int index) {
		return positions.get(index);
	}
}
