package net.yapbam.popup;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel pane = new JPanel();
        f.add(pane);
        pane.add(new JLabel("Question"));
        final JTextField field = new JTextField(10);
        field.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					JPopupMenu popup = new JPopupMenu("xxx");
					popup.add(new JMenuItem("choix 1"));
					popup.add(new JMenuItem("choix 2"));
					popup.add(new JMenuItem("choix 3"));
					popup.add(new JMenuItem("choix 4"));
					Dimension size = popup.getPreferredSize();
					popup.setPreferredSize(new Dimension(field.getWidth(), size.height));
					popup.show(field, 0, field.getHeight());
					field.requestFocus(false);
				} else {
					super.keyPressed(e);
				}
			}
		});
        pane.add(field);
        f.setSize(400,400);
        f.setLocation(200,200);
        f.setVisible(true);
//		popup.show(peer, 0, peer.getHeight());
	}

}
