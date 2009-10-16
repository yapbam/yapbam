package net.yapbam.gui.administration;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Mode;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.ModeAddedEvent;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.AbstractDialog;
import net.yapbam.gui.dialogs.AbstractModeListModel;
import net.yapbam.gui.dialogs.ModeDialog;
import net.yapbam.gui.dialogs.ModeListPanel;

@SuppressWarnings("serial")
class AdministrationModeListPanel extends ModeListPanel {
	private Account account;
	
	public AdministrationModeListPanel(GlobalData data) {
		super(data);
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
	protected Action getDuplicateButtonAction() {
		return null;
	}
	
	@Override
	protected TableModel getTableModel() {
		return new ModeListTableModel();
	}
	
	@Override
	public void setContent(Account account) {
		this.account = account;
		this.getNewButton().getAction().setEnabled(account!=null);
		((AbstractTableModel)getJTable().getModel()).fireTableDataChanged();
	}

	private final class ModeListTableModel extends AbstractModeListModel {
		ModeListTableModel() {
			((GlobalData) data).addListener(new DataListener() {
				@Override
				public void processEvent(DataEvent event) {
					if (event instanceof ModeAddedEvent) {
						if (account==((ModeAddedEvent)event).getAccount()) {
							int row = account.indexOf(((ModeAddedEvent)event).getMode());
							fireTableRowsInserted(row, row);
						}
					}
				}
			});
		}
		
		@Override
		public int getRowCount() {
			return account==null?0:account.getModesNumber()-1;
		}

		@Override
		protected Mode getMode(int rowIndex) {
			return account.getMode(rowIndex+1);
		}
	}
	
	class NewModeAction extends AbstractAction {
		public NewModeAction() {
			super(LocalizationData.get("GenericButton.new"), IconManager.NEW_MODE); //$NON-NLS-1$
	        putValue(SHORT_DESCRIPTION, LocalizationData.get("ModeDialog.New.tooltip")); //$NON-NLS-1$
	        setEnabled(false);
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			ModeDialog dialog = new ModeDialog(AbstractDialog.getOwnerWindow((Component)e.getSource()), account);
			dialog.setVisible(true);
			Mode mode = dialog.getMode();
			if (mode!=null) {
				((GlobalData)data).add(account, mode);
			}
		}
	}
	class EditModeAction extends AbstractAction {
		public EditModeAction() {
			super(LocalizationData.get("GenericButton.edit"), IconManager.EDIT_MODE); //$NON-NLS-1$
	        putValue(SHORT_DESCRIPTION, LocalizationData.get("ModeDialog.Edit.tooltip")); //$NON-NLS-1$
		}
		@Override
		public void actionPerformed(ActionEvent e) {
/*			int row = getJTable().getSelectedRow();
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
			((AbstractTableModel)getJTable().getModel()).fireTableRowsUpdated(row, row);*/
		System.out.println ("Not implemented");
		}
	}
	class DeleteModeAction extends AbstractAction {			
		public DeleteModeAction() {
			super(LocalizationData.get("GenericButton.delete"), IconManager.DELETE_MODE);
	        putValue(SHORT_DESCRIPTION, LocalizationData.get("ModeDialog.Delete.tooltip"));
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			/*
			int row = getJTable().getSelectedRow();
			((List<Mode>)data).remove(row);
			((AbstractTableModel)getJTable().getModel()).fireTableRowsDeleted(row,row);*/
			System.out.println ("Not implemented");
		}
	}
}
