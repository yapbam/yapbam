package net.yapbam.gui.dialogs;

import java.awt.Window;

import javax.swing.JPanel;

import net.yapbam.data.FilteredData;

@SuppressWarnings("serial")
public class CustomFilterDialog extends AbstractDialog {

	private CustomFilterPanel filterPanel;

	public CustomFilterDialog(Window owner, FilteredData data) {
		super(owner, "Filtre", data); //LOCAL
	}

	@Override
	/** Returns the dialog result.
	 * @returns true if the validate button was pressed
	 */
	protected Object buildResult() {
		filterPanel.apply();
		return true;
	}

	@Override
	protected JPanel createCenterPane(Object data) {
		filterPanel = new CustomFilterPanel((FilteredData) data);
		return filterPanel;
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}
}
