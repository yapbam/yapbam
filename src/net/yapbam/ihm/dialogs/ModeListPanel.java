package net.yapbam.ihm.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.yapbam.data.Account;
import net.yapbam.data.Mode;
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.actions.DeleteModeAction;
import net.yapbam.ihm.actions.EditModeAction;
import net.yapbam.ihm.administration.AbstractListAdministrationPanel;

@SuppressWarnings("serial")
public class ModeListPanel extends AbstractListAdministrationPanel {//LOCAL
	private Account account;
	
	public ModeListPanel() {
		super(new ArrayList<Mode>());
        this.account = new Account("",0);
        getJTable().setPreferredScrollableViewportSize(new Dimension(1,getJTable().getRowHeight()*6));
	}
	
	@SuppressWarnings("unchecked")
	public void setContent(Account account) {
		this.account = account;
		((List<Mode>)data).clear();
		for (int i = 0; i < account.getModesSize(); i++) {
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
	
	class NewModeAction extends AbstractAction {
		
		public NewModeAction() {
			super("Nouveau mode");
	        putValue(SHORT_DESCRIPTION, "Ce bouton permet de créer un nouveau mode");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			ModeDialog dialog = new ModeDialog(AbstractDialog.getOwnerWindow((Component)e.getSource()), account);
			dialog.setVisible(true);
			Mode mode = dialog.getMode();
			if (mode!=null) {
				account.add(mode);
				((List<Mode>)data).add(mode);
				((AbstractTableModel)getJTable().getModel()).fireTableDataChanged();
			}
		}
	}
	
	@Override
	protected TableModel getTableModel() {
		return new AbstractTableModel(){
			@Override
			public String getColumnName(int columnIndex) {
				if (columnIndex==0) return "Nom";
				if (columnIndex==1) return "Dépenses";
				if (columnIndex==2) return "Recettes";
				return "?"; //$NON-NLS-1$
			}
			@Override
			public int getColumnCount() {
				return 3;
			}
			@Override
			public int getRowCount() {
				return ((List)data).size();
			}
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				Mode mode = ((List<Mode>)data).get(rowIndex);
				if (columnIndex==0) return mode.getName();
				if (columnIndex==1) return mode.getExpenseVdc()!=null;
				if (columnIndex==2) return mode.getReceiptVdc()!=null;
				return "?";
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
}
