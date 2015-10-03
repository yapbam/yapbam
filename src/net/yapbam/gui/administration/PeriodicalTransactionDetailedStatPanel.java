package net.yapbam.gui.administration;

import javax.swing.JPanel;

import net.yapbam.data.PeriodicalTransactionSimulationData;
import net.yapbam.data.PeriodicalTransactionSimulationData.Unit;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
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

	private JPanel westPanel;
	private JLabel detailsLabel;
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
		setLayout(new BorderLayout(0, 0));
		add(getWestPanel(), BorderLayout.WEST);
		add(getDetailsLabel(), BorderLayout.CENTER);
	}

	private JPanel getWestPanel() {
		if (westPanel == null) {
			westPanel = new JPanel();
			westPanel.setBorder(new TitledBorder("Settings"));
			GridBagLayout gblWestPanel = new GridBagLayout();
			westPanel.setLayout(gblWestPanel);
			GridBagConstraints gbcNextMonth = new GridBagConstraints();
			gbcNextMonth.anchor = GridBagConstraints.WEST;
			gbcNextMonth.insets = new Insets(0, 0, 5, 0);
			gbcNextMonth.gridx = 0;
			gbcNextMonth.gridy = 0;
			westPanel.add(getNextMonth(), gbcNextMonth);
			GridBagConstraints gbcNext3months = new GridBagConstraints();
			gbcNext3months.insets = new Insets(0, 0, 5, 0);
			gbcNext3months.anchor = GridBagConstraints.WEST;
			gbcNext3months.gridx = 0;
			gbcNext3months.gridy = 1;
			westPanel.add(getNext3months(), gbcNext3months);
			GridBagConstraints gbcNextYear = new GridBagConstraints();
			gbcNextYear.anchor = GridBagConstraints.WEST;
			gbcNextYear.gridx = 0;
			gbcNextYear.gridy = 2;
			westPanel.add(getNextYear(), gbcNextYear);
		}
		return westPanel;
	}
	private JLabel getDetailsLabel() {
		if (detailsLabel == null) {
			detailsLabel = new JLabel("New label");
		}
		return detailsLabel;
	}
	private JRadioButton getNextMonth() {
		if (nextMonth == null) {
			nextMonth = new JRadioButton("Next month");
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
	private JRadioButton getNext3months() {
		if (next3months == null) {
			next3months = new JRadioButton("Next 3 months");
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
	private JRadioButton getNextYear() {
		if (nextYear == null) {
			nextYear = new JRadioButton("Next year");
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
