package net.yapbam.gui.dialogs;

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
import net.yapbam.data.Mode;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.administration.AbstractListAdministrationPanel;
import net.yapbam.gui.util.NimbusPatchBooleanTableCellRenderer;

@SuppressWarnings("serial")
public class ModeListPanel extends AbstractListAdministrationPanel {
	protected String accountName;
	
	protected ModeListPanel (Object data) {
		super(data);
        this.accountName = ""; //$NON-NLS-1$
        getJTable().setPreferredScrollableViewportSize(new Dimension(1,getJTable().getRowHeight()*6));
	}
	
	ModeListPanel() {
		this(new ArrayList<Mode>());
	}
	
	public void setContent(Account account) {
		this.accountName = account.getName();
		((List<Mode>)data).clear();
		for (int i = 0; i < account.getModesNumber(); i++) {
			Mode mode = account.getMode(i);
			if (!mode.equals(Mode.UNDEFINED)) ((List<Mode>)data).add(mode);
		}
		((AbstractTableModel)getJTable().getModel()).fireTableDataChanged();
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

	class NewModeAction extends AbstractAction {
		public NewModeAction() {
			super(LocalizationData.get("GenericButton.new"), IconManager.NEW_MODE); //$NON-NLS-1$
	        putValue(SHORT_DESCRIPTION, LocalizationData.get("ModeDialog.New.tooltip")); //$NON-NLS-1$
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			ModeDialog dialog = new ModeDialog(AbstractDialog.getOwnerWindow((Component)e.getSource()), new Account(accountName, 0, (List<Mode>)data));
			dialog.setVisible(true);
			Mode mode = dialog.getMode();
			if (mode!=null) {
				((List<Mode>)data).add(mode);
				((AbstractTableModel)getJTable().getModel()).fireTableDataChanged();
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
	class DeleteModeAction extends AbstractAction {			
		public DeleteModeAction() {
			super(LocalizationData.get("GenericButton.delete"), IconManager.DELETE_MODE);
	        putValue(SHORT_DESCRIPTION, LocalizationData.get("ModeDialog.Delete.tooltip"));
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int row = getJTable().getSelectedRow();
			((List<Mode>)data).remove(row);
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
		JTable table = new JTable(getTableModel());
		// Patch Nimbus bug (see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6723524)
		table.setDefaultRenderer(Boolean.class, new NimbusPatchBooleanTableCellRenderer());
		return table;
	}
	
	protected TableModel getTableModel() {
		return new AbstractModeListModel() {
			@Override
			public int getRowCount() {
				return ((List<Mode>)data).size();
			}
			
			protected Mode getMode(int rowIndex) {
				return ((List<Mode>)data).get(rowIndex);
			}
		};
	}

	public List<Mode> getModes() {
		return (List<Mode>) this.data;
	}

	@Override
	protected Action getDuplicateButtonAction() {
		return new DuplicateModeAction();
	}
}
