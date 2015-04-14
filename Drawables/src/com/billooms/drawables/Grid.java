package com.billooms.drawables;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

/**
 * A Drawable 2D grid of lines
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
public class Grid implements Drawable {
    private final static Color GRID_COLOR = Color.GRAY;

	private Color color = GRID_COLOR;
	private boolean visible = true;	// always visible unless specifically changed
	private double x, y, w, h;
	
	/**
	 * A drawable 2D grid of lines with no text
	 * @param x Left edge of grid in inches
	 * @param y Bottom edge of grid in inches
	 * @param w width of grid in inches
	 * @param h width of grid in inches
	 */
	public Grid(double x, double y, double w, double h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	/**
	 * Get the color of the object
	 * @return color
	 */
	@Override
	public Color getColor() {
		return color;
	}

	/**
	 * Set the point color
	 * @param c Color c
	 */
	@Override
	public void setColor(Color c) {
		this.color = c;
	}

	/**
	 * Determine if the object is currently visible
	 * @return true=visible; false=invisible
	 */
	@Override
	public boolean getVisible() {
		return visible;
	}

	/**
	 * Set the visibility of the object
	 * @param v true=visible; false=not drawn
	 */
	@Override
	public void setVisible(boolean v) {
		visible = v;
	}
	
	/**
	 * Set the dimensions of the grid
	 * @param x Left edge of grid in inches
	 * @param y Bottom edge of grid in inches
	 * @param w width of grid in inches
	 * @param h width of grid in inches
	 */
	public void setDimensions(double x, double y, double w, double h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

    /**
     * Paint the object
     * @param g2d Graphics2D
     */
	@Override
    public void paint(Graphics2D g2d) {
        if (visible) {
			if ((w <= 0.0) || (h <= 0.0)) {
				return;
			}
			g2d.setColor(color);
			float scale = (float)g2d.getTransform().getScaleX();
			float width = 1.0f / scale;
			BasicStroke solid = new BasicStroke(width);
			BasicStroke dotted = new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, new float[] {1/scale,5/scale}, 0);
			for(int xx = (int)x; xx < (x + w); xx++) {	// draw vertical lines
				if (xx == 0)
					g2d.setStroke(solid);		// line at zero is solid
				else
					g2d.setStroke(dotted);
				g2d.draw(new Line2D.Double((double)xx, y, (double)xx, y + h));
			}
			for(int yy = (int)y; yy < (y + h); yy++) {	// draw horizontal lines
				if (yy == 0)
					g2d.setStroke(solid);		// line at zero is solid
				else
					g2d.setStroke(dotted);
				g2d.draw(new Line2D.Double(x, (double)yy, x + w, (double)yy));
			}
		}
    }
}
