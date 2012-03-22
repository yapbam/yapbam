package net.yapbam.gui.filter;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import net.astesana.ajlib.utilities.NullUtils;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.AutoSelectFocusListener;
import net.yapbam.gui.widget.DateWidgetPanel;

public class DateFilterPanel extends ConsistencyCheckedPanel {
	public static Wordings TRANSACTION_DATE = new Wordings(LocalizationData.get("Transaction.date"),
			LocalizationData.get("CustomFilterPanel.date.all"), LocalizationData.get("CustomFilterPanel.date.all.toolTip"),
			LocalizationData.get("CustomFilterPanel.date.equals"), LocalizationData.get("CustomFilterPanel.date.equals.toolTip"),
			LocalizationData.get("CustomFilterPanel.date.between"), LocalizationData.get("CustomFilterPanel.date.between.toolTip"),
			LocalizationData.get("CustomFilterPanel.date.from.toolTip"), LocalizationData.get("CustomFilterPanel.date.to.toolTip"),
			LocalizationData.get("CustomFilterPanel.date.to"),
			LocalizationData.get("CustomFilterPanel.error.dateFrom"), LocalizationData.get("CustomFilterPanel.error.date.to"),
			LocalizationData.get("CustomFilterPanel.error.dateFromHigherThanTo"));
	public static Wordings VALUE_DATE = new Wordings(LocalizationData.get("Transaction.valueDate"),
			LocalizationData.get("CustomFilterPanel.valueDate.all"), LocalizationData.get("CustomFilterPanel.valueDate.all.toolTip"),
			LocalizationData.get("CustomFilterPanel.valueDate.equals"), LocalizationData.get("CustomFilterPanel.valueDate.equals.toolTip"),
			LocalizationData.get("CustomFilterPanel.valueDate.between"), LocalizationData.get("CustomFilterPanel.valueDate.between.toolTip"),
			LocalizationData.get("CustomFilterPanel.valueDate.from.toolTip"), LocalizationData.get("CustomFilterPanel.valueDate.to.toolTip"),
			LocalizationData.get("CustomFilterPanel.valueDate.to"),
			LocalizationData.get("CustomFilterPanel.error.valueDateFrom"), LocalizationData.get("CustomFilterPanel.error.valueDateTo"),
			LocalizationData.get("CustomFilterPanel.error.valueDateFromHigherThanTo"));
	
	private static final long serialVersionUID = 1L;
	private JRadioButton dateAll;
	private JRadioButton dateEquals;
	private JRadioButton dateBetween;
	private DateWidgetPanel dateFrom;
	private DateWidgetPanel dateTo;
	private Wordings wordings;
	
	public static class Wordings {
		public String title;
		public String all;
		public String allTooltip;
		public String equals;
		public String equalsTooltip;
		public String between;
		public String betweenTooltip;
		public String fromTooltip;
		public String toTooltip;
		public String to;
		public String errorFrom;
		public String errorTo;
		public String errorFromHigherThanTo;
		
		public Wordings(String title, String all, String allTooltip, String equals, String equalsTooltip, String between,
				String betweenTooltip, String fromTooltip, String toTooltip, String to, String errorFrom, String errorTo, String errorFromHigherThanTo) {
			super();
			this.title = title;
			this.all = all;
			this.allTooltip = allTooltip;
			this.equals = equals;
			this.equalsTooltip = equalsTooltip;
			this.between = between;
			this.betweenTooltip = betweenTooltip;
			this.fromTooltip = fromTooltip;
			this.toTooltip = toTooltip;
			this.to = to;
			this.errorFrom = errorFrom;
			this.errorTo = errorTo;
			this.errorFromHigherThanTo = errorFromHigherThanTo;
		}
	}

