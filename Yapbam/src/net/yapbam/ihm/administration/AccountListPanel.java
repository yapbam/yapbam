package net.yapbam.ihm.administration;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.data.event.AccountAddedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.ModeAddedEvent;
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.actions.DeleteAccountAction;
import net.yapbam.ihm.actions.NewAccountAction;
import net.yapbam.ihm.dialogs.AbstractDialog;
import net.yapbam.ihm.dialogs.AccountDialog;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.lang.Object;

public class AccountListPanel extends AbstractListAdministrationPanel { //LOCAL
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("serial")
	class EditAccountAction extends AbstractAction {
		private GlobalData data;
		
		public EditAccountAction(GlobalData data) {
			super("Editer");
	        putValue(SHORT_DESCRIPTION, "Ce bouton permet d'éditer le compte");
	        this.data = data;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Window owner = e.getSource() instanceof Component ?AbstractDialog.getOwnerWindow(getJTable()):null;
			AccountDialog dialog = new AccountDialog(owner, null, data);
			int selectedRow = getJTable().getSelectedRow();
			Account account = data.getAccount(selectedRow);
			dialog.setContent(account);
			dialog.setVisible(true);
			if (dialog.getAccount()!=null) {
				System.out.println("Account modified");//TODO
			}
		}
	}

	@SuppressWarnings("serial")
	private final class AccountTableModel extends AbstractTableModel implements DataListener {
		public AccountTableModel() {
			((GlobalData)data).addListener(this);
		}
		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex==0) return "Compte";
			if (columnIndex==1) return "Solde initial";
			return "?"; //$NON-NLS-1$
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Account account = ((GlobalData)data).getAccount(rowIndex);
			if (columnIndex==0) return account.getName();
			else if (columnIndex==1) return account.getInitialBalance();
			return "?";
		}

		@Override
		public int getRowCount() {
			return ((GlobalData)data).getAccountsNumber();
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
		@Override
		public void processEvent(DataEvent event) {
			if (event instanceof EverythingChangedEvent) {
				this.fireTableDataChanged();
			} else if (event instanceof AccountAddedEvent) {
				int index = ((AccountAddedEvent)event).getAccountIndex();
				this.fireTableRowsDeleted(index, index);
			}
		}
	}

	public AccountListPanel(Object data) {
		super(data);
	}
	
	public String getPanelToolTip() {
		return "Cet onglet permet de gérer les comptes";
	}
	
	@SuppressWarnings("serial")
	protected TableModel getTableModel() {
		return new AccountTableModel();
	}
	protected Action getNewButtonAction() {
		return new NewAccountAction((GlobalData) data);
	}
	protected Action getEditButtonAction() {
		return new EditAccountAction((GlobalData) data);
	}
	protected Action getDeleteButtonAction() {
		return new DeleteAccountAction((GlobalData) data);
	}
}
