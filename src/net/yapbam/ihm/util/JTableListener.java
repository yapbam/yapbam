package net.yapbam.ihm.util;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/** This is a standard JTable listener.
 * It has action attributes (typically edit/delete/duplicate row) which are automatically enabled/disabled when a row is selected/deselected.
 * When a double click occurs on the JTable, a default action is invoked (typically the editAction).
 * A popup menu is shown when needed (when a right click occurs under windows).
 * This class register itself with the JTable in order to receive interesting events, you just have to call the constructor.
 */
public class JTableListener extends MouseAdapter implements ListSelectionListener {
	private Action[] actions;
	private Action defaultAction;
	private JTable jTable;

	/** Constructor.
	 * @param jTable The JTable to be listened
	 * @param actions The actions that will appear in the popupMenu. Insert a null in this array to have a separator in the popup manu
	 * @param defaultAction The default action which will be invoked when a double click occurs
	 */
	public JTableListener(JTable jTable, Action[] actions, Action defaultAction) {
		super();
		this.actions = actions;
		this.defaultAction = defaultAction;
		this.jTable = jTable;
		jTable.addMouseListener(this);
		jTable.getSelectionModel().addListSelectionListener(this);
		refreshActions();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			refreshActions();
		}
	}

	private void refreshActions() {
		boolean ok = jTable.getSelectedRow()>=0;
		for (int i = 0; i < actions.length; i++) {
			if (actions[i] != null) actions[i].setEnabled(ok);
		}
	}

	public void mouseReleased(MouseEvent e) {
		maybeShowPopup(e);
	}
	
	public void mousePressed(MouseEvent e) {
		if ((e.getClickCount() == 2) && (e.getButton()==MouseEvent.BUTTON1)) {
			Point p = e.getPoint();
			int row = jTable.rowAtPoint(p);
			if (row >= 0) {
              defaultAction.actionPerformed(new ActionEvent(e.getSource(), e.getID(), ""));
            }
        } else {
        	maybeShowPopup(e);
        }
    }
	
	/** When a double click occurs, the action returned by this method is invoked.
	 * This implementation returns the defaultButton. 
	 * You can override this method in order to change this action, for instance if the JTable has a mode switch that
	 * trigger another action when it is set.
	 * @return The action to invoke.
	 */
	protected Action getDoubleClickAction() {
		return defaultAction;
	}
	
	private void maybeShowPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			Point p = e.getPoint();
		    int row = jTable.rowAtPoint(p);
		    jTable.getSelectionModel().setSelectionInterval(row, row);
			JPopupMenu popup = new JPopupMenu();
			fillPopUp(popup);
		    popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	/** Fill the popup menu.
	 * This implementation put all the actions in the popup. You may override it in order to add
	 * more actions ... or less.
	 * @param popup the pop up to be filled.
	 */
	protected void fillPopUp(JPopupMenu popup) {
		for (int i = 0; i < actions.length; i++) {
			if (actions[i]==null) {
				popup.addSeparator();
			} else {
				popup.add(new JMenuItem(actions[i]));
			}
		}
	}
}
