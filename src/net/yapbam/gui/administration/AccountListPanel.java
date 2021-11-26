package net.yapbam.gui.administration;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.table.RowSorter;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.data.event.AccountAddedEvent;
import net.yapbam.data.event.AccountPropertyChangedEvent;
import net.yapbam.data.event.AccountRemovedEvent;
import net.yapbam.data.event.CheckbookAddedEvent;
import net.yapbam.data.event.CheckbookPropertyChangedEvent;
import net.yapbam.data.event.CheckbookRemovedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.actions.NewAccountAction;
import net.yapbam.gui.dialogs.EditAccountDialog;
import net.yapbam.gui.util.JTableUtils;
import net.yapbam.util.HtmlUtils;

import java.awt.Component;
import java.awt.event.ActionEvent;

public class AccountListPanel extends AbstractListAdministrationPanel<GlobalData> {
	private static final long serialVersionUID = 1L;
	
	static final String CHECK_BOOK_ALERT_PROPERTY = "checkBookAlert";
	private boolean hasCheckBookAlert;
	private AbstractTableModel tableModel;
	
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
						Formatter.format(HtmlUtils.START_TAG+LocalizationData.get("AccountManager.deleteMessage.more")+ //$NON-NLS-1$
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
		JTableUtils.fixColumnSize(jTable, AccountTableModel.ALERT_COLUMN, 10);
		return jTable;
	}
	
	@SuppressWarnings("serial")
	private final class AccountTableModel extends AbstractTableModel implements DataListener {
		private static final int ALERT_COLUMN = 0;
		private static final int NAME_COLUMN = 1;
		private static final int INTIAL_BALANCE_COLUMN = 2;
		private static final int LESS_THRESHOLD_COLUMN = 3;
		private static final int MORE_THRESHOLD_COLUMN = 4;

		public AccountTableModel(GlobalData data) {
			data.addListener(this);
		}
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex==ALERT_COLUMN) {
				return Icon.class;
			} else if ((columnIndex>=INTIAL_BALANCE_COLUMN) && (columnIndex<=MORE_THRESHOLD_COLUMN)) {
				return Double.class;
			} else {
				return String.class;
			}
		}
		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex==ALERT_COLUMN) {
				return null;
			} else if (columnIndex==NAME_COLUMN) {
				return LocalizationData.get("Transaction.account"); //$NON-NLS-1$
			} else if (columnIndex==INTIAL_BALANCE_COLUMN) {
				return LocalizationData.get("AccountManager.balanceColumn.title"); //$NON-NLS-1$
			} else if (columnIndex==LESS_THRESHOLD_COLUMN) {
				return LocalizationData.get("AccountManager.alertThresholdLess.title"); //$NON-NLS-1$
			} else if (columnIndex==MORE_THRESHOLD_COLUMN) {
				return LocalizationData.get("AccountManager.alertThresholdMore.title"); //$NON-NLS-1$
			} else {
				return "?"; //$NON-NLS-1$
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Account account = data.getAccount(rowIndex);
			if (columnIndex==ALERT_COLUMN) {
				return account.hasRemainingChecksAlert() ? IconManager.get(Name.ALERT) : null;
			} else if (columnIndex==NAME_COLUMN) {
				return account.getName();
			} else if (columnIndex==INTIAL_BALANCE_COLUMN) {
				return account.getInitialBalance();
			} else if (columnIndex==LESS_THRESHOLD_COLUMN) {
				return account.getAlertThreshold().getLessThreshold();
			} else if (columnIndex==MORE_THRESHOLD_COLUMN) {
				return account.getAlertThreshold().getMoreThreshold();
			} else {
				return "?"; //$NON-NLS-1$
			}
		}
		
		@Override
		public int getRowCount() {
			return data.getAccountsNumber();
		}

		@Override
		public int getColumnCount() {
			return MORE_THRESHOLD_COLUMN+1;
		}

		@Override
		public void processEvent(DataEvent event) {
			Account account = null;
			if (event instanceof EverythingChangedEvent) {
				this.fireTableDataChanged();
			} else if (event instanceof AccountAddedEvent) {
				int index = data.indexOf(((AccountAddedEvent)event).getAccount());
				this.fireTableRowsInserted(index, index);
			} else if (event instanceof AccountRemovedEvent) {
				int index = ((AccountRemovedEvent)event).getIndex();
				this.fireTableRowsDeleted(index, index);
			} else if (event instanceof AccountPropertyChangedEvent) {
				account = ((AccountPropertyChangedEvent)event).getAccount();
			}
			if (account!=null) {
				int row = data.indexOf(account);
				this.fireTableRowsUpdated(row, row);
			}
		}
	}
	
	public AccountListPanel() {
		this(null);
	}

	public AccountListPanel(GlobalData data) {
		super(data);
		data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				boolean usefulEvent = (event instanceof AccountPropertyChangedEvent && AccountPropertyChangedEvent.CHECK_NUMBER_ALERT_THRESHOLD.equals(((AccountPropertyChangedEvent)event).getProperty())) ||
					event instanceof AccountRemovedEvent ||
					event instanceof CheckbookAddedEvent ||
					event instanceof CheckbookRemovedEvent ||
					event instanceof CheckbookPropertyChangedEvent ||
					event instanceof EverythingChangedEvent;
				if (usefulEvent && (hasCheckBookAlert != hasAlert(AccountListPanel.this.data))) {
					hasCheckBookAlert = !hasCheckBookAlert;
					getTableModel().fireTableDataChanged();
					AccountListPanel.this.firePropertyChange(CHECK_BOOK_ALERT_PROPERTY, !hasCheckBookAlert, hasCheckBookAlert);
				}
			}
		});
	}
	
	private static boolean hasAlert(GlobalData data) {
		for (int i=0; i<data.getAccountsNumber(); i++) {
			if (data.getAccount(i).hasRemainingChecksAlert()) {
				return true;
			}
		}
		return false;
	}

	
	private AbstractTableModel getTableModel() {
		if (tableModel==null) {
			tableModel = new AccountTableModel(data);
		}
		return tableModel;
	}
	@Override
	protected Action getNewButtonAction() {
		return new NewAccountAction(data);
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
