package net.yapbam.gui.dialogs;

import java.awt.Window;

import net.yapbam.data.Category;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;

public class CategoryDialog extends BasicInputDialog<GlobalData, Category> {
	private static final long serialVersionUID = 1L;

	private CategoryDialog(Window owner, GlobalData data) {
		super(owner, LocalizationData.get("CategoryDialog.title"), data); //$NON-NLS-1$
	}
	
	@Override
	protected String getLabel() {
		return LocalizationData.get("CategoryDialog.category"); //$NON-NLS-1$
	}

	@Override
	protected String getTooltip() {
		return LocalizationData.get("CategoryDialog.category.tooltip"); //$NON-NLS-1$
	}

	@Override
	protected Category buildResult() {
		return new Category(this.getField().getText().trim());
	}

	/** Opens the dialog, and add the newly created account to the data
	 * @param owner The frame upon which the dialog will be displayed
	 * @param data The global data where to append the new account
	 * @return The newly created category or null if the operation was canceled
	 */
	public static Category open(Window owner, GlobalData data) {
		CategoryDialog dialog = new CategoryDialog(owner, data);
		dialog.setVisible(true);
		Category newCategory = dialog.getResult();
		if (newCategory!=null) {
			data.add(newCategory);
		}
		return newCategory;
	}
	
	@Override
	protected String getOkDisabledCause() {
		String name = this.getField().getText().trim();
		if (name.length()==0) {
			return LocalizationData.get("CategoryDialog.err1"); //$NON-NLS-1$
		} else if (this.data.getCategory(name)!=null) {
			return LocalizationData.get("CategoryDialog.err2"); //$NON-NLS-1$
		}
		return null;
	}
}
