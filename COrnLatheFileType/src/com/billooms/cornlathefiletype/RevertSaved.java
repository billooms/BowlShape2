
package com.billooms.cornlathefiletype;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

/**
 * Action to go back to the last saved version of the file.
 * @author Bill Ooms Copyright (c) 2011 Studio of Bill Ooms all rights reserved
 */
@ActionID(category = "File",
id = "com.billooms.cornlathefiletype.RevertSaved")
@ActionRegistration(iconBase = "com/billooms/cornlathefiletype/icons/revert.png",
displayName = "#CTL_RevertSaved")
@ActionReferences({
	@ActionReference(path = "Menu/File", position = 400),
	@ActionReference(path = "Shortcuts", name = "D-Z")
})
@Messages("CTL_RevertSaved=Revert to Saved")
public final class RevertSaved implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		if (OpenFile.dObj != null) {					// Any unsaved changes?
			if (OpenFile.dObj.isModified()) {
				NotifyDescriptor d = new NotifyDescriptor.Confirmation(
						"Changes have not been saved!\nDo you want to revert and lose changes?",
						"Changes not saved",
						NotifyDescriptor.YES_NO_OPTION,
						NotifyDescriptor.WARNING_MESSAGE);
				d.setValue(NotifyDescriptor.YES_OPTION);
				Object result = DialogDisplayer.getDefault().notify(d);
				if (result == DialogDescriptor.YES_OPTION) {
					OpenFile.open(OpenFile.openedFile);	// YES: revert and lose changes
				}
			}
		} else {
			NotifyDescriptor d = new NotifyDescriptor.Message("No file has been opened.\nUse OpenFile from the menu.",
					NotifyDescriptor.ERROR_MESSAGE);
			DialogDisplayer.getDefault().notify(d);
		}
	}
}
