package net.yapbam.gui.graphics.balancehistory;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.Scrollable;

import net.yapbam.gui.LocalizationData;

class BalanceRule extends JComponent implements Scrollable {
	private static final long serialVersionUID = 1L;
	
	private YAxis yAxis;
	
	BalanceRule () {
		this.yAxis = new YAxis(this);
		this.yAxis.addPropertyChangeListener(YAxis.SCALE_PROPERTY_NAME, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				revalidate();
				repaint();
			}
		});
	}
	
	@Override
	public Dimension getPreferredSize() {
		double min = this.yAxis.getMin();
		double max = this.yAxis.getMax();
		String textMin = LocalizationData.getCurrencyInstance().format(min);
		String textMax = LocalizationData.getCurrencyInstance().format(max);
		FontMetrics fontMetrics = this.getFontMetrics(this.getFont());
		int width = Math.max(fontMetrics.stringWidth(textMin), fontMetrics.stringWidth(textMax));
		return new Dimension(width+15, getParent().getSize().height*yAxis.getVerticalScale());
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

		// Paint the x axis
		g2.setStroke(new BasicStroke(3));
		int y0 = this.yAxis.getY(0);
		g2.drawLine(0, y0, size.width, y0);

		// Paint the y axis
		g2.drawLine(size.width - 1, 0, size.width - 1, size.height);
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


	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 1;
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return this.getParent().getSize().height;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return true;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
}
