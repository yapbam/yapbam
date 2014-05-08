package net.yapbam.gui.dialogs.transaction;

import net.yapbam.data.Category;

public class CategoryAndType {
	private boolean receipt;
	private Category category;

	public CategoryAndType(boolean receipt, Category category) {
		this.receipt = receipt;
		this.category = category;
	}

	public boolean isReceipt() {
		return receipt;
	}

	public Category getCategory() {
		return category;
	}

	@Override
	public int hashCode() {
		return category.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CategoryAndType)) {
			return false;
		}
		if (this.receipt != ((CategoryAndType)obj).receipt) {
			return false;
		}
		return this.category.equals(((CategoryAndType)obj).category);
	}
}