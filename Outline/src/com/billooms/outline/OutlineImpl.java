
package com.billooms.outline;

import com.billooms.outline.api.*;
import com.billooms.drawables.BoundingBox;
import com.billooms.drawables.Curve;
import com.billooms.drawables.Dot;
import com.billooms.drawables.FittedCurve;
import com.billooms.drawables.Pt;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.lookup.ServiceProvider;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Defines the outline of the inner or outer surface of a shape. 
 * Points are ordered with the lowest points first and the top points last. 
 * @author Bill Ooms Copyright (c) 2011 Studio of Bill Ooms all rights reserved
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
@ServiceProvider(service = Outline.class)
public class OutlineImpl implements Outline, PropertyChangeListener {
	private final static DecimalFormat F3 = new DecimalFormat("0.000");
	private final static DecimalFormat F4 = new DecimalFormat("0.0000");
    private final static Color INSIDE_CURVE_COLOR = Color.MAGENTA;
    private final static Color OUTSIDE_CURVE_COLOR = Color.ORANGE;
	private final static boolean FIRE_PROP_CHANGE = true;
	
    private final static int DEFAULT_PTS = 3;
    private final static Point2D.Double[] INIT_PTS = {new Point2D.Double(0.000, 0.000),    // Initial points Bottom
                                               new Point2D.Double(0.700, 0.300),
                                               new Point2D.Double(1.000, 1.000)};	// Top

	private Location dotLocation = DEFAULT_DOT_LOCATION;	// location of digitized dots
	private double thickness = DEFAULT_THICKNESS;
	private double pointSpacing = DEFAULT_RESOLUTION;
	private FittedCurve dotCurve = null;		// digitized dots used for curve fitting
	private Curve insideCurve = null;			// inside surfaces
	private Curve outsideCurve =null;			// outside surfaces
	
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	/**
	 * Create an empty outline of the shape.
	 */
	public OutlineImpl() {
		dotCurve = new FittedCurve(DOT_CURVE_COLOR);
		insideCurve = new Curve(new Point2D.Double[0], INSIDE_CURVE_COLOR);
		outsideCurve = new Curve(new Point2D.Double[0], OUTSIDE_CURVE_COLOR);
        for (int i = 0; i < DEFAULT_PTS; i++) {		// Initialize points for default shape
			addPt(INIT_PTS[i], !FIRE_PROP_CHANGE);
        }
		updateCurves();
	}

	/**
	 * Update the curves after something has been changed.
	 */
	private void updateCurves() {
        if (dotCurve == null) {		// in case there are not yet any points
            return;
        }
		
		double delta;
		// Update the inside and outside curves
		if (dotLocation.isInside()) {
			insideCurve.setPoints(dotCurve.getCurvePoints(pointSpacing));
			if (dotLocation.isFront()) {
				delta = thickness;
			} else {
				delta = -thickness;
			}
			outsideCurve.setPoints(insideCurve.ptsOffset(delta));
		} else {
			outsideCurve.setPoints(dotCurve.getCurvePoints(pointSpacing));
			if (dotLocation.isFront()) {
				delta = -thickness;
			} else {
				delta = thickness;
			}
			insideCurve.setPoints(outsideCurve.ptsOffset(delta));
		}
	}

    /**
     * Paint the object.
     * @param g2d Graphics2D
     */
	@Override
	public void paint(Graphics2D g2d) {
		if (dotCurve != null) {
			dotCurve.paint(g2d);
		}
		if (insideCurve != null) {
			insideCurve.paint(g2d);
		}
		if (outsideCurve != null) {
			outsideCurve.paint(g2d);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		updateCurves();
		pcs.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());	// pass the info through for OutlineEditor
	}
	
	/**
	 * Get a string describing the outline
	 * @return string name
	 */
	@Override
	public String toString() {
		return "Outline: " + dotCurve.getSize() + " points";
	}

	/**
	 * Get the dotCurve
	 * @return dotCurve
	 */
	@Override
	public FittedCurve getDotCurve() {
		return dotCurve;
	}

	/**
	 * Get the insideCurve
	 * @return insideCurve
	 */
	@Override
	public Curve getInsideCurve() {
		return insideCurve;
	}

	/**
	 * Get the outsideCurve
	 * @return outsideCurve
	 */
	@Override
	public Curve getOutsideCurve() {
		return outsideCurve;
	}

