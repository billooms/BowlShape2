
package com.billooms.outline.api;

import com.billooms.drawables.BoundingBox;
import com.billooms.drawables.Curve;
import com.billooms.drawables.Dot;
import com.billooms.drawables.FittedCurve;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.PrintWriter;
import org.w3c.dom.Element;

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
public interface Outline {
	/** All Outline property change names start with this prefix */
	String PROP_PREFIX = "Outline";
	
	/** Property name used when adding a point */
	String PROP_ADDPT = PROP_PREFIX + "AddPoint";
	/** Property name used when moving a point */
	String PROP_MOVEPT = PROP_PREFIX + "MovePoint";
	/** Property name used when deleting a point */
	String PROP_DELETEPT = PROP_PREFIX + "DeletePoint";
	/** Property name used when clearing all points */
	String PROP_CLEAR = PROP_PREFIX + "Clear";
	/** Property name used when changing resolution */
	String PROP_THICKNESS = PROP_PREFIX + "Thickness";
	/** Property name used when changing thickness */
	String PROP_RESOLUTION = PROP_PREFIX + "Resolution";
	/** Property name used when offsetting the curve */
	String PROP_OFFSET = PROP_PREFIX + "Offset";
	/** Property name used when inverting the curve */
	String PROP_INVERT = PROP_PREFIX + "Invert";
	/** Property name used when setting a new cutter */
	String PROP_CUTTER = PROP_PREFIX + "Cutter";
	/** Property name used when reading an XML file */
	String PROP_XML = PROP_PREFIX + "XML";
	
	/** Default thickness of the shape (currently set to 0.100) */
	double DEFAULT_THICKNESS = 0.100;
	/** Default resolution of the curves (currently set to 0.020) */
    double DEFAULT_RESOLUTION = 0.020;
	/** Minimum permissible curve resolution (currently set to 0.001) */
    double MIN_RESOLUTION = 0.001;
	/** Default location for dots defining the curve */
	Location DEFAULT_DOT_LOCATION = Location.FRONT_OUTSIDE;
	/** Color of the dotCurve (currently set to LIGHT_GRAY) */
    Color DOT_CURVE_COLOR = Color.LIGHT_GRAY;	// needs to be public for OutlineEditPanel

	/** Location of the cutter relative to the work piece. */
	public static enum Location {
		/** Cutter is inside the shape, in front of center */
		FRONT_INSIDE("Front, Inside"),
		/** Cutter is inside the shape, behind center */
        FRONT_OUTSIDE("Front, Outside"),
		/** Cutter is outside the shape, in front of center */
		BACK_INSIDE("Back, Inside"),
		/** Cutter is outside the shape, in front of center */
        BACK_OUTSIDE("Back, Outside");

		/** Display name */
		private String name;

		Location(String name) {
			this.name = name;
		}
		
		/**
		 * Get the display name
		 * @return display name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Is the cutter on the inside?
		 * @return true=inside; false=outside
		 */
        public boolean isInside() {
            switch (this) {
                case FRONT_INSIDE:
                case BACK_INSIDE:
                    return true;
                default:
                    return false;
            }
        }

		/**
		 * Is the cutter on the front?
		 * @return true=front; false=back
		 */
        public boolean isFront() {
            switch (this) {
                case FRONT_INSIDE:
                case FRONT_OUTSIDE:
                    return true;
                default:
                    return false;
            }
        }

		/**
		 * Is the cutter on the FRONT_INSIDE or BACK_OUTSIDE?
		 * @return true=FRONT_INSIDE or BACK_OUTSIDE
		 */
		public boolean isFrontInOrBackOut() {
            switch (this) {
                case FRONT_INSIDE:
                case BACK_OUTSIDE:
                    return true;
                default:
                    return false;
            }
		}
	}
	
    /**
     * Paint the object.
     * @param g2d Graphics2D
     */
	void paint(Graphics2D g2d);

	/**
	 * Get the dotCurve
	 * @return dotCurve
	 */
	FittedCurve getDotCurve();

	/**
	 * Get the insideCurve
	 * @return insideCurve
	 */
	Curve getInsideCurve();

