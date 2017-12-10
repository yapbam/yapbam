package net.yapbam.gui.administration.filter;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;

import com.fathzer.soft.ajlib.swing.table.RowSorter;

import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.administration.AbstractAdministrationPanel;
import net.yapbam.gui.administration.AbstractListAdministrationPanel;

public class FilterListPanel extends AbstractListAdministrationPanel<GlobalData> implements AbstractAdministrationPanel {
	private static final String STATE_PREFIX = "net.yapbam.filterAdministration."; //$NON-NLS-1$
	private static final long serialVersionUID = 1L;
	private FiltersTableModel model;

	public FilterListPanel(GlobalData data) {
		super(data);
	}
	@Override
	public String getPanelToolTip() {
		return LocalizationData.get("FilterManager.tab.name"); //TODO //$NON-NLS-1$
	}

	@Override
	public String getPanelTitle() {
		return LocalizationData.get("FilterManager.tab.tooltip"); //LocalizationData.get("FilterAdminstration.title"); //TODO //$NON-NLS-1$
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
		return new EditFilterAction(this);
	}
	@Override
	protected Action getDeleteButtonAction() {
		return new DeleteFilterAction(this);
	}
	@Override
	protected Action getDuplicateButtonAction() {
		return new DuplicateFilterAction(this);
	}
	@Override
	protected JTable instantiateJTable() {
		JTable table = new JTable(getModel());
		TableRowSorter<FiltersTableModel> sorter = new RowSorter<FiltersTableModel>(getModel());
		table.setRowSorter(sorter);
		return table;
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

	@Override
	protected JTable getJTable() {
		return super.getJTable();
	}
}
