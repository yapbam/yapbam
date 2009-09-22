package net.yapbam.ihm.dialogs;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import net.yapbam.date.helpers.DateStepper;
import net.yapbam.ihm.widget.AutoSelectFocusListener;
import net.yapbam.ihm.widget.DateWidget;
import net.yapbam.ihm.widget.IntegerWidget;

public class GenerationPanel extends JPanel { //LOCAL

	private static final long serialVersionUID = 1L;
	private JCheckBox activated = null;
	private JLabel jLabel = null;
	private DateWidget date = null;
	private JLabel jLabel1 = null;
	private IntegerWidget nb = null;
	private JComboBox kind = null;
	
	private AbstractDialog dialog;

	/**
	 * This is the default constructor.
	 * Needed by the Visual Editor
	 */
	public GenerationPanel() {
		this(null);
	}
	
	public GenerationPanel(AbstractDialog dialog) {
		super();
		this.dialog = dialog;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.fill = GridBagConstraints.NONE;
		gridBagConstraints5.gridy = 2;
		gridBagConstraints5.weightx = 1.0;
		gridBagConstraints5.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints5.anchor = GridBagConstraints.WEST;
		gridBagConstraints5.gridx = 4;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.fill = GridBagConstraints.NONE;
		gridBagConstraints4.gridy = 2;
		gridBagConstraints4.weightx = 0.0D;
		gridBagConstraints4.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints4.anchor = GridBagConstraints.EAST;
		gridBagConstraints4.gridx = 3;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 2;
		gridBagConstraints3.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints3.anchor = GridBagConstraints.WEST;
		gridBagConstraints3.gridy = 2;
		jLabel1 = new JLabel();
		jLabel1.setText("Répétition tous les :");
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.NONE;
		gridBagConstraints2.gridy = 2;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.gridwidth = 1;
		gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints2.anchor = GridBagConstraints.WEST;
		gridBagConstraints2.gridx = 1;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints1.anchor = GridBagConstraints.EAST;
		gridBagConstraints1.gridy = 2;
		jLabel = new JLabel();
		jLabel.setText("Prochaine date : ");
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridy = 0;
		this.setSize(479, 200);
		this.setLayout(new GridBagLayout());
		this.add(getActivated(), gridBagConstraints);
		this.add(jLabel, gridBagConstraints1);
		this.add(getDate(), gridBagConstraints2);
		this.add(jLabel1, gridBagConstraints3);
		this.add(getNb(), gridBagConstraints4);
		this.add(getKind(), gridBagConstraints5);
	}

	/**
	 * This method initializes activated	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	public JCheckBox getActivated() {
		if (activated == null) {
			activated = new JCheckBox();
			activated.setText("Activée");
			activated.setSelected(true);
			activated.setToolTipText("Décochez cette case pour suspendre la génération de cette opération");
		}
		return activated;
	}

	/**
	 * This method initializes date	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public DateWidget getDate() {
		if (date == null) {
			date = new DateWidget();
			date.addKeyListener(new AutoUpdateOkButtonKeyListener(this.dialog));
			date.addFocusListener(new AutoSelectFocusListener());
			date.setColumns(6);
			date.setToolTipText("Entrez ici la date de la prochaine opération");
		}
		return date;
	}

	/**
	 * This method initializes nb	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private IntegerWidget getNb() {
		if (nb == null) {
	        nb = new IntegerWidget(1, Integer.MAX_VALUE);
	        nb.addFocusListener(new AutoSelectFocusListener());
	        nb.addKeyListener(new AutoUpdateOkButtonKeyListener(dialog));
			nb.setColumns(2);
			nb.setToolTipText("Entrez ici l'interval entre deux opérations");
		}
		return nb;
	}

	/**
	 * This method initializes kind	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getKind() {
		if (kind == null) {
			kind = new JComboBox(new String[]{"mois","jours"});
		}
		return kind;
	}
	
	public DateStepper getDateStepper() {
		return null; //TODO
	}

	public void setDateStepper(DateStepper nextDateBuilder) {
		// TODO Auto-generated method stub
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
