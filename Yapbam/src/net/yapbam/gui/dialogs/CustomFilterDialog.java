package net.yapbam.gui.dialogs;

import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.filter.CustomFilterPanel;
import net.yapbam.gui.util.AbstractDialog;

@SuppressWarnings("serial")
public class CustomFilterDialog extends AbstractDialog<FilteredData, Boolean> {

	private CustomFilterPanel filterPanel;

	public CustomFilterDialog(Window owner, FilteredData data) {
		super(owner, LocalizationData.get("MainMenuBar.customizedFilter"), data); //$NON-NLS-1$
		this.pack();
	}

	@Override
	/** Returns the dialog result.
	 * @returns true if the validate button was pressed
	 */
	protected Boolean buildResult() {
		filterPanel.apply();
		return true;
	}

	@Override
	protected JPanel createCenterPane() {
		filterPanel = new CustomFilterPanel(data);
		filterPanel.addPropertyChangeListener(CustomFilterPanel.INCONSISTENCY_CAUSE_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateOkButtonEnabled();
			}
		});
		return filterPanel;
	}

	@Override
	protected String getOkDisabledCause() {
		return filterPanel.getInconsistencyCause();
	}
}
