package com.billooms.drawables;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * A Drawable round dot defined by inch(cm) location
 * @author Bill Ooms. Copyright 2011 Studio of Bill Ooms. All rights reserved.
 */
public class Dot extends Pt {

	/**
	 * A drawable dot defined by inch location and color
	 * @param pos dot location in inches
	 * @param c Color
	 */
	public Dot(Point2D.Double pos, Color c) {
		super(pos, c);
	}

	/**
	 * A drawable dot defined by inch location (default color)
	 * @param pos dot location in inches
	 */
	public Dot(Point2D.Double pos) {
		super(pos);
	}

	/**
	 * Paint the object
	 * @param g2d Graphics2D
	 */
	@Override
	public void paint(Graphics2D g2d) {
		super.paint(g2d);	// sets color and stroke
		if (visible) {
			double size = getPtSize(g2d);
			if (fill) {
				g2d.fill(new Ellipse2D.Double(pos.x - size / 2.0, pos.y - size / 2.0, size, size));
			} else {
				g2d.draw(new Ellipse2D.Double(pos.x - size / 2.0, pos.y - size / 2.0, size, size));
			}
		}
	}
}
