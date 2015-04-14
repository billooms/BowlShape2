package com.billooms.drawables;

import java.awt.geom.Point2D;

/**
 * Bounding box of a drawable shape
 * @author Bill Ooms Copyright (c) 2011 Studio of Bill Ooms, all rights reserved
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
public class BoundingBox {

	/** Lower left corner */
	public Point2D.Double min;
	/** Upper right corner */
	public Point2D.Double max;

	/**
	 * Create a bounding box with the given dimensions
	 * @param xmin minimum x
	 * @param ymin minimum y
	 * @param xmax maximum x
	 * @param ymax maximum y
	 */
	public BoundingBox(double xmin, double ymin, double xmax, double ymax) {
		min = new Point2D.Double(xmin, ymin);
		max = new Point2D.Double(xmax, ymax);
	}

	/**
	 * Create a bounding box which is encompasses both the given bounding boxes
	 * @param b1 BoundingBox
	 * @param b2 BoundingBox
	 */
	public BoundingBox(BoundingBox b1, BoundingBox b2) {
		min = new Point2D.Double(Math.min(b1.min.x, b2.min.x), Math.min(b1.min.y, b2.min.y));
		max = new Point2D.Double(Math.max(b1.max.x, b2.max.x), Math.max(b1.max.y, b2.max.y));
	}

	/**
	 * Get the width of the bounding box
	 * @return width
	 */
	public double getWidth() {
		return Math.abs(max.x - min.x);
	}

	/**
	 * Get the height of the bounding box
	 * @return height
	 */
	public double getHeight() {
		return Math.abs(max.y - min.y);
	}
}
