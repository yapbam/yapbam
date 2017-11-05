package net.yapbam.gui.transactiontable;

import net.yapbam.gui.LocalizationData;

public class AmountRenderer extends ObjectRenderer {
	private static final long serialVersionUID = 1L;
	
	public AmountRenderer () {
		super();
	}

	@Override
	public void setValue(Object value) {
		String text;
		if (value==null) {
			text = ""; //$NON-NLS-1$
		} else {
			double[] amount = (double[]) value;
			if (amount.length == 1) {
				text = toString(amount[0]);
			} else {
				StringBuilder buf = new StringBuilder("<html><body>").append(toString(amount[0])); //$NON-NLS-1$
				for (int i = 1; i < amount.length; i++) {
					if (isNaN(amount[i])) {
						buf.append("<BR>"); //$NON-NLS-1$
					} else {
						buf.append("<BR>[").append(toString(amount[i])).append("]"); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
				buf.append("</body></html>"); //$NON-NLS-1$
				text = buf.toString().replace(" ", "&nbsp;"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		setText(text);
	}
	
	private String toString(double amount) {
		return isNaN(amount) ? "" : LocalizationData.getCurrencyInstance().format(amount); //$NON-NLS-1$
	}
	
	private boolean isNaN(double value) {
		// This test succeeds if value is Double.NaN (be aware that Double.NaN==Double.NaN always returns false).
		return value!=value;
	}
}
