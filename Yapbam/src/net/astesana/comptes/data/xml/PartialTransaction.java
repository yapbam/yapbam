package net.astesana.comptes.data.xml;

import java.util.Map;

import net.astesana.comptes.data.Account;
import net.astesana.comptes.data.Category;
import net.astesana.comptes.data.GlobalData;
import net.astesana.comptes.data.Mode;

class PartialTransaction {
	Account account;
	double amount;
	String description;
	Mode mode;
	Category category;

	PartialTransaction(GlobalData data, Map<String, String> attributes) {
		String accountId = attributes.get(Serializer.ACCOUNT_ATTRIBUTE);
		account = data.getAccount(accountId);
		if (account == null) {
			throw new IllegalArgumentException("Unknown account id : "+accountId);
		}
		amount = Double.parseDouble(attributes.get(Serializer.AMOUNT_ATTRIBUTE));
		description = attributes.get(Serializer.DESCRIPTION_ATTRIBUTE);
		String modeId = attributes.get(Serializer.MODE_ATTRIBUTE);
		mode = modeId==null ? Mode.UNDEFINED : account.getMode(modeId);
		String categoryId = attributes.get(Serializer.CATEGORY_ATTRIBUTE);
		category = data.getCategory(categoryId);
	}
}
