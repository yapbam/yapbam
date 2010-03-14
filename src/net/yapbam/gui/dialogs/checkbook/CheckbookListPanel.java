package net.yapbam.gui.dialogs.checkbook;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.yapbam.data.Account;
import net.yapbam.data.Checkbook;
import net.yapbam.data.GlobalData;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.administration.AbstractListAdministrationPanel;
import net.yapbam.gui.dialogs.AbstractDialog;

@SuppressWarnings("serial")
public class CheckbookListPanel extends AbstractListAdministrationPanel {
	private GlobalData data;
	private Account account;
	
	public CheckbookListPanel (Object data) {
		super(data);
		this.data = (GlobalData) data;
		this.data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				((AbstractTableModel)getJTable().getModel()).fireTableDataChanged();
			}
		});
        this.account = null;
        getJTable().setPreferredScrollableViewportSize(new Dimension(1,getJTable().getRowHeight()*6));
	}
		
	public void setContent(Account account) {
		this.account = account;
		this.getNewButton().getAction().setEnabled(account!=null);
		((AbstractTableModel)getJTable().getModel()).fireTableDataChanged();
	}

	@Override
	protected Action getDeleteButtonAction() {
		return new DeleteBookAction();
	}

	@Override
	protected Action getEditButtonAction() {
		return null; //TODO
	}

	@Override
	protected Action getNewButtonAction() {
		return new NewCheckbookAction();
	}

	class NewCheckbookAction extends AbstractAction {
		public NewCheckbookAction() {
			super(LocalizationData.get("GenericButton.new"), IconManager.NEW_MODE); //$NON-NLS-1$
	        putValue(SHORT_DESCRIPTION, LocalizationData.get("checkbookDialog.New.tooltip")); //$NON-NLS-1$
	        setEnabled(false);
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			CheckbookDialog dialog = new CheckbookDialog(AbstractDialog.getOwnerWindow((Component)e.getSource()));
			dialog.setVisible(true);
			Checkbook book = dialog.getCheckbook();
			if (book!=null) {
				data.add(account, book);
			}
		}
	}
	class EditBookAction extends AbstractAction {
		public EditBookAction() {
			super(LocalizationData.get("GenericButton.edit"), IconManager.EDIT_MODE); //$NON-NLS-1$
	        putValue(SHORT_DESCRIPTION, LocalizationData.get("ModeDialog.Edit.tooltip")); //$NON-NLS-1$
		}
		@Override
		public void actionPerformed(ActionEvent e) {
//			int row = getJTable().getSelectedRow();
//			Mode old = ((List<Mode>)data).remove(row);
//			ModeDialog dialog = new ModeDialog(AbstractDialog.getOwnerWindow((Component)e.getSource()), new Account(accountName, 0, (List<Mode>)data));
//			dialog.setContent(old);
//			dialog.setVisible(true);
//			Mode mode = dialog.getMode();
//			if (mode==null) {
//				((List<Mode>)data).add(row,old);
//			} else {
//				((List<Mode>)data).add(row,mode);
//			}
//			((AbstractTableModel)getJTable().getModel()).fireTableRowsUpdated(row, row);
		}
	}
	class DeleteBookAction extends AbstractAction {			
		public DeleteBookAction() {
			super(LocalizationData.get("GenericButton.delete"), IconManager.DELETE_MODE);
	        putValue(SHORT_DESCRIPTION, LocalizationData.get("checkbookDialog.Delete.tooltip"));
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int row = getJTable().getSelectedRow();
			data.remove(account, account.getCheckbook(row));
		}
	}
	
	@Override
	protected JTable instantiateJTable() {
		return new JTable(getTableModel());
	}
	
	protected TableModel getTableModel() {
		return new AbstractCheckbookListModel() {		
			@Override
			public int getRowCount() {
				if (account==null) return 0;
				return account.getCheckbooksNumber();
			}
			
			protected Checkbook getCheckBook(int rowIndex) {
				return account.getCheckbook(rowIndex);
			}
		};
	}

	@Override
	protected Action getDuplicateButtonAction() {
		return null;
	}
}
