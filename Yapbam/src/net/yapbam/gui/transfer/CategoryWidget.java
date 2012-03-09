package net.yapbam.gui.transfer;

import javax.swing.JComboBox;

import net.yapbam.data.Category;
import net.yapbam.data.GlobalData;

import net.yapbam.gui.LocalizationData;

public class CategoryWidget extends AbstractSelector<Category> {
	private static final long serialVersionUID = 1L;
	public static final String CATEGORY_PROPERTY = "category"; //$NON-NLS-1$

	public CategoryWidget(GlobalData data) {
		JComboBox combo = getCombo();
		for (int i = 0; i < data.getCategoriesNumber(); i++) {
			combo.addItem(data.getCategory(i));
		}
	}
	
	protected String getLabel() {
		return LocalizationData.get("TransactionDialog.category");
	}
	
	protected String getComboTip() {
		return LocalizationData.get("TransactionDialog.category.tooltip");
	}
	
	protected String getNewButtonTip() {
		return LocalizationData.get("TransactionDialog.category.new.tooltip");
	}

	@Override
	protected String getPropertyName() {
		return CATEGORY_PROPERTY;
	}

	@Override
	public Category get() {
		Category category = (Category) super.get();
		return category==null?Category.UNDEFINED:category;
	}
}
