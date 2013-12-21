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
		GridBagConstraints gbc_rdbtnEuropeanCentralBank = new GridBagConstraints();
		gbc_rdbtnEuropeanCentralBank.weightx = 1.0;
		gbc_rdbtnEuropeanCentralBank.anchor = GridBagConstraints.WEST;
		gbc_rdbtnEuropeanCentralBank.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnEuropeanCentralBank.gridx = 0;
		gbc_rdbtnEuropeanCentralBank.gridy = 0;
		add(getRdbtnEuropeanCentralBank(), gbc_rdbtnEuropeanCentralBank);
		GridBagConstraints gbc_rdbtnYahoo = new GridBagConstraints();
		gbc_rdbtnYahoo.weighty = 1.0;
		gbc_rdbtnYahoo.weightx = 1.0;
		gbc_rdbtnYahoo.anchor = GridBagConstraints.NORTHWEST;
		gbc_rdbtnYahoo.gridx = 0;
		gbc_rdbtnYahoo.gridy = 1;
		add(getRdbtnYahoo(), gbc_rdbtnYahoo);
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
		}
		return rdbtnEuropeanCentralBank;
	}
	private JRadioButton getRdbtnYahoo() {
		if (rdbtnYahoo == null) {
			rdbtnYahoo = new JRadioButton("Yahoo");
		}
		return rdbtnYahoo;
	}
	
	@Override
	public boolean updatePreferences() {
		// TODO Auto-generated method stub
		return false;
	}
}