	/**
	 * Get the number of points defining the curves
	 * @return number of points
	 */
	@Override
	public int getNumPts() {
		return dotCurve.getSize(); 
	}

	/**
	 * Get the total length of the inside curve
	 * @return total length of the inside curve
	 */
	@Override
	public double getInsideLength() {
		if (insideCurve != null) {
			return insideCurve.getLength();
		}
		return 0.0;
	}

	/**
	 * Get the total length of the outside curve
	 * @return total length of the outside curve
	 */
	@Override
	public double getOutsideLength() {
		if (outsideCurve != null) {
			return outsideCurve.getLength();
		}
		return 0.0;
	}

	/**
	 * Add an outline point.
	 * Points are in sorted order from bottom (first point) to top (last point).
	 * This fires a PROP_ADDPT property change with the new point.
	 * @param p new point to add
	 */
	@Override
	public synchronized void addPt(Point2D.Double p) {
		addPt(p, FIRE_PROP_CHANGE);
	}

	/**
	 * Add an outline point.
	 * Points are in sorted order from bottom (first point) to top (last point).
	 * This fires a PROP_ADDPT property change with the new point if fire is true.
	 * @param p new point to add
	 * @param fire 
	 */
	protected synchronized void addPt(Point2D.Double p, boolean fire) {
		Pt pt = dotCurve.insertPt(p);
		pt.addPropertyChangeListener(this);
		updateCurves();
		if (fire) {
			pcs.firePropertyChange(PROP_ADDPT, null, p);
		}
	}

	/**
	 * Delete the given point.
	 * This fires a PROP_DELETEPT property change with the old point.
	 * @param pt point to delete
	 */
	@Override
	public synchronized void deletePt(Dot pt) {
		dotCurve.deletePt(pt);
		pt.removePropertyChangeListener(this);
		updateCurves();
		pcs.firePropertyChange(PROP_DELETEPT, pt, null);
	}

	/**
	 * Clear all points.
	 * This fires a PROP_CLEAR property change.
	 */
	@Override
	public synchronized void clear() {
		clear(FIRE_PROP_CHANGE);
	}

	/**
	 * Clear all points.
	 * This fires a PROP_CLEAR property change if fire is true.
	 * @param fire 
	 */
	protected synchronized void clear(boolean fire) {
		for (Pt pt : dotCurve.getAllPoints()) {
			pt.removePropertyChangeListener(this);
		}
		dotCurve.clear();
		insideCurve.clear();
		outsideCurve.clear();
		if (fire) {
			pcs.firePropertyChange(PROP_CLEAR, null, null);
		}
	}

	/**
	 * Find the closest outline point to the given point
	 * which is within a given distance.
	 * @param pt given point
	 * @param dist distance to search
	 * @return closest point (or null if no point with in the given distance)
	 */
	@Override
	public Dot getClosestPt(Point2D.Double pt, double dist) {
		return (Dot)dotCurve.closestPt(pt, dist);
	}

	/**
	 * Get the bounding box for all of the curves
	 * @return bounding box of all curves
	 */
	@Override
	public BoundingBox getBoundingBox() {
		BoundingBox bb = new BoundingBox(0.0, 0.0, 2.0, 2.0);	// default size is 2 x 2
		if (dotCurve == null) {
			return bb;
		}
		bb = dotCurve.getBoundingBox();
		if (insideCurve != null)
			bb = new BoundingBox(bb, insideCurve.getBoundingBox());
		if (outsideCurve != null)
			bb = new BoundingBox(bb, outsideCurve.getBoundingBox());
		if (dotCurve != null)
			bb = new BoundingBox(bb, dotCurve.getBoundingBox());
		return bb;
	}

	/**
	 * Get the thickness of the shape.
	 * @return thickness of the shape
	 */
	@Override
	public double getThickness() {
		return thickness;
	}

	/**
	 * Set the thickness of the shape.
	 * This fires a PROP_THICKNESS property change with the old and new values.
	 * @param t new thickness of the shape
	 */
	@Override
	public void setThickness(double t) {
		if (t != thickness) {
			double old = thickness;
			this.thickness = Math.max(0.0, t);	// don't go below 0.0
			updateCurves();
			pcs.firePropertyChange(PROP_THICKNESS, old, thickness);
		}
	}

	/**
	 * Get the resolution of the curves.
	 * @return resolution
	 */
	@Override
	public double getResolution() {
		return pointSpacing;
	}

