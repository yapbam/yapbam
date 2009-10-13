package net.yapbam.popup;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.yapbam.data.GlobalData;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel pane = new JPanel();
        f.add(pane);
        pane.add(new JLabel("Question : "));
        String[] array = new String[] {"Carrefour", "Plein voiture", "Plein moto", "Carrefour Market", "Magazines", "Réparation", "Charges", "Impôts locaux",
        		"Impots fonciers", "Impôt sur le revenu", "Cadeau Isa", "Inscription vacances CE", "Cinéma", "Barrete mémoire Maman"/**/};
        final PopupTextFieldList field = new PopupTextFieldList();
        field.setPredefined(array);
        field.setColumns(10);
        pane.add(field);
        f.setSize(400,200);
        f.setLocation(200,200);
        f.setVisible(true);
        GlobalData data = new GlobalData();
        try {
        	long start = System.currentTimeMillis();
			data.read(new File("C:/Users/Jean-Marc/Documents/Comptes.xml"));
			System.out.println ("Lecture des "+data.getTransactionsNumber()+" opérations en "+(System.currentTimeMillis()-start)+"ms");
	        TestDialog dialog = new TestDialog(f, "Dialogue", data);
			dialog.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
