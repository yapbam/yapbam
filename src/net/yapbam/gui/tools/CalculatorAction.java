package net.yapbam.gui.tools;

import java.awt.Dialog.ModalityType;
import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

import net.astesana.ajlib.swing.Utils;
import net.yapbam.gui.tools.calculator.CalculatorPanel;

@SuppressWarnings("serial")
public class CalculatorAction extends AbstractAction {
	private JDialog frame;
	
	CalculatorAction() {
		super(Messages.getString("ToolsPlugIn.calculator.title")); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, Messages.getString("ToolsPlugIn.calculator.toolTip")); //$NON-NLS-1$
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (frame==null) {
			frame = new JDialog(Utils.getOwnerWindow((Component) e.getSource()), Messages.getString("ToolsPlugIn.calculator.title"), ModalityType.MODELESS);
			frame.setContentPane(new CalculatorPanel());
			frame.pack();
			frame.setResizable(false);
		}
		frame.setVisible(true);
	}
}
