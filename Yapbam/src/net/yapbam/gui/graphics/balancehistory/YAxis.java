package net.yapbam.gui.graphics.balancehistory;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.yapbam.data.GlobalData;

class YAxis {
	public static final String SCALE_PROPERTY_NAME = "VERTICAL_SCALE"; //$NON-NLS-1$
	
	private Component owner;
	private double min;
	private double max;
	private int yOffset;
	private double yRatio;
	private List<Graduation> yGraduations;
	
	private int lastHeight;
	private int lastFontHeight;
	private double lastMin;
	private double lastMax;
	private PropertyChangeSupport pcSupport;
	private int scale;
	
	YAxis (Component owner) {
		this.owner = owner;
		this.scale = 1;
		this.setBounds(0, 0);
		pcSupport = new PropertyChangeSupport(this);
	}

	void setBounds(double min, double max) {
		if (GlobalData.AMOUNT_COMPARATOR.compare(min, max) == 0) {
			min--;
			max++;
		}
		if (GlobalData.AMOUNT_COMPARATOR.compare(min, lastMin) != 0 || GlobalData.AMOUNT_COMPARATOR.compare(max, lastMax) != 0) {
			this.min = min;
			this.max = max;
			// Force the first computing
			this.lastHeight = -1;
		}
	}
	
	public void setVerticalScale(int scale) {
		if (scale!=this.scale) {
			double old = this.scale;
			this.scale = scale;
			// Force yRatio computing
			this.lastHeight = -1;
			this.pcSupport.firePropertyChange(SCALE_PROPERTY_NAME, old, this.scale);
		}
	}
	
	public int getVerticalScale() {
		return this.scale;
	}

	private void update() {
		int parentHeight = this.owner.getSize().height;
		int fontHeight = this.owner.getFontMetrics(this.owner.getFont()).getHeight();
		
		if ((parentHeight!=lastHeight) || (fontHeight!=lastFontHeight) ||
				(GlobalData.AMOUNT_COMPARATOR.compare(min, lastMin) != 0) ||
				(GlobalData.AMOUNT_COMPARATOR.compare(max, lastMax) != 0)) {
			this.lastMin = min;
			this.lastMax = max;
			this.lastFontHeight = fontHeight;
			this.lastHeight = parentHeight;
			this.yOffset = fontHeight+2;
			double yVariation = max - min;
			if (yVariation==0) {
				yVariation = 1;
			}
			this.yRatio = (double)(parentHeight-2*yOffset)/yVariation;
			computeYGraduations();
		}
	}
	
	private void computeYGraduations() {
		FontMetrics fontMetrics = this.owner.getFontMetrics(this.owner.getFont());
		Dimension size = this.owner.getSize();
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

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcSupport.addPropertyChangeListener(propertyName, listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcSupport.removePropertyChangeListener(listener);
    }
}
