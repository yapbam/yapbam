package net.yapbam.dialogs;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.JCheckBox;

import net.yapbam.gui.util.DialogUtils;

public class DialogSpy extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JCheckBox chckbxModalDialogOpened;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DialogSpy frame = new DialogSpy();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DialogSpy() {
		setTitle("Dialog Spy");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		chckbxModalDialogOpened = new JCheckBox("Modal dialog opened");
		contentPane.add(chckbxModalDialogOpened, BorderLayout.NORTH);
		
		SwingWorker<Void, Boolean> worker = new SwingWorker<Void, Boolean>() {
			@Override
			public Void doInBackground() {
				while (true) {
					try {
						Thread.sleep(1000, 0);
						publish(DialogUtils.isModalDialogShowing());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			@Override
			protected void process(List<Boolean> chunks) {
				chckbxModalDialogOpened.setSelected(chunks.get(0));
			}
		};
		worker.execute();
	}
}
