package net.yapbam.gui.administration;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.table.RowSorter;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.data.event.AccountAddedEvent;
import net.yapbam.data.event.AccountPropertyChangedEvent;
import net.yapbam.data.event.AccountRemovedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.actions.NewAccountAction;
import net.yapbam.gui.dialogs.EditAccountDialog;
import net.yapbam.util.HtmlUtils;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;

public class AccountListPanel extends AbstractListAdministrationPanel<GlobalData> {
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("serial")
	class DeleteAccountAction extends AbstractAction {
		DeleteAccountAction () {
			super(LocalizationData.get("GenericButton.delete"), IconManager.get(Name.DELETE_ACCOUNT)); //$NON-NLS-1$
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
						MessageFormat.format(HtmlUtils.START_TAG+LocalizationData.get("AccountManager.deleteMessage.more")+ //$NON-NLS-1$
						HtmlUtils.NEW_LINE_TAG+LocalizationData.get("AccountManager.deleteMessage.confirm")+HtmlUtils.END_TAG, nb); //$NON-NLS-1$
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
			super(LocalizationData.get("GenericButton.edit"), IconManager.get(Name.EDIT_ACCOUNT)); //$NON-NLS-1$
			putValue(SHORT_DESCRIPTION, LocalizationData.get("AccountManager.editAccount.toolTip")); //$NON-NLS-1$
		}
	
		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow = getJTable().convertRowIndexToModel(getJTable().getSelectedRow());
			EditAccountDialog.edit(data, Utils.getOwnerWindow(getEditButton()), data.getAccount(selectedRow));
		}
	}

	@SuppressWarnings("serial")
	@Override
	protected JTable instantiateJTable() {
		JTable jTable = new com.fathzer.soft.ajlib.swing.table.JTable(getTableModel());
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
			if ((columnIndex>=1) && (columnIndex<=3)) {
				return Double.class;
			} else if ((columnIndex>=4) && (columnIndex<=6)) {
				return Integer.class;
			} else {
				return String.class;
			}
		}
		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex==0) {
				return LocalizationData.get("Transaction.account"); //$NON-NLS-1$
			} else if (columnIndex==1) {
				return LocalizationData.get("AccountManager.balanceColumn.title"); //$NON-NLS-1$
			} else if (columnIndex==2) {
				return LocalizationData.get("AccountManager.alertThresholdLess.title"); //$NON-NLS-1$
			} else if (columnIndex==3) {
				return LocalizationData.get("AccountManager.alertThresholdMore.title"); //$NON-NLS-1$
			} else {
				return "?"; //$NON-NLS-1$
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Account account = ((GlobalData)data).getAccount(rowIndex);
			if (columnIndex==0) {
				return account.getName();
			} else if (columnIndex==1) {
				return account.getInitialBalance();
			} else if (columnIndex==2) {
				return account.getAlertThreshold().getLessThreshold();
			} else if (columnIndex==3) {
				return account.getAlertThreshold().getMoreThreshold();
			} else {
				return "?"; //$NON-NLS-1$
			}
		}
		
		@Override
		public int getRowCount() {
			return ((GlobalData)data).getAccountsNumber();
		}

		@Override
		public int getColumnCount() {
			return 4;
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
}