	/**
	 * Get the outsideCurve
	 * @return outsideCurve
	 */
	Curve getOutsideCurve();

	/**
	 * Get the number of points defining the curves
	 * @return number of points
	 */
	int getNumPts();

	/**
	 * Get the total length of the inside curve
	 * @return total length of the inside curve
	 */
	double getInsideLength();

	/**
	 * Get the total length of the outside curve
	 * @return total length of the outside curve
	 */
	double getOutsideLength();

	/**
	 * Add an outline point.
	 * Points are in sorted order from bottom (first point) to top (last point).
	 * This fires a PROP_ADDPT property change with the new point.
	 * @param p new point to add
	 */
	void addPt(Point2D.Double p);

	/**
	 * Delete the given point.
	 * This fires a PROP_DELETEPT property change with the old point.
	 * @param pt point to delete
	 */
	void deletePt(Dot pt);

	/**
	 * Clear all points.
	 * This fires a PROP_CLEAR property change.
	 */
	void clear();

	/**
	 * Find the closest outline point to the given point
	 * which is within a given distance.
	 * @param pt given point
	 * @param dist distance to search
	 * @return closest point (or null if no point with in the given distance)
	 */
	Dot getClosestPt(Point2D.Double pt, double dist);

	/**
	 * Get the bounding box for all of the curves
	 * @return bounding box of all curves
	 */
	BoundingBox getBoundingBox();

	/**
	 * Get the thickness of the shape.
	 * @return thickness of the shape
	 */
	double getThickness();

	/**
	 * Set the thickness of the shape.
	 * This fires a PROP_THICKNESS property change with the old and new values.
	 * @param t new thickness of the shape
	 */
	void setThickness(double t);

	/**
	 * Get the resolution of the curves.
	 * @return resolution
	 */
	double getResolution();

	/**
	 * Set the resolution of the curves.
	 * This fires a PROP_RESOLUTION property change with the old and new values.
	 * @param r new resolution of the curves
	 */
	void setResolution(double r);
	
	/**
	 * Get the location of the dots
	 * @return location of the dots
	 */
	Location getLocation();

	/**
	 * Add a property change listener
	 * @param listener
	 */
    void addPropertyChangeListener(PropertyChangeListener listener);

	/**
	 * Remove the given property change listener
	 * @param listener
	 */
    void removePropertyChangeListener(PropertyChangeListener listener);

	/**
	 * When there are two points, make them exactly vertical.
	 * This fires a PROP_MOVEPT property change with the new point.
	 * An error dialog is displayed if there are not two points.
	 */
	void set2PtsVertical();

	/**
	 * When there are two points, make them exactly horizontal.
	 * This fires a PROP_MOVEPT property change with the new point.
	 * An error dialog is displayed if there are not two points.
	 */
	void set2PtsHorizontal();

	/**
	 * Offset the curve vertically so that the top of the dotCurve is at zero.
	 * This fires a PROP_OFFSET property change.
	 * @return the distance moved
	 */
	double offsetTopZero();

	/**
	 * Offset the curve vertically so that the bottom of the dotCurve is at zero.
	 * This fires a PROP_OFFSET property change.
	 * @return the distance moved
	 */
	double offsetBottomZero();

	/**
	 * Invert the curve from top to bottom.
	 * Note that the order of the points is reversed so the bottom point is first
	 * and the top point is last.
	 */
	void invert();

	/**
	 * Write the coordinates of the digitized surface to a tab-delimited output file
	 * @param out output stream for writing
	 */
	void writeOutline(PrintWriter out);
	
	/**
	 * Read outline information from an xml file.
	 * This fires a PROP_XML property change.
	 * @param root root element of the xml file
	 * @param file File that the root element came from
	 */
	void readXML(Element root, File file);

	/**
	 * Write "dotLocation", "dotCurve", and "safePath" information to an xml file.
	 * @param out output stream for writing
	 */
	void writeXML(PrintWriter out);
	
	/**
	 * Read outline information from an old BowlShape xml file.
	 * @param root root element of the xml file
	 * @param file File that the root element came from
	 */
	void readOldXML(Element root, File file);
}
