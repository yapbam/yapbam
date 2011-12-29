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
				text = LocalizationData.getCurrencyInstance().format(amount[0]);
			} else {
				StringBuilder buf = new StringBuilder("<html><body>").append(LocalizationData.getCurrencyInstance().format(amount[0])); //$NON-NLS-1$
				for (int i = 1; i < amount.length; i++) {
					buf.append("<BR>[").append(LocalizationData.getCurrencyInstance().format(amount[i])).append("]"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				buf.append("</body></html>"); //$NON-NLS-1$
				text = buf.toString();
			}
		}
		setText(text);
	}
}
