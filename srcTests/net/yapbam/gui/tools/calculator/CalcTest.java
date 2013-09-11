package net.yapbam.gui.tools.calculator;

import java.awt.Container;

import com.fathzer.soft.ajlib.swing.framework.Application;


public class CalcTest extends Application {

	@Override
	protected Container buildMainPanel() {
		return new CalculatorPanel();
	}

	@Override
	protected boolean onStart() {
		this.getJFrame().setResizable(false);
		this.getJFrame().pack();
		return super.onStart();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new CalcTest().launch();
	}
}
