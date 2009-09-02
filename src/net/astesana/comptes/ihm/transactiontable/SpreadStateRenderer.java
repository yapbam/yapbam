package net.astesana.comptes.ihm.transactiontable;

import net.astesana.comptes.ihm.IconManager;

class SpreadStateRenderer extends ObjectRenderer {
	private static final long serialVersionUID = 1L;
	
	SpreadStateRenderer () {
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
