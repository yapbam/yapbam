package net.yapbam.gui.transactiontable;

import java.awt.GridBagLayout;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.PreferencePanel;
import net.yapbam.gui.Preferences;

import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.Insets;

import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;
import java.util.Locale;

import javax.swing.JButton;

public class TransactionsPreferencePanel extends PreferencePanel {
	private static final long serialVersionUID = 1L;
	private JPanel jPanel = null;
	private BalanceReportField positiveBalanceReport = null;
	private BalanceReportField negativeBalanceReport = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private JButton setTodefault = null;
	private JPanel jPanel1 = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	
	static String NEGATIVE_KEY = "net.yapbam.balanceReport.negative"; //$NON-NLS-1$
	static String POSITIVE_KEY = "net.yapbam.balanceReport.positive"; //$NON-NLS-1$
	static Color DEFAULT_POSITIVE = new Color(0,200,0);
	static Color DEFAULT_NEGATIVE = Color.RED;
		
	/**
	 * This is the default constructor
	 */
	public TransactionsPreferencePanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.fill = GridBagConstraints.BOTH;
		gridBagConstraints6.weighty = 1.0D;
		gridBagConstraints6.weightx = 1.0D;
		gridBagConstraints6.gridy = 1;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints4.anchor = GridBagConstraints.NORTH;
		gridBagConstraints4.weightx = 1.0D;
		gridBagConstraints4.gridy = 0;
		this.setSize(473, 196);
		this.setLayout(new GridBagLayout());
		this.add(getJPanel(), gridBagConstraints4);
		this.add(getJPanel1(), gridBagConstraints6);
	}

	@Override
	public String getTitle() {
		return LocalizationData.get("MainFrame.Transactions.Preferences.title"); //$NON-NLS-1$
	}

	@Override
	public String getToolTip() {
		return LocalizationData.get("MainFrame.Transactions.Preferences.tooltip"); //$NON-NLS-1$
	}

	@Override
	public boolean updatePreferences() {
		Color positive = positiveBalanceReport.getForeground();
		Color negative = negativeBalanceReport.getForeground();
		if (positive.equals(BalanceReportField.POSITIVE_COLOR) &&
				negative.equals(BalanceReportField.NEGATIVE_COLOR)) {
			return false;
		}
		BalanceReportField.POSITIVE_COLOR = positive;
		BalanceReportField.NEGATIVE_COLOR = negative;
		Preferences.INSTANCE.setProperty(TransactionsPreferencePanel.POSITIVE_KEY, Integer.toString(positive.getRGB()));
		Preferences.INSTANCE.setProperty(TransactionsPreferencePanel.NEGATIVE_KEY, Integer.toString(negative.getRGB()));
		return true;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
			gridBagConstraints61.gridx = 2;
			gridBagConstraints61.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints61.weightx = 1.0D;
			gridBagConstraints61.gridy = 0;
			jLabel1 = new JLabel();
			jLabel1.setText(""); //$NON-NLS-1$
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints3.gridy = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints2.gridy = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridy = 0;
			negativeBalanceReport = new BalanceReportField(LocalizationData.get("MainFrame.Transactions.Preferences.balanceSummary.negativeSample")); //$NON-NLS-1$
			negativeBalanceReport.setValue(-100);
			positiveBalanceReport = new BalanceReportField(LocalizationData.get("MainFrame.Transactions.Preferences.balanceSummary.positiveSample")); //$NON-NLS-1$
			positiveBalanceReport.setValue(100);
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.setBorder(BorderFactory.createTitledBorder(null, LocalizationData.get("MainFrame.Transactions.Preferences.balanceSummary.title"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51))); //$NON-NLS-1$ //$NON-NLS-2$
			jPanel.add(positiveBalanceReport, gridBagConstraints);
			jPanel.add(negativeBalanceReport, gridBagConstraints1);
			jPanel.add(getJButton(), gridBagConstraints2);
			jPanel.add(getJButton1(), gridBagConstraints3);
			jPanel.add(jLabel1, gridBagConstraints61);
		}
		return jPanel;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText(LocalizationData.get("MainFrame.Transactions.Preferences.changeColor")); //$NON-NLS-1$
			jButton.setToolTipText(LocalizationData.get("MainFrame.Transactions.Preferences.changeColor.tooltip")); //$NON-NLS-1$
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Color c = localizedColorChooser();
					if (c!=null) {
						positiveBalanceReport.setForeground(c);
					}
				}
			});
		}
		return jButton;
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText(LocalizationData.get("MainFrame.Transactions.Preferences.changeColor")); //$NON-NLS-1$
			jButton1.setToolTipText(LocalizationData.get("MainFrame.Transactions.Preferences.changeColor.tooltip")); //$NON-NLS-1$
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Color c = localizedColorChooser();
					if (c!=null) {
						negativeBalanceReport.setForeground(c);
					}
				}
			});
		}
		return jButton1;
	}

	/**
	 * This method initializes setTodefault	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSetTodefault() {
		if (setTodefault == null) {
			setTodefault = new JButton();
			setTodefault.setText(LocalizationData.get("MainFrame.Transactions.Preferences.setDefault")); //$NON-NLS-1$
			setTodefault.setToolTipText(LocalizationData.get("MainFrame.Transactions.Preferences.setDefault.tooltip")); //$NON-NLS-1$
			setTodefault.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					negativeBalanceReport.setForeground(DEFAULT_NEGATIVE);
					positiveBalanceReport.setForeground(DEFAULT_POSITIVE);
				}
			});
		}
		return setTodefault;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			gridBagConstraints7.weightx = 1.0D;
			gridBagConstraints7.weighty = 1.0D;
			gridBagConstraints7.gridy = 0;
			jLabel = new JLabel();
			jLabel.setText(""); //$NON-NLS-1$
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.anchor = GridBagConstraints.NORTH;
			gridBagConstraints5.fill = GridBagConstraints.NONE;
			gridBagConstraints5.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints5.gridy = 0;
			jPanel1 = new JPanel();
			jPanel1.setLayout(new GridBagLayout());
			jPanel1.add(getSetTodefault(), gridBagConstraints5);
			jPanel1.add(jLabel, gridBagConstraints7);
		}
		return jPanel1;
	}

	private Color localizedColorChooser() {
		//FIXME The JColorChooser locale is wrong, it's always the system default locale
		//This is a JRE known bug fixed in java 7 (http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6524757)
		//TODO test with JRE 7
		Locale old = Locale.getDefault();
		Locale.setDefault(LocalizationData.getLocale());
		//TODO probably better to have a customized panel with a BalanceReport field
		Color c = JColorChooser.showDialog(jButton, LocalizationData.get("MainFrame.Transactions.Preferences.ChooseColorDialog.title"), BalanceReportField.POSITIVE_COLOR); //$NON-NLS-1$
		Locale.setDefault(old);
		return c;
	}
}  //  @jve:decl-index=0:visual-constraint="64,14"