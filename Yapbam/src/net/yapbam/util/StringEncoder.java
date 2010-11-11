package net.yapbam.util;

/**
*  Cette classe permet de coder une chaîne de caractères de façon
 *  à en supprimer un certain nombre de caractères interdits.
 *  <BR>Le codage est réalisé suivant un principe simple qui ne permet
 *  l'utilisation de caractères interdits nombreux et totalement quelconques.
 *  Cependant, il s'est, jusqu'à présent, avéré suffisant.
 *  <BR>Le principe est le suivant :
 *  <BR>Les caractères interdits sont codés sur deux caractères, le premier
 *  caractère (dit de codage) indique que ce qui suit est un code de caractère interdit.
 *  Le code suivant est spécifique du caractère; '0' pour le caractère de codage,
 *  '1' pour le premier caractère interdit ... et ainsi de suite.
 *  <BR>Il découle de ceci les limitations suivantes :
 *  <UL>
 *  <LI>Le caractère de codage doit être différent des caractères interdits</LI>
 *  <LI>Les caractères interdits doivent être différents de '0' à 'n' (n étant le
 *  nombre de caractères interdits</LI>
 *  </UL>
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public class StringEncoder {

}
