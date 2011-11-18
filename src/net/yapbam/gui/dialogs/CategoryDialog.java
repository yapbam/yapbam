package net.yapbam.gui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.*;
import javax.swing.event.DocumentListener;

import net.yapbam.data.Category;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.AbstractDialog;
import net.yapbam.gui.widget.AutoSelectFocusListener;

public class CategoryDialog extends AbstractDialog<String, Category> {
	private static final long serialVersionUID = 1L;
	
	private JTextField categoryField;
	private GlobalData globalData;

	private CategoryDialog(Window owner, String message, GlobalData data) {
		super(owner, LocalizationData.get("CategoryDialog.title"), message); //$NON-NLS-1$
		this.globalData = data;
	}
	
	@Override
	protected JPanel createCenterPane() {
		// Create the content pane.
		JPanel centerPane = new JPanel(new GridBagLayout());
		DocumentListener listener = new AutoUpdateOkButtonDocumentListener(this);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.WEST;
		if (data != null) {
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = GridBagConstraints.REMAINDER;
			centerPane.add(new JLabel(data), c);
			c.fill = GridBagConstraints.NONE;
			c.gridwidth = 1;
			c.gridy++;
		}

		JLabel titleCompte = new JLabel(LocalizationData.get("CategoryDialog.category")); //$NON-NLS-1$
		centerPane.add(titleCompte, c);
		categoryField = new JTextField(20);
		categoryField.addFocusListener(AutoSelectFocusListener.INSTANCE);
		categoryField.getDocument().addDocumentListener(listener);
		categoryField.setToolTipText(LocalizationData.get("CategoryDialog.category.tooltip")); //$NON-NLS-1$
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		centerPane.add(categoryField, c);

		return centerPane;
	}
	
	@Override
	protected Category buildResult() {
		return new Category(this.categoryField.getText().trim());
	}

	/** Opens the dialog, and add the newly created account to the data
	 * @param data The global data where to append the new account
	 * @param owner The frame upon which the dialog will be displayed
	 * @param message A optional message (for instance to explain that before creating a transaction, you have to
	 * 	create an account. Null if no message is required
	 * @return The newly created account or null if the operation was canceled
	 */
	public static Category open(GlobalData data, Window owner, String message) {
		CategoryDialog dialog = new CategoryDialog(owner, message, data);
		dialog.setVisible(true);
		Category newCategory = dialog.getResult();
		if (newCategory!=null) {
			data.add(newCategory);
		}
		return newCategory;
	}
	
	@Override
	protected String getOkDisabledCause() {
		String name = this.categoryField.getText().trim();
		if (name.length()==0) {
			return LocalizationData.get("CategoryDialog.err1"); //$NON-NLS-1$
		} else if (this.globalData.getCategory(name)!=null) {
			return LocalizationData.get("CategoryDialog.err2"); //$NON-NLS-1$
		}
		return null;
	}
}
