
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
 * Action to clear the outline and all CutPoints.
 * @author Bill Ooms Copyright (c) 2011 Studio of Bill Ooms all rights reserved
 */
@ActionID(category = "Edit",
id = "com.billooms.outlineeditor.ClearAll")
@ActionRegistration(displayName = "#CTL_ClearAll")
@ActionReferences({
	@ActionReference(path = "Menu/Outline", position = 1200)
})
@Messages("CTL_ClearAll=Clear All")
public final class ClearAll implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		Lookup.getDefault().lookup(Outline.class).clear();
	}
}
