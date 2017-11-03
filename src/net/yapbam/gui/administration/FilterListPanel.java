package net.yapbam.gui.administration;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JTable;

import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;

public class FilterListPanel extends AbstractListAdministrationPanel<GlobalData> implements AbstractAdministrationPanel {
	private static final String STATE_PREFIX = "net.yapbam.filterAdministration."; //$NON-NLS-1$
	private static final long serialVersionUID = 1L;
	private FiltersTableModel model;

	public FilterListPanel(GlobalData data) {
		super(data);
	}
	@Override
	public String getPanelToolTip() {
		return "FilterAdminstration.toolTip"; //TODO
	}

	@Override
	public String getPanelTitle() {
		return "Filters"; //LocalizationData.get("FilterAdminstration.title"); //TODO
	}

	@Override
	protected int getBottomInset() {
		return 5;
	}
	@Override
	protected Action getNewButtonAction() {
		return new NewFilterAction(data);
	}
	@Override
	protected Action getEditButtonAction() {
		System.out.println("edit was pressed");
		return null;
	}
	@Override
	protected Action getDeleteButtonAction() {
		return new DeleteFilterAction(this);
	}
	@Override
	protected Action getDuplicateButtonAction() {
		System.out.println("duplicate was pressed");
		return null;
	}
	@Override
	protected JTable instantiateJTable() {
		return new JTable(getModel());
	}
	private FiltersTableModel getModel() {
		if (model ==null) {
			model = new FiltersTableModel(this);
		}
		return model;
	}
	@Override
	protected String getStatePrefix() {
		return STATE_PREFIX;
	}
	@Override
	public JComponent getPanel() {
		return this;
	}
	
	GlobalData getData() {
		return data;
	}
}
