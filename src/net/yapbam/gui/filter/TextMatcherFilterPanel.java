package net.yapbam.gui.filter;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.widget.TextWidget;

import net.yapbam.gui.HelpManager;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.widget.AutoSelectFocusListener;
import net.yapbam.util.TextMatcher;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class TextMatcherFilterPanel extends ConsistencyCheckedPanel {
	private static final long serialVersionUID = 1L;

	public static final Wordings DESCRIPTION_WORDING = new Wordings(LocalizationData.get("Transaction.description"), //$NON-NLS-1$
			LocalizationData.get("CustomFilterPanel.description.equals.toolTip"), //$NON-NLS-1$
			LocalizationData.get("CustomFilterPanel.description.contains.toolTip"), //$NON-NLS-1$
			LocalizationData.get("CustomFilterPanel.description.regularExpression.toolTip"), //$NON-NLS-1$
			LocalizationData.get("CustomFilterPanel.description.toolTip")); //$NON-NLS-1$

	public static final Wordings COMMENT_WORDING = new Wordings(LocalizationData.get("Transaction.comment"), //$NON-NLS-1$
			LocalizationData.get("CustomFilterPanel.comment.equals.toolTip"),  //$NON-NLS-1$
			LocalizationData.get("CustomFilterPanel.comment.contains.toolTip"), //$NON-NLS-1$
			LocalizationData.get("CustomFilterPanel.comment.regularExpression.toolTip"), //$NON-NLS-1$
			LocalizationData.get("CustomFilterPanel.comment.toolTip")); //$NON-NLS-1$

	public static final Wordings NUMBER_WORDING = new Wordings(LocalizationData.get("Transaction.number"), //$NON-NLS-1$
			LocalizationData.get("CustomFilterPanel.number.equals.toolTip"), //$NON-NLS-1$
			LocalizationData.get("CustomFilterPanel.number.contains.toolTip"), //$NON-NLS-1$
			LocalizationData.get("CustomFilterPanel.number.regularExpression.toolTip"), //$NON-NLS-1$
			LocalizationData.get("CustomFilterPanel.number.toolTip")); //$NON-NLS-1$
	
	public static final Wordings STATEMENT_WORDING = new Wordings(LocalizationData.get("Transaction.statement"), //$NON-NLS-1$
			LocalizationData.get("CustomFilterPanel.statement.equals.toolTip"), //$NON-NLS-1$
			LocalizationData.get("CustomFilterPanel.statement.contains.toolTip"), //$NON-NLS-1$
			LocalizationData.get("CustomFilterPanel.statement.regularExpression.toolTip"), //$NON-NLS-1$
			LocalizationData.get("CustomFilterPanel.statement.toolTip")); //$NON-NLS-1$
	
	public static class Wordings {
		private String title;
		private String equalsToolTip;
		private String containsToolTip;
		private String regExprToolTip;
		private String descriptionToolTip;
		
		public Wordings(String title, String equalsToolTip, String containsToolTip, String regExprToolTip, String descriptionToolTip) {
			super();
			this.title = title;
			this.equalsToolTip = equalsToolTip;
			this.containsToolTip = containsToolTip;
			this.regExprToolTip = regExprToolTip;
			this.descriptionToolTip = descriptionToolTip;
		}
	}

	private Wordings wordings;
	private JPanel jPanel1;
	private JRadioButton descriptionEqualsTo;
	private JRadioButton descriptionContains;
	private JRadioButton descriptionRegular;
	private JPanel jPanel2;
	private JCheckBox ignoreDiacritics;
	private JCheckBox ignoreCase;
	private JTextField description;
	private JPanel panel;
	
	public TextMatcherFilterPanel(Wordings wordings) {
		this.wordings = wordings;
		setBorder(BorderFactory.createTitledBorder(null, wordings.title, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION)); //$NON-NLS-1$
		setLayout(new GridBagLayout());
		GridBagConstraints gbcPanel = new GridBagConstraints();
		gbcPanel.weighty = 1.0;
		gbcPanel.weightx = 1.0;
		gbcPanel.fill = GridBagConstraints.BOTH;
		gbcPanel.gridx = 0;
		gbcPanel.gridy = 0;
		add(getPanel(), gbcPanel);
	}
	
	public TextMatcher getTextMatcher() {
		String text = getDescription().getText().trim();
		if (text.length()==0) {
			return null;
		} else {
			TextMatcher.Kind kind = null;
			if (getDescriptionEqualsTo().isSelected()) {
				kind = TextMatcher.Kind.EQUALS;
			} else if (getDescriptionContains().isSelected()) {
				kind = TextMatcher.Kind.CONTAINS;
			} else if (getDescriptionRegular().isSelected()) {
				kind = TextMatcher.Kind.REGULAR;
			}
			return new TextMatcher(kind, text, !getIgnoreCase().isSelected(), !getIgnoreDiacritics().isSelected());
		}
	}

	public void clear() {
		getDescriptionContains().setSelected(true);
		getDescription().setText(""); //$NON-NLS-1$
	}

	public void setTextMatcher(TextMatcher tMatcher) {
		if ((tMatcher==null) || (tMatcher.getKind()==TextMatcher.Kind.CONTAINS)) {
			getDescriptionContains().setSelected(true);
		} else if (tMatcher.getKind()==TextMatcher.Kind.EQUALS) {
			getDescriptionEqualsTo().setSelected(true);
		} else {
			getDescriptionRegular().setSelected(true);
		}
		ignoreDiacritics.setSelected((tMatcher==null)||!tMatcher.isDiacriticalSensitive());
		ignoreCase.setSelected((tMatcher==null)||!tMatcher.isCaseSensitive());
		description.setText(tMatcher==null?"":tMatcher.getFilter()); //$NON-NLS-1$
	}
	
	public void setCheckBoxesVisible (boolean visible) {
		this.getIgnoreCase().setVisible(visible);
		this.ignoreDiacritics.setVisible(visible);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		getDescriptionContains().setEnabled(enabled);
		getDescriptionEqualsTo().setEnabled(enabled);
		getDescriptionRegular().setEnabled(enabled);
		getDescription().setEnabled(enabled);		
	}

	private final static class RegexprListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			HelpManager.show(e.getComponent(), HelpManager.REGULAR_EXPRESSIONS);
		}
	}
	
	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			GridBagConstraints gridBagConstraints36 = new GridBagConstraints();
			gridBagConstraints36.gridx = 0;
			gridBagConstraints36.gridy = 2;
			GridBagConstraints gridBagConstraints35 = new GridBagConstraints();
			gridBagConstraints35.gridx = 0;
			gridBagConstraints35.anchor = GridBagConstraints.WEST;
			gridBagConstraints35.gridheight = 1;
			gridBagConstraints35.gridwidth = 1;
			gridBagConstraints35.weightx = 0.0D;
			gridBagConstraints35.gridy = 1;
			GridBagConstraints gridBagConstraints34 = new GridBagConstraints();
			gridBagConstraints34.gridx = 0;
			gridBagConstraints34.anchor = GridBagConstraints.WEST;
			gridBagConstraints34.gridy = 0;
			GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
			gridBagConstraints32.anchor = GridBagConstraints.WEST;
			gridBagConstraints32.gridy = 2;
			gridBagConstraints32.weightx = 1.0D;
			gridBagConstraints32.gridx = 1;
			jPanel1 = new JPanel();
			jPanel1.setLayout(new GridBagLayout());
			JLabel regexpHelp = new JLabel();
			regexpHelp.setToolTipText(LocalizationData.get("CustomFilterPanel.regexprHelp.toolTip")); //$NON-NLS-1$
			regexpHelp.setIcon(IconManager.get(Name.HELP));
			regexpHelp.addMouseListener(new RegexprListener());
			jPanel1.add(regexpHelp, gridBagConstraints32);
			jPanel1.add(getDescriptionEqualsTo(), gridBagConstraints34);
			jPanel1.add(getDescriptionContains(), gridBagConstraints35);
			jPanel1.add(getDescriptionRegular(), gridBagConstraints36);
			ButtonGroup group = new ButtonGroup();
			group.add(getDescriptionEqualsTo());
			group.add(getDescriptionContains());
			group.add(getDescriptionRegular());
		}
		return jPanel1;
	}

	/**
	 * This method initializes descriptionEqualsTo	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getDescriptionEqualsTo() {
		if (descriptionEqualsTo == null) {
			descriptionEqualsTo = new JRadioButton();
			descriptionEqualsTo.setText(LocalizationData.get("CustomFilterPanel.description.equals")); //$NON-NLS-1$
			descriptionEqualsTo.setToolTipText(wordings.equalsToolTip);
		}
		return descriptionEqualsTo;
	}

	/**
	 * This method initializes descriptionContains	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getDescriptionContains() {
		if (descriptionContains == null) {
			descriptionContains = new JRadioButton();
			descriptionContains.setSelected(true);
			descriptionContains.setText(LocalizationData.get("CustomFilterPanel.description.contains")); //$NON-NLS-1$
			descriptionContains.setToolTipText(wordings.containsToolTip);
		}
		return descriptionContains;
	}

	/**
	 * This method initializes descriptionRegular	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getDescriptionRegular() {
		if (descriptionRegular == null) {
			descriptionRegular = new JRadioButton();
			descriptionRegular.setText(LocalizationData.get("CustomFilterPanel.description.regularExpression")); //$NON-NLS-1$
			descriptionRegular.setToolTipText(wordings.regExprToolTip);
			descriptionRegular.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					checkConsistency();
				}
			});
		}
		return descriptionRegular;
	}

	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.anchor = GridBagConstraints.NORTH;
			gridBagConstraints18.gridwidth = 0;
			gridBagConstraints18.gridx = 0;
			gridBagConstraints18.gridy = 1;
			gridBagConstraints18.weightx = 1.0;
			gridBagConstraints18.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.anchor = GridBagConstraints.WEST;
			gridBagConstraints17.gridy = 0;
			gridBagConstraints17.gridx = 1;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 0;
			gridBagConstraints16.anchor = GridBagConstraints.WEST;
			gridBagConstraints16.gridy = -1;
			jPanel2 = new JPanel();
			jPanel2.setLayout(new GridBagLayout());
			jPanel2.add(getIgnoreCase(), gridBagConstraints16);
			jPanel2.add(getIgnoreDiacritics(), gridBagConstraints17);
			jPanel2.add(getDescription(), gridBagConstraints18);
		}
		return jPanel2;
	}
	
	/**
	 * This method initializes ignoreCase	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getIgnoreCase() {
		if (ignoreCase == null) {
			ignoreCase = new JCheckBox();
			ignoreCase.setText(LocalizationData.get("CustomFilterPanel.description.ignoreCase")); //$NON-NLS-1$
			ignoreCase.setToolTipText(LocalizationData.get("CustomFilterPanel.description.ignoreCase.toolTip")); //$NON-NLS-1$
		}
		return ignoreCase;
	}

	/**
	 * This method initializes description	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getDescription() {
		if (description == null) {
			description = new TextWidget();
			description.setToolTipText(wordings.descriptionToolTip);
			description.addFocusListener(AutoSelectFocusListener.INSTANCE);
			description.addPropertyChangeListener(TextWidget.TEXT_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					checkConsistency();
				}
			});
		}
		return description;
	}

	/**
	 * This method initializes ignoreDiacritics	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getIgnoreDiacritics() {
		if (ignoreDiacritics == null) {
			ignoreDiacritics = new JCheckBox();
			ignoreDiacritics.setText(LocalizationData.get("CustomFilterPanel.description.ignoreDiacriticals")); //$NON-NLS-1$
			ignoreDiacritics.setToolTipText(LocalizationData.get("CustomFilterPanel.description.ignoreDiacriticals.toolTip")); //$NON-NLS-1$
		}
		return ignoreDiacritics;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			GridBagLayout gblPanel = new GridBagLayout();
			panel.setLayout(gblPanel);
			GridBagConstraints gbcJPanel1 = new GridBagConstraints();
			gbcJPanel1.anchor = GridBagConstraints.NORTHWEST;
			gbcJPanel1.insets = new Insets(0, 0, 0, 5);
			gbcJPanel1.gridx = 0;
			gbcJPanel1.gridy = 0;
			panel.add(getJPanel1(), gbcJPanel1);
			GridBagConstraints gbcJPanel2 = new GridBagConstraints();
			gbcJPanel2.weightx = 1.0;
			gbcJPanel2.fill = GridBagConstraints.HORIZONTAL;
			gbcJPanel2.gridx = 1;
			gbcJPanel2.gridy = 0;
			panel.add(getJPanel2(), gbcJPanel2);
		}
		return panel;
	}

	@Override
	protected String computeInconsistencyCause() {
		if (!getDescriptionRegular().isSelected()) {
			return null;
		}
		String text = getDescription().getText();
		try {
			Pattern.compile(text);
			return null;
		} catch (PatternSyntaxException e) {
			return Formatter.format(LocalizationData.get("TextMatcherFilterPanel.invalidRegularExpression"), text); //$NON-NLS-1$
		}
	}
}
