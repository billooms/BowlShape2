
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
 * Action for setting the bottom of a curve to zero.
 * @author Bill Ooms Copyright (c) 2011 Studio of Bill Ooms all rights reserved
 */
@ActionID(category = "Edit",
id = "com.billooms.outlineeditor.BottomZero")
@ActionRegistration(iconBase = "com/billooms/outlineeditor/icons/BottomZero16.png",
displayName = "#CTL_BottomZero")
@ActionReferences({
	@ActionReference(path = "Menu/Outline", position = 400)
})
@Messages("CTL_BottomZero=Offset Bottom to Zero")
public final class BottomZero implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		Outline outline = Lookup.getDefault().lookup(Outline.class);
		double delta = outline.offsetBottomZero();
	}
}
