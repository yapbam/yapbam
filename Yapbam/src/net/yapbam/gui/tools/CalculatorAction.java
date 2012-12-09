package net.yapbam.gui.tools;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import net.astesana.ajlib.swing.Utils;
import net.yapbam.gui.tools.calculator.CalculatorPanel;

@SuppressWarnings("serial")
public class CalculatorAction extends AbstractAction {
	private JFrame frame;
	
	CalculatorAction() {
		super(Messages.getString("ToolsPlugIn.calculator.title")); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, Messages.getString("ToolsPlugIn.calculator.toolTip")); //$NON-NLS-1$
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (frame==null) {
			Window owner = Utils.getOwnerWindow((Component) e.getSource());
			frame = new JFrame(Messages.getString("ToolsPlugIn.calculator.title"));
			frame.setContentPane(new CalculatorPanel());
			frame.pack();
			frame.setResizable(false);
			Utils.centerWindow(frame, owner);
			owner.addWindowListener(new WindowAdapter() {
				/* (non-Javadoc)
				 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
				 */
				@Override
				public void windowClosing(WindowEvent e) {
					frame.dispose();
					frame = null;
					super.windowClosing(e);
				}

				/* (non-Javadoc)
				 * @see java.awt.event.WindowAdapter#windowIconified(java.awt.event.WindowEvent)
				 */
				@Override
				public void windowIconified(WindowEvent e) {
					frame.setState(Frame.ICONIFIED);
					super.windowIconified(e);
				}

				/* (non-Javadoc)
				 * @see java.awt.event.WindowAdapter#windowDeiconified(java.awt.event.WindowEvent)
				 */
				@Override
				public void windowDeiconified(WindowEvent e) {
					frame.setState(Frame.NORMAL);
					super.windowDeiconified(e);
				}
			});
		}
		frame.setState(Frame.NORMAL);
		frame.setVisible(true);
	}
}
