package net.yapbam.gui.administration;

import java.awt.Component;
import java.awt.GridBagLayout;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.yapbam.gui.YapbamState;

import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.JLabel;

import com.fathzer.soft.ajlib.swing.table.JTableListener;

/** This panel is an abstract panel containing a table and "create", "delete", "duplicate", "edit" buttons.
 *  The "edit" and "duplicates" button can be omitted.
 *  If there's an edit button, its action is called when a double-click occurs on the table.
 */
public abstract class AbstractListAdministrationPanel<V> extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTable jTable = null;
	private JScrollPane jScrollPane = null;
	private JPanel southPanel = null;
	private JButton newButton = null;
	private JButton editButton = null;
	private JButton duplicateButton = null;
	private JButton deleteButton = null;
	private Action newButtonAction;
	private Action editButtonAction;
	private Action duplicateButtonAction;  //  @jve:decl-index=0:
	private Action deleteButtonAction;

	protected V data;

	/**
	 * This is the default constructor
	 */
	protected AbstractListAdministrationPanel() {
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
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(getSouthPanel(), BorderLayout.SOUTH);
		this.add(getJScrollPane(), BorderLayout.CENTER);
		ArrayList<Action> actions = new ArrayList<Action>();
		if (editButtonAction!=null) {
			actions.add(editButtonAction);
		}
		if (duplicateButtonAction!=null) {
			actions.add(duplicateButtonAction);
		}
		if (deleteButtonAction!=null) {
			actions.add(deleteButtonAction);
		}
		jTable.addMouseListener(new JTableListener(actions.toArray(new Action[actions.size()]), editButtonAction));
		jTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				refreshActions();
			}
		});
		refreshActions();
	}

	/** Refreshes the enabled/disabled state of actions when the selected line changes.
	 * <br>By default, edit, duplicate and delete actions are enabled only if a line is selected.
	 * Override this method to change this behavior. 
	 */
	protected void refreshActions() {
		boolean ok = getJTable().getSelectedRow()>=0;
		if (editButtonAction!=null) {
			editButtonAction.setEnabled(ok);
		}
		if (duplicateButtonAction!=null) {
			duplicateButtonAction.setEnabled(ok);
		}
		if (deleteButtonAction!=null) {
			deleteButtonAction.setEnabled(ok);
		}
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

	protected abstract JTable instantiateJTable();

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
			southPanel = new JPanel();
			southPanel.setLayout(new GridBagLayout());
			
			Component top = getTopComponent();
			if (top!=null) {
				GridBagConstraints gridBagConstraintsTop = new GridBagConstraints();
				gridBagConstraintsTop.gridwidth = 0;
				gridBagConstraintsTop.weightx = 1.0;
				gridBagConstraintsTop.fill = GridBagConstraints.HORIZONTAL;
				southPanel.add(top, gridBagConstraintsTop);
			}

			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			Insets leftInset = new Insets(0, 5, getBottomInset(), 0);
			
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.insets = new Insets(0, 0, getBottomInset(), 0);
			southPanel.add(getNewButton(), gridBagConstraints1);
			
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.insets = leftInset;
			gridBagConstraints.gridy = 1;
			if (getEditButton()!=null) {
				southPanel.add(getEditButton(), gridBagConstraints);
			}

			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 2;
			gridBagConstraints11.insets = leftInset;
			gridBagConstraints11.gridy = 1;
			if (getDuplicateButton()!=null) {
				southPanel.add(getDuplicateButton(), gridBagConstraints11);
			}

			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = leftInset;
			gridBagConstraints2.gridx = 3;
			gridBagConstraints2.gridy = 1;
			southPanel.add(getDeleteButton(), gridBagConstraints2);
			
			
			Component rightComponent = getRightComponent();
			if (rightComponent==null) {
				rightComponent = new JLabel(" "); //$NON-NLS-1$
			}
			GridBagConstraints gbcLblToto = new GridBagConstraints();
			gbcLblToto.anchor = GridBagConstraints.EAST;
			Insets rightInset = new Insets(0, 5, getBottomInset(), 0);
			gbcLblToto.insets = rightInset;
			gbcLblToto.gridx = 4;
			gbcLblToto.gridy = 1;
			gbcLblToto.weightx = 1.0D;
			southPanel.add(rightComponent, gbcLblToto);
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
	
	public void restoreState() {
		YapbamState.INSTANCE.restoreState(getJTable(), getStatePrefix());
	}
	protected String getStatePrefix() {
		return this.getClass().getCanonicalName();
	}
	public void saveState() {
		YapbamState.INSTANCE.saveState(getJTable(), getStatePrefix());
	}
	
	protected Component getRightComponent() {
		return null;
	}
	
	protected Component getTopComponent() {
		return null;
	}
	
	/** Gets the bottom inset of the panel.
	 * This inset will be applied to the buttons at the bottom of the panel.   
	 * @return an int (0 in this implementation)
	 */
	protected int getBottomInset() {
		return 0;
	}
}
