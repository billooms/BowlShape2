package com.billooms.view3d;

import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;
import javax.media.j3d.*;


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