	/**
	 * Set the resolution of the curves.
	 * This fires a PROP_RESOLUTION property change with the old and new values.
	 * @param r new resolution of the curves
	 */
	@Override
	public void setResolution(double r) {
		if (r != pointSpacing) {
			double old = pointSpacing;
			this.pointSpacing = Math.max(MIN_RESOLUTION, r);	// don't go below minimum
			updateCurves();
			pcs.firePropertyChange(PROP_RESOLUTION, old, pointSpacing);
		}
	}
	
	/**
	 * Get the location of the dots
	 * @return location of the dots
	 */
	@Override
	public Location getLocation() {
		return dotLocation;
	}

	/**
	 * Add a property change listener
	 * @param listener
	 */
	@Override
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

	/**
	 * Remove the given property change listener
	 * @param listener
	 */
	@Override
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

	/**
	 * When there are two points, make them exactly vertical.
	 * This fires a PROP_MOVEPT property change with the new point.
	 * An error dialog is displayed if there are not two points.
	 */
	@Override
	public synchronized void set2PtsVertical() {
		if (dotCurve.getSize() != 2) {
			NotifyDescriptor d = new NotifyDescriptor.Message(
					"This only works for 2 points!",
					NotifyDescriptor.WARNING_MESSAGE);
			DialogDisplayer.getDefault().notify(d);
			return;
		}
		Dot d = (Dot) dotCurve.getPt(1);
		d.setPos(new Point2D.Double(dotCurve.getPt(0).getPos().x, d.getPos().y));
		updateCurves();
		pcs.firePropertyChange(PROP_MOVEPT, null, d);
	}

	/**
	 * When there are two points, make them exactly horizontal.
	 * This fires a PROP_MOVEPT property change with the new point.
	 * An error dialog is displayed if there are not two points.
	 */
	@Override
	public synchronized void set2PtsHorizontal() {
		if (dotCurve.getSize() != 2) {
			NotifyDescriptor d = new NotifyDescriptor.Message(
					"This only works for 2 points!",
					NotifyDescriptor.WARNING_MESSAGE);
			DialogDisplayer.getDefault().notify(d);
			return;
		}
		Dot d = (Dot) dotCurve.getPt(1);
		d.setPos(new Point2D.Double(d.getPos().x, dotCurve.getPt(0).getPos().y));
		updateCurves();
		pcs.firePropertyChange(PROP_MOVEPT, null, d);
	}

	/**
	 * Offset the curve vertically so that the top of the dotCurve is at zero.
	 * This fires a PROP_OFFSET property change.
	 * @return the distance moved
	 */
	@Override
	public synchronized double offsetTopZero() {
		double delta = dotCurve.getBoundingBox().max.y;
		dotCurve.offsetY(delta);					// move the dotCurve
		updateCurves();								// update the curves
		pcs.firePropertyChange(PROP_OFFSET, null, null);
		return delta;
	}

	/**
	 * Offset the curve vertically so that the bottom of the dotCurve is at zero.
	 * This fires a PROP_OFFSET property change.
	 * @return the distance moved
	 */
	@Override
	public synchronized double offsetBottomZero() {
		double delta = dotCurve.getBoundingBox().min.y;
		dotCurve.offsetY(delta);					// move the dotCurve
		updateCurves();								// update the curves
		pcs.firePropertyChange(PROP_OFFSET, null, null);
		return delta;
	}

	/**
	 * Invert the curve from top to bottom.
	 * Note that the order of the points is reversed so the bottom point is first
	 * and the top point is last.
	 */
	@Override
	public synchronized void invert() {
		dotCurve.invert();
		updateCurves();								// update the curves
		pcs.firePropertyChange(PROP_INVERT, null, null);
	}

	/**
	 * Write the coordinates of the digitized surface to a tab-delimited output file
	 * @param out output stream for writing
	 */
	@Override
	public void writeOutline(PrintWriter out) {
		Point2D.Double[] pts;
		double len;
		if (dotLocation.isInside()) {
			pts = insideCurve.getPoints();
			len = insideCurve.getLength();
		} else {
			pts = outsideCurve.getPoints();
			len = outsideCurve.getLength();
		}
		out.println("Length:" + "\t" + F3.format(len));
		out.println();
		out.println("X" + "\t" + "Z" + "\t" + "Diameter" + "\t" + "Circumference");
		for (int i = pts.length - 1; i >= 0; i--) {
			out.println(F3.format(pts[i].x) +
					"\t" + F3.format(pts[i].y) +
					"\t" + F3.format(Math.abs(2.0 * pts[i].x)) +
					"\t" + F3.format(Math.abs(2.0 * Math.PI * pts[i].x)));
		}
	}
	
