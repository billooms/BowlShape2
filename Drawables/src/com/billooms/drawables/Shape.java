package com.billooms.drawables;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Abstract definition of a shape that can be drawn.
 * Extend this to define lines with particular characteristics (i.e. FittedCurve, PiecedLine, etc).
 * @author Bill Ooms. Copyright 2011 Studio of Bill Ooms. All rights reserved.
 */
public abstract class Shape implements Drawable {

	/** Solid line stroke */
	public final static BasicStroke SOLID_LINE = new BasicStroke(1.0f);
	
	/** Light dotted stroke */
	public final static BasicStroke LIGHT_DOT = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10, new float[]{1, 5}, 0);
	
	private final static BasicStroke DEFAULT_STROKE = SOLID_LINE;
	private final static Color DEFAULT_COLOR = Color.BLACK;
	
	/** Color of the object */
	protected Color color = DEFAULT_COLOR;
	/** Visible (i.e. drawn) or not. */
	protected boolean visible = true;		// always visible unless specifically changed
	/** Stroke used when drawing the shape */
	protected BasicStroke stroke = DEFAULT_STROKE;

	/**
	 * A drawable shape.
	 * @param c Color
	 * @param s BasicStroke
	 */
	public Shape(Color c, BasicStroke s) {
		this.color = c;
		this.stroke = s;
	}

	/**
	 * A drawable shape (default stroke).
	 * @param c Color
	 */
	public Shape(Color c) {
		this.color = c;
	}

	/**
	 * A drawable shape (default stroke and color).
	 */
	public Shape() {}

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
	 * Get the stroke for this shape.
	 * @return stroke
	 */
	public BasicStroke getStroke() {
		return stroke;
	}

	/**
	 * Set the stroke for the shape.
	 * @param s BasicStroke
	 */
	public void setStroke(BasicStroke s) {
		stroke = s;
	}

	/**
	 * Paint the shape.
     * @param g2d Graphics2D
	 */
	@Override
	public void paint(Graphics2D g2d) {	// This is further customized by extensions
		if (visible) {
			float scale = (float) g2d.getTransform().getScaleX();
			float array[] = stroke.getDashArray();
			if ((array == null) || (array.length == 0)) {
				g2d.setStroke(new BasicStroke(1.0f / scale));
			} else {
				for (float f : array) {
					f = f / scale;
				}
				g2d.setStroke(new BasicStroke(1.0f / scale, stroke.getEndCap(), stroke.getLineJoin(), 1.0f, array, stroke.getDashPhase() / scale));
			}
			g2d.setColor(color);
		}
	}
}
