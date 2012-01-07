package net.yapbam.gui.dialogs;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JComboBox;

import net.yapbam.date.helpers.DateStepper;
import net.yapbam.date.helpers.DayDateStepper;
import net.yapbam.date.helpers.MonthDateStepper;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.AutoSelectFocusListener;
import net.yapbam.gui.widget.DateWidgetPanel;
import net.yapbam.gui.widget.IntegerWidget;
import net.yapbam.util.NullUtils;

import java.math.BigInteger;
import java.util.Date;

public class GenerationPanel extends JPanel {
	// Properties
	public static final String ACTIVATED_PROPERTY = "activated";  //  @jve:decl-index=0: //$NON-NLS-1$
	public static final String DATE_STEPPER_PROPERTY = "dateStepper"; //$NON-NLS-1$
	public static final String NEXT_DATE_PROPERTY = "nextDate"; //$NON-NLS-1$  //  @jve:decl-index=0:

	private static final long serialVersionUID = 1L;
	private JCheckBox activatedBox = null;
	private JLabel jLabel = null;
	private DateWidgetPanel date = null;
	private JLabel jLabel1 = null;
	private IntegerWidget nb = null;
	private JComboBox kind = null;
	
	private JPanel jPanel = null;
	private JLabel jLabel2 = null;
	private IntegerWidget day = null;

	private DateStepper currentDateStepper;  //  @jve:decl-index=0:
	private Date currentNextDate;  //  @jve:decl-index=0:
	private JLabel jLabel3 = null;
	private DateWidgetPanel lastDate = null;

