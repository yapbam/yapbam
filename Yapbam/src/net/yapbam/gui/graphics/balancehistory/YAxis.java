package net.yapbam.gui.graphics.balancehistory;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.yapbam.data.BalanceHistory;
import net.yapbam.data.GlobalData;

class YAxis {
	private Component parent;
	private BalanceHistory balanceHistory;
	private int yOffset;
	private double yRatio;
	private List<Graduation> yGraduations;
	
	private int lastParentHeight;
	private int lastFontHeight;
	private double lastMin;
	private double lastMax;
	
	YAxis (Component parent, BalanceHistory history) {
		this.parent = parent;
		this.setBalanceHistory(history);
	}

	void setBalanceHistory(BalanceHistory balanceHistory) {
		this.balanceHistory = balanceHistory;
		this.lastParentHeight = -1; //To force the first computing
	}

	void update() {
		int parentHeight = this.parent.getSize().height;
		int fontHeight = this.parent.getFontMetrics(this.parent.getFont()).getHeight();
		double min = Math.min(0, this.balanceHistory.getMinBalance());
		double max = Math.max(0,this.balanceHistory.getMaxBalance());
		
		if (GlobalData.AMOUNT_COMPARATOR.compare(min, max) == 0) {
			min--;
			max++;
		}
		if ((parentHeight!=lastParentHeight) || (fontHeight!=lastFontHeight) ||
				(min!=lastMin) || (max!=lastMax)) {
			this.lastMin = min;
			this.lastMax = max;
			this.lastFontHeight = fontHeight;
			this.lastParentHeight = parentHeight;
			this.yOffset = fontHeight+2;
			double yVariation = max - min;
			if (yVariation==0) yVariation = 1;
			this.yRatio = (double)(parentHeight-2*yOffset)/yVariation;
			computeYGraduations();
		}
	}
	
	private void computeYGraduations() {
		FontMetrics fontMetrics = this.parent.getFontMetrics(this.parent.getFont());
		Dimension size = this.parent.getSize();
		int lineHeight = fontMetrics.getHeight();
		int h = lineHeight * 2;
		double step = RuleBuilder.getNormalizedStep(((double) h) / this.yRatio);

		yGraduations = new ArrayList<Graduation>();
		double current = 0;
		do {
			current += step;
			int y = this.getY(current);
			if (y > fontMetrics.getAscent() / 2 + 5) {
				yGraduations.add(new Graduation(y, current));
			} else {
				break;
			}
		} while (true);
		current = 0;
		do {
			current -= step;
			int y = this.getY(current);
			if (y < size.height - lineHeight) {
				yGraduations.add(new Graduation(y, current));
			} else {
				break;
			}
		} while (true);
	}
	
	int getY(double value) {
		update();
		return this.yOffset+ (int)((this.lastMax-value)*this.yRatio);
	}
	
	public Iterator<Graduation> getYGraduations() {
		update();
		return this.yGraduations.iterator();
	}
}
