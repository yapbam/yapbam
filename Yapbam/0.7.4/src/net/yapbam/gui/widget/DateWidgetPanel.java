package net.yapbam.gui.widget;

import java.awt.Color;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import java.awt.GridBagConstraints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/** This panel contains a DateWidget and a button that shows a calendar popup.
 * As this widget (especially the DateWidget it contains) represents years with two digits, it can only represent dates near today (ie, impossible to represent a date before 1900) 
 * @see DateWidget
 * @see DateChooserPanel
 */
public class DateWidgetPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	/** Date change property name.
	 * The panel is a bean. Every time the chosen date changed, it sends a PropertyChangedEvent.
	 */
	public static final String DATE_PROPERTY = DateWidget.DATE_PROPERTY;  //  @jve:decl-index=0:
	/** Content validity property name.
	 * The panel is a bean. Every time the content validity changed, it sends a PropertyChangedEvent.
	 */
	public static final String CONTENT_VALID_PROPERTY = DateWidget.CONTENT_VALID_PROPERTY;
	
	private DateWidget dateWidget = null;
	private DateChooserPanel dateChooser;
	private JPopupMenu popup;
	private JLabel jLabel = null;

	/**
	 * This is the default constructor.
	 * Creates a new panel with the system default locale.
	 * The date is set to today
	 */
	public DateWidgetPanel() {
		super();
		popup = new JPopupMenu();
		dateChooser = new DateChooserPanel();
		dateChooser.setChosenDateButtonColor(Color.RED);
		dateChooser.setChosenOtherButtonColor(Color.GRAY);
		dateChooser.setChosenMonthButtonColor(Color.WHITE);
		popup.add(dateChooser);
		initialize();
		dateChooser.addPropertyChangeListener(DateChooserPanel.DATE_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				getDateWidget().setDate((Date)evt.getNewValue());
				popup.setVisible(false);
			}
		});
	}
	
	/** Get the currently choosen date.
	 * @return the currently choosen date. It is guaranteed that the hours, minutes, seconds,
	 * milliseconds of the date are set to 0.
	 */
	public Date getDate() {
		return getDateWidget().getDate();
	}
	
	/** Set the currently choosen date.
	 * @param date the date to be set.
	 */
	public void setDate(Date date) {
		getDateWidget().setDate(date);
		// No need to fire a property change.
		// The change property event will be sent by the property change listener
		// that is waiting for change of the DateWidget
	}
	
	/** Set the number of columns of the date text field.
	 * @param nb number of columns of the text field
	 */
	public void setColumns(int nb) {
		this.getDateWidget().setColumns(nb);
	}
	
	/** Set the locale.
	 * Changes the calendar popup appearence and the text field format.
	 */
	public void setLocale(Locale locale) {
		getDateWidget().setLocale(locale);
		dateChooser.setLocale(locale);
	}

	/** Set the DateWidget tooltip.
	 * @param text the new tooltip text
	 */
	@Override
	public void setToolTipText(String text) {
		getDateWidget().setToolTipText(text);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 1;
		gridBagConstraints11.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints11.gridy = 0;
		jLabel = new JLabel();
		jLabel.setText("");
		jLabel.setIcon(new ImageIcon(getClass().getResource("/net/yapbam/gui/widget/calendar.png")));
		jLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (jLabel.isEnabled() && !popup.isVisible()) {
					DateWidget widget = getDateWidget();
					dateChooser.setDate(widget.getDate());
					popup.show(widget, 0, widget.getHeight());
				}
			}
		});
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridx = 0;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(getDateWidget(), gridBagConstraints);
		this.add(jLabel, gridBagConstraints11);
	}

	/**
	 * Gets the DateWidget used by this component.
	 * @return a DateWidget instance
	 */
	public DateWidget getDateWidget() {
		if (dateWidget == null) {
			dateWidget = new DateWidget();
			PropertyChangeListener listener = new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
				}
			};
			dateWidget.addPropertyChangeListener(DateWidget.DATE_PROPERTY, listener);
			dateWidget.addPropertyChangeListener(DateWidget.CONTENT_VALID_PROPERTY, listener);
		}
		return dateWidget;
	}

	/**
	 * @see DateWidget#setIsEmptyNullDateIsValid(boolean)
	 */
	public void setIsEmptyNullDateIsValid(boolean valid) {
		this.getDateWidget().setIsEmptyNullDateIsValid(valid);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		dateWidget.setEnabled(enabled);
		jLabel.setEnabled(enabled);
	}
	
	/** Gets the content validity.
	 * @return true if the content is valid, false if it is not.
	 */
	public boolean isContentValid() {
		return this.dateWidget.isContentValid();
	}
}
