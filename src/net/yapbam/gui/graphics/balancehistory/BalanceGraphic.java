package net.yapbam.gui.graphics.balancehistory;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.*;
import java.text.DateFormatSymbols;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.Scrollable;

import net.yapbam.data.BalanceHistory;
import net.yapbam.data.BalanceHistoryElement;
import net.yapbam.gui.LocalizationData;

class BalanceGraphic extends JPanel implements Scrollable {
	static final String SELECTED_DATE_PROPERTY = "selectedDate";
	private static final int PIXEL_PER_DAY = 3;
	private static final long serialVersionUID = 1L;
	private static final int X_OFFSET = 0;
	private static final Color POSITIVE = new Color (127,255,127);
	private static final Color NEGATIVE = new Color (255,127,127);
	
	private BalanceHistory balanceHistory;
	private boolean needUpdate;
	private Date startDate;
	private Date endDate;
	private YAxis yAxis;

	private Date selectedDate;

	private Point[] points;
	private boolean gridIsVisible;
	
	BalanceGraphic(BalanceHistory history, YAxis yAxis) {
		super();
		this.selectedDate=new Date();
		this.balanceHistory = history;
		this.needUpdate = true;
		this.points = null;
		this.yAxis = yAxis;
		MouseListener listener = new MouseListener();
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
		this.setAutoscrolls(true);
	}
	
	private void restoreHistory() {
		if (needUpdate) {
			this.points = null;
			this.startDate = balanceHistory.get(0).getFrom();
			if (this.startDate==null) {
				this.startDate = new Date();
				Date end = balanceHistory.get(0).getTo();
				if (end!=null) {
					this.startDate.setTime(end.getTime()-3*24*3600000); // three days before end
				} // else start is today
			}
			BalanceHistoryElement lastElement = balanceHistory.get(balanceHistory.size()-1);
			this.endDate = lastElement.getTo();
			if (this.endDate==null) {
				this.endDate = (Date) (lastElement.getFrom()!=null?lastElement.getFrom():this.startDate).clone();
				this.endDate.setTime(this.endDate.getTime()+3*24*3600000); // three days after last date
			}
			needUpdate = false;
		}
	}
	
	private Date getStartDate() {
		restoreHistory();
		return startDate;
	}

	private Date getEndDate() {
		restoreHistory();
		return endDate;
	}
	
	Date getSelectedDate() {
		return selectedDate;
	}

	public boolean isGridVisible() {
		return gridIsVisible;
	}

	public void setGridVisible(boolean gridIsVisible) {
		if (gridIsVisible!=this.gridIsVisible) {
			this.gridIsVisible = gridIsVisible;
			this.repaint();
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		setBackground(Color.white);
    	Graphics2D g2 = (Graphics2D) g;
        //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		this.points = convert();

		Dimension size = getSize();
		g2.drawLine(0, size.height-1, size.width, size.height-1);

		// Draw the balance curve color background
		int y0 = yAxis.getY(0);
		GradientPaint negativeGradient = new GradientPaint(0,y0,Color.WHITE,0,yAxis.getY(this.balanceHistory.getMinBalance()),NEGATIVE);
		GradientPaint positiveGradient = new GradientPaint(0,yAxis.getY(this.balanceHistory.getMaxBalance()),POSITIVE,0,y0,Color.WHITE);
		Point p = this.points[0];
        for (int i = 0; i < points.length; i++) {
        	Point p2 = points[i];
        	if ((p2.y == p.y) && (Math.abs(p.y-y0)>1)) {
        		int y, h;
        		Paint c;
        		if (p.y>y0) { // balance is negative
        			y = y0;
        			h =  p.y - y0;
        			c = negativeGradient;
        		} else {
        			y = p.y;
        			h =  y0 - p.y;
        			c = positiveGradient;
        		}
                Paint oldColor = g2.getPaint();
                g2.setPaint(c);
                g2.fillRect(p.x+1, y, p2.x-p.x, h);
                g2.setPaint(oldColor);
        	}
        	p = p2;
		}
        
		paintXAxis(g2);
		paintYAxis(g2);

        // Draw the balance curve
		p = this.points[0];
        for (int i = 0; i < points.length; i++) {
        	Point p2 = points[i];
        	g2.drawLine(p.x, p.y, p2.x, p2.y);
        	p = p2;
		}

        // Draw the selected date line
		Stroke oldStroke = g2.getStroke();
		int stroke = 4;
        g2.setStroke(new BasicStroke(stroke));
        Color oldColor = g2.getColor();
    	g2.setColor(Color.ORANGE);
    	int x = getX(this.selectedDate)+stroke/2;
		g2.drawLine(x, 0, x, size.height); //Selected Date line
    	g2.setColor(oldColor);
    	g2.setStroke(oldStroke);
	}

	@SuppressWarnings("deprecation")
	private void paintXAxis(Graphics2D g2) {
		int x;
		int y0 = yAxis.getY(0);
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(3));
		Dimension size = getSize();
		g2.drawLine(0, y0, size.width, y0); // x axis
    	g2.setStroke(oldStroke);
		String[] months = DateFormatSymbols.getInstance(LocalizationData.getLocale()).getMonths();
		Date date = (Date) this.getStartDate().clone();
		x = getX(date);
		date.setDate(1);
		FontMetrics metrics = g2.getFontMetrics();
		while (x<size.width) {
			int month = date.getMonth();;
			if (x>0) {
				if (this.isGridVisible()) {
					Color color = g2.getColor();
					g2.setColor(Color.LIGHT_GRAY);
					g2.drawLine(x, 0, x, size.height); // y axis
					g2.setColor(color);
				}
				g2.drawLine(x, y0-3, x, y0+3); // y axis
				StringBuffer buf = new StringBuffer();
				buf.append(months[month]);
				if (month==1) buf.append(" ").append(date.getYear()+1900);
				String dateString = buf.toString();
				g2.drawString(dateString, x - metrics.stringWidth(dateString)/2, y0+metrics.getHeight());
			}
			month++;
			if (month>12) {
				month = 1;
				date.setYear(date.getYear()+1);
			}
			date.setMonth(month);
			x = getX(date);
		}
	}

