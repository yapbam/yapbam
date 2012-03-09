package net.yapbam.gui.transfer;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import net.yapbam.data.Category;
import net.yapbam.data.GlobalData;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.CategoryDialog;

@SuppressWarnings("serial")
public class CategoryWidget extends AbstractSelector<Category> {
	public static final String CATEGORY_PROPERTY = "category"; //$NON-NLS-1$
	
	private GlobalData data;

	public CategoryWidget(GlobalData data) {
		this.data = data;
		getCombo().setRenderer(new CategoryRenderer());
		if (data!=null) {
			populateCombo();
		}
	}

	private void populateCombo() {
		getCombo().setActionEnabled(false);
		getCombo().removeAllItems();
		for (int i = 0; i < data.getCategoriesNumber(); i++) {
			getCombo().addItem(data.getCategory(i));
		}
		getCombo().setActionEnabled(true);
	}
	
	private static class CategoryRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			if (value!=null) value = ((Category)value).getName();
			return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
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
	protected void createNew() {
		if (data!=null) {
			Category c = CategoryDialog.open(data, CategoryDialog.getOwnerWindow(this), null);
			if (c != null) {
				populateCombo();
				getCombo().setSelectedIndex(data.indexOf(c));
				CategoryDialog.getOwnerWindow(this).pack();
			}
		}
	}
}
