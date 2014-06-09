package net.yapbam.gui;

import net.yapbam.data.Account;

/** A class that is able to choose a default account for an action.
 * <br>Is typically used in order to select the default account when creating a transaction.
 */
public interface AccountSelector {
	/** Gets the default account or null if no default account is defined.
	 * @return an Account or null.
	 */
	abstract Account getSelectedAccount();
}
