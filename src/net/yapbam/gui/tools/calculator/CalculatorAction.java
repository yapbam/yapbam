package net.yapbam.gui.tools.calculator;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import com.fathzer.soft.ajlib.swing.ToolsFrame;
import com.fathzer.soft.ajlib.swing.Utils;

import net.yapbam.gui.tools.Messages;

@SuppressWarnings("serial")
public class CalculatorAction extends AbstractAction {
	private JFrame frame;
	
	public CalculatorAction() {
		super(Messages.getString("ToolsPlugIn.calculator.title")); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, Messages.getString("ToolsPlugIn.calculator.toolTip")); //$NON-NLS-1$
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (frame==null) {
			Window owner = Utils.getOwnerWindow((Component) e.getSource());
			frame = new ToolsFrame(owner, new CalculatorPanel());
			frame.setTitle(Messages.getString("ToolsPlugIn.calculator.title")); //$NON-NLS-1$
		}
		frame.setState(Frame.NORMAL);
		frame.setVisible(true);
	}
}
