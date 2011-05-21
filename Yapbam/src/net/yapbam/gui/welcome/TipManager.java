package net.yapbam.gui.welcome;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Random;

import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public class TipManager extends ArrayList<String> {
	private Random random;

	public TipManager() {
		super();
		random = new Random(System.currentTimeMillis());
		String tip = MessageFormat.format(LocalizationData.get("Tip.1"), //$NON-NLS-1$
				LocalizationData.get("MainMenu.File"),LocalizationData.get("MainMenu.Preferences"),LocalizationData.get("PreferencesDialog.LookAndFeel.title")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		this.add(tip);
		tip = MessageFormat.format(LocalizationData.get("Tip.2"),LocalizationData.get("BalanceHistory.title"),LocalizationData.get("AdministrationPlugIn.title")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		this.add(tip);
		tip = MessageFormat.format(LocalizationData.get("Tip.3"), LocalizationData.get("AdministrationPlugIn.title"), LocalizationData.get("MainMenu.Transactions.convertToPeriodical")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		this.add(tip);
		tip = LocalizationData.get("Tip.4"); //$NON-NLS-1$
		this.add(tip);
		tip = MessageFormat.format(LocalizationData.get("Tip.5"), LocalizationData.get("AdministrationPlugIn.title")); //$NON-NLS-1$ //$NON-NLS-2$
		this.add(tip);
		this.add(LocalizationData.get("Tip.6")); //$NON-NLS-1$
	}
	
	public int getRandom() {
		int result = random.nextInt(size());
		return result;
	}
}