	/**
	 * Read outline information from an xml file.
	 * This fires a PROP_XML property change.
	 * @param root root element of the xml file
	 * @param file File that the root element came from
	 */
	@Override
	public void readXML(Element root, File file) {
		clear(!FIRE_PROP_CHANGE);	// clear() but without firePropertyChange
		
		double version = Double.parseDouble(root.getAttribute("version"));	// Note: this won't work for 2.10, etc.
				
		NodeList nodes = root.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i) instanceof Element) {
				Element element = (Element) nodes.item(i);

				if (element.getTagName().equals("dimensions")) {		// prior to version 2.0
					thickness = Double.parseDouble(element.getAttribute("t"));
					pointSpacing = Double.parseDouble(element.getAttribute("pointSpacing"));
				}

				if (element.getTagName().equals("dotLocation")) {		// prior to version 2.0
					dotLocation = Location.valueOf(element.getTextContent());
				}

				if (element.getTagName().equals("dotCurve")) {
					if (version >= 2.0) {
						dotLocation = Location.valueOf(element.getAttribute("dotLocation"));
						thickness = Double.parseDouble(element.getAttribute("t"));
						pointSpacing = Double.parseDouble(element.getAttribute("pointSpacing"));
					}
					NodeList dotNodes = element.getChildNodes();
					for (int j = 0; j < dotNodes.getLength(); j++) {
						if (dotNodes.item(j) instanceof Element) {
							Element dotElement = (Element) dotNodes.item(j);
							if (dotElement.getTagName().equals("point")) {	// Read data points
								double x = Double.parseDouble(dotElement.getAttribute("x"));
								double z = Double.parseDouble(dotElement.getAttribute("z"));
								Pt pt = dotCurve.addPt(new Point2D.Double(x, z));	// Add points in order they were read
								pt.addPropertyChangeListener(this);
							}
						}
					}
					updateCurves();
				}
			}
		}		
		pcs.firePropertyChange(PROP_XML, null, file.getName());
	}
	
	/**
	 * Read outline information from an old BowlShape xml file.
	 * @param root root element of the xml file
	 * @param file File that the root element came from
	 */
	@Override
	public void readOldXML(Element root, File file) {
		clear(!FIRE_PROP_CHANGE);	// clear() but without firePropertyChange
		
		NodeList nodes = root.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i) instanceof Element) {
				Element element = (Element) nodes.item(i);
				if (element.getTagName().equals("curve")) {
					NodeList dotNodes = element.getChildNodes();
					for (int j = 0; j < dotNodes.getLength(); j++) {
						if (dotNodes.item(j) instanceof Element) {
							Element dotElement = (Element) dotNodes.item(j);
							if (dotElement.getTagName().equals("point")) {	// Read data points
								double x = Double.parseDouble(dotElement.getAttribute("x"));
								double z = Double.parseDouble(dotElement.getAttribute("y"));
								Pt pt = dotCurve.addPt(new Point2D.Double(x, z));	// Add points in order they were read
								pt.addPropertyChangeListener(this);
							}
						}
					}
					updateCurves();
				}
			}
		}	
	}

	/**
	 * Write "dotLocation", "dotCurve", and "safePath" information to an xml file.
	 * @param out output stream for writing
	 */
	@Override
	public void writeXML(PrintWriter out) {
		if (dotCurve.getSize() != 0) {
			out.println("  <dotCurve nPts='" + dotCurve.getSize() 
					+ "' dotLocation='" + dotLocation.toString() 
					+ "' t='" + F3.format(thickness) 
					+ "' pointSpacing='" + F3.format(pointSpacing)
					+ "'>");
			for (int i = 0; i < dotCurve.getSize(); i++) {
				double x = dotCurve.getPt(i).getPos().x;
				double z = dotCurve.getPt(i).getPos().y;
				out.println("    <point x='" + F4.format(x) + "' z='" + F4.format(z) + "'/>");
			}
			out.println("  </dotCurve>");
		}
	}

}
