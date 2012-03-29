package net.yapbam.gui.widget;

import javax.swing.JList;
import javax.swing.ListModel;

@SuppressWarnings("serial")
/** A JList that auto scrolls when its selected index is programaticaly changed. */
public class AutoScrollJList<E> extends JList<E> {
	public AutoScrollJList() {
		super();
		setAutoscrolls(true);
	}

	public AutoScrollJList(ListModel<E> model) {
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