
package com.billooms.outlineeditor;

import com.billooms.outline.api.Outline;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 * Action to set the top of a curve to zero.
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
id = "com.billooms.outlineeditor.TopZero")
@ActionRegistration(iconBase = "com/billooms/outlineeditor/icons/TopZero16.png",
displayName = "#CTL_TopZero")
@ActionReferences({
	@ActionReference(path = "Menu/Outline", position = 300)
})
@Messages("CTL_TopZero=Offset Top to Zero")
public final class TopZero implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		Outline outline = Lookup.getDefault().lookup(Outline.class);
		double delta = outline.offsetTopZero();
	}
}
