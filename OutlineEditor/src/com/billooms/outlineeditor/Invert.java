
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
 * Action to invert a curve.
 * @author Bill Ooms Copyright (c) 2011 Studio of Bill Ooms all rights reserved
 */
@ActionID(category = "Edit",
id = "com.billooms.outlineeditor.Invert")
@ActionRegistration(iconBase = "com/billooms/outlineeditor/icons/Invert16.png",
displayName = "#CTL_Invert")
@ActionReferences({
	@ActionReference(path = "Menu/Outline", position = 500)
})
@Messages("CTL_Invert=Invert")
public final class Invert implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		Lookup.getDefault().lookup(Outline.class).invert();
	}
}
