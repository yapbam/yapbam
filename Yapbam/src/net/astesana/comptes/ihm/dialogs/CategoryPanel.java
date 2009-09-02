package net.astesana.comptes.ihm.dialogs;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Window;

import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import javax.swing.JComboBox;
import javax.swing.JButton;

import net.astesana.comptes.data.Category;
import net.astesana.comptes.data.GlobalData;
import net.astesana.comptes.ihm.widget.CoolJComboBox;

import javax.swing.ImageIcon;

public class CategoryPanel extends JPanel {

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
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 2;
		gridBagConstraints2.gridy = 0;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.gridx = 1;
		this.setSize(300, 84);
		this.setLayout(new GridBagLayout());
		this.add(getComboBox(), gridBagConstraints1);
		this.add(getNewButton(), gridBagConstraints2);
	}

	/**
	 * This method initializes comboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getComboBox() {
		if (comboBox == null) {
			comboBox = new CoolJComboBox();
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
			newButton.setToolTipText("Cliquez ici pour créer une nouvelle catégorie");
			newButton.setIcon(new ImageIcon(getClass().getResource("/net/astesana/comptes/ihm/images/new.png")));
			newButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// New category required
					Container wd = CategoryPanel.this.getParent();
					while (!(wd instanceof Window)) {
						wd = wd.getParent();
					}
					Category c = NewCategoryDialog.open(data, (Window) wd, null);;
					if (c!=null) {
						buildCategories();
						comboBox.setSelectedIndex(data.indexOf(c));
					}
				}
			});
		}
		return newButton;
	}

	public Category getCategory() {
		if (data==null) return null;
		return this.data.getCategory(getComboBox().getSelectedIndex());
	}

	public void setCategory(Category category) {
		Object oldValue = this.getCategory();
		this.firePropertyChange("category", oldValue, category);
		this.comboBox.setSelectedIndex(this.data.indexOf(category));
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
