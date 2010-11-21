package net.yapbam.gui.transactiontable;

import java.awt.Color;
import java.awt.Font;
import java.text.MessageFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;

public class BalanceReportField extends JLabel {
	private static final long serialVersionUID = 1L;
	static Color POSITIVE_COLOR;
	static Color NEGATIVE_COLOR;
	
	static {
		try {
			POSITIVE_COLOR = new Color(Integer.parseInt(Preferences.INSTANCE.getProperty(TransactionsPreferencePanel.POSITIVE_KEY)));
			NEGATIVE_COLOR = new Color(Integer.parseInt(Preferences.INSTANCE.getProperty(TransactionsPreferencePanel.NEGATIVE_KEY)));
		} catch (Throwable e) {
			POSITIVE_COLOR = TransactionsPreferencePanel.DEFAULT_POSITIVE;
			NEGATIVE_COLOR = TransactionsPreferencePanel.DEFAULT_NEGATIVE;
		}
	}
	
	private String contentPattern;

	public BalanceReportField(String contentPattern) {
		super();
		this.contentPattern = contentPattern;
		this.setOpaque(true);
    this.setFont(new Font(getFont().getFontName(), getFont().getStyle() ^ Font.BOLD, 12));
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		this.setValue(0);
	}
	
	public void setValue(double balance) {
		this.setForeground(balance<0?NEGATIVE_COLOR:POSITIVE_COLOR);
		setText(MessageFormat.format(this.contentPattern, LocalizationData.getCurrencyInstance().format(balance)));
	}
}
