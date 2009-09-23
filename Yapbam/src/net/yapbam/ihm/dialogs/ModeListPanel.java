package net.yapbam.ihm.dialogs;

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
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.actions.DeleteModeAction;
import net.yapbam.ihm.administration.AbstractListAdministrationPanel;

@SuppressWarnings("serial")
public class ModeListPanel extends AbstractListAdministrationPanel {
	private String accountName;
	private JTable table;
	
	public ModeListPanel() {
		super(new ArrayList<Mode>());
        this.accountName = ""; //$NON-NLS-1$
        getJTable().setPreferredScrollableViewportSize(new Dimension(1,getJTable().getRowHeight()*6));
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

	@Override
	protected String getPanelToolTip() {
		return null;
	}
	
	@Override
	protected String getTitle() {
		return null;
	}



	class NewModeAction extends AbstractAction {
		public NewModeAction() {
			super(LocalizationData.get("GenericButton.new")); //$NON-NLS-1$
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
			super(LocalizationData.get("GenericButton.edit")); //$NON-NLS-1$
	        putValue(SHORT_DESCRIPTION, LocalizationData.get("ModeDialog.Edit.tooltip")); //$NON-NLS-1$
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
	
	private  TableModel getTableModel() {
		return new AbstractTableModel(){
			@Override
			public String getColumnName(int columnIndex) {
				if (columnIndex==0) return LocalizationData.get("ModeDialog.name.short"); //$NON-NLS-1$
				if (columnIndex==1) return LocalizationData.get("ModeDialog.forDebts.short"); //$NON-NLS-1$
				if (columnIndex==2) return LocalizationData.get("ModeDialog.forReceipts.short"); //$NON-NLS-1$
				return "?"; //$NON-NLS-1$
			}
			@Override
			public int getColumnCount() {
				return 3;
			}
			@Override
			public int getRowCount() {
				return ((List<Mode>)data).size();
			}
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				Mode mode = ((List<Mode>)data).get(rowIndex);
				if (columnIndex==0) return mode.getName();
				if (columnIndex==1) return mode.getExpenseVdc()!=null;
				if (columnIndex==2) return mode.getReceiptVdc()!=null;
				return "?"; //$NON-NLS-1$
			}
			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				if ((columnIndex==1)||(columnIndex==2)) return Boolean.class;
				return String.class;
			}
		};
	}

	public List<Mode> getModes() {
		return (List<Mode>) this.data;
	}

	@Override
	protected Action getDuplicateButtonAction() {
		// TODO Auto-generated method stub
		return null;
	}
}
