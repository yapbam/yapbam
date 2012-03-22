package net.yapbam.gui.administration;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.astesana.ajlib.swing.dialog.AbstractDialog;
import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Mode;
import net.yapbam.data.PeriodicalTransaction;
import net.yapbam.data.Transaction;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.ModeAddedEvent;
import net.yapbam.data.event.ModeRemovedEvent;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
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
					} else if (event instanceof ModeRemovedEvent) {
						ModeRemovedEvent e = (ModeRemovedEvent) event;
						if (account==((ModeRemovedEvent)event).getAccount()) {
							int row = e.getIndex()-1; // -1 because undefined mode is the first one and is not displayed
							fireTableRowsDeleted(row, row);
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
	
	private boolean isUsed(Mode mode) {
		GlobalData gData = (GlobalData)data;
		for (int i = 0; i < gData.getTransactionsNumber(); i++) {
			Transaction transaction = gData.getTransaction(i);
			if (transaction.getAccount().equals(account) && transaction.getMode().equals(mode)) return true;
		}
		for (int i = 0; i < gData.getPeriodicalTransactionsNumber(); i++) {
			PeriodicalTransaction periodicalTransaction = gData.getPeriodicalTransaction(i);
			if (periodicalTransaction.getAccount().equals(account) && periodicalTransaction.getMode().equals(mode)) return true;			
		}
		return false;
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
			Mode mode = dialog.getResult();
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
			int row = getJTable().getSelectedRow();
			Mode old = account.getMode(row+1);
			ModeDialog dialog = new ModeDialog(AbstractDialog.getOwnerWindow((Component)e.getSource()), account);
			dialog.setContent(old);
			dialog.setVisible(true);
			Mode mode = dialog.getResult();
			if (mode!=null) {
				((GlobalData)data).setMode(account, old, mode);
				((AbstractTableModel)getJTable().getModel()).fireTableRowsUpdated(row, row);
			}
		}
	}
	class DeleteModeAction extends AbstractAction {			
		public DeleteModeAction() {
			super(LocalizationData.get("GenericButton.delete"), IconManager.DELETE_MODE);
	        putValue(SHORT_DESCRIPTION, LocalizationData.get("ModeDialog.Delete.tooltip"));
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow = getJTable().getSelectedRow();
			Mode mode = ((ModeListTableModel)getJTable().getModel()).getMode(selectedRow);
			boolean confirmed = true;
			if (isUsed(mode)) {
				String mess = ("<HTML>"+LocalizationData.get("ModeManager.deleteMessage.head")+ //$NON-NLS-1$ //$NON-NLS-2$
						"<BR>"+LocalizationData.get("ModeManager.deleteMessage.confirm")+"</HTML>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				Object[] options = {LocalizationData.get("GenericButton.ok"),LocalizationData.get("GenericButton.cancel")}; //$NON-NLS-1$ //$NON-NLS-2$
				int ok = JOptionPane.showOptionDialog(getJTable(), mess, LocalizationData.get("ModeManager.deleteMessage.title"), //$NON-NLS-1$
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);		
				confirmed = (ok==0);
			}
			if (confirmed) {
				((GlobalData)data).remove(account, mode);
			}
		}
	}
}
