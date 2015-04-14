package com.billooms.view3dmodel;

import com.billooms.outline.api.Outline;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.vecmath.*;
import javax.media.j3d.*;
import org.openide.util.Lookup;

/**
 * A Bowl is a BranchGroup for 3D display
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
public class Bowl implements PropertyChangeListener {
	public final static int DEFAULT_SECTORS = 360;
	public final static Color DEFAULT_BOWL_COLOR = new Color(186, 99, 18);

	private Material mat;			// material used for all of the bowl
	private Color3f ambientColor = new Color3f(0.2f, 0.2f, 0.2f);		// from ambient light source
	private Color3f emissiveColor = new Color3f(0.0f, 0.0f, 0.0f);		// glow in the dark color
	private Color3f diffuseColor = new Color3f(DEFAULT_BOWL_COLOR);     // main shading color
	private Color3f specularColor = new Color3f(1.0f, 1.0f, 1.0f);		// for shiny highlights
	

	private Outline outline = Lookup.getDefault().lookup(Outline.class);	// the outline that controls the shape of the bowl

	/**
	 *							bowlBG
	 * -----------------------------------------------------------
	 *	inOutBG				spiralBG		surfaceBG		circleBG
	 *	insideShape		(BGs for spirals)	surfaceShape (BGs for circles)
	 *	outsideShape
	 */
	private BranchGroup bowlBG;
		private BranchGroup inOutBG;
			private LatheShape3D insideShape = null;
			private LatheShape3D outsideShape = null;
//		private BranchGroup spiralBG;
//		    ArrayList<BranchGroup>;
//		private BranchGroup surfaceBG;
//			private SurfaceShape3D surfaceShape = null;
//		private BranchGroup circleBG;
//		    ArrayList<BranchGroup>;

	/**
     * A Bowl is a BranchGroup for 3D display.
	 */
	public Bowl() {
		mat = new Material(ambientColor, emissiveColor, diffuseColor, specularColor, 75.0f);
		mat.setCapability(Material.ALLOW_COMPONENT_WRITE);
		mat.setLightingEnable(true);

		bowlBG = new BranchGroup();
		bowlBG.setCapability(BranchGroup.ALLOW_CHILDREN_READ);		// Allow reading in real-time
		bowlBG.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);		// Allow changes in real-time
		bowlBG.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);	// Allow additions in real-time
		bowlBG.setCapability(BranchGroup.ALLOW_DETACH);				// Allow deleting in real-time

		makeInOut();
	}

    /**
     * Get the BranchGroup.
     * @return the BranchGroup
     */
	public BranchGroup getBG() {					// return the BranchGroup of this bowl
		return bowlBG;
	}

	/**
	 * Get the diffuse color of the bowl.
	 * @return diffuse color of the bowl
	 */
	public Color getColor() {
		return new Color(diffuseColor.x, diffuseColor.y, diffuseColor.z);
	}

	/**
	 * Set the diffuse color of the bowl.
	 * @param c new diffuse color of the bowl
	 */
	public void setColor(Color c) {
		if (c != null) {
            diffuseColor = new Color3f(c);			// Remember selected color for next time.
			mat.setDiffuseColor(diffuseColor);		// diffuse colour
		}
	}

    /**
     * Make the inside and outside bowl shape from curves.
     */
	private void makeInOut() {
		if ((outline.getInsideCurve().getSize() == 0) || 
			(outline.getOutsideCurve().getSize() == 0)) {	// This is in case the last dot was just deleted
			deleteInOut();
			return;
		}
		if (insideShape == null) {
			inOutBG = new BranchGroup();
			inOutBG.setCapability(BranchGroup.ALLOW_CHILDREN_READ);		// Allow reading in real-time
			inOutBG.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);	// Allow changes in real-time
			inOutBG.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);	// Allow additions in real-time
			inOutBG.setCapability(BranchGroup.ALLOW_DETACH);			// Allow deleting in real-time
			insideShape = new LatheShape3D(outline.getInsideCurve().getPoints(), mat);	// make the lathe shape primitive
			inOutBG.addChild(insideShape);
			outsideShape = new LatheShape3D(outline.getOutsideCurve().getPoints(), mat);	// make the lathe shape primitive
			inOutBG.addChild(outsideShape);
			bowlBG.addChild(inOutBG);
		} else {
			insideShape.makeGeom(outline.getInsideCurve().getPoints());
			outsideShape.makeGeom(outline.getOutsideCurve().getPoints());
        }
	}

	/**
	 * Detach the inOutBG and set insideShape=null and outsideShape=null.
	 */
    private void deleteInOut() {
        if (inOutBG != null) {
			inOutBG.detach();
			insideShape = null;
			outsideShape = null;
		}
    }


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt == null) {
			return;
		}
//		System.out.println("Bowl.propertyChange: " + evt.getPropertyName() + " " + evt.getOldValue() + " " + evt.getNewValue());
		String evtName = evt.getPropertyName();

		if (evtName.equals(Outline.PROP_CLEAR)) {	// special case when the outline is cleared
			deleteInOut();
		}
		makeInOut();
	}

}
