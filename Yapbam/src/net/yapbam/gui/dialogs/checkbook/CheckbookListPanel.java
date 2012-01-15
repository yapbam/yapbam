package net.yapbam.gui.dialogs.checkbook;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.yapbam.data.Account;
import net.yapbam.data.Checkbook;
import net.yapbam.data.GlobalData;
import net.yapbam.data.event.CheckbookPropertyChangedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.administration.AbstractListAdministrationPanel;
import net.yapbam.gui.util.AbstractDialog;

@SuppressWarnings("serial")
public class CheckbookListPanel extends AbstractListAdministrationPanel<GlobalData> {
	private Account account;
	
	public CheckbookListPanel (GlobalData data) {
		super(data);
		this.data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				((AbstractTableModel)getJTable().getModel()).fireTableDataChanged();
				if (event instanceof CheckbookPropertyChangedEvent) { // Test if a checkbook is finished
					CheckbookPropertyChangedEvent checkbookChangedEvt = (CheckbookPropertyChangedEvent)event;
					Checkbook old = checkbookChangedEvt.getOldCheckbook();
					if (!old.isEmpty() && (checkbookChangedEvt.getNewCheckbook().isEmpty())) {
						// A checkbook just finished, ask what to do
						String message = MessageFormat.format(LocalizationData.get("checkbookDialog.finished.message"),old.getFullNumber(old.getFirst()),old.getFullNumber(old.getLast())); //$NON-NLS-1$
						String[] options = new String[]{LocalizationData.get("GenericButton.ignore"),LocalizationData.get("checkbookDialog.finished.delete"),LocalizationData.get("checkbookDialog.finished.createNew"),LocalizationData.get("checkbookDialog.finished.DeleteAndCreateNew")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						int choice = JOptionPane.showOptionDialog(AbstractDialog.getOwnerWindow(CheckbookListPanel.this), message, LocalizationData.get("checkbookDialog.finished.title"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[3]);  //$NON-NLS-1$
						if ((choice==1) || (choice==3)) {
							CheckbookListPanel.this.data.remove(checkbookChangedEvt.getAccount(), checkbookChangedEvt.getNewCheckbook());
						}
						if (choice > 1) {
							createBook(AbstractDialog.getOwnerWindow(CheckbookListPanel.this), checkbookChangedEvt.getAccount());
						}
					}
				}
			}
		});
		this.account = null;
		getJTable().setPreferredScrollableViewportSize(new Dimension(1, getJTable().getRowHeight() * 6));
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
		return new EditBookAction();
	}

	@Override
	protected Action getNewButtonAction() {
		return new NewCheckbookAction();
	}

	private final class NewCheckbookAction extends AbstractAction {
		public NewCheckbookAction() {
			super(LocalizationData.get("GenericButton.new"), IconManager.NEW_MODE); //$NON-NLS-1$
			putValue(SHORT_DESCRIPTION, LocalizationData.get("checkbookDialog.New.tooltip")); //$NON-NLS-1$
			setEnabled(false);
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			createBook(AbstractDialog.getOwnerWindow((Component)e.getSource()), account);
		}
	}
	private final class EditBookAction extends AbstractAction {
		public EditBookAction() {
			super(LocalizationData.get("GenericButton.edit"), IconManager.EDIT_MODE); //$NON-NLS-1$
			putValue(SHORT_DESCRIPTION, LocalizationData.get("ModeDialog.Edit.tooltip")); //$NON-NLS-1$
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			int row = getJTable().getSelectedRow();
			Checkbook old = account.getCheckbook(row);
			CheckbookDialog dialog = new CheckbookDialog(AbstractDialog.getOwnerWindow((Component)e.getSource()));
			dialog.setContent(old);
			dialog.setVisible(true);
			Checkbook checkbook = dialog.getResult();
			if (checkbook!=null) {
				((GlobalData)data).setCheckbook(account, old, checkbook);
			}
		}
	}
	private final class DeleteBookAction extends AbstractAction {			
		public DeleteBookAction() {
			super(LocalizationData.get("GenericButton.delete"), IconManager.DELETE_MODE); //$NON-NLS-1$
			putValue(SHORT_DESCRIPTION, LocalizationData.get("checkbookDialog.Delete.tooltip")); //$NON-NLS-1$
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
			
			@Override
			protected Checkbook getCheckBook(int rowIndex) {
				return account.getCheckbook(rowIndex);
			}
		};
	}

	@Override
	protected Action getDuplicateButtonAction() {
		return null;
	}

	private void createBook(Window owner, Account account) {
		CheckbookDialog dialog = new CheckbookDialog(owner);
		dialog.setVisible(true);
		Checkbook book = dialog.getResult();
		if (book!=null) {
			data.add(account, book);
		}
	}
}
