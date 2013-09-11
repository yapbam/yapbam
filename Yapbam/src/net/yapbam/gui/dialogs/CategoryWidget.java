package net.yapbam.gui.dialogs;

import com.fathzer.soft.ajlib.swing.Utils;

import net.yapbam.data.Category;
import net.yapbam.data.GlobalData;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.AbstractSelector;

@SuppressWarnings("serial")
/** A category selector widget. */
public class CategoryWidget extends AbstractSelector<Category, GlobalData> {
	public static final String CATEGORY_PROPERTY = "category"; //$NON-NLS-1$
	
	public CategoryWidget(GlobalData data) {
		super(data);
	}

	@Override
	protected String getLabel() {
		return LocalizationData.get("TransactionDialog.category"); //$NON-NLS-1$
	}
	
	@Override
	protected String getComboTip() {
		return LocalizationData.get("TransactionDialog.category.tooltip"); //$NON-NLS-1$
	}
	
	@Override
	protected String getNewButtonTip() {
		return LocalizationData.get("TransactionDialog.category.new.tooltip"); //$NON-NLS-1$
	}

	@Override
	protected String getPropertyName() {
		return CATEGORY_PROPERTY;
	}

	@Override
	protected void populateCombo() {
		if (getParameters()!=null) {
			for (int i = 0; i < getParameters().getCategoriesNumber(); i++) {
				getCombo().addItem(getParameters().getCategory(i));
			}
		}
	}
		
	@Override
	protected Object getDefaultRenderedValue(Category category) {
		return category==null ? category : category.equals(Category.UNDEFINED)?LocalizationData.get("Category.undefined"):category.getName();
	}

	@Override
	protected Category createNew() {
		if (getParameters()!=null) {
			return CategoryDialog.open(Utils.getOwnerWindow(this), getParameters());
		} else {
			return null;
		}
	}
}
