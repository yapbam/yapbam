package net.yapbam.ihm.dialogs;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JComboBox;

import net.yapbam.date.helpers.DateStepper;
import net.yapbam.ihm.widget.AutoSelectFocusListener;
import net.yapbam.ihm.widget.DateWidget;
import net.yapbam.ihm.widget.IntegerWidget;
import java.lang.Integer;

public class GenerationPanel extends JPanel { //LOCAL

	private static final long serialVersionUID = 1L;
	private JCheckBox activated = null;
	private JLabel jLabel = null;
	private DateWidget date = null;
	private JLabel jLabel1 = null;
	private IntegerWidget nb = null;
	private JComboBox kind = null;
	
	private AbstractDialog dialog;
	private JPanel jPanel = null;
	private JLabel jLabel2 = null;
	private IntegerWidget day = null;

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
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.gridwidth = 3;
		gridBagConstraints11.anchor = GridBagConstraints.WEST;
		gridBagConstraints11.gridy = 3;
		jLabel1 = new JLabel();
		jLabel1.setText("Puis tous les");
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.NONE;
		gridBagConstraints2.gridy = 0;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.gridwidth = 1;
		gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints2.anchor = GridBagConstraints.WEST;
		gridBagConstraints2.gridx = 2;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints1.anchor = GridBagConstraints.EAST;
		gridBagConstraints1.gridy = 0;
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
		this.add(getJPanel(), gridBagConstraints11);
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

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints7.gridx = 4;
			gridBagConstraints7.gridy = 0;
			gridBagConstraints7.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints7.weightx = 1.0;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 3;
			gridBagConstraints6.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints6.gridy = 0;
			jLabel2 = new JLabel();
			jLabel2.setText("le");
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints5.weightx = 1.0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.anchor = GridBagConstraints.EAST;
			gridBagConstraints4.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints4.gridx = -1;
			gridBagConstraints4.gridy = -1;
			gridBagConstraints4.weightx = 0.0D;
			gridBagConstraints4.fill = GridBagConstraints.NONE;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.gridx = -1;
			gridBagConstraints3.gridy = -1;
			gridBagConstraints3.insets = new Insets(5, 5, 5, 5);
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(jLabel1, gridBagConstraints3);
			jPanel.add(getNb(), gridBagConstraints4);
			jPanel.add(getKind(), gridBagConstraints5);
			jPanel.add(jLabel2, gridBagConstraints6);
			jPanel.add(getDay(), gridBagConstraints7);
		}
		return jPanel;
	}

	/**
	 * This method initializes day	
	 * 	
	 * @return net.yapbam.ihm.widget.IntegerWidget	
	 */
	private IntegerWidget getDay() {
		if (day == null) {
			day = new IntegerWidget(1, Integer.MAX_VALUE);
			day.setToolTipText("Entrez ici l\'interval entre deux opérations");
			day.setColumns(2);
		}
		return day;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
