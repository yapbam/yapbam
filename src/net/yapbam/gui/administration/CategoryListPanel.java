package net.yapbam.gui.administration;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.yapbam.data.GlobalData;
import net.yapbam.data.event.CategoryAddedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.AbstractDialog;
import net.yapbam.gui.dialogs.CategoryDialog;

import java.awt.event.ActionEvent;
import java.lang.Object;

public class CategoryListPanel extends AbstractListAdministrationPanel implements AbstractAdministrationPanel { //LOCAL
	private static final long serialVersionUID = 1L;

	public CategoryListPanel(Object data) {
		super(data);
	}
	
	@Override
	protected JTable instantiateJTable() {
		return new JTable(getTableModel());
	}

	private TableModel getTableModel() {
		return new CategoryTableModel();
	}
	
	@SuppressWarnings("serial")
	private final class CategoryTableModel extends AbstractTableModel implements DataListener {
		CategoryTableModel() {
			((GlobalData)data).addListener(this);
		}
		
		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex==0) return "Catégorie";
			return "?"; //$NON-NLS-1$
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex==0) return ((GlobalData)data).getCategory(rowIndex).getName();
			return "?";
		}

		@Override
		public int getRowCount() {
			return ((GlobalData)data).getCategoriesNumber();
		}

		@Override
		public int getColumnCount() {
			return 1;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		@Override
		public void processEvent(DataEvent event) {
			if (event instanceof CategoryAddedEvent) {
				int row = ((GlobalData)data).indexOf(((CategoryAddedEvent)event).getCategory());
				fireTableRowsInserted(row, row);
/*			} else if (event instanceof CategoryRemovedEvent) {
				//TODO*/
			}
		}
	}

	@Override
	protected Action getNewButtonAction() { return new NewAction(); }
	@Override
	protected Action getDeleteButtonAction() { return new DeleteAction(); }
	@Override
	protected Action getEditButtonAction() { return null; }
	@Override
	protected Action getDuplicateButtonAction() { return null; }
	
	@SuppressWarnings("serial")
	private final class DeleteAction extends AbstractAction {
		DeleteAction () {
			super(LocalizationData.get("GenericButton.delete"), IconManager.DELETE); //$NON-NLS-1$
			putValue(SHORT_DESCRIPTION, "Supprime la catégorie sélectionnée"); //$NON-NLS-1$
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println ("Not yet implemented");
			// TODO Auto-generated method stub
		}
	}

	@SuppressWarnings("serial")
	private final class NewAction extends AbstractAction {
		public NewAction() {
			super(LocalizationData.get("GenericButton.new"), IconManager.NEW_CATEGORY); //$NON-NLS-1$
	        putValue(SHORT_DESCRIPTION, "Crée une nouvelle catégorie"); //$NON-NLS-1$
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			CategoryDialog.open((GlobalData)data, AbstractDialog.getOwnerWindow(CategoryListPanel.this), null);
		}
	}

	@Override
	public String getPanelTitle() {
		return "Gestion des catégories";
	}

	@Override
	public String getPanelToolTip() {
		return "Cet onglet permet de gérer les catégories";
	}

	@Override
	public JComponent getPanel() {
		return this;
	}

	@Override
	public void restoreState() {
	}

	@Override
	public void saveState() {
	}
}
