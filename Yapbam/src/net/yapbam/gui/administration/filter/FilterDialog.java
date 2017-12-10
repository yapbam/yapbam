package net.yapbam.gui.administration.filter;

import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.widget.TextWidget;

import net.yapbam.data.Filter;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.CustomFilterDialog;

public class FilterDialog extends CustomFilterDialog {
	private static final long serialVersionUID = 1L;
	private FilterNamePanel namePanel;

	public FilterDialog(Window owner, FilterData data) {
		super(owner, data);
	}

	@Override
	protected Boolean buildResult() {
		Filter filter = data.getFilter();
		filter.setSuspended(true);
		filter.setName(namePanel.getNameField().getText());
		return super.buildResult();
	}

	@Override
	protected JComponent createExtraComponent() {
		namePanel = new FilterNamePanel(getFilterPanel());
		namePanel.getNameField().addPropertyChangeListener(TextWidget.TEXT_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				FilterDialog.this.updateOkButtonEnabled();
			}
		});
		return namePanel;
	}

	@Override
	protected String getOkDisabledCause() {
		String cause = super.getOkDisabledCause();
		if (cause == null) {
			String oldName = data.getFilter().getName();
			String name = namePanel.getNameField().getText();
			if (name.isEmpty()) {
				cause = LocalizationData.get("CustomFilterPanel.error.filterNameIsMissing"); //$NON-NLS-1$
			} else if (!name.equals(oldName) && data.getGlobalData().getFilter(name)!=null) {
				cause = Formatter.format(LocalizationData.get("CustomFilterPanel.error.filterNameExists"),name); //$NON-NLS-1$
			}
		}
		return cause;
	}

	void setFilterName(String name) {
		namePanel.getNameField().setText(name);
	}
}
