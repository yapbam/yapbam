package net.yapbam.ihm;

import java.awt.Color;
import java.text.MessageFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class BalanceReportField extends JLabel {
	private static final long serialVersionUID = 1L;
	private String contentPattern;

	public BalanceReportField(String contentPattern) {
		super();
		this.contentPattern = contentPattern;
        this.setOpaque(true);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	}
	
	public void setValue(double balance) {
		this.setForeground(balance<0?Color.RED:Color.GREEN);
		setText(MessageFormat.format(this.contentPattern,
				LocalizationData.getCurrencyInstance().format(balance)));
	}
}


