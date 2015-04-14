package com.billooms.view3d;

import com.billooms.view3dmodel.Bowl;
import com.billooms.view3dmodel.GridAxis;
import java.io.File;
import javax.swing.*;
import java.awt.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.behaviors.vp.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.netbeans.spi.print.PrintPage;

/**
 * This panel holds the 3D canvas where the loaded image is displayer
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
public class DisplayPanel extends JPanel implements PrintPage {
	/** Default background color */
    public final static Color3f DEFAULT_BACK_COLOR = new Color3f(0.9f, 0.9f, 0.9f);

	private final int BOUNDSIZE = 100;				// larger than world
	private final Point3d USERPOSN = new Point3d(0, 6, 15); // initial user userPosn
	private final Point3d USERLOOK = new Point3d(0, 2, 0);	// initial user looking at this point
	private final Vector3f LIGHT1 = new Vector3f(-1.0f, -1.0f, -1.0f);	// left, down, backwards
	private final Vector3f LIGHT2 = new Vector3f(1.0f, -1.0f, 1.0f);	// right, down, forwards
	
	
	private Canvas3D canvas3D;
    private OffScreenCanvas3D offScreenCanvas3D;
	private SimpleUniverse su;
    private Point3d userPosn = new Point3d(USERPOSN);
    private Point3d userLook = new Point3d(USERLOOK);
	private BranchGroup sceneBG;
	private BoundingSphere bounds;					// for environment nodes
	private TransformGroup mouseTG;                 // TG for mouse rotation
	private MouseRotate myMouseRotate;
	private MouseTranslate myMouseTranslate;
	private MouseZoom myMouseZoom;
	private OrbitBehavior orbit;

	private Color3f backColor = DEFAULT_BACK_COLOR;
	private Switch gridSwitch;
	private Background background;
	private boolean showGrid = true;
	private boolean mouseControlsBowl = false;		// true: mouse moves bowl, false: mouse changes view

    /**
     * A panel holding a 3D canvas: the usual way of linking Java3d to Swing.
     * @param theBowl A Bowl object to be displayed.
     */
	public DisplayPanel(Bowl bowl) {
        initComponents();
        
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

		// create the on-screen canvas
		canvas3D = new Canvas3D(config);
		add("Center", canvas3D);
		canvas3D.setFocusable(true);     // give focus to the canvas
		canvas3D.requestFocus();

		// create a simple universe
		su = new SimpleUniverse(canvas3D);

		// create the main scene graph
		createSceneGraph(bowl);
		initUserPosition();				// set user's viewpoint
		orbitControls(canvas3D);		// controls for moving the viewpoint

		// create an off Screen Canvas
	    offScreenCanvas3D = new OffScreenCanvas3D(config, true);

		// set the offscreen to match the onscreen
	    Screen3D sOn = canvas3D.getScreen3D();
	    Screen3D sOff = offScreenCanvas3D.getScreen3D();
	    sOff.setSize(sOn.getSize());
	    sOff.setPhysicalScreenWidth(sOn.getPhysicalScreenWidth());
	    sOff.setPhysicalScreenHeight(sOn.getPhysicalScreenHeight());

		// attach the same view to the offscreen canvas
		su.getViewer().getView().addCanvas3D(offScreenCanvas3D);

		su.addBranchGraph(sceneBG);
	}

    /**
     * Initialize the scene.
     * @param theBowl The bowl object to be displayed
     */
	private void createSceneGraph(Bowl theBowl) {
		sceneBG = new BranchGroup();
		bounds = new BoundingSphere(new Point3d(0,0,0), BOUNDSIZE);

		lightScene();						// add the lights
		addBackground();					// add the sky
		
		gridSwitch = new Switch();
		gridSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
		gridSwitch.setWhichChild(Switch.CHILD_ALL);
		gridSwitch.addChild(new GridAxis().getBG());
		sceneBG.addChild(gridSwitch);		// add the grid & axis

		mouseTG = new TransformGroup();		// TG for mouse rotation
        mouseTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        mouseTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        mouseTG.addChild(theBowl.getBG());	// add the bowl to the TransformGroup
        sceneBG.addChild(mouseTG);

		myMouseRotate = new MouseRotate();
        myMouseRotate.setTransformGroup(mouseTG);
        myMouseRotate.setSchedulingBounds(new BoundingSphere());
		myMouseRotate.setEnable(mouseControlsBowl);
        sceneBG.addChild(myMouseRotate);

		myMouseTranslate = new MouseTranslate();
        myMouseTranslate.setTransformGroup(mouseTG);
        myMouseTranslate.setSchedulingBounds(new BoundingSphere());
		myMouseTranslate.setEnable(mouseControlsBowl);
        sceneBG.addChild(myMouseTranslate);

		myMouseZoom = new MouseZoom();
        myMouseZoom.setTransformGroup(mouseTG);
        myMouseZoom.setSchedulingBounds(new BoundingSphere());
		myMouseZoom.setEnable(mouseControlsBowl);
        sceneBG.addChild(myMouseZoom);

		sceneBG.compile();					// fix the scene
	}

    /**
     * One ambient light, 2 directional lights.
     */
	private void lightScene() {
		Color3f white = new Color3f(Color.WHITE);

		// Set up the ambient light
		AmbientLight ambientLightNode = new AmbientLight(white);
		ambientLightNode.setInfluencingBounds(bounds);
		sceneBG.addChild(ambientLightNode);

		// Set up the directional lights
		DirectionalLight light1 = new DirectionalLight(white, LIGHT1);
		light1.setInfluencingBounds(bounds);
		sceneBG.addChild(light1);

		DirectionalLight light2 = new DirectionalLight(white, LIGHT2);
		light2.setInfluencingBounds(bounds);
		sceneBG.addChild(light2);
	}

    /**
     * Add background color.
     */
	private void addBackground() {
		background = new Background();
		background.setCapability(Background.ALLOW_COLOR_WRITE);
		background.setCapability(Background.ALLOW_GEOMETRY_WRITE);
		background.setApplicationBounds(bounds);
		background.setColor(backColor);    // background colour
		sceneBG.addChild(background);
	}

    /**
     * Set the user's initial viewpoint using userLook().
     */
	private void initUserPosition() {
		ViewingPlatform vp = su.getViewingPlatform();
		TransformGroup steerTG = vp.getViewPlatformTransform();

		Transform3D t3d = new Transform3D();
		steerTG.getTransform(t3d);

		// args are: viewer posn, where looking, up direction
		t3d.lookAt(userPosn, userLook, new Vector3d(0,1,0));
		t3d.invert();

		steerTG.setTransform(t3d);
	}

    /**
     * OrbitBehaviour allows the user to rotate around the scene, and to zoom in and out.
     * @param c the 3D canvas
     */
	private void orbitControls(Canvas3D c) {
		orbit = new OrbitBehavior(c, OrbitBehavior.REVERSE_ALL);
		orbit.setSchedulingBounds(bounds);
		orbit.setEnable(!mouseControlsBowl);

		ViewingPlatform vp = su.getViewingPlatform();
		vp.setViewPlatformBehavior(orbit);
	}

	/**
	 * Get the background color.
	 * @return background color
	 */
	public Color getBackgroundColor() {
		return new Color(backColor.x, backColor.y, backColor.z);
	}

	/**
	 * Set the background color.
	 * @param c new background color
	 */
	public void setBackgroundColor(Color c) {
		if (c != null) {
            this.backColor = new Color3f(c);		// Remember selected color for next time.
			background.setColor(backColor);
		}
	}

    /**
     * Restore the view to the original setting scaled by the bowl size.
     */
    public void restoreView() {
        initUserPosition();
		mouseTG.setTransform(new Transform3D());
    }

    /**
     * Set so the mouse rotates the Bowl, not the view point.
     */
	public void setMouseControlsBowl() {	// mouse controls the bowl
		mouseControlsBowl = true;
		myMouseRotate.setEnable(mouseControlsBowl);
		myMouseTranslate.setEnable(mouseControlsBowl);
		myMouseZoom.setEnable(mouseControlsBowl);
		orbit.setEnable(!mouseControlsBowl);
	}

    /**
     * Set so the mouse rotates the view point, the Bowl remains stationary.
     */
	public void setMouseControlsView() {	// mouse controls the view
		mouseControlsBowl = false;
		myMouseRotate.setEnable(mouseControlsBowl);
		myMouseTranslate.setEnable(mouseControlsBowl);
		myMouseZoom.setEnable(mouseControlsBowl);
		orbit.setEnable(!mouseControlsBowl);
	}

    /**
     * Turn the grid display on or off.
     * @param show true=show, false=don't show
     */
	public void showGrid(boolean show) {
        showGrid = show;
        if (showGrid) {
            gridSwitch.setWhichChild(Switch.CHILD_ALL);
        } else {
            gridSwitch.setWhichChild(Switch.CHILD_NONE);
        }
    }

	/**
	 * Is the grid showing?
	 * @return true=showing, false=not showing
	 */
	public boolean isGridShowing() {
		return showGrid;
	}

	@Override
	public void print(Graphics g) {
		offScreenCanvas3D.setOffScreenLocation(canvas3D.getLocationOnScreen());
		BufferedImage bImage= offScreenCanvas3D.doRender(canvas3D.getWidth(), canvas3D.getHeight());

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform t2d = new AffineTransform();
//		double xscale  = pf.getImageableWidth() / (double)bImage.getWidth();
//		double yscale  = pf.getImageableHeight() / (double)bImage.getHeight();
//		double scale = Math.min(xscale, yscale);
//		scale = Math.min(1.0, scale);		// don't enlarge a small window
//		t2d.scale(scale, scale);
		g2d.drawImage(bImage, t2d, this);
	}

    /**
     * Save the current 3D view to a file
     * @param file File to save the view
     * @param format Format of the file (i.e. png, jpeg, etc).
     */
    public void writeGraphicFile(File file, String format) {
		offScreenCanvas3D.setOffScreenLocation(canvas3D.getLocationOnScreen());
		BufferedImage bufImage= offScreenCanvas3D.doRender(canvas3D.getWidth(), canvas3D.getHeight());

		try {
			boolean hasFormat = ImageIO.write(bufImage, format, file);
			if (!hasFormat)
				throw new Exception(format + " format is not available.");
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error occured while trying to save the image:\n" + e, "Save Graphic Error" , JOptionPane.ERROR_MESSAGE);
		}
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
