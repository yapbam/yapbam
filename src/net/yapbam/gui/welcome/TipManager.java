package net.yapbam.gui.welcome;

import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("serial")
public class TipManager extends ArrayList<String> {
	private Random random;

	public TipManager() {
		super();
		random = new Random(System.currentTimeMillis());
		this.add("<html>L'apparence de Yapbam peut être paramétrée dans les préférences.<br>" +
				"Dans le menu<b>{0}</b>, sélection <b>{1}</b> puis l'onglet <b>{2}</b></html>");
		this.add("<html>Seuil d'alerte sur le solde</html>");
		this.add("<html>Pointage</html>");
		this.add("<html>Modification solde initial, modes de paiement, etc</html>");
		this.add("<html>Raccourcis clavier pour la saisie des dates</html>");
	}
	
	public int getRandom() {
		return random.nextInt(size());
	}
}
