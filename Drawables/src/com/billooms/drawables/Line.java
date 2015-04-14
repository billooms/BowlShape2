package com.billooms.drawables;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * A Drawable straight line.
 * @author Bill Ooms. Copyright 2011 Studio of Bill Ooms. All rights reserved.
 */
public class Line extends Shape {

	private Point2D.Double pos;	// position in inches
	private double w, h;		// in inches

	/**
	 * A drawable line specified in inches
	 * @param pos Point2D.Double defining the beginning of the line
	 * @param w width of the line in inches
	 * @param h height of the line in inches
	 * @param c Color
	 */
	public Line(Point2D.Double pos, double w, double h, Color c) {
		super(c);
		this.pos = pos;
		this.w = w;
		this.h = h;
	}

	/**
	 * A drawable line specified in inches
	 * @param pos Point2D.Double defining the beginning of the line
	 * @param w width of the line in inches
	 * @param h height of the line in inches
	 * @param c Color
	 * @param s BasicStroke
	 */
	public Line(Point2D.Double pos, double w, double h, Color c, BasicStroke s) {
		super(c, s);
		this.pos = pos;
		this.w = w;
		this.h = h;
	}

	/**
	 * Get the line beginning position in inches
	 * @return line beginning position in inches
	 */
	public Point2D.Double getPos() {
		return pos;
	}

	/**
	 * Move the line to a new point defined in inches
	 * @param p new point in inches
	 */
	public void moveTo(Point2D.Double p) {
		pos = p;
	}

	/**
	 * Get the width (x-direction) in inches
	 * @return width (x-direction) in inches
	 */
	public double getW() {
		return w;
	}
	
	/**
	 * Set the width (x-direction) in inches
	 * @param w new width (x-direction) in inches
	 */
	public void setW(double w) {
		this.w = w;
	}

	/**
	 * Get the height (y-direction) in inches
	 * @return height (y-direction) in inches
	 */
	public double getH() {
		return h;
	}
	
	/**
	 * Set the height (y-direction) in inches
	 * @param h new height (y-direction) in inches
	 */
	public void setH(double h) {
		this.h = h;
	}

    /**
     * Paint the object
     * @param g2d Graphics2D
     */
	@Override
	public void paint(Graphics2D g2d) {
		if (visible) {
			float scale = (float) g2d.getTransform().getScaleX();
			float array[] = stroke.getDashArray();
			if ((array == null) || (array.length == 0)) {
				g2d.setStroke(new BasicStroke(1.0f/scale));
			} else {
				for (int i = 0; i < array.length; i++) {
					array[i] = array[i]/scale;
				}
				g2d.setStroke(new BasicStroke(1.0f/scale, stroke.getEndCap(), stroke.getLineJoin(), 1.0f, array, stroke.getDashPhase()/scale));
			}
			g2d.setColor(color);
			g2d.draw(new Line2D.Double(pos.x, pos.y, pos.x + w, pos.y + h));
		}
	}
}
