package com.billooms.drawables;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * A Drawable rectangle defined by inch(cm) position.
 * This is used primarily for an outline for ECF.
 * The origin is the center of one edge (center of width).
 * @author Bill Ooms. Copyright 2011 Studio of Bill Ooms. All rights reserved.
 */
public class Rect1 extends Shape {
	private Point2D.Double pos;		// position in inches
	private double width, depth;
	private double angle = 0.0;		// degrees, zero is when cutter is aligned along z-axis, + is toward front

	/**
	 * A drawable circle defined by inch position
	 * @param pos circle position in inches
	 * @param w width in inches
	 * @param d depth in inches
	 * @param ang Angle in degrees
	 * @param c Color
	 * @param s BasicStroke
	 */
	public Rect1(Point2D.Double pos, double w, double d, double ang, Color c, BasicStroke s) {
		super(c, s);
		this.pos = pos;
		this.width = w;
		this.depth = d;
		this.angle = ang;
	}

	/**
	 * Get the circle position in inches
	 * @return circle position in inches
	 */
	public Point2D.Double getPos() {
		return pos;
	}

	/**
	 * Move the circle to a new point defined in inches
	 * @param p new point in inches
	 */
	public void moveTo(Point2D.Double p) {
		pos = p;
	}

	/**
	 * Get the width of the rectangle.
	 * @return width
	 */
	public double getWidth() {
		return width;
	}
	
	/**
	 * Set the width of the rectangle.
	 * @param w width
	 */
	public void setWidth(double w) {
		this.width = w;
	}

	/**
	 * Get the depth of the rectangle.
	 * @return depth
	 */
	public double getDepth() {
		return depth;
	}
	
	/**
	 * Set the depth of the rectangle.
	 * @param d depth
	 */
	public void setDepth(double d) {
		this.depth = d;
	}
	
	/**
	 * Get the rotation angle of the rectangle in degrees.
	 * Zero is when cutter is aligned along z-axis, + is toward front.
	 * @return rotation angle
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * Set the rotation angle of the rectangle in degrees.
	 * Zero is when cutter is aligned along z-axis, + is toward front.
	 * @param a rotation angle
	 */
	public void setAngle(double a) {
		this.angle = a;
	}
	
    /**
     * Paint the object
     * @param g2d Graphics2D
     */
	@Override
	public void paint(Graphics2D g2d) {
		if (visible) {
			AffineTransform saveXform = g2d.getTransform();	// save for later restoration
			float scale = (float)saveXform.getScaleX();

			AffineTransform at = new AffineTransform();
			at.translate(pos.x, pos.y);			// this should give rotate first, then translate
			at.rotate(Math.toRadians(-angle));
			g2d.transform(at);

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
			g2d.draw(new Rectangle2D.Double(-width/2.0, -depth, width, depth));

			g2d.setTransform(saveXform);					// Restore transform
		}
	}
}
