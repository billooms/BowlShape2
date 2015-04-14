package com.billooms.view3dmodel;

import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;
import java.awt.geom.Point2D;

/**
 * Creates a lathe shape from a given array of points representing the profile.
 * @author Bill Ooms. Copyright 2011 Studio of Bill Ooms. All rights reserved.
 */
public class LatheShape3D extends Shape3D {
    private final static int NSECT = 36;	// the number of radial sections (every 10 degrees)
    
	private int numpts;						// total number of points in the shape

    /**
     * Creates a lathe shape from a given array of points representing the profile.
     * @param pts An array of Point2D which defines the outline. First point is the bottom.
     * @param mat Material to define the appearance
     */
    public LatheShape3D(Point2D.Double[] pts, Material mat) {
        setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);

        // Create appearance
        Appearance app = new Appearance();
        PolygonAttributes pa = new PolygonAttributes();
//		pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);		// wire frame only
        pa.setCullFace(PolygonAttributes.CULL_NONE);			// so can see the Ribbon from both sides
        pa.setBackFaceNormalFlip(true);							// so that both sides have reflection
        app.setPolygonAttributes(pa);
        app.setMaterial(mat);
        setAppearance(app);

		makeGeom(pts);		// Make the geometry of the shape
    }

    /**
     * Create the geometry of the shape
     * @param pts An array of Point2d which defines the outline. First point is the bottom.
     */
    protected final void makeGeom(Point2D.Double[] pts) {
        numpts = (pts.length-1) * (2*NSECT + 2) + 3*NSECT;

        // Create coordinates
        Point3d[] coords = makeCoords(pts);

        // Create geometry
        int stripCounts[] = {numpts};				// points are in one strip of numpts
        GeometryInfo gi = new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
        gi.setCoordinates(coords);
        gi.setStripCounts(stripCounts);
        NormalGenerator ng = new NormalGenerator();
        ng.generateNormals(gi);
        setGeometry(gi.getGeometryArray());
    }

    /**
     * Make the coordinates in 3D space
     * @param pts An array of Point2d which defines the outline. First point is the bottom.
     * @return an array of 3D points
     */
    private Point3d[] makeCoords(Point2D.Double[] pts) {
        double phi;
        double dPhi = 2 * Math.PI / NSECT;				// angle increment in radians
        Point3d[] coords = new Point3d[numpts];

        int idx = 0;
        double botRadius = pts[0].x;					// first point is the bottom
        double botHeight = pts[0].y;
        for (int j = 0; j < NSECT; j++) {               // make the bottom circle
            phi = j * dPhi;
            coords[idx] = new Point3d(0.0, botHeight, 0.0);
            coords[idx + 1] = new Point3d(botRadius * Math.cos(phi), botHeight, botRadius * Math.sin(phi));
            coords[idx + 2] = new Point3d(botRadius * Math.cos(phi + dPhi), botHeight, botRadius * Math.sin(phi + dPhi));
            idx = idx + 3;
        }
        for (int i = 0; i < pts.length - 1; i++) {
            for (int j = 0; j <= NSECT; j++) {
                phi = j * dPhi;
                coords[idx] = new Point3d(pts[i].x * Math.cos(phi), pts[i].y, pts[i].x * Math.sin(phi));
                coords[idx + 1] = new Point3d(pts[i + 1].x * Math.cos(phi), pts[i + 1].y, pts[i + 1].x * Math.sin(phi));
                idx = idx + 2;
            }
        }
        return coords;
    }
}
