package net.yapbam.gui.administration;

import java.awt.GridBagLayout;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;

import net.yapbam.gui.util.JTableListener;

import java.awt.Insets;
import java.util.ArrayList;

/** This panel is an abstract panel containing a table and "create", "delete", "duplicate", "edit" buttons.
 *  The "edit" and "duplicates" button can be ommited.
 *  If there's an edit button, its action is called when a double-clic occurs on the table.
 */
public abstract class AbstractListAdministrationPanel<V> extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTable jTable = null;
	private JScrollPane jScrollPane = null;
	private JPanel southPanel = null;
	private JButton newButton = null;
	private JButton editButton = null;
	private JButton deleteButton = null;
	private Action newButtonAction;
	private Action editButtonAction;
	private Action duplicateButtonAction;  //  @jve:decl-index=0:
	private Action deleteButtonAction;

	protected V data;

	private JButton duplicateButton = null;
	
	/**
	 * This is the default constructor
	 */
	public AbstractListAdministrationPanel() {
		this(null);
	}
	
	public AbstractListAdministrationPanel(V data) {
		super();
		this.data = data;
		initialize();
	}
	
	protected abstract Action getNewButtonAction();
	protected abstract Action getEditButtonAction();
	protected abstract Action getDeleteButtonAction();
	protected abstract Action getDuplicateButtonAction();

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
//		this.setSize(300, 200);
		this.setLayout(new BorderLayout());
		this.add(getSouthPanel(), BorderLayout.SOUTH);
		this.add(getJScrollPane(), BorderLayout.CENTER);
		ArrayList<Action> actions = new ArrayList<Action>();
		if (editButtonAction!=null) actions.add(editButtonAction);
		if (duplicateButtonAction!=null) actions.add(duplicateButtonAction);
		if (deleteButtonAction!=null) actions.add(deleteButtonAction);
		new JTableListener(jTable, actions.toArray(new Action[actions.size()]), editButtonAction);
	}

	/**
	 * This method initializes jTable	
	 * @return javax.swing.JTable	
	 */
	protected JTable getJTable() {
		if (jTable == null) {
			jTable = instantiateJTable();
			jTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return jTable;
	}

	protected JTable instantiateJTable() {
		return new JTable();
	}

	/**
	 * This method initializes jScrollPane		
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes southPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSouthPanel() {
		if (southPanel == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 2;
			gridBagConstraints11.insets = new Insets(5, 0, 5, 5);
			gridBagConstraints11.gridy = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = new Insets(5, 0, 5, 5);
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridx = 3;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0D;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(5, 0, 5, 5);
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.insets = new Insets(5, 0, 5, 5);
			gridBagConstraints.gridy = 0;
			southPanel = new JPanel();
			southPanel.setLayout(new GridBagLayout());
			southPanel.add(getNewButton(), gridBagConstraints1);
			if (getEditButton()!=null) southPanel.add(getEditButton(), gridBagConstraints);
			southPanel.add(getDeleteButton(), gridBagConstraints2);
			if (getDuplicateButton()!=null) southPanel.add(getDuplicateButton(), gridBagConstraints11);
		}
		return southPanel;
	}

	/**
	 * This method initializes newButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	protected JButton getNewButton() {
		if (newButton == null) {
			newButton = new JButton();
			newButtonAction = getNewButtonAction();
			newButton.setAction(newButtonAction);
		}
		return newButton;
	}

	/**
	 * This method initializes editButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	protected JButton getEditButton() {
		if (editButton == null) {
			editButtonAction = getEditButtonAction();
			if (editButtonAction!=null) {
				editButton = new JButton();
				editButton.setAction(editButtonAction);
			}
		}
		return editButton;
	}

	/**
	 * This method initializes deleteButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	protected JButton getDeleteButton() {
		if (deleteButton == null) {
			deleteButton = new JButton();
			deleteButtonAction = getDeleteButtonAction();
			deleteButton.setAction(deleteButtonAction);
		}
		return deleteButton;
	}

	/**
	 * This method initializes duplicateButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getDuplicateButton() {
		if (duplicateButton == null) {
			duplicateButtonAction = getDuplicateButtonAction();
			if (duplicateButtonAction!= null) {
				duplicateButton = new JButton();
				duplicateButton.setAction(duplicateButtonAction);
			}
		}
		return duplicateButton;
	}
}
