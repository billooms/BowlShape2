
package com.billooms.view3d;

import com.billooms.view3dmodel.Bowl;
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
 * Action to change the color of the bowl in the View3D window
 * @author Bill Ooms Copyright (c) 2011 Studio of Bill Ooms all rights reserved
 */
@ActionID(category = "Edit",
id = "com.billooms.view3d.SetBowlColor")
@ActionRegistration(displayName = "#CTL_SetBowlColor")
@ActionReferences({
	@ActionReference(path = "Menu/View3D", position = 100)
})
@Messages("CTL_SetBowlColor=Set Bowl Color...")
public final class SetBowlColor implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		Bowl bowl = WindowManager.getDefault().findTopComponent("View3DTopComponent").getLookup().lookup(Bowl.class);
		bowl.setColor(JColorChooser.showDialog(null, "Bowl Color", bowl.getColor()));
	}
}
