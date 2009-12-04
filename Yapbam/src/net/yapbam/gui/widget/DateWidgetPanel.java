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

import javax.swing.JButton;

public class DateWidgetPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final String DATE_PROPERTY = DateWidget.DATE_PROPERTY;
	
	private DateWidget dateWidget = null;
	private JButton jButton = null;
	private DateChooserPanel dateChooser;
	private JPopupMenu popup;

	/**
	 * This is the default constructor
	 */
	public DateWidgetPanel() {
		super();
		popup = new JPopupMenu();
		dateChooser = new DateChooserPanel();
		dateChooser.setChosenDateButtonColor(Color.RED);
		dateChooser.setChosenOtherButtonColor(Color.GRAY);
		dateChooser.setChosenMonthButtonColor(Color.WHITE);
		dateChooser.addPropertyChangeListener(DateChooserPanel.DATE_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				getDateWidget().setDate((Date)evt.getNewValue());
			}
		});
		popup.add(dateChooser);
		initialize();
	}
	
	public Date getDate() {
		return getDateWidget().getDate();
	}
	
	public void setDate(Date date) {
		getDateWidget().setDate(date);
		// No need to fire a property change.
		// The change property event will be sent by the property change listener
		// that is waiting for change of the DateWidget
	}
	
	public void setColumns(int nb) {
		this.getDateWidget().setColumns(nb);
	}
	
	public void setLocale(Locale locale) {
		getDateWidget().setLocale(locale);
		dateChooser.setLocale(locale);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridx = 0;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(getDateWidget(), gridBagConstraints);
		this.add(getJButton(), gridBagConstraints1);
	}

	/**
	 * This method initializes dateWidget
	 * 	
	 * @return net.yapbam.gui.widget.DateWidget	
	 */
	private DateWidget getDateWidget() {
		if (dateWidget == null) {
			dateWidget = new DateWidget();
			dateWidget.addPropertyChangeListener(DateWidget.DATE_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					firePropertyChange(DATE_PROPERTY, evt.getOldValue(), evt.getNewValue());
				}
			});
		}
		return dateWidget;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setFocusable(false);
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (!popup.isVisible()) {
						DateWidget widget = getDateWidget();
						dateChooser.setDate(widget.getDate());
						popup.show(widget, 0, widget.getHeight());
					}
				}
			});
		}
		return jButton;
	}

	public void setIsEmptyNullDateIsValid(boolean valid) {
		this.getDateWidget().setIsEmptyNullDateIsValid(valid);
	}
}
