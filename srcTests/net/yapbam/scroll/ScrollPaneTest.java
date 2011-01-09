package net.yapbam.scroll;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ScrollPaneTest {
	public ScrollPaneTest() {
		JScrollPane scrollPane1 = new JScrollPane(getPanel(), JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JScrollPane scrollPane2 = new JScrollPane(getPanel());

		Synchronizer synchronizer = new Synchronizer(scrollPane1, scrollPane2);
		scrollPane1.getVerticalScrollBar().addAdjustmentListener(synchronizer);
		scrollPane1.getHorizontalScrollBar().addAdjustmentListener(synchronizer);
		scrollPane2.getVerticalScrollBar().addAdjustmentListener(synchronizer);
		scrollPane2.getHorizontalScrollBar().addAdjustmentListener(synchronizer);

		JPanel panel = new JPanel(new GridLayout(1, 0));
		panel.add(scrollPane1);
		panel.add(scrollPane2);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(panel);
		f.setSize(500, 300);
		f.setLocation(200, 200);
		f.setVisible(true);
	}

	@SuppressWarnings("serial")
	private JPanel getPanel() {
		JPanel panel = new JPanel() {
			GradientPaint gradient;

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				if (gradient == null) init();
				g2.setPaint(gradient);
				g2.fillRect(0, 0, getWidth(), getHeight());
			}

			private void init() {
				gradient = new GradientPaint(0, 0, Color.red, getWidth(), getHeight(), Color.blue);
			}
		};
		panel.setPreferredSize(new Dimension(400, 400));
		return panel;
	}

	public static void main(String[] args) {
		new ScrollPaneTest();
	}
}

class Synchronizer implements AdjustmentListener {
	JScrollBar v1, h1, v2, h2;

	public Synchronizer(JScrollPane sp1, JScrollPane sp2) {
		v1 = sp1.getVerticalScrollBar();
		h1 = sp1.getHorizontalScrollBar();
		v2 = sp2.getVerticalScrollBar();
		h2 = sp2.getHorizontalScrollBar();
	}

	public void adjustmentValueChanged(AdjustmentEvent e) {
		JScrollBar scrollBar = (JScrollBar) e.getSource();
		int value = scrollBar.getValue();
		JScrollBar target = null;

		if (scrollBar == v1) target = v2;
		if (scrollBar == h1) target = h2;
		if (scrollBar == v2) target = v1;
		if (scrollBar == h2) target = h1;

		target.setValue(value);
	}
}