package net.yapbam.gui.dialogs;

/** The interface of classes able to compute lists of predefined values of a PopupTextFieldList
 * @see PopupTextFieldList#setPredefined(String[], int[])
 */
public interface PredefinedDescriptionComputer {
	public String[] getPredefined();
	public int[] getGroupSizes();
}