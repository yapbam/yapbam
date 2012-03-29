package net.yapbam.gui.dialogs;

/** The interface of classes able to compute lists of predefined values of a PopupTextFieldList
 */
public interface PredefinedDescriptionComputer {
	public String[] getPredefined();
	public int getUnsortedSize();
}