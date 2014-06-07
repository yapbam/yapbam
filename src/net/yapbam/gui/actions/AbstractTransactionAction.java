package net.yapbam.gui.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import net.yapbam.gui.TransactionSelector;

@SuppressWarnings("serial")
public abstract class AbstractTransactionAction extends AbstractAction {
	protected TransactionSelector selector;

	public AbstractTransactionAction(TransactionSelector selector, String name, Icon icon, String tooltip) {
		super(name, icon);
		putValue(SHORT_DESCRIPTION, tooltip);
		this.selector = selector;
		this.updateEnabled();
		this.selector.addPropertyChangeListener(TransactionSelector.SELECTED_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateEnabled();
			}
		});
	}
	
	protected void updateEnabled() {
		this.setEnabled((this.selector!=null)&&(selector.getSelectedTransactions().length==1));
	}
}
