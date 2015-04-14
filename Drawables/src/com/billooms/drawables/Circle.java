package com.billooms.drawables;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * A Drawable round circle defined by inch(cm) position
 * @author Bill Ooms. Copyright 2011 Studio of Bill Ooms. All rights reserved.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class Circle extends Shape {

	private Point2D.Double pos;		// position in inches
	private double radius;
	private double rotate = 0.0;	// degrees, zero is flat in the visible plane
	private double angle = 0.0;		// degrees, zero is elongated up/down, + is CW rotation

	/**
	 * A drawable circle defined by inch position
	 * @param pos circle position in inches
	 * @param r radius in inches
	 * @param rot Rotation in degrees from flat
	 * @param ang Angle in degrees from vertical (+ is ClockWise)
	 * @param c Color
	 * @param s BasicStroke
	 */
	public Circle(Point2D.Double pos, double r, double rot, double ang, Color c, BasicStroke s) {
		super(c, s);
		this.pos = pos;
		this.radius = r;
		this.rotate = rot;
		this.angle = ang;
	}

	/**
	 * A drawable circle defined by inch position
	 * @param pos circle position in inches
	 * @param r radius in inches
	 * @param c Color
	 * @param s BasicStroke
	 */
	public Circle(Point2D.Double pos, double r, Color c, BasicStroke s) {
		super(c, s);
		this.pos = pos;
		this.radius = r;
	}

	/**
	 * A drawable circle defined by inch position (default stroke)
	 * @param pos circle position in inches
	 * @param r radius in inches
	 * @param c Color
	 */
	public Circle(Point2D.Double pos, double r, Color c) {
		super(c);
		this.pos = pos;
		this.radius = r;
	}

	/**
	 * A drawable circle defined by inch position (default stroke and color
	 * @param pos circle position in inches
	 * @param r radius in inches
	 */
	public Circle(Point2D.Double pos, double r) {
		super();
		this.pos = pos;
		this.radius = r;
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
	 * Get the circle radius
	 * @return radius in inches
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Set the circle radius
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
			float scale = (float) saveXform.getScaleX();

			AffineTransform at = new AffineTransform();
			at.translate(pos.x, pos.y);			// this should give rotate first, then translate
			at.rotate(Math.toRadians(-angle));
			g2d.transform(at);

			float array[] = stroke.getDashArray();
			if ((array == null) || (array.length == 0)) {
				g2d.setStroke(new BasicStroke(1.0f / scale));
			} else {
				for (int i = 0; i < array.length; i++) {
					array[i] = array[i] / scale;
				}
				g2d.setStroke(new BasicStroke(1.0f / scale, stroke.getEndCap(), stroke.getLineJoin(), 1.0f, array, stroke.getDashPhase() / scale));
			}
			g2d.setColor(color);
			double rCos = Math.abs(radius * Math.cos(Math.toRadians(rotate)));		// no negative
			g2d.draw(new Ellipse2D.Double(-rCos, -radius, 2.0 * rCos, 2.0 * radius));	// offset is taken care of by transform

			g2d.setTransform(saveXform);					// Restore transform
		}
	}
}
