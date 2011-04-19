package net.yapbam.gui.dialogs;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JButton;

import net.yapbam.data.Category;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.AbstractDialog;
import net.yapbam.gui.widget.CoolJComboBox;
import java.awt.BorderLayout;

public class CategoryPanel extends JPanel {
	private static final String CATEGORY_PROPERTY = "category"; //$NON-NLS-1$
	private static final long serialVersionUID = 1L;
	private CoolJComboBox comboBox = null;
	private JButton newButton = null;
	private GlobalData data;
	
	public CategoryPanel(GlobalData data) {
		this();
		setData(data);
	}
	
	/**
	 * This is the default constructor
	 */
	public CategoryPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		this.add(getComboBox(), BorderLayout.CENTER);
		this.add(getNewButton(), BorderLayout.EAST);
	}

	/**
	 * This method initializes comboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getComboBox() {
		if (comboBox == null) {
			comboBox = new CoolJComboBox();
			comboBox.setToolTipText(LocalizationData.get("TransactionDialog.category.tooltip")); //$NON-NLS-1$
			//FIXME Need to fire a property change event when the selection changes
		}
		return comboBox;
	}
	
	/**
	 * This method initializes newButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getNewButton() {
		if (newButton == null) {
			newButton = new JButton();
			Dimension dimension = getComboBox().getPreferredSize();
			newButton.setFocusable(false);
			newButton.setPreferredSize(new Dimension(dimension.height, dimension.height));
			newButton.setToolTipText(LocalizationData.get("TransactionDialog.category.new.tooltip")); //$NON-NLS-1$
			newButton.setIcon(IconManager.NEW_CATEGORY);
			newButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// New category required
					Container wd = CategoryPanel.this.getParent();
					while (!(wd instanceof Window)) {
						wd = wd.getParent();
					}
					Category c = CategoryDialog.open(data, (Window) wd, null);
					if (c != null) {
						buildCategories();
						comboBox.setSelectedIndex(data.indexOf(c));
						AbstractDialog.getOwnerWindow(newButton).pack();
					}
				}
			});
		}
		return newButton;
	}

	public Category getCategory() {
		if (data==null) return null;
		Category category = this.data.getCategory((String)getComboBox().getSelectedItem());
		return category==null?Category.UNDEFINED:category;
	}

	public void setCategory(Category category) {
		Object oldValue = this.getCategory();
		this.comboBox.setSelectedIndex(this.data.indexOf(category));
		this.firePropertyChange(CATEGORY_PROPERTY, oldValue, category);
	}

	public void setData(GlobalData data) {
		this.data = data;
		this.getNewButton().setEnabled(data!=null);
		buildCategories();
	}

	private void buildCategories() {
		this.comboBox.setActionEnabled(false);
		this.comboBox.removeAllItems();
		if (data!=null){
			int nb = ((GlobalData) data).getCategoriesNumber();
			for (int i = 0; i < nb; i++) {
				this.comboBox.addItem(((GlobalData) data).getCategory(i).getName());
			}
		}
		comboBox.setActionEnabled(true);
	}
}  //  @jve:decl-index=0:visual-constraint="10,53"
