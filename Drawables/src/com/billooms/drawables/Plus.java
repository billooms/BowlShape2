package com.billooms.drawables;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * A Drawable Plus defined by inch(cm) location
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
public class Plus extends Pt {

	/**
	 * A drawable plus defined by inch location and color
	 * @param pos plus location in inches
	 * @param c Color
	 */
	public Plus(Point2D.Double pos, Color c) {
		super(pos, c);
	}

	/**
	 * A drawable plus defined by inch location (default color)
	 * @param pos plus location in inches
	 */
	public Plus(Point2D.Double pos) {
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
			g2d.draw(new Line2D.Double(pos.x, pos.y + size/2.0, pos.x, pos.y - size/2.0));
			g2d.draw(new Line2D.Double(pos.x + size/2.0, pos.y, pos.x - size/2.0, pos.y));
		}
	}
}
