package net.yapbam.ihm.dialogs;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;

import javax.swing.*;

import net.yapbam.data.Account;
import net.yapbam.data.Mode;
import net.yapbam.ihm.widget.AutoSelectFocusListener;

public class NewModeDialog extends AbstractDialog {
	private static final long serialVersionUID = 1L;
	
	private static final boolean DEBUG = false;

	private JTextField name;
	private JCheckBox chequeBook;
	private ModePanel leftPane;
	private ModePanel rightPane;
	private Account account;
	
	private NewModeDialog(Window owner, Account account) {
		super(owner, "Nouveau mode de paiement", account); //LOCAL
		this.account = account;
	}
	
	protected JPanel createCenterPane(Object data) {
		Account account = (Account) data;
        //Create the content pane.
        JPanel centerPane = new JPanel(new BorderLayout());
        centerPane.add(new JLabel(MessageFormat.format("Compte : {0}", account.getName())), BorderLayout.NORTH);
        JPanel main = new JPanel(new GridBagLayout());
        if (DEBUG) main.setBorder(BorderFactory.createTitledBorder("main"));
        centerPane.add(main, BorderLayout.CENTER);
        
        JPanel idPanel = new JPanel(new GridBagLayout());
        if (DEBUG) idPanel.setBorder(BorderFactory.createTitledBorder("idPanel"));
        GridBagConstraints c = new GridBagConstraints(); c.insets = new Insets(10,5,5,5);
        c.anchor=GridBagConstraints.WEST;
        idPanel.add(new JLabel("Nom :"),c);
        c.gridx=1; c.weightx=1.0; c.fill=GridBagConstraints.HORIZONTAL;
        name = new JTextField(10);
        name.addKeyListener(new AutoUpdateOkButtonKeyListener(this));
        name.addFocusListener(new AutoSelectFocusListener());
		idPanel.add(name,c);       
        
        chequeBook = new JCheckBox("Utiliser un chéquier");
		leftPane = new ModePanel("Utilisable pour les dépenses", chequeBook, this);    
        rightPane = new ModePanel("Utilisable pour les recettes", null, this);
        Listener listener = new Listener();
		leftPane.addPropertyChangeListener(listener);
		rightPane.addPropertyChangeListener(listener);

		c = new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL; c.gridwidth=GridBagConstraints.REMAINDER;
		main.add(idPanel, c);
		c = new GridBagConstraints();
        c.gridy=1; c.anchor = GridBagConstraints.NORTH; c.insets=new Insets(0,0,0,5);
        main.add(leftPane,c);
        c.gridx=1; c.insets=new Insets(0,5,0,0);
        main.add(rightPane,c);

        return centerPane;
    }
	
	public Mode getMode() {
		return (Mode) getResult();
	}

	@Override
	protected Object buildResult() {
		return new Mode(name.getText(),rightPane.getValueDateComputer(),leftPane.getValueDateComputer(),chequeBook.isSelected());
	}
	
	/** Opens the dialog, and add the newly created mode to the data
	 * @param data The account where to add the new mode
	 * @param owner The frame upon which the dialog will be displayed
	 * @return Mode the mode which was added, null if the operation was canceled
	 */
	public static Mode open(Account account, Window owner) {
		NewModeDialog dialog = new NewModeDialog(owner, account);
		dialog.setVisible(true);
		Mode newMode = dialog.getMode();
		if (newMode!=null) {
			account.add(newMode);
		}
		return newMode;
	}

	@Override
	protected String getOkDisabledCause() {
		String name = this.name.getText().trim();
		if (name.length()==0) {
			return "Ce bouton est désactivé car le nom du mode est vide";
		} else if (this.account.getMode(name)!=null) {
			return MessageFormat.format("Ce bouton est désactivé car le mode {0} existe déjà dans le compte {1}",name, account.getName());
		} else if (!(leftPane.isSelected()||rightPane.isSelected())) {
			return "Ce bouton est désactivé car le mode n'est utilisable ni pour les recettes, ni pour les dépenses";
		} else if (leftPane.isSelected() && !leftPane.hasValidContent()) {
			return "Ce bouton est désactivé car les informations d'utilisation pour les dépenses sont incohérentes";
		} else if (rightPane.isSelected() && !rightPane.hasValidContent()) {
			return "Ce bouton est désactivé car les informations d'utilisation pour les recettes sont incohérentes";
		}
		return null;
	}

	class Listener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			updateOkButtonEnabled();
		}
	}
	

}