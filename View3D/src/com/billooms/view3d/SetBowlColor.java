
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
