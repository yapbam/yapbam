package net.yapbam.gui.transactiontable;

import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;

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
		if ((state!=null) && state.isSpreadable()) {
			setIcon(IconManager.get(state.isSpread() ? Name.SPREAD : Name.SPREADABLE));
		} else {
			setIcon(null);
		}
	}
}
