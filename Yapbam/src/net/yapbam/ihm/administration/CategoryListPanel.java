package net.yapbam.ihm.administration;

import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.actions.DeletePeriodicalTransactionAction;
import net.yapbam.ihm.actions.EditPeriodicalTransactionAction;
import net.yapbam.ihm.actions.NewPeriodicalTransactionAction;

import java.lang.Object;

public class CategoryListPanel extends AbstractListAdministrationPanel { //LOCAL
	private static final long serialVersionUID = 1L;

	public CategoryListPanel(Object data) {
		super(data);
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
	protected Action getNewButtonAction() {
		return new NewPeriodicalTransactionAction(); //TODO
	}
	protected Action getEditButtonAction() {
		return new EditPeriodicalTransactionAction(); //TODO
	}
	protected Action getDeleteButtonAction() {
		return new DeletePeriodicalTransactionAction(); //TODO
	}
}
