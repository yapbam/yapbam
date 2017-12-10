package net.yapbam.gui.administration.filter;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import net.yapbam.data.Filter;
import net.yapbam.data.GlobalData;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.FiltersAddedEvent;
import net.yapbam.data.event.FiltersRemovedEvent;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
final class FiltersTableModel extends AbstractTableModel {
	private final GlobalData data;
	private List<Filter> filters;

	FiltersTableModel(FilterListPanel panel) {
		this.data = panel.getData();
		this.filters = new ArrayList<Filter>();
		data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if (event instanceof FiltersAddedEvent) {
					updateFilterList();
				} else if (event instanceof FiltersRemovedEvent) {
					updateFilterList();
				} else if (event instanceof EverythingChangedEvent) {
					filters.clear();
					updateFilterList();
				}
			}
		});
		updateFilterList();
	}
	
	private void updateFilterList() {
		this.filters.clear();
		for (int i = 0; i<data.getFiltersNumber(); i++) {
			filters.add(data.getFilter(i));
		}
		fireTableDataChanged();
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex==0) {
			return LocalizationData.get("FilterManager.column.name.title"); //TODO //$NON-NLS-1$
		} else {
			return "?"; //$NON-NLS-1$
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex==0) {
			return this.filters.get(rowIndex).getName();
		}
		throw new IllegalArgumentException();
	}

	@Override
	public int getRowCount() {
		return filters.size();
	}

	@Override
	public int getColumnCount() {
		return 1;
	}
	
	void update(int rowIndex) {
		fireTableRowsUpdated(rowIndex, rowIndex);
	}
}