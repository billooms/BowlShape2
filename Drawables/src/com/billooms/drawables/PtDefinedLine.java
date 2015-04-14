package com.billooms.drawables;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract definition of a line or curve defined by a number of points (Pt, Plus, etc).
 * Points are ordered, bottom first and top last.
 * Extend this to define lines with particular characteristics (i.e. FittedCurve, PiecedLine, etc).
 * @author Bill Ooms. Copyright 2011 Studio of Bill Ooms. All rights reserved.
 */
public abstract class PtDefinedLine implements Drawable {
	private final static Color DEFAULT_COLOR = Color.PINK;
	
	/** Color of the object */
	protected Color color = DEFAULT_COLOR;
	/** Visible (i.e. drawn) or not. */
	protected boolean visible = true;		// always visible unless specifically changed
	/** The list of points */
    protected  ArrayList<Pt> ptList;		// point list

	/**
	 * A line or curve defined by a number of points (Pt, Plus, etc).
	 * @param c Color
	 */
	public PtDefinedLine(Color c) {
		this.color = c;
        ptList = new ArrayList<Pt>();
	}
	
	/**
	 * A line or curve defined by a number of points (Pt, Plus, etc) 
	 * with default color.
	 */
	public PtDefinedLine() {
        ptList = new ArrayList<Pt>();
	}

    /**
     * Paint the object.
     * @param g2d Graphics2D
     */
	@Override
	public void paint(Graphics2D g2d) {		// This is further customized by extensions
		if (visible) {
			if (ptList.isEmpty()) {
				return;
			} else {
				for (Pt d : ptList) {
					d.paint(g2d);
				}
			}
		}
	}

	/**
	 * Get the color of the object.
	 * @return color
	 */
	@Override
	public Color getColor() {
		return color;
	}

	/**
	 * Set the point color.
	 * @param c Color c
	 */
	@Override
	public void setColor(Color c) {
		this.color = c;
	}

	/**
	 * Determine if the object is currently visible.
	 * @return true=visible; false=invisible
	 */
	@Override
	public boolean getVisible() {
		return visible;
	}

	/**
	 * Set the visibility of the object.
	 * @param v true=visible; false=not drawn
	 */
	@Override
	public void setVisible(boolean v) {
		visible = v;
	}

	/**
	 * Clear the point list.
	 */
	public void clear() {
		ptList.clear();
	}

    /**
     * Get the number of dots defining the curve.
     * @return The number of dots
     */
    public int getSize() {
        return ptList.size();
    }

    /**
     * Get a point from the point list.
     * @param idx Index of the desired point
     * @return the point (or null if idx >= size() or is negative)
     */
    public Pt getPt(int idx) {
		if ((idx < 0) || (idx >= ptList.size())) {
			return null;
		}
        return ptList.get(idx);
    }

    /**
     * Get the last (that is, the uppermost) point.
     * @return the point
     */
    public Pt getLastPt() {
        return getPt(ptList.size()-1);
    }

	/**
	 * Get all points
	 * @return list of all points
	 */
	public List<Pt> getAllPoints() {
		return ptList;
	}
	
    /**
     * Insert a point at the correct location in the list (sorted by height).
     * @param newPt the point to be inserted
     */
    public void insertPt(Pt newPt) {
        if (ptList.isEmpty()) {
            ptList.add(newPt);
            return;
        }
        double newYInch = newPt.getPos().y;
        if (newYInch < ptList.get(0).getPos().y) {      // point is below the bottom
            ptList.add(0, newPt);
            return;
        }
        if (newYInch >= getLastPt().getPos().y) {        // point is above the top
            ptList.add(newPt);
            return;
        }
        for (int i = 1; i < ptList.size(); i++) {      // point is somewhere between
            if ((newYInch >= ptList.get(i-1).getPos().y) && (newYInch < ptList.get(i).getPos().y)) {
                ptList.add(i, newPt);
                break;
            }
        }
    }

    /**
     * Delete a given point.
     * @param pt point to be deleted
     */
    public void deletePt(Pt pt) {
        ptList.remove(pt);
    }

	/**
	 * Find the closest point (within dis) to the given point.
	 * @param p given point
	 * @param dis distance to measure
	 * @return the closest point (within dis), null if no point is within dis
	 */
    public Pt closestPt(Point2D.Double p, double dis) {
        Pt d = null;
        double minSep = dis;
        for (Pt dd : ptList) {
            double sep = dd.separation(p);
            if (sep < minSep) {
                minSep = sep;
                d = dd;
            }
        }
        return d;
    }

	/**
	 * Get the bounding box for the points defining this shape.
	 * @return bounding box (which might not include 0.0, 0.0)
	 */
	public BoundingBox getBoundingBox() {
        if (ptList.isEmpty()) {
			return new BoundingBox(0.0, 0.0, 0.0, 0.0);
		}
		Point2D.Double p0 = ptList.get(0).getPos();
		double minX = p0.x, maxX = p0.x, minY = p0.y, maxY = p0.y;
		for (int i = 0; i < ptList.size(); i++) {
			Point2D.Double p = ptList.get(i).getPos();
			if (p.y < minY) 
				minY = p.y;
			if (p.y > maxY) 
				maxY = p.y;
			if (p.x > maxX) 
				maxX = p.x;
			if (p.x < minX) 
				minX = p.x;
		}
		return new BoundingBox(minX, minY, maxX, maxY);
	}

    /**
     * Invert the points around zero and reverse the order of the points.
     */
    public void invert() {
		ArrayList<Pt> newList = new ArrayList<Pt>();
		for (int i = ptList.size()-1; i >= 0; i--) {
			Pt p = ptList.get(i);
			p.invert();
			newList.add(p);			// make a new list with the points in reverse order
		}
		ptList = newList;
    }

	/**
	 * Offset in the y-direction by subtracting the given value from y-coordinate.
	 * @param deltaY offset amount (in inches);
	 */
	public void offsetY(double deltaY) {
		for (Pt pt : ptList) {
			pt.offsetY(deltaY);
		}
	}
}
