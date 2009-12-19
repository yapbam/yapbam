package net.yapbam.gui.dialogs;

import java.awt.Window;

import javax.swing.JPanel;

import net.yapbam.data.FilteredData;

@SuppressWarnings("serial")
public class CustomFilterDialog extends AbstractDialog {

	public CustomFilterDialog(Window owner, FilteredData data) {
		super(owner, "Filtre", data); //LOCAL
	}

	@Override
	protected Object buildResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JPanel createCenterPane(Object data) {
		return new CustomFilterPanel((FilteredData) data);
	}

	@Override
	protected String getOkDisabledCause() {
		// TODO Auto-generated method stub
		return null;
	}

}
