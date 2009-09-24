package net.yapbam.ihm.administration;

import java.awt.GridBagLayout;
import java.awt.Point;

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

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.Object;
import java.util.Properties;

public abstract class AbstractListAdministrationPanel extends JPanel {
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

	protected Object data;

	private JButton duplicateButton = null;
	
	/**
	 * This is the default constructor
	 */
	public AbstractListAdministrationPanel() {
		this(null);
	}
	
	public AbstractListAdministrationPanel(Object data) {
		super();
		this.data = data;
		initialize();
		this.setToolTipText(getPanelToolTip());
	}
	
	protected abstract String getPanelToolTip();
	protected abstract String getTitle();
	protected abstract Action getNewButtonAction();
	protected abstract Action getEditButtonAction();
	protected abstract Action getDeleteButtonAction();
	protected abstract Action getDuplicateButtonAction();

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setLayout(new BorderLayout());
		this.add(getJScrollPane(), BorderLayout.CENTER);
		this.add(getSouthPanel(), BorderLayout.SOUTH);
		editButtonAction.setEnabled(false);
		duplicateButtonAction.setEnabled(false);
		deleteButtonAction.setEnabled(false);
	}

	/**
	 * This method initializes jTable	
	 * @return javax.swing.JTable	
	 */
	protected JTable getJTable() {
		if (jTable == null) {
			jTable = instantiateJTable();
		    jTable.getTableHeader().setReorderingAllowed(false);
			jTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					ListSelectionModel m = (javax.swing.ListSelectionModel) e.getSource();
					if (!e.getValueIsAdjusting()) {
						boolean ok = m.getMinSelectionIndex()>=0;
						editButtonAction.setEnabled(ok);
						deleteButtonAction.setEnabled(ok);
						duplicateButtonAction.setEnabled(ok);
					}
				}
			});
			jTable.addMouseListener(new MouseAdapter() {
	            public void mousePressed(MouseEvent e) {
	                if (e.getClickCount() == 2) {
	                  Point p = e.getPoint();
	                  int row = jTable.rowAtPoint(p);
	                  if (row >= 0) {
	                	  editButtonAction.actionPerformed(new ActionEvent(e.getSource(), e.getID(), ""));
	                  }
	                }
	              }
			});

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
			gridBagConstraints11.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints11.gridy = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridx = 3;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0D;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(0, 5, 0, 5);
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints.gridy = 0;
			southPanel = new JPanel();
			southPanel.setLayout(new GridBagLayout());
			southPanel.add(getNewButton(), gridBagConstraints1);
			southPanel.add(getEditButton(), gridBagConstraints);
			southPanel.add(getDeleteButton(), gridBagConstraints2);
			southPanel.add(getDuplicateButton(), gridBagConstraints11);
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
			editButton = new JButton();
			editButtonAction = getEditButtonAction();
			editButton.setAction(editButtonAction);
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
			duplicateButton = new JButton();
			duplicateButtonAction = getDuplicateButtonAction();
			duplicateButton.setAction(duplicateButtonAction);
		}
		return duplicateButton;
	}

	public void saveState(Properties properties) {
	}

	public void restoreState(Properties properties) {
	}
}
