package net.yapbam.gui.dialogs.checkbook;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.yapbam.data.Account;
import net.yapbam.data.Checkbook;
import net.yapbam.data.Mode;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.administration.AbstractListAdministrationPanel;
import net.yapbam.gui.dialogs.AbstractDialog;
import net.yapbam.gui.dialogs.ModeDialog;

@SuppressWarnings("serial")
public class CheckbookListPanel extends AbstractListAdministrationPanel {
	protected String accountName;
	
	protected CheckbookListPanel (Object data) {
		super(data);
        this.accountName = ""; //$NON-NLS-1$
        getJTable().setPreferredScrollableViewportSize(new Dimension(1,getJTable().getRowHeight()*6));
	}
	
	public CheckbookListPanel() {
		this(new ArrayList<Checkbook>());
	}
	
	public void setContent(Account account) {
		this.accountName = account.getName();
		getCheckbooks().clear();
		for (int i = 0; i < account.getCheckbooksNumber(); i++) {
			Checkbook book = account.getCheckbook(i);
			getCheckbooks().add(book);
		}
		((AbstractTableModel)getJTable().getModel()).fireTableDataChanged();
	}

	@Override
	protected Action getDeleteButtonAction() {
		return new DeleteBookAction();
	}

	@Override
	protected Action getEditButtonAction() {
		return new EditBookAction();
	}

	@Override
	protected Action getNewButtonAction() {
		return new NewCheckbookAction();
	}

	class NewCheckbookAction extends AbstractAction {
		public NewCheckbookAction() {
			super(LocalizationData.get("GenericButton.new"), IconManager.NEW_MODE); //$NON-NLS-1$
	        putValue(SHORT_DESCRIPTION, LocalizationData.get("checkbookDialog.New.tooltip")); //$NON-NLS-1$
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			CheckbookDialog dialog = new CheckbookDialog(AbstractDialog.getOwnerWindow((Component)e.getSource()));
			dialog.setVisible(true);
			Checkbook book = dialog.getCheckbook();
			if (book!=null) {
				getCheckbooks().add(book);
				((AbstractTableModel)getJTable().getModel()).fireTableDataChanged();
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
			int row = getJTable().getSelectedRow();
			Mode old = ((List<Mode>)data).remove(row);
			ModeDialog dialog = new ModeDialog(AbstractDialog.getOwnerWindow((Component)e.getSource()), new Account(accountName, 0, (List<Mode>)data));
			dialog.setContent(old);
			dialog.setVisible(true);
			Mode mode = dialog.getMode();
			if (mode==null) {
				((List<Mode>)data).add(row,old);
			} else {
				((List<Mode>)data).add(row,mode);
			}
			((AbstractTableModel)getJTable().getModel()).fireTableRowsUpdated(row, row);
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
			getCheckbooks().remove(row);
			((AbstractTableModel)getJTable().getModel()).fireTableRowsDeleted(row,row);
		}
	}
	class DuplicateModeAction extends AbstractAction {
		public DuplicateModeAction() {
			super(LocalizationData.get("GenericButton.duplicate")); //$NON-NLS-1$
	        putValue(SHORT_DESCRIPTION, LocalizationData.get("ModeDialog.Duplicate.tooltip")); //$NON-NLS-1$
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			ModeDialog dialog = new ModeDialog(AbstractDialog.getOwnerWindow((Component)e.getSource()), new Account(accountName, 0, (List<Mode>)data));
			dialog.setContent(((List<Mode>)data).get(getJTable().getSelectedRow()));
			dialog.setVisible(true);
			Mode mode = dialog.getMode();
			if (mode!=null) {
				((List<Mode>)data).add(mode);
				((AbstractTableModel)getJTable().getModel()).fireTableDataChanged();
			}
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
				return getCheckbooks().size();
			}
			
			protected Checkbook getCheckBook(int rowIndex) {
				return getCheckbooks().get(rowIndex);
			}
		};
	}

	public List<Checkbook> getCheckbooks() {
		return (List<Checkbook>) this.data;
	}

	@Override
	protected Action getDuplicateButtonAction() {
		return new DuplicateModeAction();
	}
}
