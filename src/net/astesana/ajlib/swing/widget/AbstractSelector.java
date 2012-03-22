package net.astesana.ajlib.swing.widget;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.JPanel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import net.astesana.ajlib.swing.framework.Utils;
import net.astesana.ajlib.utilities.NullUtils;

/** An abstract widget composed of an optional label, a combo box and a new button.
 * <br>It is typically used to select a value in a list of possible values.
 * <br>This widget defines a property that reflects the selection changes. Its name is defined by the method getPropertyName.
 * <br>It also allows the combo box display to be customized using method getDefaultRenderedValue.
 * @see #getPropertyName()
 * @see #getDefaultRenderedValue(Object)
 */
public abstract class AbstractSelector<T,V> extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel JLabel;
	private CoolJComboBox combo;
	private JButton newButton;

	private T lastSelected;
	private V parameters;

	/**
	 * Constructor.
	 * @param parameters The init parameters of the selector (that object will be used to populate the combo).
	 */
	public AbstractSelector(V parameters) {
		this.parameters = parameters;
		initialize();
		internalPopulate();
		this.lastSelected = get();
	}

	private void internalPopulate() {
		getCombo().setActionEnabled(false);
		getCombo().removeAllItems();
		populateCombo();
		getCombo().setActionEnabled(true);
	}
	
	protected Icon getNewButtonIcon() {
		return null;
	}
	
	/** Gets the parameters of the widget.
	 * @return The argument passed to the constructor.
	 * @see #AbstractSelector(Object)
	 */
	protected V getParameters() {
		return this.parameters;
	}
	
	/** Populates the combo.
	 * <br>You should override this method to define how the combo is populated.
	 * <br>Usually, you will use the getParameters method in order to retrieve the argument of the constructor.
	 * <br>Note that this method is always called with a empty combo.
	 * @see #getParameters()
	 */
	protected abstract void populateCombo();
	
	protected void refresh() {
		T old = get();
		getCombo().setActionEnabled(false);
		getCombo().removeAllItems();
		populateCombo();
		if (getCombo().contains(old)) getCombo().setSelectedItem(old);
		getCombo().setActionEnabled(true);
	}

	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbc_JLabel = new GridBagConstraints();
		gbc_JLabel.insets = new Insets(0, 0, 0, 5);
		gbc_JLabel.anchor = GridBagConstraints.WEST;
		gbc_JLabel.gridx = 0;
		gbc_JLabel.gridy = 0;
		add(getJLabel(), gbc_JLabel);
		GridBagConstraints gbc_combo = new GridBagConstraints();
		gbc_combo.weightx = 1.0;
		gbc_combo.fill = GridBagConstraints.HORIZONTAL;
		gbc_combo.gridx = 1;
		gbc_combo.gridy = 0;
		add(getCombo(), gbc_combo);
		GridBagConstraints gbc_newButton = new GridBagConstraints();
		gbc_newButton.gridx = 2;
		gbc_newButton.gridy = 0;
		add(getNewButton(), gbc_newButton);

		Dimension dimension = getCombo().getPreferredSize();
		getNewButton().setPreferredSize(new Dimension(dimension.height, dimension.height));

		if (getLabel()!=null) getJLabel().setText(getLabel());
		if (getComboTip()!=null) getCombo().setToolTipText(getComboTip());
		if (getNewButtonTip()!=null) getNewButton().setToolTipText(getNewButtonTip());
	}

	/** Gets the label.
	 * @return a JLabel
	 */
	public JLabel getJLabel() {
		if (JLabel == null) {
			JLabel = new JLabel();
		}
		return JLabel;
	}
	
	/** Gets the new button.
	 * @return a JButton
	 */
	public JButton getNewButton() {
		if (newButton == null) {
			newButton = new JButton();
			Icon icon = getNewButtonIcon();
			newButton = new JButton(icon);
			newButton.setFocusable(false);
			newButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					T c = createNew();
					if (c != null) {
						internalPopulate();
						getCombo().setSelectedItem(c);
						Utils.getOwnerWindow(AbstractSelector.this).pack();
					}
				}
			});
			newButton.setVisible(isNewButtonVisible());
		}
		return newButton;
	}
	
	protected boolean isNewButtonVisible() {
		return true;
	}

	/** Gets the ComboBox.
	 * @return a CoolJComboBox.
	 */
	public CoolJComboBox getCombo() {
		if (combo == null) {
			combo = new CoolJComboBox();
			combo.setRenderer(new Renderer());
			combo.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					T old = lastSelected;
					lastSelected = get();
					if (!NullUtils.areEquals(old, lastSelected)) {
						firePropertyChange(getPropertyName(), old, lastSelected);
					}
				}
			});
		}
		return combo;
	}
	
	@SuppressWarnings("serial")
	private class Renderer extends DefaultListCellRenderer {
		@SuppressWarnings("unchecked")
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			return super.getListCellRendererComponent(list, getDefaultRenderedValue((T)value), index, isSelected, cellHasFocus);
    }
	}
	
	/** Returns the default value displayed in the combo box.
	 * <br><i><b>Default</b></i> means that this value is used by the default renderer. If you define your own ListCellRenderer, this method will never been called.
	 * The default implementation returns the value itself (the default renderer will display the toString of that object).
	 * You may override this method in order to customized the combo box displayed
	 * @param value The value to be displayed (it can be null)
	 * @return The value that will be displayed.
	 */
	protected Object getDefaultRenderedValue (T value) {
		return value;
	}
	
	/** Creates a new element.
	 *  This method is called when the new button is clicked. It should ask for the new instance to be created, then, adds it to the object used
	 *  by method populateCombo, then, returns the created object.
	 *  @return The created object, null if the object creation was cancelled.
	 */
	protected abstract T createNew();
	
	/** Gets the widget's label.
	 * @return a String, null to have no label.
	 */
	protected String getLabel() {
		return null;
	}
	
	/** Gets the combo's tooltip.
	 * @return a String, null to have no tooltip.
	 */
	protected String getComboTip() {
		return null;
	}
	
	/** Gets the new button's tooltip.
	 * @return a String, null to have no tooltip.
	 */
	protected String getNewButtonTip() {
		return null;
	}
	
	/** Gets the name of the property that changes when the selection changes.
	 * @return a String
	 */
	protected abstract String getPropertyName(); 
	
	@SuppressWarnings("unchecked")
	/** Gets the selected value.
	 * @return the selected value.
	 */
	public T get() {
		return (T)getCombo().getSelectedItem();
	}
	
	/** Sets the selected value.
	 * @param value The value to select
	 */
	public void set(T value) {
		Object oldValue = this.get();
		if (!value.equals(oldValue)) getCombo().setSelectedItem(value);
	}
}
