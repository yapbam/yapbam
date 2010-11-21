package net.yapbam.gui.dialogs;

import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public class CustomFilterDialog extends AbstractDialog {

	private CustomFilterPanel filterPanel;

	public CustomFilterDialog(Window owner, FilteredData data) {
		super(owner, LocalizationData.get("MainMenuBar.customizedFilter"), data); //$NON-NLS-1$
		this.pack();
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
