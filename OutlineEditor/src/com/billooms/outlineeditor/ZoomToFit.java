
package com.billooms.outlineeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;

/**
 * Action to zoom the window to fit the displayed data.
 * @author Bill Ooms Copyright (c) 2011 Studio of Bill Ooms all rights reserved
 */
@ActionID(category = "Edit",
id = "com.billooms.outlineeditor.ZoomToFit")
@ActionRegistration(displayName = "#CTL_ZoomToFit")
@ActionReferences({
	@ActionReference(path = "Menu/Outline", position = 2100, separatorBefore = 2000),
	@ActionReference(path = "Shortcuts", name = "D-F")
})
@Messages("CTL_ZoomToFit=Zoom to Fit")
public final class ZoomToFit implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		WindowManager.getDefault().findTopComponent("OutlineEditorTopComponent").getLookup().lookup(OutlineEditPanel.class).zoomToFit();
	}
}
