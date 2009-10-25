package net.yapbam.currency.converter;


import javax.swing.JMenu;
import javax.swing.JMenuItem;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.AbstractPlugIn;

public class UtilitiesPlugIn extends AbstractPlugIn {
	public UtilitiesPlugIn(FilteredData data, Object restoreData) {}
	
	@Override
	public JMenu[] getPlugInMenu() {
		JMenu jmenu = new JMenu("Outils"); //LOCAL
		jmenu.add(new JMenuItem(new CurrencyConverterAction()));
		return new JMenu[]{jmenu};
	}

}
