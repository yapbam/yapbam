package net.yapbam.gui.dialogs;

import java.awt.Window;

import javax.swing.JPanel;

import net.yapbam.data.GlobalData;

@SuppressWarnings("serial")
public class CustomFilterDialog extends AbstractDialog {

	public CustomFilterDialog(Window owner, GlobalData data) {
		super(owner, "Filtre", data); //LOCAL
	}

	@Override
	protected Object buildResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JPanel createCenterPane(Object data) {
		JPanel result = new JPanel();
		result.add(new CustomFilterPanel((GlobalData) data));
		return result;
	}

	@Override
	protected String getOkDisabledCause() {
		// TODO Auto-generated method stub
		return null;
	}

}
