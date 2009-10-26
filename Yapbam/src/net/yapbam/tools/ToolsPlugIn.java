package net.yapbam.tools;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.tools.currency.converter.CurrencyConverterAction;

public class ToolsPlugIn extends AbstractPlugIn {
	public ToolsPlugIn(FilteredData data, Object restoreData) {
		Messages.reset();
	}
	
	@Override
	public JMenu[] getPlugInMenu() {
		JMenu jmenu = new JMenu(Messages.getString("ToolsPlugIn.menu.title")); //$NON-NLS-1$
        jmenu.setToolTipText(Messages.getString("ToolsPlugIn.menu.toolTip")); //$NON-NLS-1$
		jmenu.add(new JMenuItem(new CurrencyConverterAction()));
		return new JMenu[]{jmenu};
	}
}