	public GenerationPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		jLabel3 = new JLabel();
		jLabel3.setText(LocalizationData.get("PeriodicalTransactionDialog.until"));
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.gridwidth = 3;
		gridBagConstraints11.anchor = GridBagConstraints.WEST;
		gridBagConstraints11.gridy = 1;
		jLabel1 = new JLabel();
		jLabel1.setText(LocalizationData.get("PeriodicalTransactionDialog.period")); //$NON-NLS-1$
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.NONE;
		gridBagConstraints2.gridy = 0;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.gridwidth = 1;
		gridBagConstraints2.insets = new Insets(5, 5, 5, 0);
		gridBagConstraints2.anchor = GridBagConstraints.WEST;
		gridBagConstraints2.gridx = 2;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints1.anchor = GridBagConstraints.EAST;
		gridBagConstraints1.gridy = 0;
		jLabel = new JLabel();
		jLabel.setText(LocalizationData.get("PeriodicalTransactionDialog.nextDate")); //$NON-NLS-1$
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridy = 0;
		this.setSize(400, 165);
		this.setLayout(new GridBagLayout());
		this.setActivated(true);
		this.add(getActivatedBox(), gridBagConstraints);
		this.add(jLabel, gridBagConstraints1);
		this.add(getDate(), gridBagConstraints2);
		this.add(getJPanel(), gridBagConstraints11);
		updateDateStepper();
		currentNextDate = getDate().getDate();
	}

	/**
	 * This method initializes activatedBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getActivatedBox() {
		if (activatedBox == null) {
			activatedBox = new JCheckBox();
			activatedBox.setText(LocalizationData.get("PeriodicalTransactionDialog.activated")); //$NON-NLS-1$
			activatedBox.setSelected(true);
			activatedBox.setToolTipText(LocalizationData.get("PeriodicalTransactionDialog.activated.toolTip")); //$NON-NLS-1$
			activatedBox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					firePropertyChange(ACTIVATED_PROPERTY, !activatedBox.isSelected(), activatedBox.isSelected());
				}
			});
		}
		return activatedBox;
	}
	
	public boolean isActivated() {
		return getActivatedBox().isSelected();
	}
	
	public void setActivated(boolean activated) {
		getActivatedBox().setSelected(activated);
	}

	/**
	 * This method initializes date	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private DateWidgetPanel getDate() {
		if (date == null) {
			date = new DateWidgetPanel();
			date.setLocale(LocalizationData.getLocale());
			date.addPropertyChangeListener(DateWidgetPanel.DATE_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					Date old = currentNextDate;
					currentNextDate = date.getDate();
					if (!areEquals(old, currentNextDate)) {
						firePropertyChange(NEXT_DATE_PROPERTY, old, currentNextDate);
					}
				}
			});
			date.getDateWidget().addFocusListener(AutoSelectFocusListener.INSTANCE);
			date.setColumns(6);
			date.setToolTipText(LocalizationData.get("PeriodicalTransactionDialog.nextDate.toolTip")); //$NON-NLS-1$
		}
		return date;
	}
	
	public Date getNextDate() {
		return currentNextDate;
	}
	
	public void setNextDate(Date next) {
		if (!areEquals(next, currentNextDate)) {
			Object old = currentNextDate;
			currentNextDate = next;
			this.getDate().setDate(currentNextDate);
			firePropertyChange(NEXT_DATE_PROPERTY, old, currentNextDate);
		}
	}

	private boolean areEquals (Date d1, Date d2) {
		if (d1==null) {
			return (d2==null);
		} else {
			return d1.equals(d2);
		}
	}
	
	/**
	 * This method initializes nb	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private IntegerWidget getNb() {
		if (nb == null) {
			nb = new IntegerWidget(BigInteger.ONE, IntegerWidget.INTEGER_MAX_VALUE);
			nb.addFocusListener(AutoSelectFocusListener.INSTANCE);
			nb.addPropertyChangeListener(IntegerWidget.VALUE_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					updateDateStepper();
				}
			});
			nb.setColumns(2);
			nb.setToolTipText(LocalizationData.get("PeriodicalTransactionDialog.step.toolTip")); //$NON-NLS-1$
		}
		return nb;
	}

	/**
	 * This method initializes kind	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getKind() {
		if (kind == null) {
			kind = new JComboBox(new String[]{LocalizationData.get("PeriodicalTransactionDialog.months"),LocalizationData.get("PeriodicalTransactionDialog.days")}); //$NON-NLS-1$ //$NON-NLS-2$
			kind.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					boolean visible = isMonthly();
					jLabel2.setVisible(visible);
					day.setVisible(visible);
					updateDateStepper();
				}
			});
		}
		return kind;
	}
	
	private void updateDateStepper() {
		DateStepper newStepper;
		if (nb.getValue()==null) {
			newStepper = null;
		} else if (isMonthly()) {
			if (day.getValue()==null) {
				newStepper = null;
			} else {
				newStepper = new MonthDateStepper(nb.getValue().intValue(), day.getValue().intValue(), getLastDate().getDate());
			}
		} else {
			newStepper = new DayDateStepper(nb.getValue().intValue(), getLastDate().getDate());
		}
		if (!areEquals(newStepper,currentDateStepper)) {
			Object old = currentDateStepper;
			currentDateStepper = newStepper;
			firePropertyChange(DATE_STEPPER_PROPERTY, old, currentDateStepper);
		}
	}
	
	private boolean areEquals (DateStepper s1, DateStepper s2) {
		if (s1==null) {
			return (s2==null);
		} else if (s2 == null) {
			return false;
		} else if (!NullUtils.areEquals(s1.getLastDate(),s2.getLastDate())) {
			return false;
		} else if (s1 instanceof DayDateStepper) {
			if (!(s2 instanceof DayDateStepper)) return false;
			return ((DayDateStepper)s1).equals((DayDateStepper)s2);
		} else if (s1 instanceof MonthDateStepper) {
			return ((MonthDateStepper)s1).equals((MonthDateStepper)s2);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public DateStepper getDateStepper() {
		return currentDateStepper;
	}

	public void setDateStepper(DateStepper nextDateBuilder) {
		if (!areEquals(nextDateBuilder, currentDateStepper)) {
			if (nextDateBuilder instanceof MonthDateStepper) {
				kind.setSelectedIndex(0);
				nb.setValue(((MonthDateStepper)nextDateBuilder).getPeriod());
				day.setValue(((MonthDateStepper)nextDateBuilder).getDay());
				getLastDate().setDate(nextDateBuilder.getLastDate());
			} else if (nextDateBuilder instanceof DayDateStepper) {
				kind.setSelectedIndex(1);
				nb.setValue(((DayDateStepper)nextDateBuilder).getStep());
				getLastDate().setDate(nextDateBuilder.getLastDate());
			} else if (nextDateBuilder==null) {
				kind.setSelectedIndex(-1);
				nb.setText(""); //$NON-NLS-1$
				day.setText(""); //$NON-NLS-1$
			} else {
				throw new IllegalArgumentException();
			}
			Object old = currentDateStepper;
			currentDateStepper = nextDateBuilder;
			firePropertyChange(DATE_STEPPER_PROPERTY, old, currentDateStepper);
		}
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints9.gridy = 0;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints9.gridx = 6;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints8.gridy = 0;
			gridBagConstraints8.gridx = 5;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints7.gridx = 4;
			gridBagConstraints7.gridy = 0;
			gridBagConstraints7.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints7.weightx = 1.0;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 3;
			gridBagConstraints6.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints6.gridy = 0;
			jLabel2 = new JLabel();
			jLabel2.setText(LocalizationData.get("PeriodicalTransactionDialog.the")); //$NON-NLS-1$
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints5.weightx = 1.0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.anchor = GridBagConstraints.EAST;
			gridBagConstraints4.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints4.gridx = -1;
			gridBagConstraints4.gridy = -1;
			gridBagConstraints4.weightx = 0.0D;
			gridBagConstraints4.fill = GridBagConstraints.NONE;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.gridx = -1;
			gridBagConstraints3.gridy = -1;
			gridBagConstraints3.insets = new Insets(5, 5, 5, 5);
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(jLabel1, gridBagConstraints3);
			jPanel.add(getNb(), gridBagConstraints4);
			jPanel.add(getKind(), gridBagConstraints5);
			jPanel.add(jLabel2, gridBagConstraints6);
			jPanel.add(getDay(), gridBagConstraints7);
			jPanel.add(jLabel3, gridBagConstraints8);
			jPanel.add(getLastDate(), gridBagConstraints9);
		}
		return jPanel;
	}

	/**
	 * This method initializes day	
	 * 	
	 * @return net.yapbam.ihm.widget.IntegerWidget	
	 */
	private IntegerWidget getDay() {
		if (day == null) {
			day = new IntegerWidget(BigInteger.ONE, BigInteger.valueOf(31));
			day.addFocusListener(AutoSelectFocusListener.INSTANCE);
			day.addPropertyChangeListener(IntegerWidget.VALUE_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					updateDateStepper();
				}
			});
			day.setToolTipText(LocalizationData.get("PeriodicalTransactionDialog.day.toolTip")); //$NON-NLS-1$
			day.setColumns(2);
		}
		return day;
	}

	private boolean isMonthly() {
		return (kind.getSelectedIndex()==0);
	}

	/**
	 * This method initializes lastDate	
	 * 	
	 * @return net.yapbam.gui.widget.DateWidget	
	 */
	private DateWidgetPanel getLastDate() {
		if (lastDate == null) {
			lastDate = new DateWidgetPanel();
			lastDate.setLocale(LocalizationData.getLocale());
			lastDate.setDate(null);
			lastDate.setColumns(6);
			lastDate.setToolTipText(LocalizationData.get("PeriodicalTransactionDialog.lastDate.toolTip")); //$NON-NLS-1$
			lastDate.getDateWidget().addFocusListener(AutoSelectFocusListener.INSTANCE);
			lastDate.addPropertyChangeListener(DateWidgetPanel.DATE_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					updateDateStepper();
				}
			});
		}
		return lastDate;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
