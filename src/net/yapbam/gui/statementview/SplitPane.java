package net.yapbam.gui.statementview;

import java.awt.Component;

import javax.swing.JSplitPane;

public class SplitPane extends JSplitPane {
	private static final long serialVersionUID = 1L;

	private int defaultDividerSize;

	public SplitPane() {
		this(JSplitPane.HORIZONTAL_SPLIT);
	}

	public SplitPane(int newOrientation, boolean newContinuousLayout, Component newLeftComponent, Component newRightComponent) {
		super(newOrientation, newContinuousLayout, newLeftComponent, newRightComponent);
		defaultDividerSize = getDividerSize();
	}

	public SplitPane(int newOrientation, boolean newContinuousLayout) {
		this(newOrientation, newContinuousLayout, null, null);
	}

	public SplitPane(int newOrientation, Component newLeftComponent, Component newRightComponent) {
		this(newOrientation, false, newLeftComponent, newRightComponent);
	}

	public SplitPane(int newOrientation) {
		this(newOrientation, false);
	}

	public void setDividerVisible(boolean visible) {
		setDividerSize(visible ? defaultDividerSize : 0);
	}
	
	public boolean isDividerVisible() {
		return getDividerSize()!=0;
	}
}
