package net.yapbam.gui.administration;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.Utils;

import net.yapbam.data.Category;
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
	private final FilterListPanel panel;
	private final GlobalData data;
	private List<Filter> filters;

	FiltersTableModel(FilterListPanel panel) {
		this.data = panel.data;
		this.panel = panel;
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
			return "Name"; //TODO
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

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		String name = ((String)value).trim();
		String errorMessage = null;
		if (name.length()==0) {
			errorMessage = "Filter.error.message.empty";
		} else {
			Filter category = data.getFilter(name);
			if (category!=null) {
				if (category==data.getFilter(row)) {
					return;
				}
				errorMessage = Formatter.format(LocalizationData.get("CategoryManager.error.message.alreadyUsed"), name); //$NON-NLS-1$
				//TODO We could merge the two categories, on demand
			}
		}
		if (errorMessage!=null) {
			JOptionPane.showMessageDialog(Utils.getOwnerWindow(panel),
					errorMessage, LocalizationData.get("CategoryManager.error.title"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
			fireTableRowsUpdated(row, row);
		} else {
			data.getFilter(row).setName(name);
			fireTableDataChanged();
		}
	}
}