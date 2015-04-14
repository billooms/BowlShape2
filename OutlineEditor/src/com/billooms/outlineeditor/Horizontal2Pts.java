
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
 * Action to make an outline with two points horizontal.
 * @author Bill Ooms Copyright (c) 2011 Studio of Bill Ooms all rights reserved
 */
@ActionID(category = "Edit",
id = "com.billooms.outlineeditor.Horizontal2Pts")
@ActionRegistration(iconBase = "com/billooms/outlineeditor/icons/Horizontal16.png",
displayName = "#CTL_Horizontal2Pts")
@ActionReferences({
	@ActionReference(path = "Menu/Outline", position = 200)
})
@Messages("CTL_Horizontal2Pts=2Pts: Horizontal")
public final class Horizontal2Pts implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		Lookup.getDefault().lookup(Outline.class).set2PtsHorizontal();
	}
}
