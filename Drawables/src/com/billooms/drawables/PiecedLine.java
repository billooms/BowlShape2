package com.billooms.drawables;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * A pieced line defined by a number of points (pluses).
 * @author Bill Ooms. Copyright 2011 Studio of Bill Ooms. All rights reserved.
 */
public class PiecedLine extends PtDefinedLine {

	/**
	 * A pieced line defined by a number of Pluses.
	 * @param c Color
	 */
	public PiecedLine(Color c) {
		super(c);
	}
	
	/**
	 * A pieced line defined by a number of Pluses (default color).
	 */
	public PiecedLine() {
		super();
	}

    /**
     * Paint the object.
     * @param g2d Graphics2D
     */
	@Override
	public void paint(Graphics2D g2d) {
		super.paint(g2d);
        if (visible) {
			if (ptList.size() == 1)		// no line for only one point
				return;

			g2d.setColor(color);
			Point2D.Double p, pm1;
			for (int i = 1; i < ptList.size(); i++) {
				p = ptList.get(i - 1).getPos();
				pm1 = ptList.get(i).getPos();
				g2d.draw(new Line2D.Double(pm1.x, pm1.y, p.x, p.y));
			}
		}
	}

	/**
	 * Add a point at the end (that is, the top) of the curve.
	 * @param p new point location in inches
	 * @return new point
	 */
    public Plus addPt(Point2D.Double p) {
        Plus newPlus = new Plus(p, color);
        ptList.add(newPlus);
		return newPlus;
    }

	/**
     * Insert a point at the correct location in the list (sorted by height).
	 * @param p new point location in inches
	 * @return new point
	 */
    public Plus insertPt(Point2D.Double p) {
        Plus newPlus = new Plus(p, color);
        insertPt(newPlus);
		return newPlus;
    }

	/**
	 * Get the points in the PiecedLine.
	 * @return array of Point2D.Double, an empty array if no points
	 */
	public Point2D.Double[] getPoints() {
		Point2D.Double[] pts = new Point2D.Double[ptList.size()];
		for (int i = 0; i < ptList.size(); i++) {
			Pt p = ptList.get(i);
			pts[i] = p.getPos();
		}
		return pts;
	}

}
