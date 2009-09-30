package net.yapbam.gui.administration;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.data.event.AccountAddedEvent;
import net.yapbam.data.event.AccountRemovedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.ModeAddedEvent;
import net.yapbam.data.event.TransactionAddedEvent;
import net.yapbam.data.event.TransactionRemovedEvent;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.actions.NewAccountAction;
import net.yapbam.gui.dialogs.AbstractDialog;
import net.yapbam.gui.dialogs.AccountDialog;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.lang.Object;
import java.text.MessageFormat;

public class AccountListPanel extends AbstractListAdministrationPanel { //LOCAL
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("serial")
	class EditAccountAction extends AbstractAction {
		private GlobalData data;
		
		EditAccountAction(GlobalData data) {
			super("Editer");
	        putValue(SHORT_DESCRIPTION, "Ce bouton permet d'éditer le compte sélectionné");
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
	class DeleteAccountAction extends AbstractAction {
		private GlobalData data;
		DeleteAccountAction (GlobalData data) {
			super("Supprimer");
			putValue(SHORT_DESCRIPTION, "Ce bouton permet de supprimer le compte sélectionné");
			this.data = data;
		}
	
		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow = getJTable().getSelectedRow();
			Account account = data.getAccount(selectedRow);
			boolean confirmed = true;
			int nb = account.getTransactionsNumber();
			if (nb!=0) {
				String mess = nb==1?"La suppression de ce compte implique la suppression de son unique opération":
						MessageFormat.format("<HTML>La suppression de ce compte implique la suppression de ses {0,number,integer} opérations.<BR>"+
						"Confirmez-vous la suppression</HTML>", nb);
				int ok = JOptionPane.showConfirmDialog(getJTable(), mess, "Suppression d'un compte contenant des opérations", JOptionPane.OK_CANCEL_OPTION);
				confirmed = (ok==0);
			}
			if (confirmed) {
				data.remove(account);
			}
		}
	}

	@Override
	protected JTable instantiateJTable() {
		return new JTable(getTableModel());
	}
	
	@SuppressWarnings("serial")
	private final class AccountTableModel extends AbstractTableModel implements DataListener {
		public AccountTableModel(GlobalData data) {
			data.addListener(this);
		}
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex==1) return Double.class;
			else if (columnIndex==2) return Integer.class;
			return String.class;
		}
		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex==0) return "Compte";
			if (columnIndex==1) return "Solde initial";
			if (columnIndex==2) return "Nombre d'opérations";
			return "?"; //$NON-NLS-1$
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Account account = ((GlobalData)data).getAccount(rowIndex);
			if (columnIndex==0) return account.getName();
			else if (columnIndex==1) return account.getInitialBalance();
			else if (columnIndex==2) return account.getTransactionsNumber();
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
		@Override
		public void processEvent(DataEvent event) {
			if (event instanceof EverythingChangedEvent) {
				this.fireTableDataChanged();
			} else if (event instanceof AccountAddedEvent) {
				int index = ((AccountAddedEvent)event).getAccountIndex();
				this.fireTableRowsInserted(index, index);
			} else if (event instanceof AccountRemovedEvent) {
				int index = ((AccountRemovedEvent)event).getIndex();
				this.fireTableRowsDeleted(index, index);
			} else if (event instanceof TransactionAddedEvent) {
				int row = ((TransactionAddedEvent)event).getTransactionIndex();
				this.fireTableRowsUpdated(row, row);
			} else if (event instanceof TransactionRemovedEvent) {
				Account account = ((TransactionRemovedEvent)event).getRemoved().getAccount();
				int row = ((GlobalData)data).indexOf(account);
				this.fireTableRowsUpdated(row, row);				
			}
		}
	}

	public AccountListPanel(GlobalData data) {
		super(data);
	}
	
	@Override
	protected String getTitle() {
		return "Gestion des comptes";
	}

	public String getPanelToolTip() {
		return "Cet onglet permet de gérer les comptes";
	}
	
	private TableModel getTableModel() {
		return new AccountTableModel((GlobalData) data);
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

	@Override
	protected Action getDuplicateButtonAction() {
		// TODO Auto-generated method stub
		return null;
	}
}
