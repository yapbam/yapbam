package net.yapbam.gui.transfer;

import javax.swing.JComboBox;

import net.yapbam.data.Category;
import net.yapbam.data.GlobalData;

import net.yapbam.gui.LocalizationData;

public class CategoryWidget extends AbstractSelector<Category> {
	private static final long serialVersionUID = 1L;

	public CategoryWidget(GlobalData data) {
		super(LocalizationData.get("TransactionDialog.category"), LocalizationData.get("TransactionDialog.category.tooltip"), LocalizationData.get("TransactionDialog.category.new.tooltip"));
		JComboBox<Category> combo = getCombo();
		for (int i = 0; i < data.getCategoriesNumber(); i++) {
			combo.addItem(data.getCategory(i));
		}
	}

}
