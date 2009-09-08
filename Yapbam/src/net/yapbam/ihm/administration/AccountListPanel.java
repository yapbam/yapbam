package net.yapbam.ihm.administration;

import javax.swing.Action;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.actions.DeletePeriodicalTransactionAction;
import net.yapbam.ihm.actions.EditPeriodicalTransactionAction;
import net.yapbam.ihm.actions.NewPeriodicalTransactionAction;

import java.lang.Object;

public class AccountListPanel extends AbstractListAdministrationPanel { //LOCAL
	private static final long serialVersionUID = 1L;

	public AccountListPanel(Object data) {
		super(data);
	}
	
	public String getPanelToolTip() {
		return "Cet onglet permet de gérer les comptes";
	}
	
	@SuppressWarnings("serial")
	protected TableModel getTableModel() {
		return new AbstractTableModel() {
			@Override
			public String getColumnName(int columnIndex) {
				if (columnIndex==0) return "Compte";
				if (columnIndex==1) return "Solde initial";
				if (columnIndex==2) return "Modes de paiement";
				return "?"; //$NON-NLS-1$
			}
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				Account account = ((GlobalData)data).getAccount(rowIndex);
				if (columnIndex==0) return account.getName();
				else if (columnIndex==1) return account.getInitialBalance();
				else if (columnIndex==2) {
					//TODO
					return "TODO";
				}
				return "?";
			}
			@Override
			public int getRowCount() {
				return ((GlobalData)data).getAccountsNumber();
			}
			@Override
			public int getColumnCount() {
				return 3;
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
