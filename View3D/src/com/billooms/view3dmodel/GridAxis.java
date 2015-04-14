package com.billooms.view3dmodel;

import java.awt.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.Text2D;
import javax.vecmath.*;

/**
 * Make a BranchGroup for a grid and 3D axis.
 * Note: Axis definitions for Java3D are +X to the right, +Y is up, +Z is toward you.
 * This is NOT the same as the CNC lathe axis definitions.
 * @author Bill Ooms. Copyright 2011 Studio of Bill Ooms. All rights reserved.
 */
public class GridAxis {
    private final static int FLOOR_LEN = 12;  // should be even
    private final static int NUMPTS = 4 * (FLOOR_LEN + 1);
    private final static Color3b GRID_COLOR = new Color3b(Color.BLACK);
    private final static Color3f GRID_X_COLOR = new Color3f(Color.RED);
    private final static Color3f GRID_Y_COLOR = new Color3f(Color.GREEN);
    private final static Color3f GRID_Z_COLOR = new Color3f(Color.BLUE);
	
    private BranchGroup floorBG;

    /**
     * Creates a BranchGroup for display of a grid and 3D axis.
     */
    public GridAxis() {
        floorBG = new BranchGroup();
        floorBG.addChild(new GridShape());
        labelAxes();
    }

    /**
     * Place numbers along the X, Y and Z-axes at the integer positions.
     */
    private void labelAxes() {
        Vector3d pt = new Vector3d();
        for (int i = -FLOOR_LEN/2; i <= FLOOR_LEN/2; i++) {
            pt.x = i;
            floorBG.addChild(makeText(pt, Integer.toString(i), GRID_X_COLOR));   // along x-axis
        }
        pt.x = 0;
        for (int i = -FLOOR_LEN/2; i <= FLOOR_LEN/2; i++) {
            pt.z = i;
            floorBG.addChild(makeText(pt, Integer.toString(i), GRID_Z_COLOR));   // along z-axis
        }
        pt.z = 0;
        for (int i = 0; i <= FLOOR_LEN/2; i++) {	// only up (not down)
            pt.y = i;
            floorBG.addChild(makeText(pt, Integer.toString(i), GRID_Y_COLOR));   // along y-axis
        }
    }

    /**
     * Create a Text2D object at the specified vertex.
     * @param vertex location in 3D
     * @param text text to place
     * @param color color of the text
     * @return
     */
    private TransformGroup makeText(Vector3d vertex, String text, Color3f color) {
        Text2D message = new Text2D(text, color, "SansSerif", 48, Font.BOLD);	// 36 point bold Sans Serif
        TransformGroup tg = new TransformGroup();
        Transform3D t3d = new Transform3D();
        t3d.setTranslation(vertex);
        tg.setTransform(t3d);
        tg.addChild(message);
        return tg;
    }

    /**
     * Retrieve the BranchGroup.
     * @return the BranchGroup with the grid and axis
     */
    public BranchGroup getBG() {
        return floorBG;
    }


    
    /**
     * Nested class for the grid .
     */
    private class GridShape extends Shape3D {

        /**
         * Construct a gridwork of lines.
         */
        public GridShape() {
            Point3d[] coords = new Point3d[NUMPTS];
            Color3b[] colors = new Color3b[NUMPTS];
            int i = 0;
            for (int xy = -FLOOR_LEN/2; xy <= FLOOR_LEN/2; xy++) {
                coords[i] = new Point3d(xy, 0, -FLOOR_LEN/2);
                coords[i + 1] = new Point3d(xy, 0, FLOOR_LEN/2);
                coords[i + 2] = new Point3d(-FLOOR_LEN/2, 0, xy);
                coords[i + 3] = new Point3d(FLOOR_LEN/2, 0, xy);
                colors[i] = GRID_COLOR;
                colors[i + 1] = GRID_COLOR;
                colors[i + 2] = GRID_COLOR;
                colors[i + 3] = GRID_COLOR;
                i = i + 4;
            }
            LineArray grid = new LineArray(NUMPTS, LineArray.COORDINATES | LineArray.COLOR_3);
            grid.setCoordinates(0, coords);
            grid.setColors(0, colors);
            setGeometry(grid);
        }
    }

} 

