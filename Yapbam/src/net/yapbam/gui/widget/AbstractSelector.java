package net.yapbam.gui.widget;

import java.awt.Dimension;

import javax.swing.Icon;

import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;

/** An abstract widget composed of an optional label, a combo box and a new button.
 * <br>It is typically used to select a value in a list of possible values.
 * <br>This widget defines a property that reflects the selection changes. Its name is defined by the method getPropertyName.
 * <br>It also allows the combo box display to be customized using method getDefaultRenderedValue.
 * @see #getPropertyName()
 * @see #getDefaultRenderedValue(Object)
 */
public abstract class AbstractSelector<T,V> extends com.fathzer.soft.ajlib.swing.widget.AbstractSelector<T, V> {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * @param parameters The init parameters of the selector (that object will be used to populate the combo).
	 */
	public AbstractSelector(V parameters) {
		super(parameters);
		getNewButton().setOpaque(false);
		getNewButton().setContentAreaFilled(false);
		getNewButton().setBorderPainted(false);
		getNewButton().setPreferredSize(new Dimension(getNewButton().getIcon().getIconWidth(),getNewButton().getIcon().getIconHeight()));
	}
	
	@Override
	protected Icon getNewButtonIcon() {
		return IconManager.get(Name.NEW);
	}
}
