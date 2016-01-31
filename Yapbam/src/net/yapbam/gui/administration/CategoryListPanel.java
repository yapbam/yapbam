package net.yapbam.gui.administration;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.widget.CharWidget;

import net.yapbam.data.Category;
import net.yapbam.data.GlobalData;
import net.yapbam.data.comparator.CategoryComparator;
import net.yapbam.data.event.CategoryAddedEvent;
import net.yapbam.data.event.CategoryRemovedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.SubCategorySeparatorChangedEvent;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.CategoryDialog;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

public class CategoryListPanel extends AbstractListAdministrationPanel<GlobalData> implements AbstractAdministrationPanel {
	private static final long serialVersionUID = 1L;
	private CharWidget subcategorySeparator;

	public CategoryListPanel(GlobalData data) {
		super(data);
	}
	
	@SuppressWarnings("serial")
	@Override
	protected JTable instantiateJTable() {
		JTable table = new com.fathzer.soft.ajlib.swing.table.JTable(getTableModel()) {
			// Implement table cell tool tips.
			@Override
			public String getToolTipText(MouseEvent e) {
				return LocalizationData.get("CategoryManager.nameColumn.toolTip"); //$NON-NLS-1$;
			}
		};
		table.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if (getJTable().isEditing()) {
					getJTable().getCellEditor().cancelCellEditing();
				}
			}
		});
		return table;
	}

	private TableModel getTableModel() {
		return new CategoryTableModel();
	}
	
	@SuppressWarnings("serial")
	private final class CategoryTableModel extends AbstractTableModel implements DataListener {
		Category[] content;
		
		CategoryTableModel() {
			data.addListener(this);
			buildContent();
		}
		
		private void buildContent() {
			// We will ignore first category which is the UNDEFINED one and is not displayed in the table
			content = new Category[data.getCategoriesNumber()-1];
			for (int i = 0; i < content.length; i++) {
				content[i] = data.getCategory(i+1);
			}
			Arrays.sort(content, new CategoryComparator(getLocale(), data.getSubCategorySeparator()));
		}
		
		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex==0) {
				return LocalizationData.get("Transaction.category"); //$NON-NLS-1$
			}
			return "?"; //$NON-NLS-1$
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Category category = getCategory(rowIndex);
			return category.equals(Category.UNDEFINED)?LocalizationData.get("Category.undefined"):category.getName(); //$NON-NLS-1$
		}
		
		Category getCategory(int row) {
			return content[row];
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
					if (category==getCategory(row)) {
						return;
					}
					errorMessage = Formatter.format(LocalizationData.get("CategoryManager.error.message.alreadyUsed"), name); //$NON-NLS-1$
					//TODO We could merge the two categories, on demand
				}
			}
			if (errorMessage!=null) {
				JOptionPane.showMessageDialog(Utils.getOwnerWindow(CategoryListPanel.this),
						errorMessage, LocalizationData.get("CategoryManager.error.title"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
				fireTableRowsUpdated(row, row);
			} else {
				((GlobalData)data).setName(getCategory(row), name);
				fireTableDataChanged();
			}
		}

		@Override
		public int getRowCount() {
			return content.length;
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
			if (event instanceof CategoryAddedEvent || event instanceof CategoryRemovedEvent || event instanceof EverythingChangedEvent) {
				buildContent();
				fireTableDataChanged();
			}
		}
	}

	@Override
	protected Action getNewButtonAction() {
		return new NewAction();
	}
	
	@Override
	protected Action getDeleteButtonAction() {
		return new DeleteAction();
	}
	
	@Override
	protected Action getEditButtonAction() {
		return null;
	}
	
	@Override
	protected Action getDuplicateButtonAction() {
		return null;
	}
	
	private boolean isUsed(Category category) {
		GlobalData gData = (GlobalData)data;
		for (int i = 0; i < gData.getTransactionsNumber(); i++) {
			if (gData.getTransaction(i).hasCategory(category)) {
				return true;
			}
		}
		for (int i = 0; i < gData.getPeriodicalTransactionsNumber(); i++) {
			if (gData.getPeriodicalTransaction(i).hasCategory(category)) {
				return true;			
			}
		}
		return false;
	}
	
	@SuppressWarnings("serial")
	private final class DeleteAction extends AbstractAction {
		DeleteAction () {
			super(LocalizationData.get("GenericButton.delete"), IconManager.get(Name.DELETE_CATEGORY)); //$NON-NLS-1$
			putValue(SHORT_DESCRIPTION, LocalizationData.get("CategoryManager.delete.toolTip")); //$NON-NLS-1$
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			if (getJTable().getCellEditor() != null) {
		    getJTable().getCellEditor().cancelCellEditing();
			}			
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
			super(LocalizationData.get("GenericButton.new"), IconManager.get(Name.NEW_CATEGORY)); //$NON-NLS-1$
	        putValue(SHORT_DESCRIPTION, LocalizationData.get("CategoryManager.new.toolTip")); //$NON-NLS-1$
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			CategoryDialog.open(Utils.getOwnerWindow(CategoryListPanel.this), (GlobalData)data);
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

	/* (non-Javadoc)
	 * @see net.yapbam.gui.administration.AbstractListAdministrationPanel#getRightComponent()
	 */
	@Override
	protected Component getRightComponent() {
		JPanel result = new JPanel();
		result.add(new JLabel(LocalizationData.get("Subcategories.separatorField.title"))); //$NON-NLS-1$
		result.add(getSeparator());
		return result;
	}
	
	private JTextField getSeparator() {
		if (subcategorySeparator == null) {
			subcategorySeparator = new CharWidget();
			subcategorySeparator.setChar(data.getSubCategorySeparator());
			subcategorySeparator.setDefaultChar('.');
			data.addListener(new DataListener() {
				@Override
				public void processEvent(DataEvent event) {
					if (event instanceof SubCategorySeparatorChangedEvent || event instanceof EverythingChangedEvent) {
						subcategorySeparator.setChar(data.getSubCategorySeparator());
					}
				}
			});
			subcategorySeparator.addPropertyChangeListener(CharWidget.CHAR_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					data.setSubCategorySeparator((Character) evt.getNewValue());
				}
			});
			subcategorySeparator.setToolTipText(LocalizationData.get("Subcategories.separatorField.tooltip")); //$NON-NLS-1$
		}
		return subcategorySeparator;
	}
}
