package net.yapbam.gui.tools;

import net.yapbam.gui.PreferencePanel;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class CurrencyConverterPreferencePanel extends PreferencePanel{
	private static final long serialVersionUID = 1L;
	private JRadioButton rdbtnEuropeanCentralBank;
	private JRadioButton rdbtnYahoo;

	public CurrencyConverterPreferencePanel() {
		initialize();
	}
	private void initialize() {
		setDisplayed(false);
		setBorder(BorderFactory.createTitledBorder(null, "Data source", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION));
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbcRdbtnEuropeanCentralBank = new GridBagConstraints();
		gbcRdbtnEuropeanCentralBank.weightx = 1.0;
		gbcRdbtnEuropeanCentralBank.anchor = GridBagConstraints.WEST;
		gbcRdbtnEuropeanCentralBank.insets = new Insets(0, 0, 5, 0);
		gbcRdbtnEuropeanCentralBank.gridx = 0;
		gbcRdbtnEuropeanCentralBank.gridy = 0;
		add(getRdbtnEuropeanCentralBank(), gbcRdbtnEuropeanCentralBank);
		GridBagConstraints gbcRdbtnYahoo = new GridBagConstraints();
		gbcRdbtnYahoo.weighty = 1.0;
		gbcRdbtnYahoo.weightx = 1.0;
		gbcRdbtnYahoo.anchor = GridBagConstraints.NORTHWEST;
		gbcRdbtnYahoo.gridx = 0;
		gbcRdbtnYahoo.gridy = 1;
		add(getRdbtnYahoo(), gbcRdbtnYahoo);
		ButtonGroup group = new ButtonGroup();
		group.add(getRdbtnEuropeanCentralBank());
		group.add(getRdbtnYahoo());
	}

	@Override
	public String getTitle() {
		return Messages.getString("ToolsPlugIn.currencyConverter.title");
	}

	@Override
	public String getToolTip() {
		return null;
	}
	
	private JRadioButton getRdbtnEuropeanCentralBank() {
		if (rdbtnEuropeanCentralBank == null) {
			rdbtnEuropeanCentralBank = new JRadioButton("European central bank");
			rdbtnEuropeanCentralBank.setSelected(CurrencyConverterSource.ECB.equals(CurrencyConverterAction.getSource()));
		}
		return rdbtnEuropeanCentralBank;
	}
	private JRadioButton getRdbtnYahoo() {
		if (rdbtnYahoo == null) {
			rdbtnYahoo = new JRadioButton("Yahoo");
			rdbtnYahoo.setSelected(CurrencyConverterSource.YAHOO.equals(CurrencyConverterAction.getSource()));
		}
		return rdbtnYahoo;
	}
	
	@Override
	public boolean updatePreferences() {
		CurrencyConverterAction.setSource(getRdbtnYahoo().isSelected() ? CurrencyConverterSource.YAHOO: CurrencyConverterSource.ECB);
		return false;
	}
}
