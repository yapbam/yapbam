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
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.widget.AutoSelectFocusListener;

public class ModeDialog extends AbstractDialog {
	private static final long serialVersionUID = 1L;
	
	private static final boolean DEBUG = false;

	private JTextField name;
	private JCheckBox chequeBook;
	private ModePanel leftPane;
	private ModePanel rightPane;
	
	private String initialName;
	
	public ModeDialog(Window owner, Account account) {
		super(owner, LocalizationData.get("ModeDialog.new.title"), account); //$NON-NLS-1$
		this.initialName = null;
	}
	
	protected JPanel createCenterPane(Object data) {
		Account account = (Account) data;
        //Create the content pane.
        JPanel centerPane = new JPanel(new BorderLayout());
        centerPane.add(new JLabel(MessageFormat.format(LocalizationData.get("ModeDialog.account"), account.getName())), BorderLayout.NORTH); //$NON-NLS-1$
        JPanel main = new JPanel(new GridBagLayout());
        if (DEBUG) main.setBorder(BorderFactory.createTitledBorder("main")); //$NON-NLS-1$
        centerPane.add(main, BorderLayout.CENTER);
        
        JPanel idPanel = new JPanel(new GridBagLayout());
        if (DEBUG) idPanel.setBorder(BorderFactory.createTitledBorder("idPanel")); //$NON-NLS-1$
        GridBagConstraints c = new GridBagConstraints(); c.insets = new Insets(10,5,5,5);
        c.anchor=GridBagConstraints.WEST;
        idPanel.add(new JLabel(LocalizationData.get("ModeDialog.name")),c); //$NON-NLS-1$
        c.gridx=1; c.weightx=1.0; c.fill=GridBagConstraints.HORIZONTAL;
        name = new JTextField(10);
        name.addKeyListener(new AutoUpdateOkButtonKeyListener(this));
        name.addFocusListener(new AutoSelectFocusListener());
		idPanel.add(name,c);       
        
        chequeBook = new JCheckBox(LocalizationData.get("ModeDialog.useCheckBook")); //$NON-NLS-1$
		leftPane = new ModePanel(LocalizationData.get("ModeDialog.forDebts"), chequeBook, this);     //$NON-NLS-1$
        rightPane = new ModePanel(LocalizationData.get("ModeDialog.forReceipts"), null, this); //$NON-NLS-1$
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
		ModeDialog dialog = new ModeDialog(owner, account);
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
		Account account = (Account) this.data;
		if (name.length()==0) {
			return LocalizationData.get("ModeDialog.bad.emptyName"); //$NON-NLS-1$
		} else if ((account.getMode(name)!=null) && !name.equalsIgnoreCase(initialName)) {
			return MessageFormat.format(LocalizationData.get("ModeDialog.bad.duplicateMode"),name, account.getName()); //$NON-NLS-1$
		} else if (!(leftPane.isSelected()||rightPane.isSelected())) {
			return LocalizationData.get("ModeDialog.bad.neverAvalaible"); //$NON-NLS-1$
		} else if (leftPane.isSelected() && !leftPane.hasValidContent()) {
			return LocalizationData.get("ModeDialog.bad.debt"); //$NON-NLS-1$
		} else if (rightPane.isSelected() && !rightPane.hasValidContent()) {
			return LocalizationData.get("ModeDialog.bad.receipt"); //$NON-NLS-1$
		}
		return null;
	}

	class Listener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			updateOkButtonEnabled();
		}
	}
	

}