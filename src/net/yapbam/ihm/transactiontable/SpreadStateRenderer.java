package net.yapbam.ihm.transactiontable;

import net.yapbam.ihm.IconManager;

public class SpreadStateRenderer extends ObjectRenderer {
	private static final long serialVersionUID = 1L;
	
	public SpreadStateRenderer () {
		super();
		this.setHorizontalAlignment(CENTER);
		this.setVerticalAlignment(TOP);
	}

	@Override
    public void setValue(Object value) {
		SpreadState state = (SpreadState) value;
		if (state.spreadable) {
			setIcon(state.spread?IconManager.SPREAD:IconManager.SPREADABLE);
		} else {
			setIcon(null);
		}
    }
}
