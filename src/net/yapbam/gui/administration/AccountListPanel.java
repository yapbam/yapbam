package net.yapbam.gui.administration;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import net.astesana.ajlib.swing.Utils;
import net.astesana.ajlib.swing.table.RowSorter;
import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.data.event.AccountAddedEvent;
import net.yapbam.data.event.AccountPropertyChangedEvent;
import net.yapbam.data.event.AccountRemovedEvent;
import net.yapbam.data.event.CheckbookAddedEvent;
import net.yapbam.data.event.CheckbookRemovedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.ModeAddedEvent;
import net.yapbam.data.event.ModeRemovedEvent;
import net.yapbam.data.event.TransactionsAddedEvent;
import net.yapbam.data.event.TransactionsRemovedEvent;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.actions.NewAccountAction;
import net.yapbam.gui.dialogs.EditAccountDialog;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.lang.Object;
import java.text.MessageFormat;

public class AccountListPanel extends AbstractListAdministrationPanel<GlobalData> {
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("serial")
	class DeleteAccountAction extends AbstractAction {
		DeleteAccountAction () {
			super(LocalizationData.get("GenericButton.delete"), IconManager.DELETE); //$NON-NLS-1$
			putValue(SHORT_DESCRIPTION, LocalizationData.get("AccountManager.deleteAccount.toolTip")); //$NON-NLS-1$
		}
	
		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow = getJTable().convertRowIndexToModel(getJTable().getSelectedRow());
			Account account = data.getAccount(selectedRow);
			boolean confirmed = true;
			int nb = account.getTransactionsNumber();
			if (nb!=0) {
				String mess = nb==1?LocalizationData.get("AccountManager.deleteMessage.one"): //$NON-NLS-1$
						MessageFormat.format("<HTML>"+LocalizationData.get("AccountManager.deleteMessage.more")+ //$NON-NLS-1$ //$NON-NLS-2$
						"<BR>"+LocalizationData.get("AccountManager.deleteMessage.confirm")+"</HTML>", nb); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				Object[] options = {LocalizationData.get("GenericButton.ok"),LocalizationData.get("GenericButton.cancel")}; //$NON-NLS-1$ //$NON-NLS-2$
				int ok = JOptionPane.showOptionDialog(getJTable(), mess, LocalizationData.get("AccountManager.deleteMessage.title"), //$NON-NLS-1$
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);		
				confirmed = (ok==0);
			}
			if (confirmed) {
				data.remove(account);
			}
		}
	}

	@SuppressWarnings("serial")
	class EditAccountAction extends AbstractAction {
		EditAccountAction () {
			super(LocalizationData.get("GenericButton.edit"), IconManager.EDIT); //$NON-NLS-1$
			putValue(SHORT_DESCRIPTION, LocalizationData.get("AccountManager.editAccount.toolTip")); //$NON-NLS-1$
		}
	
		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow = getJTable().convertRowIndexToModel(getJTable().getSelectedRow());
			EditAccountDialog dialog = new EditAccountDialog(Utils.getOwnerWindow(getEditButton()), LocalizationData.get("AccountDialog.title.edit"), data); //$NON-NLS-1$
			dialog.setAccountIndex(selectedRow);
			dialog.setVisible(true);
			Account account = dialog.getResult();
			if (account!=null) {
				Account oldAccount = data.getAccount(selectedRow);
				if (!account.getName().equals(oldAccount.getName())) data.setName(oldAccount, account.getName());
				if (account.getInitialBalance()!=oldAccount.getInitialBalance()) data.setInitialBalance(oldAccount, account.getInitialBalance());
				if (!account.getAlertThreshold().equals(oldAccount.getAlertThreshold())) data.setAlertThreshold(oldAccount, account.getAlertThreshold());
			}
		}
	}

	@SuppressWarnings("serial")
	@Override
	protected JTable instantiateJTable() {
		JTable jTable = new JTable(getTableModel());
		jTable.setDefaultRenderer(Double.class, new DefaultTableCellRenderer(){
		    @Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
		    		boolean hasFocus, int row, int column) {
		    	super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		    	if ((value==null) || value.equals(Double.POSITIVE_INFINITY) || value.equals(Double.NEGATIVE_INFINITY)) {
		    		setText(""); //$NON-NLS-1$
		    	} else {
		    		setText(LocalizationData.getCurrencyInstance().format(value));
		    	}
			    this.setHorizontalAlignment(SwingConstants.RIGHT);
		    	return this;
		    }
		});
		jTable.setRowSorter(new RowSorter<TableModel>(getTableModel()));
		return jTable;
	}
	
	@SuppressWarnings("serial")
	private final class AccountTableModel extends AbstractTableModel implements DataListener {
		public AccountTableModel(GlobalData data) {
			data.addListener(this);
		}
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if ((columnIndex>=1) && (columnIndex<=3)) return Double.class;
			else if ((columnIndex>=4) && (columnIndex<=6)) return Integer.class;
			return String.class;
		}
		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex==0) return LocalizationData.get("Transaction.account"); //$NON-NLS-1$
			if (columnIndex==1) return LocalizationData.get("AccountManager.balanceColumn.title"); //$NON-NLS-1$
			if (columnIndex==2) return LocalizationData.get("AccountManager.alertThresholdLess.title"); //$NON-NLS-1$
			if (columnIndex==3) return LocalizationData.get("AccountManager.alertThresholdMore.title"); //$NON-NLS-1$
			if (columnIndex==4) return LocalizationData.get("AccountManager.transactionsNumber.title"); //$NON-NLS-1$
			if (columnIndex==5) return LocalizationData.get("AccountManager.modesNumber.title"); //$NON-NLS-1$
			if (columnIndex==6) return LocalizationData.get("AccountManager.checkbooksNumber.title"); //$NON-NLS-1$
			return "?"; //$NON-NLS-1$
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Account account = ((GlobalData)data).getAccount(rowIndex);
			if (columnIndex==0) return account.getName();
			else if (columnIndex==1) return account.getInitialBalance();
			else if (columnIndex==2) return account.getAlertThreshold().getLessThreshold();
			else if (columnIndex==3) return account.getAlertThreshold().getMoreThreshold();
			else if (columnIndex==4) return account.getTransactionsNumber();
			else if (columnIndex==5) return account.getModesNumber()-1; // The undefined mode is returned in getModesNumber
			else if (columnIndex==6) return account.getCheckbooksNumber();
			return "?"; //$NON-NLS-1$
		}
		
		@Override
		public int getRowCount() {
			return ((GlobalData)data).getAccountsNumber();
		}

		@Override
		public int getColumnCount() {
			return 5;
		}

		@Override
		public void processEvent(DataEvent event) {
			Account account = null;
			if (event instanceof EverythingChangedEvent) {
				this.fireTableDataChanged();
			} else if (event instanceof AccountAddedEvent) {
				int index = ((GlobalData)data).indexOf(((AccountAddedEvent)event).getAccount());
				this.fireTableRowsInserted(index, index);
			} else if (event instanceof AccountRemovedEvent) {
				int index = ((AccountRemovedEvent)event).getIndex();
				this.fireTableRowsDeleted(index, index);
			} else if (event instanceof AccountPropertyChangedEvent) {
				account = ((AccountPropertyChangedEvent)event).getAccount();
			} else if (event instanceof TransactionsAddedEvent) {
				this.fireTableDataChanged(); //TODO Refresh only modified accounts ?
			} else if (event instanceof TransactionsRemovedEvent) {
				this.fireTableDataChanged(); //TODO Refresh only modified accounts ?
			} else if (event instanceof ModeAddedEvent) {
				account = ((ModeAddedEvent)event).getAccount();
			} else if (event instanceof ModeRemovedEvent) {
				account = ((ModeRemovedEvent)event).getAccount();
			} else if (event instanceof CheckbookAddedEvent) {
				account = ((CheckbookAddedEvent)event).getAccount();
			} else if (event instanceof CheckbookRemovedEvent) {
				account = ((CheckbookRemovedEvent)event).getAccount();
			}
			if (account!=null) {
				int row = ((GlobalData)data).indexOf(account);
				this.fireTableRowsUpdated(row, row);
			}
		}
	}
	
	public AccountListPanel() {
		this(null);
	}

	public AccountListPanel(GlobalData data) {
		super(data);
	}
	
	private TableModel getTableModel() {
		return new AccountTableModel((GlobalData) data);
	}
	@Override
	protected Action getNewButtonAction() {
		return new NewAccountAction((GlobalData) data);
	}
	@Override
	protected Action getEditButtonAction() {
		return new EditAccountAction();
	}
	@Override
	protected Action getDeleteButtonAction() {
		return new DeleteAccountAction();
	}
	@Override
	protected Action getDuplicateButtonAction() {
		return null;
	}

	@Override
	public JTable getJTable() {
		return super.getJTable();
	}
}
