package net.yapbam.gui.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Icon;

@SuppressWarnings("serial")
public abstract class AbstractTransactionAction extends AbstractAction {
	protected TransactionSelector selector;

	public AbstractTransactionAction(TransactionSelector selector, String name, Icon icon, String tooltip) {
		super(name, icon);
		putValue(SHORT_DESCRIPTION, tooltip);
		this.selector = selector;
		this.setEnabled(selector.getSelectedTransaction()!=null);
		this.selector.addPropertyChangeListener(TransactionSelector.SELECTED_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setEnabled(evt.getNewValue()!=null);
			}
		});
	}
}
