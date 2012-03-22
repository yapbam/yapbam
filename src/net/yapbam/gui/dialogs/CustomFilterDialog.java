package net.yapbam.gui.dialogs;

import java.awt.Window;

import javax.swing.JPanel;

import net.astesana.ajlib.swing.dialog.AbstractDialog;
import net.yapbam.data.FilteredData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.filter.CustomFilterPanel;
import net.yapbam.gui.util.AutoUpdateOkButtonPropertyListener;

@SuppressWarnings("serial")
public class CustomFilterDialog extends AbstractDialog<FilteredData, Boolean> {

	private CustomFilterPanel filterPanel;

	public CustomFilterDialog(Window owner, FilteredData data) {
		super(owner, LocalizationData.get("MainMenuBar.customizedFilter"), data); //$NON-NLS-1$
		this.pack();
	}

	@Override
	/** Returns the dialog result.
	 * @returns true if the edited filter was changed (and, of course, the ok button is pressed).
	 */
	protected Boolean buildResult() {
		return filterPanel.apply();
	}

	@Override
	protected JPanel createCenterPane() {
		filterPanel = new CustomFilterPanel(data);
		filterPanel.addPropertyChangeListener(CustomFilterPanel.INCONSISTENCY_CAUSE_PROPERTY, new AutoUpdateOkButtonPropertyListener(this));
		return filterPanel;
	}

	@Override
	protected String getOkDisabledCause() {
		return filterPanel.getInconsistencyCause();
	}
}
