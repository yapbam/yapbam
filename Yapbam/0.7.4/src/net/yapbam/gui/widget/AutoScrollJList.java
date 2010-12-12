package net.yapbam.gui.widget;

import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListModel;

@SuppressWarnings("serial")
/** A JList that autoscrolls when the selected index is programaticaly changed. */
public class AutoScrollJList extends JList {
	public AutoScrollJList() {
		super();
		setAutoscrolls(true);
	}

	public AutoScrollJList(Object[] listData) {
		super(listData);
		setAutoscrolls(true);
	}

	public AutoScrollJList(Vector<?> listData) {
		super(listData);
		setAutoscrolls(true);
	}

	public AutoScrollJList(ListModel model) {
		super(model);
		setAutoscrolls(true);
	}

	@Override
	public void setSelectedIndex(int index) {
		super.setSelectedIndex(index);
		if (index>=0) {
			ensureIndexIsVisible(index);
		} else {
			clearSelection();
		}
	}
}