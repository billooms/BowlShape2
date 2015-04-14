package com.billooms.view3d;

import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;
import javax.media.j3d.*;

/**
 * 
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
class OffScreenCanvas3D extends Canvas3D {
    OffScreenCanvas3D(GraphicsConfiguration gConfig, boolean offScreenFlag) {
		super(gConfig, offScreenFlag);
    }

    BufferedImage doRender(int width, int height) {
		BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // was ARGB
		ImageComponent2D buffer = new ImageComponent2D(ImageComponent.FORMAT_RGB, bImage);	 // was RGBA (changed for JPEG)

//		buffer.setCapability(ImageComponent2D.ALLOW_IMAGE_READ);
		setOffScreenBuffer(buffer);
		renderOffScreenBuffer();		// This is triggering the display to mess up
		waitForOffScreenRendering();
		bImage = getOffScreenBuffer().getImage();
		return bImage;
    } 

    @Override
    public void postSwap() {
	// No-op since we always wait for off-screen rendering to complete
    }
}
