package net.yapbam.gui.transactiontable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import com.fathzer.jlocal.Formatter;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;

public class BalanceReportField extends JToggleButton {
	private static final long serialVersionUID = 1L;
	static Color POSITIVE_COLOR;
	static Color NEGATIVE_COLOR;
	
	static {
		try {
			POSITIVE_COLOR = new Color(Integer.parseInt(Preferences.INSTANCE.getProperty(TransactionsPreferencePanel.POSITIVE_KEY)));
			NEGATIVE_COLOR = new Color(Integer.parseInt(Preferences.INSTANCE.getProperty(TransactionsPreferencePanel.NEGATIVE_KEY)));
		} catch (Exception e) {
			POSITIVE_COLOR = TransactionsPreferencePanel.DEFAULT_POSITIVE;
			NEGATIVE_COLOR = TransactionsPreferencePanel.DEFAULT_NEGATIVE;
		}
	}
	
	private String contentPattern;

	public BalanceReportField(String contentPattern) {
		super();
		this.contentPattern = contentPattern;
		this.setOpaque(true);
		this.setFont(this.getFont().deriveFont(Font.BOLD));
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setFocusable(false);
		this.setValue(0, true);
		Dimension size = this.getPreferredSize();
		size.height += size.height/2;
		this.setPreferredSize(size);
	}
	
	public void setValue(double balance, boolean absolute) {
		this.setFont(this.getFont().deriveFont(absolute?Font.BOLD:Font.ITALIC+Font.BOLD));
		this.setForeground(balance<0?NEGATIVE_COLOR:POSITIVE_COLOR);
		String text = Formatter.format(this.contentPattern, LocalizationData.getCurrencyInstance().format(balance));
		setText(text);
	}
}
