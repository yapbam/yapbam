package net.yapbam.gui.widget;

import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.*;

/** A default InputVeriier that turns the component background to red when it is not valid */
abstract class DefaultInputVerifier extends InputVerifier {

	@Override
	public boolean verify(JComponent input) {
		return check(input, false);
	}

	@Override
	public boolean shouldYieldFocus(JComponent input) {
		boolean inputOK = verify(input);
        if (inputOK) {
        	check(input,true);
        	input.setBackground(Color.WHITE);
            return true;
        } else {
        	input.setBackground(Color.RED);
            Toolkit.getDefaultToolkit().beep();
            return false;
        }
	}

	/** check if the input is right or wrong
	 * @param input The component to be checked.
	 * @param changeAllowed if true, the method is allowed to change the text of the component
	 *  (for instance, if the current content is a valid shortcut, the method can change the
	 *  the content to the shortcut target)
	 *  Be aware that, if change is false the method MUST NOT have any side effect.
	 * @return true if the content is valid.
	 */
	protected abstract boolean check (JComponent input, boolean changeAllowed);
}
