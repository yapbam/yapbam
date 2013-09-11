package net.yapbam.gui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;
import com.fathzer.soft.ajlib.swing.widget.TextWidget;

import net.yapbam.gui.util.AutoUpdateOkButtonPropertyListener;
import net.yapbam.gui.widget.AutoSelectFocusListener;

public abstract class BasicInputDialog<T, V> extends AbstractDialog<T, V> {
	private static final long serialVersionUID = 1L;
	
	private TextWidget field;

	protected BasicInputDialog(Window owner, String title, T data) {
		super(owner, title, data);
	}
	
	protected abstract String getLabel();

	protected abstract String getTooltip();

	@Override
	protected JPanel createCenterPane() {
		// Create the content pane.
		JPanel centerPane = new JPanel(new GridBagLayout());
		PropertyChangeListener listener = new AutoUpdateOkButtonPropertyListener(this);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.WEST;
		JLabel titleCompte = new JLabel(getLabel());
		centerPane.add(titleCompte, c);
		field = new TextWidget(20);
		field.addFocusListener(AutoSelectFocusListener.INSTANCE);
		field.addPropertyChangeListener(TextWidget.TEXT_PROPERTY, listener);
		field.setToolTipText(getTooltip());
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		centerPane.add(field, c);
		return centerPane;
	}
	
	protected TextWidget getField() {
		return field;
	}
}
