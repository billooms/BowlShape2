package com.billooms.drawables;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DecimalFormat;

/**
 * Abstract definition of a drawable point defined by inch(cm) position.
 * Extend this to define points with a particular visible shape (i.e. Dot, Plus, etc).
 * @author Bill Ooms. Copyright 2011 Studio of Bill Ooms. All rights reserved.
 */
public abstract class Pt implements Drawable {
	/** All property change names start with this prefix */
	public final static String PROP_PREFIX = "Pt";
	/** Property name used when moving a point */
	public final static String PROP_MOVE = PROP_PREFIX + "Move";
	/** Property name used when dragging a point */
	public final static String PROP_DRAG = PROP_PREFIX + "Drag";
	
	private final static DecimalFormat F3 = new DecimalFormat("0.000");
	private final static int PT_SIZE = 6;	// default pixel size of a drawn point (Dot, Plus, SquarePt, etc)
	private final static Color DEFAULT_COLOR = Color.BLACK;
	/** Position of the object in inches (or cm) */
	protected Point2D.Double pos;
	/** Color of the object */
	protected Color color = DEFAULT_COLOR;
	/** Visible (i.e. drawn) or not.  */
	protected boolean visible = true;
	/** Fill the shapes (false = hollow shapes) */
	protected boolean fill = true;
	/** Size of the object in pixels */
	protected int ptSize = PT_SIZE;
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	/**
	 * A drawable point defined by inch position
	 * @param pos point position in inches
	 * @param c Color
	 */
	public Pt(Point2D.Double pos, Color c) {
		this.pos = pos;
		this.color = c;
	}

	/**
	 * A drawable point defined by inch position, default color
	 * @param pos point position in inches
	 */
	public Pt(Point2D.Double pos) {
		this.pos = pos;
	}

	/**
	 * String representation of this object
	 * @return string representation
	 */
	@Override
	public String toString() {
		return "Point x=" + F3.format(pos.x) + " y=" + F3.format(pos.y);
	}

	/**
	 * Get the point position in inches
	 * @return point position in inches
	 */
	public Point2D.Double getPos() {
		return pos;
	}

	/**
	 * Move the point to a new point defined in inches.
	 * This fires a PROP_MOVE property change with the new points.
	 * @param pos new point in inches
	 */
	public void setPos(Point2D.Double pos) {
		this.pos = pos;
		pcs.firePropertyChange(PROP_MOVE, null, pos);
	}

	/**
	 * Drag the point to a new point defined in inches.
	 * This is used to differentiate between a dragging movement and a final movement.
	 * This fires a PROP_DRAG property change with the new points.
	 * @param pos new point in inches
	 */
	public void drag(Point2D.Double pos) {
		this.pos = pos;
		pcs.firePropertyChange(PROP_DRAG, null, pos);
	}

	/**
	 * Get the x-position.
	 * @return x-position
	 */
	public double getX() {
		return pos.x;
	}

	/**
	 * Set the x-position.
	 * This fires a PROP_MOVE property change with the new points.
	 * @param x new x-position
	 */
	public void setX(double x) {
		this.pos.x = x;
		pcs.firePropertyChange(PROP_MOVE, null, pos);
	}

	/**
	 * Get the y-position.
	 * @return y-position
	 */
	public double getY() {
		return pos.y;
	}

	/**
	 * Set the y-position.
	 * This fires a PROP_MOVE property change with the new points.
	 * @param y new y-position
	 */
	public void setY(double y) {
		this.pos.y = y;
		pcs.firePropertyChange(PROP_MOVE, null, pos);
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
	 * Get the fill parameter
	 * @return fill
	 */
	public boolean getFill() {
		return fill;
	}

	/**
	 * Set the fill of the object
	 * @param f true=filled; false=hollow
	 */
	public void setFill(boolean f) {
		fill = f;
	}

	/**
	 * Get the size of the point in inches
	 * @param g2d Graphics2D
	 * @return size of the point in inches
	 */
	public double getPtSize(Graphics2D g2d) {
		return (double) ptSize / g2d.getTransform().getScaleX();
	}

	/**
	 * Set the point size (diameter) in pixels
	 * @param s Size in pixels
	 */
	public void setPtSize(int s) {
		this.ptSize = s;
	}

	/**
	 * Paint the object.
	 * This sets the stroke to solid 1 pixel and sets the color 
	 * whether the object is visible or not
	 * @param g2d Graphics2D
	 */
	@Override
	public void paint(Graphics2D g2d) {	// This is further customized by extensions
		float scale = (float) g2d.getTransform().getScaleX();
		g2d.setStroke(new BasicStroke(1.0f / scale));
		g2d.setColor(color);
		if (visible) {
			g2d.draw(new Line2D.Double(pos, pos));
		}
	}

	/**
	 * Get the separation from this point to the specified point
	 * @param p point in inches
	 * @return distance in inches
	 */
	public double separation(Point2D.Double p) {
		return pos.distance(p);
	}

	/**
	 * Invert the point around the y-axis (i.e. x = x; y = -y)
	 */
	public void invert() {
		pos.y = -pos.y;
	}

	/**
	 * Offset in the y-direction by subtracting the given value from y-coordinate
	 * @param deltaY offset amount (in inches);
	 */
	public void offsetY(double deltaY) {
		pos.y = pos.y - deltaY;
	}

	/**
	 * Add a property change listener
	 * @param listener
	 */
	public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	/**
	 * Remove the given property change listener
	 * @param listener
	 */
	public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}
}
