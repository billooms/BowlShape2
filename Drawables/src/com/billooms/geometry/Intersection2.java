package com.billooms.geometry;

import java.awt.geom.Point2D;


/**
 * An intersection of two geometries defined by up to 2 points
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
public class Intersection2 {
	/** First point (null if there is no intersection or total intersection */
	public Point2D.Double p0;
	/** Second point (null if there is only one point intersection) */
	public Point2D.Double p1;

	/**
	 * Create an intersection between two circles.
	 * @param c0 First circle
	 * @param c1 Second circle
	 */
	public Intersection2(CircleGeom c0, CircleGeom c1) {
		double d = Math.hypot(c0.p.x - c1.p.x, c0.p.y - c1.p.y);
		if ((d > (c0.r + c1.r)) || (d < Math.abs(c0.r - c1.r))) {	// no intersection
			p0 = null;
			p1 = null;
			return;
		}
		if ((c0.r == c1.r) && (d == 0.0)) {	// coincident
			p0 = null;
			p1 = null;
			return;
		}
		double a = (c0.r*c0.r - c1.r*c1.r + d*d)/(2.0*d);
		Point2D.Double p2 = new Point2D.Double(c0.p.x + a*(c1.p.x - c0.p.x)/d, c0.p.y + a*(c1.p.y - c0.p.y)/d);
		if (d == (c0.r + c1.r)) {		// single point intersection
			p0 = p2;
			p1 = null;
			return;
		}
		double h = Math.sqrt(c0.r*c0.r - a*a);
		p0 = new Point2D.Double(p2.x + h*(c1.p.y - c0.p.y)/d, p2.y - h*(c1.p.x - c0.p.x)/d);
		p1 = new Point2D.Double(p2.x - h*(c1.p.y - c0.p.y)/d, p2.y + h*(c1.p.x - c0.p.x)/d);
	}

	/**
	 * Create an intersection between a line and a circle.
	 * @param line The line
	 * @param cir The circle
	 */
	public Intersection2(LineGeom line, CircleGeom cir) {
		LineGeom perp = new LineGeom(cir.p, line);
		Point2D.Double p2 = line.intersection(perp);	// perpendicular intersect with line
		double a = Math.hypot(cir.p.x - p2.x, cir.p.y - p2.y);
		if (a > cir.r) {		// no intersection
			p0 = null;
			p1 = null;
			return;
		}
		if (a == cir.r) {		// single point intersection
			p0 = p2;
			p1 = null;
			return;
		}
		double h = Math.sqrt(cir.r*cir.r - a*a);
		p0 = new Point2D.Double(p2.x + h*(p2.y - cir.p.y)/a, p2.y - h*(p2.x - cir.p.x)/a);
		p1 = new Point2D.Double(p2.x - h*(p2.y - cir.p.y)/a, p2.y + h*(p2.x - cir.p.x)/a);
	}

	/**
	 * Check to see if there is a null (or total) intersection
	 * @return true = no intersection
	 */
	public boolean nullIntersect() {
		if ((p0 == null) || (p1 == null))
			return true;
		else
			return false;
	}

	/**
	 * Check to see if intersection is only 1 point
	 * @return true = single point intersect
	 */
	public boolean singleIntersect() {
		if (p1 == null)
			return true;
		else
			return false;
	}

	/**
	 * Check to see if intersection is 2 points
	 * @return true = two points intersect
	 */
	public boolean doubleIntersect() {
		return !(p0 == null) && !(p1 == null);
	}

	/**
	 * Get the distance between the 2 points
	 * @return distance, 0.0 if there is no intersection or only 1 point intersection
	 */
	public double getDistance() {
		if (nullIntersect() || singleIntersect()) {
			return 0.0;
		}
		return Math.hypot(p0.x - p1.x, p0.y - p1.y);
	}

}
