package net.yapbam.popup;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.yapbam.data.GlobalData;
import net.yapbam.gui.widget.PopupTextFieldList;

public class Test {

//	private static final String DATA_FILE = "C:/Users/Jean-Marc/Documents/Comptes.xml";
	private static final String DATA_FILE = "//home/jma/Comptes.xml";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel pane = new JPanel();
        f.add(pane);
        pane.add(new JLabel("Question : "));
        String[] array = new String[] {"Carrefour", "Plein voiture", "Plein moto", "Carrefour Market", "Magazines", "R�paration", "Charges", "Imp�ts locaux",
        		"Impots fonciers", "Imp�t sur le revenu", "Cadeau Isa", "Inscription vacances CE", "Cin�ma", "Barrete m�moire Maman"/**/};
        final PopupTextFieldList field = new PopupTextFieldList();
        field.setPredefined(array);
        field.setColumns(10);
        pane.add(field);
        f.setSize(400,200);
        f.setLocation(200,200);
        f.setVisible(true);
/*        
        GlobalData data = new GlobalData();
        try {
        	long start = System.currentTimeMillis();
			data.read(new File(DATA_FILE));
			System.out.println ("Lecture des "+data.getTransactionsNumber()+" op�rations en "+(System.currentTimeMillis()-start)+"ms");
	        TestDialog dialog = new TestDialog(f, "Dialogue", data);
			dialog.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

}
