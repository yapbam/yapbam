package net.yapbam.data.xml;

import java.util.Map;

import net.yapbam.data.Account;
import net.yapbam.data.Category;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Mode;

class PartialTransaction {
	Account account;
	double amount;
	String description;
	Mode mode;
	Category category;
	String comment;

	PartialTransaction(GlobalData data, Map<String, String> attributes) {
		String accountId = attributes.get(Serializer.ACCOUNT_ATTRIBUTE);
		account = data.getAccount(accountId);
		if (account == null) {
			throw new IllegalArgumentException("Unknown account id : "+accountId); //$NON-NLS-1$
		}
		amount = Double.parseDouble(attributes.get(Serializer.AMOUNT_ATTRIBUTE));
		description = attributes.get(Serializer.DESCRIPTION_ATTRIBUTE);
		comment = attributes.get(Serializer.COMMENT_ATTRIBUTE);
		String modeId = attributes.get(Serializer.MODE_ATTRIBUTE);
		mode = modeId==null ? Mode.UNDEFINED : account.getMode(modeId.trim());
		String categoryId = attributes.get(Serializer.CATEGORY_ATTRIBUTE);
		category = categoryId==null ? Category.UNDEFINED : data.getCategory(categoryId.trim());
	}
}
