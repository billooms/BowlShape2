package com.billooms.drawables;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

/**
 * A Drawable arc defined by inch(cm) position
 * @author Bill Ooms. Copyright 2011 Studio of Bill Ooms. All rights reserved.
 */
public class Arc extends Shape {
	private Point2D.Double pos;		// position in inches
    private double radius;
	private double rotate = 0.0;	// degrees, zero is flat (like HCF)
	private double angle = 0.0;		// degrees, zero is up/down (like cutter angle), + is CW rotation (like cutter angle)
	private double centerAngle;		// angle to the center of the arc (degrees)
	private double arcAngle;		// total swing of the arc (degrees)

	/**
	 * A drawable arc defined by inch position
	 * @param pos arc position in inches
	 * @param r radius in inches
	 * @param rot Rotation in degrees from flat
	 * @param ang Angle in degrees from vertical (+ is ClockWise)
	 * @param center angle to the center of the arc (degrees)
	 * @param arc total swing of the arc (degrees)
	 * @param c Color
	 */
    public Arc(Point2D.Double pos, double r, double rot, double ang, double center, double arc, Color c) {
		super(c);
		this.pos = pos;
        this.radius = r;
		this.rotate = rot;
		this.angle = ang;
		this.centerAngle = center;
		this.arcAngle = arc;
    }

	/**
	 * A drawable arc defined by inch position
	 * @param pos arc position in inches
	 * @param r radius in inches
	 * @param center angle to the center of the arc (degrees)
	 * @param arc total swing of the arc (degrees)
	 * @param c Color
	 * @param s BasicStroke
	 */
    public Arc(Point2D.Double pos, double r, double center, double arc, Color c, BasicStroke s) {
		super(c, s);
		this.pos = pos;
        this.radius = r;
		this.centerAngle = center;
		this.arcAngle = arc;
    }

	/**
	 * A drawable arc defined by inch position
	 * @param pos arc position in inches
	 * @param r radius in inches
	 * @param center angle to the center of the arc (degrees)
	 * @param arc total swing of the arc (degrees)
	 * @param c Color
	 */
    public Arc(Point2D.Double pos, double r, double center, double arc, Color c) {
		super(c);
		this.pos = pos;
        this.radius = r;
		this.centerAngle = center;
		this.arcAngle = arc;
    }

	/**
	 * Get the arc position in inches
	 * @return arc position in inches
	 */
    public Point2D.Double getPos() {
        return pos;
    }

	/**
	 * Move the arc to a new point defined in inches
	 * @param p new point in inches
	 */
    public void moveTo(Point2D.Double p) {
		pos = p;
    }

	/**
	 * Get the arc radius
	 * @return radius in inches
	 */
	public double getRadius() {
		return radius;
	}

    /**
     * Set the arc radius
	 * @param r radius in inches
     */
    public void setRadius(double r) {
        this.radius = r;
    }

	/**
	 * Get the rotation from the flat plane
	 * @return rotation in degrees, zero is flat in the visible plane
	 */
	public double getRotation() {
		return rotate;
	}

	/**
	 * Set the rotation from the flat plane
	 * @param rot rotation in degrees, zero is flat in the visible plane
	 */
	public void setRotation(double rot) {
		this.rotate = rot;
	}
	
	/**
	 * Get the angle to the center of the arc (degrees).
	 * @return angle
	 */
	public double getCenterAngle() {
		return centerAngle;
	}
	
	/**
	 * Set the angle to the center of the arc (degrees).
	 * @param a angle in degrees
	 */
	public void setCenterAngle(double a) {
		this.centerAngle = a;
	}

	/**
	 * Set the angle within the flat plane
	 * @return angle in degrees, zero is elongated up/down, + is CW rotation
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * Set the angle within the flat plane
	 * @param ang angle in degrees, zero is elongated up/down, + is CW rotation
	 */
	public void setAngle(double ang) {
		this.angle = ang;
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
			double rCos = Math.abs(radius*Math.cos(Math.toRadians(rotate)));		// no negative
			g2d.draw(new Arc2D.Double(-rCos, -radius,
									  2.0*rCos, 2.0*radius,
									  -(centerAngle + angle) - arcAngle / 2, arcAngle, Arc2D.OPEN));	// -centerangle because g2d is scaled -Y
									  
			g2d.setTransform(saveXform);
		}
	}

}
