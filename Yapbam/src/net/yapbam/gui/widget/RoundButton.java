package net.yapbam.gui.widget;

import java.awt.*;
import java.awt.event.*;

/**
 * RoundButton - a class that produces a lightweight button.
 * 
 * Lightweight components can have "transparent" areas, meaning that you can see
 * the background of the container behind these areas.
 * 
 */
public class RoundButton extends Component {
	private static final long serialVersionUID = 1L;

	ActionListener actionListener; // Post action events to listeners
	String label; // The Button's text
	protected boolean pressed = false; // true if the button is detented.
	private float transparency;
	private boolean inside;

	/**
	 * Constructs a RoundButton with no label.
	 */
	public RoundButton() {
		this("");
	}

	/**
	 * Constructs a RoundButton with the specified label.
	 * 
	 * @param label
	 *          the label of the button
	 */
	public RoundButton(String label) {
		this.label = label;
		this.transparency = (float) 1.0;
		this.inside = false;
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}

	/**
	 * gets the label
	 * 
	 * @see setLabel
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * sets the label
	 * 
	 * @see getLabel
	 */
	public void setLabel(String label) {
		this.label = label;
		invalidate();
		repaint();
	}

	/**
	 * Gets the transparency (alpha channel)
	 * @return a float between 0 and 1 (opaque)
	 * @see setLabel
	 */
	public float getTransparency() {
		return this.transparency;
	}

	/**
	 * Sets the transparency
	 * @see getLabel
	 */
	public void setTransparency(float alpha) {
		if ((alpha<0) || (alpha>1)) throw new IllegalArgumentException();
		this.transparency = alpha;
		invalidate();
		repaint();
	}

	/**
	 * paints the RoundButton
	 */
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Composite originalComposite = g2d.getComposite();
		g2d.setComposite(makeComposite(this.transparency));
		g2d.setPaint(Color.red);

		int s = Math.min(getSize().width - 1, getSize().height - 1);

		// paint the interior of the button
		if (inside) {
			if (pressed) {
				g.setColor(getBackground().darker().darker());
			} else {
				g.setColor(getBackground().darker());
			}
			g.fillArc(0, 0, s, s, 0, 360);
		}

		g2d.setComposite(originalComposite);

		// draw the perimeter of the button
//		g.setColor(getBackground().darker().darker().darker());
//		g.drawArc(0, 0, s, s, 0, 360);

		// draw the label centered in the button
		Font f = getFont();
		if (f != null) {
			FontMetrics fm = getFontMetrics(getFont());
			g.setColor(getForeground());
			g.drawString(label, s / 2 - fm.stringWidth(label) / 2, s / 2 + fm.getMaxDescent());
		}
	}
	
	private AlphaComposite makeComposite(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type, alpha));
	}
/*
	private void drawSquares(Graphics2D g2d, float alpha) {
		Composite originalComposite = g2d.getComposite();
		g2d.setPaint(Color.blue);
		g2d.fill(blueSquare);
		g2d.setComposite(makeComposite(alpha));
		g2d.setPaint(Color.red);
		g2d.fill(redSquare);
		g2d.setComposite(originalComposite);
	}
*/

	/**
	 * The preferred size of the button.
	 */
	public Dimension getPreferredSize() {
		Font f = getFont();
		if (f != null) {
			FontMetrics fm = getFontMetrics(getFont());
			int max = Math.max(fm.stringWidth(label) + 40, fm.getHeight() + 40);
			return new Dimension(max, max);
		} else {
			return new Dimension(100, 100);
		}
	}

	/**
	 * The minimum size of the button.
	 */
	public Dimension getMinimumSize() {
		return new Dimension(100, 100);
	}

	/**
	 * Adds the specified action listener to receive action events from this
	 * button.
	 * 
	 * @param listener
	 *          the action listener
	 */
	public void addActionListener(ActionListener listener) {
		actionListener = AWTEventMulticaster.add(actionListener, listener);
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}

	/**
	 * Removes the specified action listener so it no longer receives action
	 * events from this button.
	 * 
	 * @param listener
	 *          the action listener
	 */
	public void removeActionListener(ActionListener listener) {
		actionListener = AWTEventMulticaster.remove(actionListener, listener);
	}

	/**
	 * Determine if click was inside round button.
	 */
	public boolean contains(int x, int y) {
		int mx = getSize().width / 2;
		int my = getSize().height / 2;
		return (((mx - x) * (mx - x) + (my - y) * (my - y)) <= mx * mx);
	}

	/**
	 * Paints the button and distribute an action event to all listeners.
	 */
	public void processMouseEvent(MouseEvent e) {
		switch (e.getID()) {
		case MouseEvent.MOUSE_PRESSED:
			// render myself inverted....
			pressed = true;

			// Repaint might flicker a bit. To avoid this, you can use
			// double buffering (see the Gauge example).
			repaint();
			break;
		case MouseEvent.MOUSE_RELEASED:
			if (actionListener != null) {
				actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, label));
			}
			// render myself normal again
			if (pressed == true) {
				pressed = false;

				// Repaint might flicker a bit. To avoid this, you can use
				// double buffering (see the Gauge example).
				repaint();
			}
			break;
		case MouseEvent.MOUSE_ENTERED:
			this.inside = true;

			// Repaint might flicker a bit. To avoid this, you can use
			// double buffering (see the Gauge example).
			repaint();
			break;
		case MouseEvent.MOUSE_EXITED:
			this.inside = false;
			if (pressed == true) {
				// Cancel! Don't send action event.
				pressed = false;

				// Note: for a more complete button implementation,
				// you wouldn't want to cancel at this point, but
				// rather detect when the mouse re-entered, and
				// re-highlight the button. There are a few state
				// issues that that you need to handle, which we leave
				// this an an excercise for the reader (I always
				// wanted to say that!)
			}
			// Repaint might flicker a bit. To avoid this, you can use
			// double buffering (see the Gauge example).
			repaint();

			break;
		}
		super.processMouseEvent(e);
	}

}
