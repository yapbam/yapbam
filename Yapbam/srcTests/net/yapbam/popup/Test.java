package net.yapbam.popup;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.yapbam.gui.widget.PopupTextFieldList;

public class Test {

//	private static final String DATA_FILE = "C:/Users/Jean-Marc/Documents/Comptes.xml";
//	private static final String DATA_FILE = "//home/jma/Comptes.xml";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel pane = new JPanel(new BorderLayout());
        f.add(pane);
        JPanel p1 = new JPanel(new BorderLayout());
        p1.add(new JLabel("Question : "),BorderLayout.WEST);
        String[] array = new String[] {"Carrefour", "Plein voiture", "Plein moto", "Carrefour Market", "Magazines", "Réparation", "Charges", "Impôts locaux",
        		"Impots fonciers", "Impôt sur le revenu", "Cadeau Isa", "Inscription vacances CE", "Cinéma", "Barrete mémoire Maman"/**/};
        final PopupTextFieldList field = new PopupTextFieldList();
        field.setPredefined(array, new int[]{3,2});
        field.setColumns(10);
        p1.add(field,BorderLayout.CENTER);
        pane.add(p1,BorderLayout.NORTH);
        f.setSize(400,200);
        f.setLocation(200,200);
        f.setVisible(true);
/*        
        GlobalData data = new GlobalData();
        try {
        	long start = System.currentTimeMillis();
			data.read(new File(DATA_FILE));
			System.out.println ("Lecture des "+data.getTransactionsNumber()+" opï¿½rations en "+(System.currentTimeMillis()-start)+"ms");
	        TestDialog dialog = new TestDialog(f, "Dialogue", data);
			dialog.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

}
