package com.billooms.geometry;

import java.awt.geom.Point2D;


/**
 * A circle defined by a center point and a radius
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
public class CircleGeom {
	/** Center point */
	public Point2D.Double p;
	/** Radius */
	public double r;

	/**
	 * Define a circle by a center point and a radius
	 * @param p Point2D.Double center
	 * @param r radius
	 */
	public CircleGeom(Point2D.Double p, double r) {
		this.p = p;
		this.r = r;
	}
	
	/**
	 * Determine if the given point is inside the circle
	 * @param pt given point
	 * @return true = inside (not on the circle or outside)
	 */
	public boolean isInside(Point2D.Double pt) {
		double d = Math.hypot(p.x - pt.x, p.y - pt.y);
		if (d < r)
			return true;
		else
			return false;
	}
}
