package com.billooms.drawables;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Drawable text defined by inch(cm) location
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
public class Text extends Pt {

	/** Location of the origin of the text */
	public enum Justify {
		/** Bottom, Left */
		BOT_LEFT,
		/** Bottom, Center */
		BOT_CENTER,
		/** Bottom, Right */
		BOT_RIGHT,
		/** Left (center) */
		LEFT,
		/** Center */
		CENTER,
		/** Right (center) */
		RIGHT,
		/** Top, Left */
		TOP_LEFT,
		/** Top, Center */
		TOP_CENTER,
		/** Top, Right */
		TOP_RIGHT
	};
	
	private final static Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 12);
	
	private String str;
	private Justify justify = Justify.CENTER;
	private Font font = DEFAULT_FONT;

	/**
	 * Drawable text defined by inch location and color
	 * @param pos text location in inches
	 * @param s String
	 * @param c Color
	 * @param f Font
	 * @param j Justification
	 */
	public Text(Point2D.Double pos, String s, Color c, Font f, Justify j) {
		super(pos, c);
		this.str = s;
		this.font = f;
		this.justify = j;
	}

	/**
	 * Drawable text defined by inch location and color
	 * @param pos text location in inches
	 * @param s String
	 * @param c Color
	 */
	public Text(Point2D.Double pos, String s, Color c) {
		super(pos, c);
		this.str = s;
	}

	/**
	 * Drawable text defined by inch location (default color)
	 * @param pos text location in inches
	 * @param s String
	 */
	public Text(Point2D.Double pos, String s) {
		super(pos);
		this.str = s;
	}

	/**
	 * Get the text string
	 * @return text string
	 */
	public String getText() {
		return str;
	}

	/**
	 * Set the text to a new string
	 * @param s text string
	 */
	public void setText(String s) {
		this.str = s;
	}

	/**
	 * Get the justification of the text
	 * @return one of LEFT, CENTER, RIGHT
	 */
	public Justify getJustify() {
		return justify;
	}

	/**
	 * Set the justification for the text
	 * @param j one of LEFT, CENTER, RIGHT
	 */
	public void setJustify(Justify j) {
		this.justify = j;
	}

	/**
	 * Get the font used for this text
	 * @return font
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * Set the font for this text
	 * @param f font
	 */
	public void setFont(Font f) {
		this.font = f;
	}

	/**
	 * Paint the object
	 * Note: this works in pixels because else the text is REALLY BIG!
	 * @param g2d Graphics2D
	 */
	@Override
	public void paint(Graphics2D g2d) {
		super.paint(g2d);	// sets color and stroke
		if (visible) {
			AffineTransform saveXform = g2d.getTransform();	// save for later restoration
			g2d.setTransform(new AffineTransform());		// work directly in pixels

			Point pix = scaleInchToPix(saveXform);			// location of text in pixels

			g2d.setFont(font);
			FontMetrics fm = g2d.getFontMetrics(font);
			switch (justify) {
				case BOT_LEFT:
					g2d.drawString(str, pix.x, pix.y);
					break;
				case BOT_CENTER:
					g2d.drawString(str, pix.x - fm.stringWidth(str) / 2, pix.y);
					break;
				case BOT_RIGHT:
					g2d.drawString(str, pix.x - fm.stringWidth(str), pix.y);
					break;
				case LEFT:
					g2d.drawString(str, pix.x, pix.y + fm.getAscent() / 2);
					break;
				case CENTER:
					g2d.drawString(str, pix.x - fm.stringWidth(str) / 2, pix.y + fm.getAscent() / 2);
					break;
				case RIGHT:
					g2d.drawString(str, pix.x - fm.stringWidth(str), pix.y + fm.getAscent() / 2);
					break;
				case TOP_LEFT:
					g2d.drawString(str, pix.x, pix.y + fm.getAscent());
					break;
				case TOP_CENTER:
					g2d.drawString(str, pix.x - fm.stringWidth(str) / 2, pix.y + fm.getAscent());
					break;
				case TOP_RIGHT:
					g2d.drawString(str, pix.x - fm.stringWidth(str), pix.y + fm.getAscent());
					break;
			}

			g2d.setTransform(saveXform);					// Restore transform
		}
	}

	/**
	 * Determine the location of the object in pixels for a given transform
	 * @param xform Transform
	 * @return location in pixels
	 */
	private Point scaleInchToPix(AffineTransform xform) {
		double dpi = xform.getScaleX();
		double zeroX = xform.getTranslateX();
		double zeroY = xform.getTranslateY();
		return new Point((int) zeroX + (int) (pos.x * dpi), (int) zeroY - (int) (pos.y * dpi));
	}
}
