package com.billooms.drawables;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Interface defining things that are drawable
 * Note that all Drawable objects are defined in 2 dimensional space using XY coordinate system.
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
public interface Drawable {

	/**
	 * Get the color of the object
	 * @return color
	 */
	Color getColor();

	/**
	 * Set the object color
	 * @param c Color c
	 */
	void setColor(Color c);

	/**
	 * Determine if the object is currently visible
	 * @return true=visible; false=invisible
	 */
	boolean getVisible();

	/**
	 * Set the visibility of the object
	 * @param v true=visible; false=not drawn
	 */
	void setVisible(boolean v);

	/**
	 * Paint the object
	 * @param g2d Graphics2D g
	 */
	void paint(Graphics2D g2d);
}
