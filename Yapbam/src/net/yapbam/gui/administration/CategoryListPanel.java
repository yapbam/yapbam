package net.yapbam.gui.administration;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;

import java.awt.event.ActionEvent;
import java.lang.Object;

public class CategoryListPanel extends AbstractListAdministrationPanel { //LOCAL
	private static final long serialVersionUID = 1L;

	public CategoryListPanel(Object data) {
		super(data);
	}
	
	@Override
	protected String getTitle() {
		return "Gestion des catégories";
	}

	public String getPanelToolTip() {
		return "Cet onglet permet de gérer les catégories";
	}
	
	@Override
	protected JTable instantiateJTable() {
		return new JTable(getTableModel());
	}

	@SuppressWarnings("serial")
	private TableModel getTableModel() {
		return new AbstractTableModel() {
			@Override
			public String getColumnName(int columnIndex) {
				if (columnIndex==0) return "Catégorie";
				return "?"; //$NON-NLS-1$
			}
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				if (columnIndex==0) return ((GlobalData)data).getCategory(rowIndex).getName();
				return "?";
			}
			@Override
			public int getRowCount() {
				return ((GlobalData)data).getCategoriesNumber();
			}
			@Override
			public int getColumnCount() {
				return 1;
			}
			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}
		};
	}
	@SuppressWarnings("serial")
	protected Action getNewButtonAction() {
		return new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println ("Not yet implemented");
				// TODO Auto-generated method stub
			}
		};
	}
	@SuppressWarnings("serial")
	protected Action getEditButtonAction() {
		return new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println ("Not yet implemented");
				// TODO Auto-generated method stub
			}
		};
	}
	@SuppressWarnings("serial")
	protected Action getDeleteButtonAction() {
		return new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println ("Not yet implemented");
				// TODO Auto-generated method stub
			}
		};
	}

	@Override
	protected Action getDuplicateButtonAction() {
		// TODO Auto-generated method stub
		return null;
	}
}
