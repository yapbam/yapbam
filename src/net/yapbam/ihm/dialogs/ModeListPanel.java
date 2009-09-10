package net.yapbam.ihm.dialogs;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.Action;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.yapbam.data.Account;
import net.yapbam.data.Mode;
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.actions.DeleteModeAction;
import net.yapbam.ihm.actions.EditModeAction;
import net.yapbam.ihm.actions.NewModeAction;
import net.yapbam.ihm.administration.AbstractListAdministrationPanel;
import net.yapbam.ihm.transactiontable.SpreadState;

@SuppressWarnings("serial")
public class ModeListPanel extends AbstractListAdministrationPanel {//LOCAL
	public ModeListPanel() {
		super(new ArrayList<Mode>());
        getJTable().setPreferredScrollableViewportSize(new Dimension(1,getJTable().getRowHeight()*6));
	}
	
	@SuppressWarnings("unchecked")
	public void setContent(Account account) {
		((List<Mode>)data).clear();
		for (int i = 0; i < account.getModesSize(); i++) {
			Mode mode = account.getMode(i);
			if (!mode.equals(Mode.UNDEFINED)) ((List<Mode>)data).add(mode);
		}
		((AbstractTableModel)getJTable().getModel()).fireTableDataChanged();
	}

	@Override
	protected Action getDeleteButtonAction() {
		return new DeleteModeAction();
	}

	@Override
	protected Action getEditButtonAction() {
		return new EditModeAction();
	}

	@Override
	protected Action getNewButtonAction() {
		return new NewModeAction();
	}

	@Override
	protected String getPanelToolTip() {
		return "tooltip";
	}

	@Override
	protected TableModel getTableModel() {
		return new AbstractTableModel(){
			@Override
			public String getColumnName(int columnIndex) {
				if (columnIndex==0) return "Nom";
				if (columnIndex==1) return "Dépenses";
				if (columnIndex==2) return "Recettes";
				return "?"; //$NON-NLS-1$
			}
			@Override
			public int getColumnCount() {
				return 3;
			}
			@Override
			public int getRowCount() {
				return ((List)data).size();
			}
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				Mode mode = ((List<Mode>)data).get(rowIndex);
				if (columnIndex==0) return mode.getName();
				if (columnIndex==1) return mode.getExpenseVdc()!=null;
				if (columnIndex==2) return mode.getReceiptVdc()!=null;
				return "?";
			}
			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				if ((columnIndex==1)||(columnIndex==2)) return Boolean.class;
				return String.class;
			}

		};
	}
}
