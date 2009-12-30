package net.yapbam.gui.widget;

import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.*;

/** A default InputVerifier that turns the component background to red when it is not valid.
 * This class is deprecated, because the behaviour of class controlled by it is ... strange.
 * Example : The component contains an invalid text, then you enter programaticaly a right value and
 *  disable the component => I've found no way to have the default disabled component background color.
 *  The focus system behaviour is strange too; The tab key doesn't switch to another component if
 *  the component is not valid, but it leave the component if you click elsewhere. 
 * @deprecated */
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
        	input.setBackground(input.isEnabled()?Color.WHITE:Color.LIGHT_GRAY);
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