	private void paintYAxis(Graphics2D g2) {
		if (this.isGridVisible()) {
			Dimension size = getSize();
			Color color = g2.getColor();
			g2.setColor(Color.LIGHT_GRAY);
			for (Iterator<Graduation> iterator = this.yAxis.getYGraduations(); iterator.hasNext();) {
				Graduation graduation = iterator.next();
				g2.drawLine(0, graduation.getPosition(), size.width, graduation.getPosition());
			}
			g2.setColor(color);
		}
	}

	private void setLinePosition(double x) {
		int day = ((int)x-X_OFFSET)/PIXEL_PER_DAY;
		// The following line introduce a bug when there's a summer/winter time change, because theses days doesn't
		// have 24 hours. So, we will use a gregorianCalendar and add the amount of days to it.
		// long time = this.getStartDate().getTime()+3600000*24*day;
		GregorianCalendar dummy = new GregorianCalendar();
		dummy.setTime(this.getStartDate());
		dummy.add(GregorianCalendar.DATE, day);
		long time = dummy.getTimeInMillis();
		long max = Math.max(xToTime(this.getSize().width-1), this.getEndDate().getTime());
		if (time>max) {
			time = max;
		} else if (this.getStartDate().getTime()>time) {
			time = this.getStartDate().getTime();
		}
		if (time!=this.selectedDate.getTime()) {
			Date oldValue = (Date) this.selectedDate.clone();
			this.selectedDate.setTime(time);
			this.firePropertyChange(SELECTED_DATE_PROPERTY, oldValue, this.selectedDate);
	        this.repaint();
		}
	}

	private int getX(Date date) {
		return X_OFFSET + (int) ((date.getTime()-getStartDate().getTime())/(3600000*24)*PIXEL_PER_DAY);
	}
	
	private long xToTime(int x) {
		return ((long)(x-X_OFFSET))*3600000*24/PIXEL_PER_DAY + getStartDate().getTime();
	}

	private Point[] convert() {
		Point[] result = new Point[balanceHistory.size()*2];
		for (int i = 0; i < balanceHistory.size(); i++) {
			BalanceHistoryElement element = balanceHistory.get(i);
			Date from = element.getFrom();
			if (from==null) from = getStartDate();
			int y = yAxis.getY(element.getBalance());
			long x = getX(from);
			result[i*2] = new Point((int)x, y);
			Date to = element.getTo();
			if (to==null) {
				long rightWindowTime = xToTime(this.getSize().width-1);
				to = new Date(from.getTime()+((3600000*24)*3)); // Three days after last known date
				if (rightWindowTime>to.getTime()) to.setTime(rightWindowTime);
			}
			x = getX(to);
			result[i*2+1] = new Point((int)x,(int)y);
		}
		return result;
	}

	private class MouseListener extends MouseAdapter {
	    @Override
		public void mouseDragged(MouseEvent e) {
            int x = e.getX();
			Rectangle r = new Rectangle(x, e.getY(), 1, 1);
            ((JPanel)e.getSource()).scrollRectToVisible(r);
			computeEvent(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			computeEvent(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			computeEvent(e);
		}

		private void computeEvent(MouseEvent e) {
	    	BalanceGraphic bgp = (BalanceGraphic) e.getSource();
	    	bgp.setLinePosition(e.getX());
	    }
	}

	public Dimension getPreferredSize() {
    	Dimension parentSize = this.getParent().getSize();
    	long days = 1+(this.getEndDate().getTime() - this.getStartDate().getTime())/24/3600000;
    	int pixels = (int) (days * PIXEL_PER_DAY);
    	int width = Math.max(pixels, parentSize.width);
    	return new Dimension(width, parentSize.height);
    }

    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return this.getParent().getSize().width;
	}

	public boolean getScrollableTracksViewportHeight() {
		return true; // The component doesn't need a vertical scrollbar and ensure that it always displays in the height of the parent component
	}

	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return PIXEL_PER_DAY; // Increments by one day
	}
}
