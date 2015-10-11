package net.yapbam.gui.administration;

import javax.swing.JPanel;

import net.yapbam.data.PeriodicalTransactionSimulationData;
import net.yapbam.data.PeriodicalTransactionSimulationData.Unit;
import net.yapbam.gui.LocalizationData;

import java.awt.GridBagLayout;
import javax.swing.border.TitledBorder;
import javax.swing.JRadioButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.ButtonGroup;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

@SuppressWarnings("serial")
public class PeriodicalTransactionDetailedStatPanel extends JPanel {
	private PeriodicalTransactionSimulationData data;

	private JRadioButton nextMonth;
	private JRadioButton next3months;
	private JRadioButton nextYear;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	/**
	 * Create the panel.
	 * @param data 
	 */
	public PeriodicalTransactionDetailedStatPanel(PeriodicalTransactionSimulationData data) {
		this.data = data;
		initialize();
	}
	
	private void initialize() {
		setBorder(new TitledBorder(LocalizationData.get("PeriodicalTransactionManager.settings.title"))); //$NON-NLS-1$
		GridBagLayout gblWestPanel = new GridBagLayout();
		setLayout(gblWestPanel);
		GridBagConstraints gbcNextMonth = new GridBagConstraints();
		gbcNextMonth.anchor = GridBagConstraints.WEST;
		gbcNextMonth.insets = new Insets(0, 0, 5, 0);
		gbcNextMonth.gridx = 0;
		gbcNextMonth.gridy = 0;
		add(getNextMonth(), gbcNextMonth);
		GridBagConstraints gbcNext3months = new GridBagConstraints();
		gbcNext3months.insets = new Insets(0, 0, 5, 0);
		gbcNext3months.anchor = GridBagConstraints.WEST;
		gbcNext3months.gridx = 0;
		gbcNext3months.gridy = 1;
		add(getNext3months(), gbcNext3months);
		GridBagConstraints gbcNextYear = new GridBagConstraints();
		gbcNextYear.anchor = GridBagConstraints.WEST;
		gbcNextYear.gridx = 0;
		gbcNextYear.gridy = 2;
		add(getNextYear(), gbcNextYear);
	}

	JRadioButton getNextMonth() {
		if (nextMonth == null) {
			nextMonth = new JRadioButton(LocalizationData.get("PeriodicalTransactionManager.settings.nextMonth")); //$NON-NLS-1$
			nextMonth.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (nextMonth.isSelected()) {
						setEndDate(Unit.MONTH, 1);
					}
				}
			});
			nextMonth.setSelected(true);
			buttonGroup.add(nextMonth);
		}
		return nextMonth;
	}
	JRadioButton getNext3months() {
		if (next3months == null) {
			next3months = new JRadioButton(LocalizationData.get("PeriodicalTransactionManager.settings.nextQuarter")); //$NON-NLS-1$
			buttonGroup.add(next3months);
			next3months.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (next3months.isSelected()) {
						setEndDate(Unit.MONTH, 3);
					}
				}
			});
		}
		return next3months;
	}
	JRadioButton getNextYear() {
		if (nextYear == null) {
			nextYear = new JRadioButton(LocalizationData.get("PeriodicalTransactionManager.settings.nextYear")); //$NON-NLS-1$
			buttonGroup.add(nextYear);
			nextYear.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (nextYear.isSelected()) {
						setEndDate(Unit.YEAR, 1);
					}
				}
			});
		}
		return nextYear;
	}
	private void setEndDate(Unit unit, int amount) {
		data.setEndDate(unit, amount);
	}
}
