package net.yapbam.gui.tools;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.tools.calculator.CalculatorAction;
import net.yapbam.gui.tools.currencyconverter.CurrencyConverterAction;

public class ToolsPlugIn extends AbstractPlugIn {
	public ToolsPlugIn(FilteredData data, Object restoreData) {
		Messages.reset();
	}
	
	@Override
	public JMenu[] getPlugInMenu() {
		JMenu jmenu = new JMenu(Messages.getString("ToolsPlugIn.menu.title")); //$NON-NLS-1$
		jmenu.setToolTipText(Messages.getString("ToolsPlugIn.menu.toolTip")); //$NON-NLS-1$
		jmenu.add(new JMenuItem(new CurrencyConverterAction()));
		jmenu.add(new JMenuItem(new CalculatorAction()));
		return new JMenu[]{jmenu};
	}
}
