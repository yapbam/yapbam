package net.astesana.ajlib.swing.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.astesana.ajlib.swing.framework.Application;
import net.astesana.ajlib.swing.framework.JobFrame;
import net.astesana.ajlib.swing.framework.SwingWorkerJobAdapter;

public class JobFrameTest extends Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new JobFrameTest().launch();
	}

	@Override
	public String getName() {
		return "JobFrame test";
	}

	@Override
	protected JPanel buildMainPanel() {
		JPanel pane = new JPanel();
		JButton button = new JButton("Start");
		pane.add(button);
		
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new JobFrame(new Toto()).setVisible(true);
			}
		});
		return pane;
	}
	
	private static class Toto extends SwingWorkerJobAdapter<Void, Void> {
		@Override
		protected Void doInBackground() throws Exception {
			for (int i=0;i<1000;i++) {
				Thread.sleep(2);
				if (isCancelled()) return null;
			}
			setPhase("Pas de longueur", -1);
			for (int i=0;i<1000;i++) {
				Thread.sleep(1);
				if (isCancelled()) return null;
			}
			int nb = 10;
			setPhase("Longueur = "+nb, nb);
			for (int i=0;i<nb;i++) {
				Thread.sleep(300);
				try {
					reportProgress(i);
					System.out.println (i);
					if (isCancelled()) return null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	}

}
