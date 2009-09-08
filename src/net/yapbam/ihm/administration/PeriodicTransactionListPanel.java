package net.yapbam.ihm.administration;

import javax.swing.Action;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.yapbam.ihm.actions.DeletePeriodicalTransactionAction;
import net.yapbam.ihm.actions.EditPeriodicalTransactionAction;
import net.yapbam.ihm.actions.NewPeriodicalTransactionAction;

import java.lang.Object;

public class PeriodicTransactionListPanel extends AbstractListAdministrationPanel { //LOCAL
	private static final long serialVersionUID = 1L;

	/**
	 * This is the default constructor
	 */
	public PeriodicTransactionListPanel() {
		this(null);
	}
	
	public PeriodicTransactionListPanel(Object data) {
		super(data);
	}
	
	protected String getPanelToolTip() {
		return "the panel tool tip";
	}
	protected TableModel getTableModel() {
		return new AbstractTableModel() {
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public int getRowCount() {
				// TODO Auto-generated method stub
				return 0;
			}
			@Override
			public int getColumnCount() {
				// TODO Auto-generated method stub
				return 5;
			}
		};
	}
	protected Action getNewButtonAction() {
		return new NewPeriodicalTransactionAction();
	}
	protected Action getEditButtonAction() {
		return new EditPeriodicalTransactionAction();
	}
	protected Action getDeleteButtonAction() {
		return new DeletePeriodicalTransactionAction();
	}
}