	public DateFilterPanel (Wordings wordings) {
		super();
		this.wordings = wordings;
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder(null, wordings.title, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51))); //$NON-NLS-1$ 
		GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
		gridBagConstraints25.gridx = 3;
		gridBagConstraints25.gridheight = 3;
		gridBagConstraints25.weightx = 1.0D;
		gridBagConstraints25.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints25.gridy = 0;
		GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
		gridBagConstraints24.gridx = 2;
		gridBagConstraints24.gridheight = 3;
		gridBagConstraints24.insets = new Insets(0, 5, 0, 5);
		gridBagConstraints24.gridy = 0;
		JLabel jLabel = new JLabel(wordings.to);
		GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
		gridBagConstraints23.gridx = 1;
		gridBagConstraints23.gridheight = 3;
		gridBagConstraints23.weightx = 1.0D;
		gridBagConstraints23.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints23.gridy = 0;
		GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
		gridBagConstraints22.gridx = 0;
		gridBagConstraints22.anchor = GridBagConstraints.WEST;
		gridBagConstraints22.gridy = 2;
		GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
		gridBagConstraints20.gridx = 0;
		gridBagConstraints20.anchor = GridBagConstraints.WEST;
		gridBagConstraints20.gridy = 1;
		GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
		gridBagConstraints19.gridx = 0;
		gridBagConstraints19.anchor = GridBagConstraints.WEST;
		gridBagConstraints19.gridy = 0;
		add(getDateAll(), gridBagConstraints19);
		add(getDateEquals(), gridBagConstraints20);
		add(getDateBetween(), gridBagConstraints22);
		add(getDateFromField(), gridBagConstraints23);
		add(jLabel, gridBagConstraints24);
		add(getDateToField(), gridBagConstraints25);
		ButtonGroup group = new ButtonGroup();
		group.add(getDateAll());
		group.add(getDateEquals());
		group.add(getDateBetween());
	}
	
	/**
	 * This method initializes dateAll	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getDateAll() {
		if (dateAll == null) {
			dateAll = new JRadioButton();
			dateAll.setText(wordings.all);
			dateAll.setToolTipText(wordings.allTooltip);
			dateAll.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (dateAll.isSelected()) {
						getDateFromField().setEnabled(false);
						getDateFromField().setDate(null);
						getDateToField().setEnabled(false);
						getDateToField().setDate(null);
					}
				}
			});
		}
		return dateAll;
	}

	/**
	 * This method initializes dateEquals	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getDateEquals() {
		if (dateEquals == null) {
			dateEquals = new JRadioButton();
			dateEquals.setText(wordings.equals);
			dateEquals.setToolTipText(wordings.equalsTooltip);
			dateEquals.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (dateEquals.isSelected()) {
						getDateFromField().setEnabled(true);
						getDateToField().setEnabled(false);
						getDateToField().setDate(getDateFromField().getDate());
					}
				}
			});
		}
		return dateEquals;
	}

	/**
	 * This method initializes dateBetween	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getDateBetween() {
		if (dateBetween == null) {
			dateBetween = new JRadioButton();
			dateBetween.setText(wordings.between);
			dateBetween.setToolTipText(wordings.betweenTooltip);
			dateBetween.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (dateBetween.isSelected()) {
						getDateFromField().setEnabled(true);
						getDateToField().setEnabled(true);
					}
				}
			});
		}
		return dateBetween;
	}

	/**
	 * This method initializes dateFrom	
	 * 	
	 * @return net.yapbam.gui.widget.DateWidgetPanel	
	 */
	private DateWidgetPanel getDateFromField() {
		if (dateFrom == null) {
			dateFrom = new DateWidgetPanel();
			dateFrom.setToolTipText(wordings.fromTooltip);
			dateFrom.getDateWidget().addFocusListener(AutoSelectFocusListener.INSTANCE);
			dateFrom.addPropertyChangeListener(DateWidgetPanel.DATE_PROPERTY, consistencyChecker);
			dateFrom.addPropertyChangeListener(DateWidgetPanel.CONTENT_VALID_PROPERTY, consistencyChecker);
		}
		return dateFrom;
	}

	/**
	 * This method initializes dateTo	
	 * 	
	 * @return net.yapbam.gui.widget.DateWidgetPanel	
	 */
	private DateWidgetPanel getDateToField() {
		if (dateTo == null) {
			dateTo = new DateWidgetPanel();
			dateTo.setToolTipText(wordings.toTooltip);
			dateTo.getDateWidget().addFocusListener(AutoSelectFocusListener.INSTANCE);
			dateTo.addPropertyChangeListener(DateWidgetPanel.DATE_PROPERTY, consistencyChecker);
			dateTo.addPropertyChangeListener(DateWidgetPanel.CONTENT_VALID_PROPERTY, consistencyChecker);
		}
		return dateTo;
	}

	@Override
	protected String computeInconsistencyCause() {
		if (!getDateFromField().isContentValid()) return wordings.errorFrom;
		if (!getDateToField().isContentValid()) return wordings.errorTo;
		if ((getDateFromField().getDate()!=null) && (getDateToField().getDate()!=null)
				&& (getDateFromField().getDate().compareTo(getDateToField().getDate())>0)) {
			return wordings.errorFromHigherThanTo;
		}
		return null;
	}

	public void clear() {
		getDateAll().setSelected(true);
	}

	public void setDates(Date from, Date to) {
		dateAll.setSelected(from==null && to==null);
		boolean areEquals = (from!=null) && NullUtils.areEquals(from, to);
		dateEquals.setSelected(areEquals);
		dateBetween.setSelected(!NullUtils.areEquals(from, to));
		dateFrom.setDate(from);
		dateTo.setDate(to);
	}
	
	public Date getDateFrom() {
		if (getDateAll().isSelected()) return null;
		return getDateFromField().getDate();
	}
	
	public Date getDateTo() {
		if (getDateAll().isSelected()) return null;
		if (getDateEquals().isSelected()) return getDateFrom();
		return getDateToField().getDate();
	}
}
