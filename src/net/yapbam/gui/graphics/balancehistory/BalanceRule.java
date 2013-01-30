package net.yapbam.gui.graphics.balancehistory;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Iterator;

import javax.swing.JComponent;

import net.yapbam.data.BalanceHistory;
import net.yapbam.gui.LocalizationData;

class BalanceRule extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private BalanceHistory balanceHistory;
	private YAxis yAxis;
	
	BalanceRule (BalanceHistory history) {
		this.balanceHistory = history;
		this.yAxis = new YAxis(this, history);
	}
	
	void setBalanceHistory(BalanceHistory balanceHistory) {
		this.balanceHistory = balanceHistory;
		this.yAxis = new  YAxis(this, balanceHistory);
	}

	@Override
	public Dimension getPreferredSize() {
		FontMetrics fontMetrics = this.getFontMetrics(this.getFont());
		String textMin = LocalizationData.getCurrencyInstance().format(balanceHistory.getMinBalance());
		String textMax = LocalizationData.getCurrencyInstance().format(balanceHistory.getMaxBalance());
		int width = Math.max(fontMetrics.stringWidth(textMin), fontMetrics.stringWidth(textMax));
		return new Dimension(width+15, 1);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.WHITE);
		Stroke oldStroke = g2.getStroke();

		Dimension size = getSize();
		g2.fillRect(0, 0, size.width, size.height - 1);
		g2.setColor(Color.BLACK);
		g2.drawLine(0, size.height - 1, size.width, size.height - 1);

		g2.setStroke(new BasicStroke(3));
		int y0 = this.yAxis.getY(0);
		g2.drawLine(0, y0, size.width, y0); // x axis

		g2.drawLine(size.width - 1, 0, size.width - 1, size.height); // y axis
		g2.setStroke(oldStroke);

		FontMetrics fontMetrics = this.getFontMetrics(this.getFont());
		for (Iterator<Graduation> iterator = this.yAxis.getYGraduations(); iterator.hasNext();) {
			Graduation graduation = iterator.next();
			g2.drawLine(size.width - 5, graduation.getPosition(), size.width, graduation.getPosition());
			String text = LocalizationData.getCurrencyInstance().format(graduation.getValue());
			int textWidth = fontMetrics.stringWidth(text);
			g2.drawString(text, size.width - 10 - textWidth, graduation.getPosition() + fontMetrics.getAscent() / 2);
		}
	}

	YAxis getYAxis() {
		return yAxis;
	}
}
