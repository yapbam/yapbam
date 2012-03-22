package net.yapbam.gui.administration;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.astesana.ajlib.swing.dialog.AbstractDialog;
import net.yapbam.data.Category;
import net.yapbam.data.GlobalData;
import net.yapbam.data.event.CategoryAddedEvent;
import net.yapbam.data.event.CategoryRemovedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.CategoryDialog;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.lang.Object;
import java.text.MessageFormat;

public class CategoryListPanel extends AbstractListAdministrationPanel<GlobalData> implements AbstractAdministrationPanel { //TODO Add split functions
	private static final long serialVersionUID = 1L;

	public CategoryListPanel(GlobalData data) {
		super(data);
	}
	
	@SuppressWarnings("serial")
	@Override
	protected JTable instantiateJTable() {
		return new JTable(getTableModel()) {
		    //Implement table cell tool tips.
		    @Override
			public String getToolTipText(MouseEvent e) {
		        return LocalizationData.get("CategoryManager.nameColumn.toolTip"); //$NON-NLS-1$;
		    }
		};
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
			if (columnIndex==0) return LocalizationData.get("Transaction.category"); //$NON-NLS-1$
			return "?"; //$NON-NLS-1$
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return getCategory(rowIndex).getName();
		}
		
		Category getCategory(int row) {
			return ((GlobalData)data).getCategory(row+1); // rowIndex+1 because the undefined category is ommited
		}

		@Override
		public void setValueAt(Object value, int row, int col) {
			String name = ((String)value).trim();
			String errorMessage = null;
			if (name.length()==0) {
				errorMessage = LocalizationData.get("CategoryManager.error.message.empty"); //$NON-NLS-1$
			} else {
				Category category = ((GlobalData)data).getCategory(name);
				if (category!=null) {
					if (category==getCategory(row)) return;
					errorMessage = MessageFormat.format(LocalizationData.get("CategoryManager.error.message.alreadyUsed"), name); //$NON-NLS-1$
					//TODO We could merge the two categories, on demand
				}
			}
			if (errorMessage!=null) {
				JOptionPane.showMessageDialog(AbstractDialog.getOwnerWindow(CategoryListPanel.this),
						errorMessage, LocalizationData.get("CategoryManager.error.title"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
				fireTableRowsUpdated(row, row);
			} else {
				((GlobalData)data).setName(getCategory(row), name);
		        fireTableDataChanged();
			}
	    }

		@Override
		public int getRowCount() {
			return ((GlobalData)data).getCategoriesNumber()-1; // The undefined category is omitted
		}

		@Override
		public int getColumnCount() {
			return 1;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return true;
		}

		@Override
		public void processEvent(DataEvent event) {
			if (event instanceof CategoryAddedEvent) {
				int row = ((GlobalData)data).indexOf(((CategoryAddedEvent)event).getCategory())-1; // -1 because undefined category is omitted
				fireTableRowsInserted(row, row);
			} else if (event instanceof CategoryRemovedEvent) {
				int row = ((CategoryRemovedEvent)event).getIndex()-1; // -1 because undefined category is omitted
				fireTableRowsDeleted(row, row);
			} else if (event instanceof EverythingChangedEvent) {
				fireTableDataChanged();
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
	
	private boolean isUsed(Category category) {
		GlobalData gData = (GlobalData)data;
		for (int i = 0; i < gData.getTransactionsNumber(); i++) {
			if (gData.getTransaction(i).hasCategory(category)) return true;
		}
		for (int i = 0; i < gData.getPeriodicalTransactionsNumber(); i++) {
			if (gData.getPeriodicalTransaction(i).hasCategory(category)) return true;			
		}
		return false;
	}
	
	@SuppressWarnings("serial")
	private final class DeleteAction extends AbstractAction {
		DeleteAction () {
			super(LocalizationData.get("GenericButton.delete"), IconManager.DELETE); //$NON-NLS-1$
			putValue(SHORT_DESCRIPTION, LocalizationData.get("CategoryManager.delete.toolTip")); //$NON-NLS-1$
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow = getJTable().getSelectedRow();
			Category category = ((CategoryTableModel)getJTable().getModel()).getCategory(selectedRow);
			boolean confirmed = true;
			if (isUsed(category)) {
				String mess = ("<HTML>"+LocalizationData.get("CategoryManager.deleteMessage.head")+ //$NON-NLS-1$ //$NON-NLS-2$
						"<BR>"+LocalizationData.get("CategoryManager.deleteMessage.confirm")+"</HTML>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				Object[] options = {LocalizationData.get("GenericButton.ok"),LocalizationData.get("GenericButton.cancel")}; //$NON-NLS-1$ //$NON-NLS-2$
				int ok = JOptionPane.showOptionDialog(getJTable(), mess, LocalizationData.get("CategoryManager.deleteMessage.title"), //$NON-NLS-1$
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);		
				confirmed = (ok==0);
			}
			if (confirmed) {
				((GlobalData)data).remove(category);
			}
		}
	}

	@SuppressWarnings("serial")
	private final class NewAction extends AbstractAction {
		public NewAction() {
			super(LocalizationData.get("GenericButton.new"), IconManager.NEW_CATEGORY); //$NON-NLS-1$
	        putValue(SHORT_DESCRIPTION, LocalizationData.get("CategoryManager.new.toolTip")); //$NON-NLS-1$
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			CategoryDialog.open((GlobalData)data, AbstractDialog.getOwnerWindow(CategoryListPanel.this), null);
		}
	}

	@Override
	public String getPanelTitle() {
		return LocalizationData.get("CategoryManager.title"); //$NON-NLS-1$
	}

	@Override
	public String getPanelToolTip() {
		return LocalizationData.get("CategoryManager.toolTip"); //$NON-NLS-1$
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

	@Override
	protected int getBottomInset() {
		return 5;
	}
}
