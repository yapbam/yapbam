package net.yapbam.gui.transactiontable;

import net.yapbam.gui.LocalizationData;

public class AmountRenderer extends ObjectRenderer {
	private static final long serialVersionUID = 1L;
	
	public AmountRenderer () {
		super();
	}

	@Override
	public void setValue(Object value) {
		double[] amount = (double[]) value;
		String text;
		if (amount.length == 1) {
			text = LocalizationData.getCurrencyInstance().format(amount[0]);
		} else {
			StringBuilder buf = new StringBuilder("<html><body>").append(LocalizationData.getCurrencyInstance().format(amount[0]));
			for (int i = 1; i < amount.length; i++) {
				buf.append("<BR>[").append(LocalizationData.getCurrencyInstance().format(amount[i])).append("]");
			}
			buf.append("</body></html>");
			text = buf.toString();
		}
		setText(text);
	}
}
