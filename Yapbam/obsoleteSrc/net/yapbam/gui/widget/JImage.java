package net.yapbam.gui.widget;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.MediaTracker;

import javax.swing.JComponent;
import javax.swing.JPanel;

/** An image component. 
 * <br>This component displays an image.
 */
@SuppressWarnings("serial")
public class JImage extends JComponent {
	// Set up constraints so that the user supplied component and the
	// background image label overlap and resize identically
	private static GridBagConstraints gbc;

	private Image image;

	/** Constructor.
	 * @param image The image to be displayed by the component.
	 */
	public JImage(Image image) {
		this.image = image;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		MediaTracker tracker = new MediaTracker(this);
		tracker.addImage(image, 0);
		try {
			tracker.waitForID(0);
		} catch (InterruptedException exception) {
		}
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	}
	
	/** Wraps a JComponent in a background image.
	 * @param component The component to wrap in the a background image
	 * @param background The background image
	 * @return a new JPanel wrapping the component
	 */
	public static JPanel wrapInBackgroundImage(JComponent component, Image background) {
		// make the passed in swing component transparent
		component.setOpaque(false);
		// create the wrapper JPanel
		JPanel backgroundPanel = new JPanel(new GridBagLayout());
		if (gbc==null) {
			gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.anchor = GridBagConstraints.NORTHWEST;
		}
		// add the passed in swing component first to ensure that it is in front
		backgroundPanel.add(component, gbc);
		// add the background label
		backgroundPanel.add(new JImage(background), gbc);
		// return the wrapper
		return backgroundPanel;
	}
}