package net.yapbam.gui.util;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JEditorPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;

import org.slf4j.LoggerFactory;

import net.yapbam.gui.HelpManager;

/**
 * A fake cell editor used to capture the events and process them to allow link displayed by URLTableCellRenderer to be clickable.
 * @see URLTableCellRenderer
 */
public class URLTableCellEditor extends AbstractCellEditor implements TableCellEditor {
	private static final long serialVersionUID = 1L;

    // The point that the mouse click occurred
    private Point clickPoint;

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    @Override
    public Component getTableCellEditorComponent(final JTable table, Object value, boolean isSelected, int row, int column) {
        // Get the cell renderer for the current cell
        TableCellRenderer renderer = table.getCellRenderer(row, column);
        // Get the cell component for the renderer (expecting a JEditorPane)
        Component comp = table.prepareRenderer(renderer, row, column);
        // Set the bounds of the component to meet the requirements for the cell rect
        comp.setBounds(table.getCellRect(row, column, false));
        if (comp instanceof JEditorPane) {
            // Get the JEditorPane
            JEditorPane editPane = (JEditorPane) comp;
            // Adjust the click point to be within the editor context
            clickPoint.x -= comp.getLocation().x;
            clickPoint.y -= comp.getLocation().y;
            // Get the hyperlink that was clicked
            final String url = getHyperlink(editPane, clickPoint);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    // Dispose of the editor...
                	cancelCellEditing();
                    // Open the URL if possible
                    if (url != null) {
                        try {
                            HelpManager.show(table, new URI(url));
                        } catch (Exception e) {
                        	LoggerFactory.getLogger(URLTableCellEditor.class).debug("Invalid URL "+url, e); //$NON-NLS-1$
                        }
                    }
                }
            });
        }
        // As we cancel the edition, we can return null
        return null;
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        boolean editable = false;
        if (e instanceof MouseEvent) {
            MouseEvent me = (MouseEvent) e;
            if (me.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(me)) {
                editable = true;
                // We need to the point that the mouse event occurred...
                clickPoint = me.getPoint();
            }
        }
        return editable;
    }

    /*
     * Try and extract the URL from the hyperlink at the given point
     */
    private String getHyperlink(JEditorPane editor, Point p) {
        int pos = editor.viewToModel(p);
        Element h = getHyperlinkElement(editor.getDocument(), pos);
        if (h != null) {
            Object attribute = h.getAttributes().getAttribute(HTML.Tag.A);
            if (attribute instanceof AttributeSet) {
                AttributeSet set = (AttributeSet) attribute;
                return (String) set.getAttribute(HTML.Attribute.HREF);
            }
        }
        return null;
    }

    /*
     * Get the element from the document that represents a hyperlink based on the position
     * within teh document.  null if it's not a hyperlink element...
     */
    private Element getHyperlinkElement(Document doc, int pos) {
        if (pos >= 0 && doc instanceof HTMLDocument) {
            HTMLDocument hdoc = (HTMLDocument) doc;
            Element elem = hdoc.getCharacterElement(pos);
            if (elem.getAttributes().getAttribute(HTML.Tag.A) != null) {
                return elem;
            }
        }
        return null;
    }
}