package com.billooms.drawables;

import com.billooms.geometry.LineGeom;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.vecmath.Vector2d;

/**
 * A curve-fit curve defined by a number of points (dots).
 * Points are ordered, bottom first and top last.
 * The curve-fit algorithm is a quadratic Bezier curve.
 * Note that a quadratic fit will not do a good job at points of inflection
 * because one control point between two data points will give a straight line (at best)
 * at points of inflection.
 * So for segments that have an inflection, a cubic Bezier fit is used.
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

public class FittedCurve extends PtDefinedLine {

	private enum FitType {QUAD, CUBIC};	// Fit a segment either Quadratic or Cubic

	/**
	 * Data for a segment of a curve which can be a Quadratic fit or a Cubic fit.
	 */
	private class SegData {	
		protected FitType type;			// QUAD or CUBIC
		protected Point2D.Double p1;			// control point for QUAD, or 1st point for CUBIC
		protected Point2D.Double p2;			// 2nd control point for CUBIC

		/**
		 * Create the segment data from 2 control points (implies CUBIC).
		 * @param p1 1st control point
		 * @param p2 2nd control point
		 */
		public SegData(Point2D.Double p1, Point2D.Double p2) {	// 2 control points for a cubic fit
			this.type = FitType.CUBIC;
			this.p1 = p1;
			this.p2 = p2;
		}

		/**
		 * Create the segment data from 1 control point (implies QUAD).
		 * Note that p2 is set to null
		 * @param p1 control point
		 */
		public SegData(Point2D.Double p1) {				// 1 control point for a quadratic fit
			this.type = FitType.QUAD;
			this.p1 = p1;
			this.p2 = null;
		}
	}	// end SegData

	private double ptSpacing = 0.0;
	ArrayList<SegData> pC;					// control points for this curve
	double[] pAng;

	/**
	 * A curve-fit curve defined by a number of Dots.
	 * @param c Color
	 */
	public FittedCurve(Color c) {
		super(c);
	}

	/**
	 * A curve-fit curve defined by a number of Dots (default color and scale)
	 */
	public FittedCurve() {
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
			if (ptList.size() == 1)      // no line for only one point
				return;

			float scale = (float) g2d.getTransform().getScaleX();
			g2d.setStroke(new BasicStroke(1.0f / scale));
			g2d.setColor(color);
			Point2D.Double p, pm1;
			if (ptList.size() == 2) {       // straight line for 2 points
				p = ptList.get(1).getPos();
				pm1 = ptList.get(0).getPos();
				g2d.draw(new Line2D.Double(pm1.x, pm1.y, p.x, p.y));
				return;
			}

			if (ptSpacing == 0.0)
				return;

			Point2D.Double[] drawPts = getCurvePoints(ptSpacing);
			for (int i = 1; i < drawPts.length; i++) {
				g2d.draw(new Line2D.Double(drawPts[i-1].x, drawPts[i-1].y, drawPts[i].x, drawPts[i].y));
			}

			Point2D.Double[] cPts = getControlPoints();
			for (Point2D.Double pt : cPts) {
				g2d.draw(new Line2D.Double(pt.x, pt.y, pt.x, pt.y));
			}
		}
	}

	/**
	 * Add a dot at the end (that is, the top) of the curve.
	 * @param p new dot location in inches
	 * @return new dot
	 */
    public Dot addPt(Point2D.Double p) {
        Dot newDot = new Dot(p, color);
        ptList.add(newDot);
		return newDot;
    }

	/**
     * Insert a dot at the correct location in the list (sorted by height).
	 * @param p new dot location in inches
	 * @return the new dot
	 */
    public Dot insertPt(Point2D.Double p) {
        Dot newDot = new Dot(p, color);
        super.insertPt(newDot);
		return newDot;
    }

    /**
     * Return an array of Point2D for the points on the fit curve in inches (cm)
     * that are fit from the original array of given points.
     * If no points, return an empty array.
     * @param dd approximate distance that should be between returned points
     * @return array of Point2D for the points on the fit curve, empty array if no points
     */
	public Point2D.Double[] getCurvePoints(double dd) {
		this.ptSpacing = dd;
		ArrayList<Point2D.Double> pts = new ArrayList<Point2D.Double>();

		if (ptList.isEmpty()) {
			return arrayFromList(pts);
        }
		
        if (ptList.size() == 1) {
            pts.add(ptList.get(0).getPos());
			return arrayFromList(pts);
        }

        if (ptList.size() == 2) {      // straight line between 2 points
			Point2D.Double d0 = ptList.get(0).getPos();
			Point2D.Double d1 = ptList.get(1).getPos();
			double dx = d1.x-d0.x;
			double dy = d1.y-d0.y;
			double length = Math.sqrt(dx*dx + dy*dy);
			int npts = (int)(length/dd);
            for (int i = 0; i <= npts; i++) {
                double x = d0.x + (d1.x-d0.x)*i/npts;
                double y = d0.y + (d1.y-d0.y)*i/npts;
                pts.add(new Point2D.Double(x, y));
            }	
			pAng = new double[ptList.size()];		// angle is the same at each point
			pAng[0] = pAng[1] = Math.atan2(dy, dx);
			return arrayFromList(pts);
        }

		makeControlPts();
		pts.add(new Point2D.Double(ptList.get(0).getPos().x, ptList.get(0).getPos().y));	// first point
		for (int i = 0; i < ptList.size()-1; i++) {
			SegData seg = pC.get(i);
			double t, t_;
			double a, b, c, d;
			Point2D.Double di = ptList.get(i).getPos();
			Point2D.Double di1 = ptList.get(i+1).getPos();
			double length = Math.hypot(di1.x-di.x, di1.y-di.y);
			int pps = Math.max((int)(length/dd), 1);	// number of points for this segment of the curve
			double step = 1.0 / (double)pps;
			switch(seg.type) {
				case QUAD:
					for(int j = 1; j <= pps; j++) {				// Quadratic Bezier Curve
						t = j * step;
						t_ = 1.0 - t;
						a = t_ * t_;			// a = (1-t)(1-t)
						b = 2 * t_ * t;			// b = 2(1-t)t
						c = t * t;				// c = tt
												// B(t)= a*P0 + b*P1 + c*P2 + d*P3
												// P0 = p[i]
												// P1 = pC[i].p1
												// P2 = p[i+1]
						pts.add(new Point2D.Double(a * di.x + b * pC.get(i).p1.x + c * di1.x,
											a * di.y + b * pC.get(i).p1.y + c * di1.y));
					}
					break;
				case CUBIC:
					for(int j = 1; j <= pps; j++) {				// Cubic Bezier Curve
						t = j * step;
						t_ = 1.0 - t;
						a = t_ * t_ * t_;		// a = (1-t)(1-t)(1-t)
						b = 3 * t_ * t_ * t;	// b = 3(1-t)(1-t)t
						c = 3 * t_ * t * t;		// c = 3(1-t)tt
						d = t * t * t;			// d = ttt
												// B(t)= a*P0 + b*P1 + c*P2 + d*P3
												// P0 = p[i]
												// P1 = pC[i].p1
												// P2 = pC[i].p3
												// P3 = p[i+1]
						pts.add(new Point2D.Double(a * di.x + b * pC.get(i).p1.x  + c * pC.get(i).p2.x + d * di1.x,
											a * di.y + b * pC.get(i).p1.y + c * pC.get(i).p2.y + d * di1.y));
					}
					break;
			}
		}

		return arrayFromList(pts);
	}

	/**
	 * Get an array of the control points for the fit curve
	 * @return array of control points
	 */
	public Point2D.Double[] getControlPoints() {
		ArrayList<Point2D.Double> cPts = new ArrayList<Point2D.Double>();
		for(SegData sd : pC) {
			switch(sd.type) {
				case QUAD:
					cPts.add(sd.p1);
					break;
				case CUBIC:
					cPts.add(sd.p1);
					cPts.add(sd.p2);
					break;
			}
		}
		return arrayFromList(cPts);
	}

	/**
	 * Convert an ArrayList into a fixed array
	 * @param pts ArrayList
	 * @return Array
	 */
	private Point2D.Double[] arrayFromList(ArrayList<Point2D.Double> pts) {	// convert ArrayList into a fixed array
		Point2D.Double[] newPts = new Point2D.Double[pts.size()];
		for (int i = 0; i < pts.size(); i++) {
			newPts[i] = pts.get(i);
		}
		return newPts;
	}

	/**
	 * Make the control points for this curve
	 */
	private void makeControlPts() {
		Point2D.Double[] p = new Point2D.Double[ptList.size()];
		for (int i = 0; i < p.length; i++) {		// make an array of Point2D.Double for the dots (in inches)
			p[i] = ptList.get(i).getPos();
		}

		double[] segAng = new double[p.length-1];	// the angle of each segment
		double[] segL = new double[p.length-1];		// the length of each segment
		for (int i = 0; i < segAng.length; i++) {
			double dx = p[i+1].x - p[i].x;
			double dy = p[i+1].y - p[i].y;
			segAng[i] = Math.atan2(dy, dx);			// the angle of each segment
			if (i > 0) {
				if ((segAng[i-1] > Math.PI/2.0) && (segAng[i] < 0)) {
					segAng[i] += 2.0*Math.PI;		// add 2PI so angles continue smoothly
				}
				if ((segAng[i-1] < -Math.PI/2.0) && (segAng[i] > 0)) {
					segAng[i] -= 2.0*Math.PI;		// subtract 2PI so angles continue smoothly
				}
			}
			segL[i] = Math.hypot(dx, dy);			// the length of each segment
//			System.out.println(i + " segAng:" + segAng[i]*180./Math.PI + " segL:" + segL[i]);
		}

		pAng = new double[ptList.size()];		// angle at each point
		for (int i = 1; i < pAng.length-1; i++) {	// proportional to adjacent segments' length
			pAng[i] = segAng[i-1] + (segAng[i] - segAng[i-1]) * segL[i-1]/(segL[i-1] + segL[i]);
		}
		pAng[0] = 2.0*segAng[0] - pAng[1];
		pAng[pAng.length-1] = 2.0*segAng[p.length-2] - pAng[p.length-2];
//		for (int i = 0; i < pAng.length; i++) {
//			System.out.println(i + " pAng:" + pAng[i]*180./Math.PI);
//		}
		
		pC = new ArrayList<SegData>();	// control points -- at intersection of pAng lines
		for (int i = 0; i < p.length-1; i++) {
			LineGeom line0 = new LineGeom(p[i], pAng[i]);
			LineGeom line1 = new LineGeom(p[i+1], pAng[i+1]);
			if (((segAng[i] > pAng[i]) && (pAng[i+1] < segAng[i])) ||
				((segAng[i] < pAng[i]) && (pAng[i+1] > segAng[i]))) {		// point of inflection
				LineGeom ll = new LineGeom(p[i], p[i+1]);
				double x1, y1, x2, y2;
				if ((ll.isVertical()) || (Math.abs(ll.m) > 1.0)) {		// for steep lines
					y1 = 0.25 * (p[i+1].y - p[i].y) + p[i].y;		// use 25% and 75% of y
					x1 = line0.getX(y1);
					y2 = 0.75 * (p[i+1].y - p[i].y) + p[i].y;
					x2 = line1.getX(y2);
				} else {											// for shallow lines
					x1 = 0.25 * (p[i+1].x - p[i].x) + p[i].x;		// use 25% and 75% of x
					y1 = line0.getY(x1);
					x2 = 0.75 * (p[i+1].x - p[i].x) + p[i].x;
					y2 = line1.getY(x2);
				}
				pC.add(new SegData(new Point2D.Double(x1, y1), new Point2D.Double(x2, y2)));
			} else {
				Point2D.Double iSect = line0.intersection(line1);	// find intersection of 2 straight lines
				if (iSect == null) {	// if same slope, choose a point half way
					pC.add(new SegData(new Point2D.Double((p[i].x + p[i+1].x)/2.0, (p[i].y + p[i+1].y)/2.0)));
				} else {
					pC.add(new SegData(new Point2D.Double(iSect.x, iSect.y)));
				}
			}
		}
		return;
	}

	/**
	 * Offset all the dots in the curve.
	 * @param d offset amount
	 * @param dir Direction: true = FRONT_INSIDE or BACK_OUTSIDE, false = FRONT_OUTSIDE or BACK_INSIDE
	 */
	public void offsetDots(double d, boolean dir) {
		if (ptList.isEmpty()) {
			return;
		}
		makeControlPts();		// make sure that pAng is up to date
		for (int i = 0; i < ptList.size(); i++) {
			Dot dot = (Dot)ptList.get(i);
			Vector2d perp = perpendicularAt(i, dir);
			perp.scale(d);
			dot.pos.x = dot.getPos().x + perp.x;	// don't use setPos() because it fires a propertyChange
			dot.pos.y = dot.getPos().y + perp.y;
		}
		makeControlPts();
	}

	/**
	 * Perpendicular vector at a given dot
	 * @param i index of desire dot
	 * @param dir Direction: true = FRONT_INSIDE or BACK_OUTSIDE, false = FRONT_OUTSIDE or BACK_INSIDE
	 * @return normalized perpendicular vector at the dot (null if index is invalid or only 1 point)
	 */
	private Vector2d perpendicularAt(int i, boolean dir) {
		Vector2d v = null;
		if ((i >= pAng.length) || (i < 0)) {
			return v;
		}
		if (pAng.length < 2) {
			return v;
		}
		if (dir) {
			v = new Vector2d(Math.sin(pAng[i]), -Math.cos(pAng[i]));
		} else {
			v = new Vector2d(-Math.sin(pAng[i]), Math.cos(pAng[i]));
		}
		v.normalize();
		return v;
	}
}
