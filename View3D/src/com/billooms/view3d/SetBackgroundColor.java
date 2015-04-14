
package com.billooms.view3d;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JColorChooser;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;

/**
 * Action to change the background color in the View3D window
 * @author Bill Ooms Copyright (c) 2011 Studio of Bill Ooms all rights reserved
 */
@ActionID(category = "Edit",
id = "com.billooms.view3d.SetBackgroundColor")
@ActionRegistration(displayName = "#CTL_SetBackgroundColor")
@ActionReferences({
	@ActionReference(path = "Menu/View3D", position = 200)
})
@Messages("CTL_SetBackgroundColor=Set Background Color...")
public final class SetBackgroundColor implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		DisplayPanel panel3d = WindowManager.getDefault().findTopComponent("View3DTopComponent").getLookup().lookup(DisplayPanel.class);
		Color c = JColorChooser.showDialog(null, "Background Color", panel3d.getBackgroundColor());
		panel3d.setBackgroundColor(c);
	}
}
