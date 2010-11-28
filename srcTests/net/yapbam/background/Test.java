package net.yapbam.background;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Test {
	// Set up contraints so that the user supplied component and the
	// background image label overlap and resize identically
	private static final GridBagConstraints gbc;
	static {
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTHWEST;
	}

	/**
	 * Wraps a Swing JComponent in a background image.
	 * @param component The component to wrap in the a background image
	 * @param background The background image
	 * @return the wrapping JPanel
	 */
	public static JPanel wrapInBackgroundImage(JComponent component, Image background) {
		// make the passed in swing component transparent
		component.setOpaque(false);
		// create the wrapper JPanel
		JPanel backgroundPanel = new JPanel(new GridBagLayout());
		// add the passed in swing component first to ensure that it is in front
		backgroundPanel.add(component, gbc);
		// add the background label
		backgroundPanel.add(new JImage(background), gbc);
		// return the wrapper
		return backgroundPanel;
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Background Image Panel Example");

		// Create some GUI
		JPanel foregroundPanel = new JPanel(new BorderLayout(10, 10));
		foregroundPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		foregroundPanel.setOpaque(false);

		foregroundPanel.add(new JLabel("Comment:"), BorderLayout.NORTH);
		foregroundPanel.add(new JScrollPane(new JTextArea(5, 30)), BorderLayout.CENTER);
		foregroundPanel.add(new JLabel("Please enter your comments in text box above." + " HTML syntax is allowed."), BorderLayout.SOUTH);

		Image image = Toolkit.getDefaultToolkit().getImage(Test.class.getResource("background.png"));
		frame.setContentPane(wrapInBackgroundImage(foregroundPanel, image));

		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	@SuppressWarnings("serial")
	private static class JImage extends JLabel {
		private Image image;

		public JImage(Image image) {
			this.image = image;
		}

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
	}

}