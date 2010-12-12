package net.yapbam.gui.welcome;

import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("serial")
public class TipManager extends ArrayList<String> {//LOCAL
	private Random random;

	public TipManager() {
		super();
		random = new Random(System.currentTimeMillis());
		this.add("<html>L'apparence de Yapbam peut être paramétrée dans les préférences.<br>" +
				"Dans le menu <b>{0}</b>, sélection <b>{1}</b> puis l'onglet <b>{2}</b></html>");
		this.add("<html>Seuil d'alerte sur le solde</html>");
		this.add("<html>Opération périodique</html>");
		this.add("<html>Pointage, clic droit, création d'opération périodique</html>");
		this.add("<html>Modification solde initial, modes de paiement, etc dans l'écran <b>{0}</b></html>");
		this.add("<html>Raccourcis clavier pour la saisie des dates et des entiers (exemple:)</html>");
	}
	
	public int getRandom() {
		int result = random.nextInt(size());
		System.out.println ("tip returned : "+ result);
		return result;
	}
}
