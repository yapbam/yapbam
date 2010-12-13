package net.yapbam.gui.welcome;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Random;

import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public class TipManager extends ArrayList<String> {//LOCAL
	private Random random;

	public TipManager() {
		super();
		random = new Random(System.currentTimeMillis());
		String tip = MessageFormat.format("<html>L''apparence de Yapbam peut être paramétrée dans les préférences.<hr>" +
				"Pour la modifier, sélectionnez {1} dans le menu <b>{0}</b>, puis rendez vous dans l''onglet <b>{2}</b></html>",
				LocalizationData.get("MainMenu.File"),LocalizationData.get("MainMenu.Preferences"),LocalizationData.get("PreferencesDialog.LookAndFeel.title"));
		this.add(tip);
		this.add("<html>Seuil d'alerte sur le solde</html>");
		this.add("<html>Opération périodique</html>");
		this.add("<html>Pointage, clic droit, création d'opération périodique</html>");
		tip = MessageFormat.format("<html>Modification solde initial, modes de paiement, etc ... dans l''écran <b>{0}</b></html>",
				LocalizationData.get("AdministrationPlugIn.title"));
		this.add(tip);
		this.add("<html>Raccourcis clavier pour la saisie des dates et des entiers (exemple:)</html>");
	}
	
	public int getRandom() {
		int result = random.nextInt(size());
		return result;
	}
}
