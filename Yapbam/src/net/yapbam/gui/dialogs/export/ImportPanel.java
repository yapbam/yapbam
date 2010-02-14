package net.yapbam.gui.dialogs.export;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.GridLayout;

public class ImportPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JScrollPane jScrollPane = null;
	private JScrollPane jScrollPane1 = null;
	private JList fileColumns = null;
	private JList yapbamDataList = null;
	private JButton firstLine = null;
	private JPanel jPanel1 = null;
	private JPanel jPanel2 = null;
	private JPanel jPanel = null;

	/**
	 * This is the default constructor
	 */
	public ImportPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
		gridBagConstraints7.gridx = 0;
		gridBagConstraints7.gridy = 1;
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.fill = GridBagConstraints.BOTH;
		gridBagConstraints6.weightx = 1.0D;
		gridBagConstraints6.weighty = 1.0D;
		gridBagConstraints6.gridy = 0;
		jLabel1 = new JLabel();
		jLabel1.setText("Rubriques");
		jLabel = new JLabel();
		jLabel.setText("Fichier de données");
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(getFirstLine(), gridBagConstraints7);
		this.add(getJPanel(), gridBagConstraints6);
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setPreferredSize(new Dimension(0, 0));
			jScrollPane.setViewportView(getFileColumns());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setPreferredSize(new Dimension(0, 0));
			jScrollPane1.setViewportView(getYapbamDataList());
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes fileColumns	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getFileColumns() {
		if (fileColumns == null) {
			fileColumns = new JList();
			fileColumns.setSize(new Dimension(0, 0));
		}
		return fileColumns;
	}

	/**
	 * This method initializes yapbamDataList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getYapbamDataList() {
		if (yapbamDataList == null) {
			yapbamDataList = new JList();
			yapbamDataList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			yapbamDataList.setSize(new Dimension(0, 0));
		}
		return yapbamDataList;
	}

	/**
	 * This method initializes firstLine	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getFirstLine() {
		if (firstLine == null) {
			firstLine = new JButton();
			firstLine.setToolTipText("Afficher la première ligne");
		}
		return firstLine;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = -1;
			jPanel1 = new JPanel();
			jPanel1.setLayout(new GridBagLayout());
			jPanel1.add(jLabel, gridBagConstraints);
			jPanel1.add(getJScrollPane(), gridBagConstraints2);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.weighty = 1.0;
			gridBagConstraints3.gridx = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			jPanel2 = new JPanel();
			jPanel2.setLayout(new GridBagLayout());
			jPanel2.add(jLabel1, gridBagConstraints1);
			jPanel2.add(getJScrollPane1(), gridBagConstraints3);
		}
		return jPanel2;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(1);
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = -1;
			gridBagConstraints5.gridy = -1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = -1;
			gridBagConstraints4.gridy = -1;
			jPanel = new JPanel();
			jPanel.setLayout(gridLayout);
			jPanel.add(getJPanel1(), null);
			jPanel.add(getJPanel2(), null);
		}
		return jPanel;
	}

}
